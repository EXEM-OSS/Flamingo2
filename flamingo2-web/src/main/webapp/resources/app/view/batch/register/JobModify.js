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
/**
 * Created by Park on 15. 6. 30..
 */
Ext.define('Flamingo2.view.batch.register.JobModify', {
    extend: 'Ext.window.Window',
    requires: [
        'Flamingo2.view.batch.register.JobModifyController',
        'Flamingo2.view.batch.register.JobModifyModel',
        'Flamingo2.view.batch.cron.CronTrigger',
        'Flamingo2.view.designer.variableGrid.VariableGrid'
    ],
    controller: 'jobmodify',
    viewModel: {
        type: 'jobmodify'
    },

    height: 700,
    width: 700,
    modal: true,
    resizable: false,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('batch.msg.job_register'),
    titleCollapse: false,
    closeAction: 'destroy',
    layout: 'border',

    items: [
        {
            xtype: 'form',
            title: message.msg('batch.job_info'),
            region: 'north',
            height: 440,
            border: true,
            bodyPadding: 5,
            split: true,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            reference: 'jobInformation',
            itemId: 'jobInformation',
            defaults: {
                labelWidth: 150,
                labelAlign: 'right'
            },
            items: [
                {
                    xtype: 'fieldset',
                    title: message.msg('dashboard.jobdetail.job.jobinfo'),
                    layout: {
                        type: 'table',
                        columns: 2,
                        tableAttrs: {
                            style: {
                                width: '100%'
                            }
                        }
                    },
                    defaults: {
                        labelAlign: 'right'
                    },
                    items: [
                        {
                            xtype: 'textfield',
                            reference: 'jobName',
                            colspan: 2,
                            allowBlank: false,
                            width: 500,
                            itemId: 'jobName',
                            name: 'jobName',
                            fieldLabel: message.msg('batch.job_name')
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('dashboard.common.workflowId'),
                            reference: 'workflowId',
                            itemId: 'workflowId',
                            name: 'workflowId',
                            value: ''
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('batch.last_modified'),
                            reference: 'createDate',
                            itemId: 'createDate',
                            name: 'createDate',
                            value: ''
                        },
                        {
                            xtype: 'displayfield',
                            reference: 'writer',
                            itemId: 'writer',
                            name: 'writer',
                            fieldLabel: message.msg('batch.writer'),
                            value: ''
                        },
                        {
                            xtype: 'displayfield',
                            reference: 'status',
                            itemId: 'status',
                            name: 'status',
                            fieldLabel: message.msg('batch.status_code'),
                            value: ''
                        },
                        {
                            xtype: 'hiddenfield',
                            reference: 'identifier',
                            itemId: 'identifier',
                            name: 'identifier',
                            fieldLabel: message.msg('common.identifier'),
                            value: ''
                        }
                    ],
                    listeners: {
                        afterrender: function (view) {
                            setTableLayoutFixed(view);
                        }
                    }
                },
                {
                    xtype: 'fieldset',
                    title: message.msg('cron.expression_info'),
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'cronTrigger'
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'variableGrid',
            title: message.msg('workflow.common.workflow.variable'),
            flex: 1,
            border: true,
            region: 'center',
            reference: 'variableGrid'
        }
    ],
    buttonAlign: 'center',
    buttons: [
        {
            text: message.msg('common.confirm'),
            reference: 'btnOk',
            handler: 'onOKClick'
        },
        {
            text: message.msg('common.cancel'),
            handler: 'onCancelClick'
        }
    ],
    listeners: {
        afterrender: 'onAfterrender'
    }
});