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
Ext.define('Flamingo2.view.archive.yarn.ArchiveYarnAllApplications', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.archiveYarnAllApplications',

    title: message.msg('monitoring.msg.all_yarn_app'),

    bind: {
        store: '{archiveYarnAllAppsStore}'
    },
    plugins: [
        {
            ptype: 'bufferedrenderer',
            leadingBufferZone: 50,
            trailingBufferZone: 20
        }
    ],
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
                    var start = Ext.Date.parse(record.data.startTime, "Y-m-d H:i:s", true);
                    var end = new Date();
                    var diff = (end.getTime() - start.getTime()) / 1000;
                    return toHumanReadableTime(Math.floor(diff));
                } else if (
                    record.data.yarnApplicationState == 'FINISHED' ||
                    record.data.yarnApplicationState == 'FAILED' ||
                    record.data.yarnApplicationState == 'KILLED') {
                    var start = Ext.Date.parse(record.data.startTime, "Y-m-d H:i:s", true);
                    var end = Ext.Date.parse(record.data.finishTime, "Y-m-d H:i:s", true);
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
    ],
    tbar: [
        {
            xtype: 'tbtext',
            text: message.msg('common.searchCondition')
        },
        '|',
        {
            xtype: 'tbtext',
            text: message.msg('common.startDate')
        },
        {
            xtype: 'datefield',
            format: 'Y-m-d',
            itemId: 'startDate',
            editable: false,
            value: '',
            maxValue: new Date(),
            vtype: 'dateRange',
            width: 100,
            listeners: {
                select: function (field, value, eOpts) {
                    var start = dateFormat(value, 'yyyy-MM-dd');
                    var endDateFields = query('archiveYarnAllApplications #endDate');
                    var end = dateFormat(endDateFields.getValue(), 'yyyy-MM-dd');

                    if (start > end && endDateFields.getValue()) {
                        return field.setActiveError(message.msg('hdfs.audit.date.start.msg'));
                    }
                }
            }
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'tbtext',
            text: message.msg('common.endDate')
        },
        {
            xtype: 'datefield',
            format: 'Y-m-d',
            itemId: 'endDate',
            editable: false,
            value: '',
            maxValue: new Date(),
            vtype: 'dateRange',
            width: 100,
            listeners: {
                select: function (field, value, eOpts) {
                    var startDateFields = query('archiveYarnAllApplications #startDate');
                    var start = dateFormat(startDateFields.getValue(), 'yyyy-MM-dd');
                    var end = dateFormat(value, 'yyyy-MM-dd');

                    if (start > end) {
                        return field.setActiveError(message.msg('hdfs.audit.date.end.msg'));
                    }
                }
            }
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'tbtext',
            text: message.msg('common.searchCondition')
        },
        {
            xtype: 'combo',
            name: 'filter',
            itemId: 'filter',
            editable: false,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            width: 125,
            value: 'ALL',
            bind: {
                store: '{archiveYarnSearchKeyword}'
            }
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'tbtext',
            text: message.msg('monitoring.application_name')
        },
        {
            xtype: 'textfield',
            itemId: 'appName'
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.retrieve'),
            iconCls: 'common-search',
            labelWidth: 50,
            listeners: {
                click: 'onArchiveYarnAllAppsFindClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.reset'),
            iconCls: 'common-search-clear',
            labelWidth: 50,
            listeners: {
                click: 'onArchiveYarnAllAppsResetClick'
            }
        }
    ],
    dockedItems: [
        {
            xtype: 'pagingtoolbar',
            itemId: 'archiveYarnAllAppsToolbar',
            dock: 'bottom',
            bind: {
                store: '{archiveYarnAllAppsStore}'
            },
            displayInfo: true,
            doRefresh: function () {
                var yarnAppToolbar = query('archiveYarnAllApplications #archiveYarnAllAppsToolbar');
                var yarnAppGrid = query('archiveYarnAllApplications');
                var startDateFields = query('archiveYarnAllApplications #startDate');
                var endDateFields = query('archiveYarnAllApplications #endDate');
                var filter = query('archiveYarnAllApplications #filter');
                var appName = query('archiveYarnAllApplications #appName');

                yarnAppToolbar.moveFirst();
                yarnAppGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                yarnAppGrid.getStore().getProxy().extraParams.startDate = startDateFields.getValue();
                yarnAppGrid.getStore().getProxy().extraParams.endDate = endDateFields.getValue();
                yarnAppGrid.getStore().getProxy().extraParams.filter = filter.getValue();
                yarnAppGrid.getStore().getProxy().extraParams.appName = appName.getValue();
            },
            listeners: {
                beforechange: function (toolbar, page, eOpts) {
                    var startDateFields = query('archiveYarnAllApplications #startDate');
                    var endDateFields = query('archiveYarnAllApplications #endDate');
                    var filter = query('archiveYarnAllApplications #filter');
                    var appName = query('archiveYarnAllApplications #appName');

                    toolbar.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                    toolbar.getStore().getProxy().extraParams.startDate = startDateFields.getValue();
                    toolbar.getStore().getProxy().extraParams.endDate = endDateFields.getValue();
                    toolbar.getStore().getProxy().extraParams.filter = filter.getValue();
                    toolbar.getStore().getProxy().extraParams.appName = appName.getValue();
                }
            }
        }
    ],
    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('common.refresh'),
            handler: 'onArchiveYarnAllAppsRefreshClick'
        }
    ],
    listeners: {
        itemclick: 'onArchiveYarnAllAppsGridItemClick',
        afterrender: 'onArchiveYarnAllAppsAfterRender'
    }
});