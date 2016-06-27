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
Ext.define('Flamingo2.view.realtime.spark.steaming.management.ApplicationRegisterForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.applicationRegisterForm',

    requires: [
        'Flamingo2.view.designer.property._ValueGrid'
    ],

    reference: 'applicationRegisterForm',
    bodyBorder: false,
    bodyStyle: {
        background: '#FFFFFF'
    },
    layout: 'fit',
    items: [
        {
            xtype: 'panel',
            padding: 15,
            scrollable: true,
            layout: {
                type: 'table',
                columns: 1
            },
            items: [
                {
                    xtype: 'textfield',
                    reference: 'applicationName',
                    fieldLabel: message.msg('common.applicationName'),
                    labelAlign: 'right',
                    labelWidth: 120,
                    width: 350,
                    emptyText: 'QueueStream',
                    allowBlank: false
                },
                {
                    xtype: 'textfield',
                    reference: 'streamingClass',
                    fieldLabel: message.msg('spark.streaming.register.class'),
                    labelAlign: 'right',
                    labelWidth: 120,
                    width: 490,
                    emptyText: 'org.apache.spark.examples.streaming.QueueStream',
                    allowBlank: false
                },
                {
                    xtype: 'textfield',
                    reference: 'javaOpts',
                    fieldLabel: message.msg('workflow.he.java.java.jvm'),
                    labelAlign: 'right',
                    labelWidth: 120,
                    width: 490,
                    emptyText: '-XX:MaxPermSize=128m -Xms512m -Xmx512m',
                    allowBlank: true
                },
                {
                    xtype: 'filefield',
                    reference: 'jarFile',
                    name: 'jarFile',
                    fieldLabel: 'Jar File',
                    labelWidth: 120,
                    labelAlign: 'right',
                    allowBlank: true,
                    anchor: '100%',
                    buttonText: message.msg('common.browse'),
                    width: 490,
                    emptyText: 'user_defined.jar',
                    listeners: {
                        change: function (field, value) {
                            var newValue = value.replace(/(^.*(\\|\/))?/, "");
                            field.setRawValue(newValue);
                        }
                    }
                },
                {
                    xtype: 'panel',
                    bodyStyle: {
                        background: '#ffffff'
                    },
                    height: 150,
                    layout: 'border',
                    items: [
                        {
                            xtype: 'displayfield',
                            region: 'west',
                            split: true,
                            border: true,
                            width: 120,
                            fieldLabel: message.msg('workflow.common.command'),
                            labelAlign: 'right',
                            labelWidth: 120
                        },
                        {
                            xtype: 'form',
                            scrollable: true,
                            region: 'center',
                            border: true,
                            flex: 1,
                            items: [
                                {
                                    layout: {
                                        type: 'vbox',
                                        align: 'stretch'
                                    },
                                    items: [
                                        {
                                            xtype: '_valueGrid',
                                            itemId: 'valueGrid'
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]
});
