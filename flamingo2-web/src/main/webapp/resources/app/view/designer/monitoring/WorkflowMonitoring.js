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
Ext.define('Flamingo2.view.designer.monitoring.WorkflowMonitoring', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workflowMonitoring',

    requires: [
        'Flamingo2.view.designer.editor.AceEditor'
    ],

    layout: 'border',

    items: [
        {
            region: 'north',
            xtype: 'grid',
            reference: 'workflowGrid',
            itemId: 'workflowGrid',
            layout: 'fit',
            height: 130,
            enableTextSelection: false,
            stripeRows: true,
            columnLines: true,
            bind: {
                store: '{taskHistories}'
            },
            columns: [
                {
                    text: message.msg('dashboard.wdetail.column.id'),
                    width: 60,
                    dataIndex: 'id',
                    name: 'id',
                    align: 'center'
                },
                {
                    text: message.msg('common.identifier'),
                    flex: 100,
                    dataIndex: 'identifier',
                    itemId: 'identifier',
                    name: 'identifier',
                    align: 'center',
                    hidden: true
                },
                {
                    text: message.msg('dashboard.wdetail.column.taskid'),
                    width: 100,
                    dataIndex: 'taskId',
                    name: 'taskId',
                    align: 'center'
                },
                {
                    text: message.msg('dashboard.wdetail.column.name'),
                    flex: 1,
                    dataIndex: 'name',
                    name: 'name',
                    align: 'center'
                },
                {
                    text: message.msg('dashboard.wdetail.column.duration'),
                    width: 100,
                    dataIndex: 'duration',
                    name: 'duration',
                    align: 'center',
                    renderer: function (value) {
                        return App.Util.Date.toHumanReadableTime(Math.floor(value / 1000));
                    }
                },
                {
                    text: message.msg('dashboard.wdetail.column.startdate'),
                    width: 140,
                    dataIndex: 'startDate',
                    name: 'startDate',
                    align: 'center'
                },
                {
                    text: message.msg('dashboard.wdetail.column.enddate'),
                    width: 140,
                    dataIndex: 'endDate',
                    name: 'endDate',
                    align: 'center'
                },
                {
                    text: message.msg('dashboard.wdetail.column.status'),
                    width: 100,
                    dataIndex: 'status',
                    name: 'status',
                    align: 'center',
                    renderer: function (value) { // This is our Widget column
                        var id = Ext.id();
                        Ext.defer(function () {
                            if ($("#" + id).length > 0) {
                                var p = Ext.create('Ext.ProgressBar', {
                                    renderTo: id,
                                    layout: 'fit',
                                    name: 'taskProgress'
                                });
                                if (value == 'STANDBY') {
                                    p.updateProgress(0, 'STANDBY', false);
                                } else if (value == 'RUNNING') {
                                    p.wait({
                                        interval: 200,
                                        increment: 15,
                                        text: 'RUNNING...',
                                        scope: this
                                    });
                                } else if (value == 'FAILED') {
                                    p.updateProgress(100, 'FAILED', false);
                                } else if (value == 'SUCCEEDED') {
                                    p.updateProgress(100, 'SUCCEEDED', false);
                                }
                            }
                        }, 100);
                        return Ext.String.format('<div id="{0}" name="{1}"></div>', id, 'taskProgress');
                    }
                }
            ],
            listeners: {
                itemclick: 'onClickWorkflowDetailItem'
            }
        },
        {
            title: message.msg('dashboard.wdetail.execution.error'),
            xtype: 'aceEditor',
            reference: 'aceEditor',
            itemId: 'aceEditor',
            region: 'center',
            layout: 'fit',
            theme: 'eclipse',
            parser: 'plain_text',
            forceFit: true,
            printMargin: true,
            closable: false,
            readOnly: true,
            script: '',
            name: 'text',
            scrollable: 'true',
            value: message.msg('dashboard.wdetail.log.notselect'),
            tools: [
                {
                    type: 'refresh',
                    tooltip: message.msg('common.refresh'),
                    handler: 'onRefreshClick'
                }
            ],
            listeners: {
                afterrender: function (comp) {
                    // Hide toolbar
                    comp.down('toolbar').setVisible(false);
                }
            }
        }
    ]
});