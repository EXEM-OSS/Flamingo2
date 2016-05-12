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
Ext.define('Flamingo2.view.dashboard.information.SubActionDetailForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.subActionDetailForm',

    requires: [
        'Flamingo2.view.designer.editor.AceEditor'
    ],

    reference: 'subActionDetailForm',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    bodyPadding: 5,

    items: [
        {
            region: 'center',
            layout: 'fit',
            height: 140,
            border: 3,
            bodyPadding: 10,
            hidden: true,
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
            reference: 'editorTab',
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
                    value: message.msg('dashboard.wdetail.log.notselect'),
                    listeners: {
                        afterrender: 'hide'
                    }
                },
                {
                    xtype: 'aceEditor',
                    reference: 'commandAceEditor',
                    itemId: 'commandAceEditor',
                    title: message.msg('dashboard.jobdetail.log.command'),
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
                    value: message.msg('dashboard.wdetail.log.notselect'),
                    listeners: {
                        afterrender: 'hide'
                    }
                },
                {
                    xtype: 'aceEditor',
                    reference: 'scriptAceEditor',
                    itemId: 'scriptAceEditor',
                    title: message.msg('dashboard.jobdetail.log.script'),
                    region: 'center',
                    layout: 'fit',
                    theme: 'eclipse',
                    parser: 'plain_text',
                    forceFit: true,
                    printMargin: true,
                    closable: false,
                    readOnly: true,
                    script: '',
                    name: 'script',
                    value: message.msg('dashboard.wdetail.log.notselect'),
                    listeners: {
                        afterrender: 'hide'
                    }
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
                    name: 'script',
                    value: message.msg('dashboard.wdetail.log.notselect'),
                    listeners: {
                        afterrender: 'hide'
                    }
                }
            ]
        }
    ],
    listeners: {
        beforerender: 'onAceEditorReady',
        afterrender: 'onSubWorkflowSelection'
    }
});