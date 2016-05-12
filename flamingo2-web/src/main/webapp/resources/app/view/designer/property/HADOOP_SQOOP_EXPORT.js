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
/**
 * Sqoop Export View
 *
 * @class
 * @extends Flamingo2.view.designer.property.HADOOP_SQOOP_EXPORT
 * @author <a href="mailto:chiwanpark@icloud.com">Chiwan Park</a>
 */
Ext.define('Flamingo2.view.designer.property.HADOOP_SQOOP_EXPORT', {
    extend: 'Flamingo2.view.designer.property._NODE_HADOOP',
    alias: 'widget.HADOOP_SQOOP_EXPORT',

    requires: [
        'Flamingo2.view.designer.property._BrowserField'
    ],

    width: 500,
    height: 300,

    items: [
        {
            title: message.msg('workflow.he.sqoop.source.hdfs'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                anchor: '100%',
                labelWidth: 100
            },
            defaultType: 'textfield',
            items: [
                {
                    fieldLabel: message.msg('workflow.he.sqoop.hdfs.path'),
                    name: 'output',
                    xtype: '_browserField'
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    fieldLabel: message.msg('workflow.he.sqoop.field.delimiter'),
                    flex: 1,
                    items: [
                        {
                            xtype: 'combo',
                            name: 'fieldDelimiter',
                            value: ',',
                            flex: 1,
                            forceSelection: true,
                            multiSelect: false,
                            editable: false,
                            readOnly: this.readOnly,
                            displayField: 'name',
                            valueField: 'value',
                            mode: 'local',
                            queryMode: 'local',
                            triggerAction: 'all',
                            tpl: '<tpl for="."><div class="x-boundlist-item" data-qtip="{description}">{name}</div></tpl>',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['name', 'value', 'description'],
                                data: [
                                    {
                                        name: message.msg('workflow.common.delimiter.double.colon'),
                                        value: '::',
                                        description: '::'
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.comma'),
                                        value: ',',
                                        description: ','
                                    },
                                    {name: message.msg('workflow.common.delimiter.pipe'), value: '|', description: '|'},
                                    {
                                        name: message.msg('workflow.common.delimiter.tab'),
                                        value: '\'\\t\'',
                                        description: '\'\\t\''
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.blank'),
                                        value: '\'\\s\'',
                                        description: '\'\\s\''
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.user.def'),
                                        value: 'CUSTOM',
                                        description: message.msg('workflow.common.delimiter.user.def')
                                    }
                                ]
                            }),
                            listeners: {
                                change: function (combo, newValue, oldValue, eOpts) {
                                    // 콤보 값에 따라 관련 textfield 를 enable | disable 처리한다.
                                    var customValueField = combo.nextSibling('textfield');
                                    if (newValue === 'CUSTOM') {
                                        customValueField.enable();
                                        customValueField.isValid();
                                    } else {
                                        customValueField.disable();
                                        if (newValue) {
                                            customValueField.setValue(newValue);
                                        } else {
                                            customValueField.setValue(',');
                                        }
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'textfield',
                            name: 'fieldDelimiterValue',
                            flex: 1,
                            disabled: true,
                            readOnly: this.readOnly,
                            allowBlank: false,
                            value: ','
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    fieldLabel: message.msg('workflow.he.sqoop.line.delimiter'),
                    flex: 1,
                    items: [
                        {
                            xtype: 'combo',
                            name: 'lineDelimiter',
                            value: '\\n',
                            flex: 1,
                            forceSelection: true,
                            multiSelect: false,
                            editable: false,
                            readOnly: this.readOnly,
                            displayField: 'name',
                            valueField: 'value',
                            mode: 'local',
                            queryMode: 'local',
                            triggerAction: 'all',
                            tpl: '<tpl for="."><div class="x-boundlist-item" data-qtip="{description}">{name}</div></tpl>',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['name', 'value', 'description'],
                                data: [
                                    {
                                        name: message.msg('workflow.common.delimiter.double.colon'),
                                        value: '::',
                                        description: '::'
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.comma'),
                                        value: ',',
                                        description: ','
                                    },
                                    {name: message.msg('workflow.common.delimiter.pipe'), value: '|', description: '|'},
                                    {
                                        name: message.msg('workflow.common.delimiter.tab'),
                                        value: '\'\\t\'',
                                        description: '\'\\t\''
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.blank'),
                                        value: '\'\\s\'',
                                        description: '\'\\s\''
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.user.def'),
                                        value: 'CUSTOM',
                                        description: message.msg('workflow.common.delimiter.user.def')
                                    }
                                ]
                            }),
                            listeners: {
                                change: function (combo, newValue, oldValue, eOpts) {
                                    // 콤보 값에 따라 관련 textfield 를 enable | disable 처리한다.
                                    var customValueField = combo.nextSibling('textfield');
                                    if (newValue === 'CUSTOM') {
                                        customValueField.enable();
                                        customValueField.isValid();
                                    } else {
                                        customValueField.disable();
                                        if (newValue) {
                                            customValueField.setValue(newValue);
                                        } else {
                                            customValueField.setValue('\\n');
                                        }
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'textfield',
                            name: 'lineDelimiterValue',
                            flex: 1,
                            disabled: true,
                            readOnly: this.readOnly,
                            allowBlank: false,
                            value: '\\n'
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'form',
            title: message.msg('workflow.he.sqoop.destination.database'),
            border: false,
            autoScroll: true,
            defaults: {
                anchor: '100%',
                labelWidth: 100
            },
            defaultType: 'textfield',
            items: [
                {
                    name: 'jdbcUrl',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.uri')
                },
                {
                    name: 'jdbcDriver',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.driver')
                },
                {
                    name: 'sqoopUsername',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.username')
                },
                {
                    name: 'sqoopPassword',
                    inputType: 'password',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.password')
                },
                {
                    name: 'sqoopTable',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.table')
                }
            ]
        },
        {
            title: message.msg('common.references'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://sqoop.apache.org/docs/1.4.6/SqoopUserGuide.html" target="_blank">Sqoop User Guide</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://sqoop.apache.org/docs/1.4.6/SqoopDevGuide.html" target="_blank">Sqoop Developer Guide</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://sqoop.apache.org/docs/1.4.6/api/index.html" target="_blank">Sqoop API</a>'
                }
            ]
        }
    ]
});