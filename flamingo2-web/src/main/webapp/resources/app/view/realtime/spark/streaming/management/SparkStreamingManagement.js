/*
 * Copyright (C) 2011  Flamingo Project (http://www.cloudine.io).
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
Ext.define('Flamingo2.view.realtime.spark.streaming.management.SparkStreamingManagement', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.sparkStreamingManagementGrid',

    bind: {
        store: '{sparkStreamingJobsStore}'
    },

    columns: [
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text: message.msg('spark.streaming.list.server'),
            dataIndex: 'server',
            width: 150,
            align: 'center',
            hidden: true
        },
        {
            text: message.msg('common.username'), dataIndex: 'username', width: 100, align: 'center', sortable: true
        },
        {
            text: message.msg('common.registeredDate'), dataIndex: 'registeredDate', width: 100, align: 'center', hidden: true
        },
        {
            text: message.msg('common.applicationID'), dataIndex: 'applicationId', flex: 0.6, align: 'center'
        },
        {
            text: message.msg('common.applicationName'), dataIndex: 'applicationName', flex: 0.4, align: 'center', sortable: true
        },
        {
            text: message.msg('spark.streaming.register.class'), dataIndex: 'streamingClass', flex: 0.8, align: 'center', sortable: true, hidden: true
        },
        {
            text: message.msg('spark.streaming.list.workingPath'), dataIndex: 'sparkUserWorkingPath', flex: 0.4, align: 'center', sortable: true, hidden: true
        },
        {
            text: message.msg('spark.streaming.list.jarFQP'), dataIndex: 'jarFileFQP', minWidth: 200, align: 'center', flex: 1, hidden: true
        },
        {
            text: message.msg('spark.streaming.list.jarFilename'), dataIndex: 'jarFilename', minWidth: 200, align: 'center', flex: 0.4, hidden: true
        },
        {
            text: message.msg('spark.streaming.list.cli'), dataIndex: 'variables', flex: 0.3, align: 'center', hidden: true
        },
        {
            text: message.msg('common.startTime'), dataIndex: 'startTime', width: 140, align: 'center', sortable: true,
            renderer: function (value, metaData, record, row, col, store, gridView) {
                if (record.data.state == 'STANDBY') {
                    return '';
                } else {
                    return dateFormat2(value);
                }
            }
        },
        {
            text: message.msg('common.finishTime'), dataIndex: 'finishTime', width: 140, align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                if (record.data.state == 'STANDBY') {
                    return '';
                }
            }, hidden: true
        },
        {
            text: message.msg('common.duration'), dataIndex: 'duration', width: 140, align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                var end, start = new Date(record.data.startTime);
                var diff;

                if (!Ext.isEmpty(value)) {
                    if (record.data.state == 'RUNNING') {
                        end = new Date(value);
                    } else if (record.data.state == 'STANDBY') {
                        return '';
                    } else {
                        end = new Date(value);
                    }

                    diff = (end.getTime() - start.getTime()) / 1000;
                } else {
                    return '';
                }

                if (Ext.isEmpty(record.data.startTime)) {
                    return '';
                }

                return toHumanReadableTime(Math.floor(diff));
            }
        },
        {
            text: message.msg('common.status'), dataIndex: 'state', width: 80, align: 'center', sortable: true
        }
    ],
    viewConfig: {
        emptyText: message.msg('spark.streaming.list.empty'),
        enableTextSelection: true,
        deferEmptyText: false,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    },
    tbar: [
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.regist'),
            iconCls: 'common-add',
            labelWidth: 50,
            listeners: {
                click: 'onApplicationRegisterClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.start'),
            iconCls: 'common-execute',
            labelWidth: 50,
            listeners: {
                click: 'onApplicationStartClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.stop'),
            iconCls: 'common-pause',
            labelWidth: 50,
            listeners: {
                click: 'onApplicationStopClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.abort'),
            iconCls: 'common-stop',
            labelWidth: 50,
            listeners: {
                click: 'onApplicationKillClick'
            }
        }
    ],
/*    dockedItems: [
        {
            xtype: 'pagingtoolbar',
            itemId: 'sparkStreamingManagementToolbar',
            dock: 'bottom',
            bind: {
                store: '{sparkStreamingJobsStore}'
            },
            displayInfo: true,
            doRefresh: function () {
                var sparkStreamingToolbar = query('sparkStreamingManagement #sparkStreamingManagementToolbar');
                var sparkStreamingGrid = query('sparkStreamingManagement');

                sparkStreamingToolbar.moveFirst();
                sparkStreamingGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            },
            listeners: {
                beforechange: function (toolbar, page, eOpts) {
                    toolbar.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                }
            }
        }
    ],*/
    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('common.refresh'),
            handler: 'onSparkStreamingGridRefreshClick'
        }
    ],
    listeners: {
        itemclick: 'onSparkStreamingGridItemClick',
        afterrender: 'onSparkStreamingGridAfterRender'
    }
});
