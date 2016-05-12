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
Ext.define('Flamingo2.view.dashboard.information.tab.ActionDetailForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.actionDetailForm',

    requires: [
        'Flamingo2.view.designer.editor.AceEditor'
    ],

    title: message.msg('dashboard.jobdetail.action.title'),

    reference: 'actionDetailForm',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    bodyPadding: 5,

    items: [
        {
            region: 'north',
            xtype: 'grid',
            bind: {
                store: '{actionInformationStore}'
            },
            itemId: 'actionListGrid',
            reference: 'actionListGrid',
            height: 150,
            columnLines: true,
            columns: [
                {
                    text: message.msg('dashboard.jobdetail.action.headerno'),
                    flex: 0.6,
                    dataIndex: 'id',
                    align: 'center',
                    hidden: true,
                    sortable: false
                },
                {
                    text: message.msg('dashboard.wh.column.jobstringid'),
                    width: 200,
                    dataIndex: 'identifier',
                    itemId: 'identifier',
                    align: 'center',
                    hidden: true,
                    sortable: false
                },
                {
                    text: message.msg('dashboard.common.workflowId'),
                    flex: 1,
                    dataIndex: 'processId',
                    align: 'center',
                    sortable: false
                },
                {
                    text: message.msg('dashboard.jobdetail.action.headeracname'),
                    flex: 1.2,
                    dataIndex: 'name',
                    align: 'center',
                    sortable: false
                },
                {
                    text: message.msg('dashboard.jobdetail.action.headerjobid'),
                    flex: 1.2,
                    dataIndex: 'taskId',
                    align: 'center',
                    hidden: true,
                    sortable: false
                },
                {
                    text: message.msg('dashboard.jobdetail.action.headerstart'),
                    width: 130,
                    dataIndex: 'startDate',
                    align: 'center',
                    sortable: false,
                    renderer: function (value) {
                        return App.Util.Date.format(new Date(value), 'yyyy-MM-dd HH:mm:ss');
                    }
                },
                {
                    text: message.msg('dashboard.jobdetail.action.headerend'),
                    width: 130,
                    dataIndex: 'endDate',
                    align: 'center',
                    renderer: function (value, item, record) {
                        return (item.record.data.status == 'RUNNING') ? '' : App.Util.Date.format(new Date(value), 'yyyy-MM-dd HH:mm:ss');
                    }
                },
                {
                    text: message.msg('dashboard.jobdetail.action.headerelapsed'),
                    width: 60,
                    dataIndex: 'duration',
                    align: 'center',
                    renderer: function (value, item, record) {
                        var diff;

                        if (item.record.data.status == 'RUNNING') {
                            var start = new Date(item.record.data.startDate);
                            var end = new Date(item.record.data.endDate);
                            diff = (end.getTime() - start.getTime()) / 1000;
                            return App.Util.Date.toHumanReadableTime(Math.floor(diff));
                        } else {
                            diff = value / 1000;
                            return App.Util.Date.toHumanReadableTime(Math.floor(diff));
                        }
                    }
                },
                {
                    text: message.msg('dashboard.jobdetail.action.headerstatus'),
                    flex: 0.6,
                    dataIndex: 'status',
                    align: 'center',
                    sortable: false
                }
            ],
            viewConfig: {
                stripeRows: true,
                getRowClass: function () {
                    return 'job-cell-height';
                }
            },
            tbar: [
                '->',
                {
                    text: message.msg('common.refresh'),
                    iconCls: 'common-refresh',
                    handler: 'onActionGridRefreshButton'
                }
            ],
            listeners: {
                afterrender: 'onActionGridAfterRender',
                selectionchange: 'onActionGridSelection'
            }
        },
        {
            region: 'center',
            layout: 'fit',
            height: 140,
            border: 3,
            hidden: true,
            bodyPadding: 10,
            title: message.msg('dashboard.jobdetail.action.actioninfo'),
            items: [
                {
                    xtype: 'form',
                    reference: 'actionInfoForm',
                    itemId: 'actionInfoForm',
                    layout: {
                        type: 'hbox'
                    },
                    defaults: {
                        labelAlign: 'right',
                        anchor: '100%'
                    },
                    defaultType: 'textfield',
                    items: [
                        {
                            xtype: 'fieldcontainer',
                            layout: 'vbox',
                            flex: 1.5,
                            defaultType: 'textfield',
                            defaults: {
                                labelWidth: 100
                            },
                            items: [
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.action.acid'),
                                    name: 'id'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.action.jobid'),
                                    name: 'identifier'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.common.workflowId'),
                                    name: 'processId'
                                },
                                {
                                    itemId: 'logPath',
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.action.logpath'),
                                    name: 'logDirectory',
                                    hidden: true
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            layout: 'vbox',
                            flex: 1,
                            defaultType: 'textfield',
                            defaults: {
                                labelWidth: 80
                            },
                            labelStyle: 'font-weight:bold;',
                            items: [
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.action.start'),
                                    name: 'startDate'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.action.end'),
                                    name: 'endDate'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.action.acname'),
                                    name: 'name'
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            layout: 'vbox',
                            flex: 1,
                            defaultType: 'textfield',
                            defaults: {
                                labelWidth: 80
                            },
                            labelStyle: 'font-weight:bold;',
                            items: [
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.action.elapsed'),
                                    name: 'duration'
                                },
                                {
                                    xtype: 'displayfield',
                                    itemId: 'status',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.action.acstatus'),
                                    name: 'status'
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            region: 'south',
            xtype: 'tabpanel',
            flex: 1,
            layout: 'fit',
            items: [
                {
                    xtype: 'aceEditor',
                    reference: 'logAceEditor',
                    itemId: 'logAceEditor',
                    title: message.msg('dashboard.jobdetail.log.title'),
                    region: 'center',
                    layout: 'fit',
                    theme: 'eclipse',
                    parser: 'plain_text',
                    forceFit: true,
                    printMargin: true,
                    closable: false,
                    readOnly: true,
                    script: '',
                    name: 'log',
                    value: message.msg('dashboard.wdetail.log.notselect')
                },
                {
                    xtype: 'aceEditor',
                    reference: 'commandAceEditor',
                    itemId: 'commandAceEditor',
                    title: message.msg('dashboard.jobdetail.log.command'),
                    region: 'center',
                    useWrapMode: true,
                    layout: 'fit',
                    theme: 'eclipse',
                    parser: 'plain_text',
                    forceFit: true,
                    printMargin: true,
                    closable: false,
                    readOnly: true,
                    script: '',
                    name: 'command',
                    value: message.msg('dashboard.wdetail.log.notselect')
                },
                {
                    xtype: 'aceEditor',
                    reference: 'scriptAceEditor',
                    itemId: 'scriptAceEditor',
                    title: message.msg('dashboard.jobdetail.log.script'),
                    region: 'center',
                    useWrapMode: true,
                    layout: 'fit',
                    theme: 'eclipse',
                    parser: 'plain_text',
                    forceFit: true,
                    printMargin: true,
                    closable: false,
                    readOnly: true,
                    script: '',
                    name: 'script',
                    value: message.msg('dashboard.wdetail.log.notselect')
                },
                {
                    xtype: 'aceEditor',
                    reference: 'errorAceEditor',
                    itemId: 'errorAceEditor',
                    title: message.msg('dashboard.jobdetail.log.error_msg'),
                    region: 'center',
                    layout: 'fit',
                    theme: 'eclipse',
                    parser: 'plain_text',
                    forceFit: true,
                    printMargin: true,
                    closable: false,
                    readOnly: true,
                    script: '',
                    name: 'error',
                    value: message.msg('dashboard.wdetail.log.notselect')
                },
                {
                    xtype: 'panel',
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    title: message.msg('dashboard.wdetail.hadoop.job.id'),
                    items: [
                        {
                            xtype: 'grid',
                            title: message.msg('dashboard.wdetail.yarn.application.id'),//'Yarn Application ID'
                            itemId: 'yarnApplicationGrid',
                            flex: 1,
                            margin: 2,
                            border: true,
                            viewConfig: {
                                stripeRows: true,
                                columnLines: true,
                                enableTextSelection: true,
                                getRowClass: function (b, e, d, c) {
                                    return 'cell-height-30';
                                }
                            },
                            bind: {
                                store: '{yarnApplicationIdStore}'
                            },
                            columns: [
                                {
                                    dataIndex: 'id',
                                    flex: 1,
                                    text: message.msg('dashboard.wh.column.id')
                                }
                            ]
                        },
                        {
                            xtype: 'grid',
                            title: message.msg('dashboard.wdetail.mr.id'),
                            itemId: 'mapreduceGrid',
                            flex: 1,
                            margin: 2,
                            border: true,
                            viewConfig: {
                                stripeRows: true,
                                columnLines: true,
                                enableTextSelection: true,
                                getRowClass: function (b, e, d, c) {
                                    return 'cell-height-30';
                                }
                            },
                            bind: {
                                store: '{mapReducdeIdStore}'
                            },
                            columns: [
                                {
                                    dataIndex: 'id',
                                    flex: 1,
                                    text: message.msg('dashboard.wh.column.id')
                                }
                            ]
                        }
                    ]
                }
            ],
            listeners: {
                tabchange: 'onActionSubTabChange'
            }
        }
    ]/*,
     listeners: {
     beforerender: 'onAceEditorReady'
     }*/
});