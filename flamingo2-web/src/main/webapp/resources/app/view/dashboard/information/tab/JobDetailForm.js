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
Ext.define('Flamingo2.view.dashboard.information.tab.JobDetailForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.jobDetailForm',

    requires: [
        'Flamingo2.view.designer.editor.AceEditor'
    ],

    title: message.msg('dashboard.jobdetail.job.title'),

    reference: 'jobDetailForm',

    layout: 'border',

    bodyPadding: 5,

    items: [
        {
            title: message.msg('dashboard.jobdetail.job.jobinfo'),
            region: 'north',
            items: [
                {
                    xtype: 'form',
                    itemId: 'jobInfoForm',
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
                            flex: 1.3,
                            defaultType: 'textfield',
                            labelStyle: 'font-weight:bold;',
                            items: [
                                {
                                    xtype: 'displayfield',
                                    itemId: 'identifier',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.job.jobid'),
                                    name: 'identifier'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.common.workflowId'),
                                    name: 'workflowId'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('common.workflowName'),
                                    name: 'workflowName'
                                },
                                {
                                    xtype: 'displayfield',
                                    itemId: 'status',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.job.wfstatus'),
                                    name: 'status'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.job.user'),
                                    name: 'username'
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            layout: 'vbox',
                            flex: 1,
                            defaultType: 'textfield',
                            labelStyle: 'font-weight:bold;',
                            items: [
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.job.start'),
                                    name: 'startDate'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.job.end'),
                                    name: 'endDate'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.job.elapsed'),
                                    name: 'elapsed',
                                    renderer: function (value) {
                                        var diff;
                                        var status = query('jobDetailForm #status');

                                        if (status.getValue() == 'RUNNING') {
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
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.current.step'),
                                    name: 'currentStep'
                                },
                                {
                                    xtype: 'displayfield',
                                    labelAlign: 'right',
                                    fieldLabel: message.msg('dashboard.jobdetail.total.step'),
                                    name: 'totalStep'
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            region: 'center',
            xtype: 'tabpanel',
            layout: 'fit',
            items: [
                {
                    xtype: 'aceEditor',
                    title: message.msg('dashboard.jobdetail.job.xml'),
                    reference: 'jobXmlAceEditor',
                    region: 'center',
                    layout: 'fit',
                    theme: 'eclipse',
                    parser: 'xml',
                    forceFit: true,
                    printMargin: true,
                    closable: false,
                    readOnly: true,
                    script: '',
                    name: 'workflowXml',
                    value: message.msg('dashboard.wdetail.log.notselect')
                },
                {
                    xtype: 'aceEditor',
                    title: message.msg('dashboard.jobdetail.job.error'),
                    reference: 'jobErrorAceEditor',
                    region: 'center',
                    layout: 'fit',
                    theme: 'eclipse',
                    parser: 'plain_text',
                    forceFit: true,
                    printMargin: true,
                    closable: false,
                    readOnly: true,
                    script: '',
                    name: 'exception',
                    value: message.msg('dashboard.wdetail.log.notselect')
                }
            ],
            listeners: {
                afterlayout: function (tabpanel) {
                    var activeTab = tabpanel.getActiveTab();
                    var activeTabIndex = tabpanel.items.findIndex('id', activeTab.id);
                    switch (activeTabIndex) {
                        case 0:
                            Ext.ComponentQuery.query('jobDetailForm aceEditor')[0].setValue(Ext.ComponentQuery.query('jobDetailForm aceEditor')[0].value);
                            break;
                        case 1:
                            Ext.ComponentQuery.query('jobDetailForm aceEditor')[1].setValue(Ext.ComponentQuery.query('jobDetailForm aceEditor')[1].value);
                            break;
                    }
                }
            }
        }
    ]
});
