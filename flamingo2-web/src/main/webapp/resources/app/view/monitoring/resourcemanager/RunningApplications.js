/*
 * Copyright (C) 2011 Flamingo Project (http://www.cloudine.io).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
Ext.define('Flamingo2.view.monitoring.resourcemanager.RunningApplications', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.runningApplications',

    title: message.msg('monitoring.rm.run_yarn_app'),

    listeners: {
        itemclick: 'onItemClick',
        afterrender: 'onAfterrender'
    },

    bind: {
        store: '{runningApplicationsStore}'
    },

    viewConfig: {
        emptyText: message.msg('monitoring.rm.no_running_application'),
        deferEmptyText: false,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (record, index) {
            return 'cell-height-30';
        }
    },

    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('monitoring.msg.refresh_yarn_list'),
            handler: function (event, toolEl, panel) {
                var grid = query('runningApplications');
                grid.getStore().load({
                    callback: function (records, operation, success) {
                        grid.setTitle(format(message.msg('monitoring.rm.total_run_app'), this.getCount()))
                    }
                });
            }
        }
    ],

    columns: [
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false,
            locked: true
        },
        {
            text: message.msg('monitoring.application_id'),
            dataIndex: 'applicationId',
            width: 230,
            align: 'center',
            locked: true,
            renderer: function (value, metaData, record, rowIdx, colIdx, store) {
                metaData.tdAttr = 'data-qtip="'
                    + message.msg('monitoring.application_id') + ' : ' + record.get('applicationId')
                    + '<br/>'
                    + message.msg('monitoring.application_type') + ' : ' + record.get('applicationType')
                    + '<br/>'
                    + message.msg('monitoring.application_name') + ' : ' + (record.get('name') ? record.get('name') : message.msg('common.none')) + '"';
                return value;
            },
            summaryType: 'count',
            summaryRenderer: function (value, summaryData, dataIndex) {
                return format(message.msg('monitoring.rm.count'), (value === 0 || value > 1) ? value : 1);
            }
        },
        {
            text: message.msg('monitoring.application_name'), dataIndex: 'name', width: 200, align: 'center'
        },
        {
            text: message.msg('common.user'), dataIndex: 'user', width: 100, align: 'center', sortable: true
        },
        {
            text: message.msg('common.status'),
            dataIndex: 'yarnApplicationState',
            width: 80,
            align: 'center',
            sortable: true
        },
        {
            text: message.msg('common.final_status'),
            dataIndex: 'finalApplicationStatus',
            width: 100,
            align: 'center',
            sortable: true
        },
        {
            text: message.msg('common.type'), dataIndex: 'applicationType', align: 'center', sortable: true
        },
        {
            text: message.msg('common.elapsed_time'), dataIndex: 'elapsedTime', width: 80, align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                if (record.data.yarnApplicationState == 'RUNNING') {
                    var start = Ext.Date.parse(record.data.startTime, "Y-m-d H:i:s", true)
                    var end = new Date();
                    var diff = (end.getTime() - start.getTime()) / 1000;
                    return toHumanReadableTime(Math.floor(diff));
                } else {
                    return '';
                }
            }
        },
        {
            text: message.msg('common.progress'), dataIndex: 'progress', width: 110, align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return Ext.String.format('<div class="x-progress x-progress-default x-border-box">' +
                    '<div class="x-progress-text x-progress-text-back" style="width: 100px;">{0}%</div>' +
                    '<div class="x-progress-bar x-progress-bar-default" role="presentation" style="width:{0}%">' +
                    '<div class="x-progress-text" style="width: 100px;"><div>{0}%</div></div></div></div>', value);
            }
        },
        {
            text: message.msg('common.memory'), dataIndex: 'neededResourcesMemory', align: 'center', sortable: true
        },
        {
            text: message.msg('common.core'),
            dataIndex: 'neededResourcesVcores',
            width: 70,
            align: 'center',
            sortable: true
        },
        {
            text: message.msg('common.queue'), dataIndex: 'queue', align: 'center', sortable: true
        },
        {
            text: message.msg('common.start'), dataIndex: 'startTime', width: 140, align: 'center', sortable: true
        },
        {
            text: message.msg('common.finish'), dataIndex: 'finishTime', width: 140, align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                if (record.data.yarnApplicationState == 'RUNNING') {
                    return message.msg('common.running');
                } else if (
                    record.data.yarnApplicationState == 'FINISHED' ||
                    record.data.yarnApplicationState == 'FAILED' ||
                    record.data.yarnApplicationState == 'KILLED') {
                    return value;
                } else {
                    return '';
                }
            }
        }
    ]
});