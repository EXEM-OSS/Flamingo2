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
Ext.define('Flamingo2.view.monitoring.applications.AllApplications', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.allApplications',

    requires: [
        'Flamingo2.model.monitoring.resourcemanager.Application'
    ],

    title: message.msg('monitoring.msg.all_yarn_app'),

    listeners: {
        itemcontextmenu: 'onItemContextMenu',
        itemclick: 'onItemClick',
        afterrender: 'onAfterrender'
    },

    bind: {
        store: '{allApplicationsStore}'
    },

    viewConfig: {
        emptyText: message.msg('monitoring.msg.do_not_have_yarn_complete'),
        deferEmptyText: false,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (record, index) {
            // Change row color if state is running
            if (record.data.yarnApplicationState == 'RUNNING') {
                return 'selected-grid cell-height-30';
            }
            return 'cell-height-30';
        }
    },

    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('monitoring.msg.refresh_yarn_list'),
            handler: function (event, toolEl, panel) {
                var grid = query('allApplications');
                grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                grid.getStore().load({
                    callback: function (records, operation, success) {
                        grid.setTitle(Ext.String.format(message.msg('monitoring.msg.all_yarn_app_total'), this.getCount()));
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
            width: 220,
            align: 'center',
            locked: true,
            renderer: function (value, metaData, record, rowIdx, colIdx, store) {
                metaData.tdAttr = 'data-qtip="'
                    + message.msg('monitoring.application_id') + ' : ' + record.get('applicationId')
                    + '<br/>'
                    + message.msg('monitoring.application_type') + ' : ' + record.get('applicationType')
                    + '<br/>'
                    + message.msg('monitoring.application_name') + ' : ' + (record.get('name') ? record.get('name') : message.msg('monitoring.yarn.tip.na')) + '"';
                return value;
            },
            summaryType: 'count',
            summaryRenderer: function (value, summaryData, dataIndex) {
                return ((value === 0 || value > 1) ? '(' + value + ' ' + message.msg('monitoring.yarn.tip.count') + ')' : '(1 ' + message.msg('monitoring.yarn.tip.count') + ')');
            }
        },
        {
            text: message.msg('monitoring.application_name'), dataIndex: 'name', width: 200, align: 'center'
        },
        {
            text: message.msg('common.user'), dataIndex: 'user', width: 100, align: 'center', sortable: true
        },
        {
            text: message.msg('common.action'),
            align: 'center',
            width: 73,
            renderer: function (value, metaData, record, row, col, store, gridView) {
                if (record.data.yarnApplicationState == 'RUNNING') {
                    var id = Ext.id();
                    var applicationId = record.data.applicationId
                    Ext.defer(function () {
                        Ext.widget('button', {
                            renderTo: id,
                            text: message.msg('common.kill'),
                            scale: 'small',
                            handler: function () {
                                var params = {
                                    applicationId: applicationId,
                                    clusterName: ENGINE.id
                                };
                                Ext.MessageBox.show({
                                    title: message.msg('monitoring.application_kill'),
                                    message: message.msg('monitoring.msg.application_kill'),
                                    buttons: Ext.MessageBox.YESNO,
                                    icon: Ext.MessageBox.WARNING,
                                    fn: function handler(btn) {
                                        if (btn == 'yes') {
                                            invokeGet('/monitoring/resourcemanager/app/kill.json', params,
                                                function (response) {
                                                    var obj = Ext.decode(response.responseText);
                                                    if (obj.success) {
                                                        // 메시지 처리
                                                    } else {
                                                        // TODO 에러 처리
                                                    }
                                                },
                                                function (response) {
                                                    Ext.MessageBox.show({
                                                        title: message.msg('common.warn'),
                                                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                                                        buttons: Ext.MessageBox.OK,
                                                        icon: Ext.MessageBox.WARNING
                                                    });
                                                }
                                            );
                                        }
                                    }
                                });
                            }
                        });
                    }, 50);
                    return Ext.String.format('<div id="{0}"></div>', id);
                }
            }
        },
        {
            text: message.msg('common.status'),
            dataIndex: 'yarnApplicationState',
            width: 90,
            align: 'center',
            sortable: true
        },
        {
            text: message.msg('common.final_status'),
            dataIndex: 'finalApplicationStatus',
            width: 90,
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
                } else if (
                    record.data.yarnApplicationState == 'FINISHED' ||
                    record.data.yarnApplicationState == 'FAILED' ||
                    record.data.yarnApplicationState == 'KILLED') {
                    var start = Ext.Date.parse(record.data.startTime, "Y-m-d H:i:s", true)
                    var end = Ext.Date.parse(record.data.finishTime, "Y-m-d H:i:s", true)
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
            text: message.msg('common.queue'), dataIndex: 'queue', align: 'center'
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