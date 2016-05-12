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
 * HAWQ > Browser > Object Explorer > External Table > Grid context menu > Create click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._ExternalTableCreateController
 * @see Flamingo2.view.hawq.browser._ExternalTableCreateModel
 */
Ext.define('Flamingo2.view.hawq.browser._ExternalTableCreate', {
    extend: 'Ext.window.Window',
    alias: 'widget._hawqExternalTableCreateWindow',

    controller: '_hawqExternalTableCreateController',
    viewModel: {
        type: '_hawqExternalTableCreateModel'
    },

    height: 500,
    width: 850,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('hawq.title.create.exttable'),
    titleCollapse: false,
    modal: true,
    closeAction: 'destroy',
    layout: 'fit',
    autoScroll: true,
    border: false,
    bodyPadding: 10,
    bodyStyle: {
        background: '#fff'
    },

    listeners: {
        afterrender: 'extTableCreateWindowAfterrender'
    },

    items: [
        {
            xtype: 'form',
            layout: 'fit',
            items: [
                {
                    xtype: 'tabpanel',
                    plain: true,
                    activeTab: 0,
                    reference: 'hawqExternalTableCreateTabpanel',
                    items: [
                        {
                            xtype: 'panel',
                            title: message.msg('hawq.title.general'),
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            bodyPadding: 5,
                            items: [
                                {
                                    xtype: 'fieldcontainer',
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
                                        width: '100%',
                                        labelAlign: 'right',
                                        labelWidth: 120
                                    },
                                    items: [
                                        {
                                            xtype: 'combobox',
                                            fieldLabel: message.msg('hawq.label.database.name'),
                                            itemId: 'databaseNameCombobox',
                                            reference: 'databaseNameCombobox',
                                            name: 'databaseName',
                                            editable: false,
                                            bind: {
                                                store: '{hawqDatabase}'
                                            },
                                            displayField: 'databaseName',
                                            valueField: 'databaseName'
                                        },
                                        {
                                            xtype: 'combobox',
                                            fieldLabel: message.msg('hawq.label.schema.name'),
                                            name: 'schemaName',
                                            reference: 'schemaNameCombobox',
                                            editable: false,
                                            bind: {
                                                store: '{hawqSchema}'
                                            },
                                            displayField: 'schemaName',
                                            valueField: 'schemaName'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.name'),
                                            name: 'tableName',
                                            allowBlank: false,
                                            maxLength: 64
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.comment'),
                                            name: 'tableComment'
                                        },
                                        {
                                            xtype: 'fieldcontainer',
                                            layout: 'hbox',
                                            defaults: {
                                                labelAlign: 'right',
                                                labelWidth: 120
                                            },
                                            items: [
                                                {
                                                    xtype: 'checkboxfield',
                                                    fieldLabel: 'Writable',
                                                    name: 'writable',
                                                    listeners: {
                                                        change: 'hawqExternalTableWritableCheckboxChange'
                                                    }
                                                },
                                                {
                                                    xtype: 'checkboxfield',
                                                    fieldLabel: message.msg('hawq.label.exttable.web'),
                                                    name: 'webTable',
                                                    margin: '0 0 0 5'
                                                }
                                            ]
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.reference'),
                                            name: 'otherTable',
                                            enableKeyEvents: true,
                                            listeners: {
                                                keyup: 'hawqOtherTableKeyup'
                                            }
                                        }
                                    ]
                                },
                                {
                                    xtype: 'grid',
                                    flex: 1,
                                    title: message.msg('hawq.column'),
                                    border: true,
                                    reference: 'hawqColumnsGrid',
                                    bind: {
                                        store: '{hawqColumn}'
                                    },
                                    tbar: [
                                        '->',
                                        {
                                            text: message.msg('hawq.button.add'),
                                            iconCls: 'common-column-add',
                                            handler: 'hawqColumnsGridAddButtonHandler'
                                        },
                                        {
                                            text: message.msg('hawq.button.drop'),
                                            iconCls: 'common-column-remove',
                                            handler: 'hawqColumnsGridDeleteButtonHandler'
                                        }
                                    ],
                                    plugins: {
                                        ptype: 'cellediting',
                                        pluginId: 'createTableColumnsGridCellediting',
                                        clicksToEdit: 1
                                    },
                                    columns: [
                                        {
                                            text: message.msg('hawq.column.name'),
                                            dataIndex: 'columnName',
                                            flex: 1,
                                            align: 'center',
                                            editor: {
                                                xtype: 'textfield',
                                                allowBlank: false
                                            }
                                        },
                                        {
                                            text: message.msg('hawq.datatype'),
                                            dataIndex: 'dataType',
                                            width: 150,
                                            align: 'center',
                                            editor: {
                                                xtype: 'combobox',
                                                allowBlank: false,
                                                editable: false,
                                                bind: {
                                                    store: '{hawqDataType}'
                                                },
                                                displayField: 'name',
                                                valueField: 'value'
                                            }
                                        },
                                        {
                                            text: message.msg('hawq.column.length'),
                                            dataIndex: 'length',
                                            flex: .5,
                                            align: 'center',
                                            editor: 'textfield'
                                        },
                                        {
                                            text: message.msg('hawq.column.comment'),
                                            dataIndex: 'comment',
                                            flex: 1,
                                            align: 'center',
                                            editor: 'textfield'
                                        },
                                        {
                                            text: message.msg('hawq.column.distributed'),
                                            dataIndex: 'distributed',
                                            width: 80,
                                            align: 'center',
                                            editor: 'checkboxfield',
                                            hidden: true
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'grid',
                            border: true,
                            title: message.msg('hawq.externaltable.location'),
                            reference: 'hawqExternalTableLocationGrid',
                            bind: {
                                store: '{hawqExternalTableLocation}'
                            },
                            plugins: {
                                ptype: 'cellediting',
                                pluginId: 'createExternalTableLocationGridCellediting',
                                clicksToEdit: 1,

                                listeners: {
                                    edit: 'hawqExternalTableLocationGridCelleditingEdit'
                                }
                            },
                            tbar: [
                                '->',
                                {
                                    text: message.msg('hawq.button.add'),
                                    iconCls: 'common-add',
                                    handler: 'hawqExternalTableLocationGridAddButtonHandler'
                                },
                                {
                                    text: message.msg('hawq.button.drop'),
                                    iconCls: 'common-delete',
                                    handler: 'hawqExternalTableLocationGridDeleteButtonHandler'
                                }
                            ],
                            columns: [
                                {
                                    text: message.msg('hawq.externaltable.location.hint'),
                                    dataIndex: 'hint',
                                    width: 150,
                                    align: 'center',
                                    editor: {
                                        xtype: 'combobox',
                                        editable: false,
                                        bind: {
                                            store: '{hawqExternalTableLocationHint}'
                                        },
                                        displayField: 'displ',
                                        valueField: 'value'
                                    }
                                },
                                {
                                    text: message.msg('hawq.externaltable.location'),
                                    dataIndex: 'location',
                                    flex: 1,
                                    align: 'center',
                                    editor: {
                                        xtype: 'textfield',
                                        allowBlank: false
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'form',
                            title: message.msg('hawq.externaltable.format'),
                            reference: 'formFormat',
                            bodyPadding: 5,
                            layout: 'anchor',
                            items: [
                                {
                                    xtype: 'radiogroup',
                                    columns: 2,
                                    fieldLabel: message.msg('hawq.label.exttable.format.type'),
                                    labelAlign: 'right',
                                    defaults: {
                                        margin: '0 25 0 0'
                                    },
                                    items: [
                                        {boxLabel: 'TEXT', name: 'type', inputValue: 'TEXT', checked: true},
                                        {boxLabel: 'CSV', name: 'type', inputValue: 'CSV'}
                                    ],
                                    allowBlank: false,

                                    listeners: {
                                        change: 'hawqExternalTableFormatRadiogroup'
                                    }
                                },
                                {
                                    xtype: 'panel',
                                    layout: 'anchor',
                                    reference: 'formatTextPanel',
                                    defaults: {
                                        labelWidth: 120
                                    },
                                    items: [
                                        {
                                            xtype: 'checkboxfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.header'),
                                            labelAlign: 'right',
                                            name: 'header'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.delimiter'),
                                            labelAlign: 'right',
                                            name: 'delimiter'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.nullstring'),
                                            labelAlign: 'right',
                                            name: 'nullString'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.escape'),
                                            labelAlign: 'right',
                                            name: 'escape'
                                        },
                                        {
                                            xtype: 'combobox',
                                            fieldLabel: message.msg('hawq.label.exttable.format.newline'),
                                            labelAlign: 'right',
                                            name: 'newLine',
                                            editable: false,
                                            bind: {
                                                store: '{hawqFormatNewLine}'
                                            },
                                            displayField: 'displ',
                                            valueField: 'value'
                                        },
                                        {
                                            xtype: 'checkboxfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.fillmissingfields'),
                                            labelAlign: 'right',
                                            name: 'fillMissingFields'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'panel',
                                    layout: 'anchor',
                                    reference: 'formatCsvPanel',
                                    hidden: true,
                                    disabled: true,
                                    defaults: {
                                        labelWidth: 120
                                    },
                                    items: [
                                        {
                                            xtype: 'checkboxfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.header'),
                                            labelAlign: 'right',
                                            name: 'header'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.quote'),
                                            labelAlign: 'right',
                                            name: 'quote'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.delimiter'),
                                            labelAlign: 'right',
                                            name: 'delimiter'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.nullstring'),
                                            labelAlign: 'right',
                                            name: 'nullString'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.forcenotnull'),
                                            labelAlign: 'right',
                                            name: 'forceNotNull'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.escape'),
                                            labelAlign: 'right',
                                            name: 'escape'
                                        },
                                        {
                                            xtype: 'combobox',
                                            fieldLabel: message.msg('hawq.label.exttable.format.newline'),
                                            labelAlign: 'right',
                                            name: 'newLine',
                                            editable: false,
                                            bind: {
                                                store: '{hawqFormatNewLine}'
                                            },
                                            displayField: 'displ',
                                            valueField: 'value'
                                        },
                                        {
                                            xtype: 'checkboxfield',
                                            fieldLabel: message.msg('hawq.label.exttable.format.fillmissingfields'),
                                            labelAlign: 'right',
                                            name: 'fillMissingFields'
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ],

    buttons: [
        {
            text: message.msg('hawq.button.create'),
            iconCls: 'common-complete',
            handler: 'externalTableCreateButtonHandler'
        },
        {
            text: message.msg('hawq.button.cancel'),
            iconCls: 'common-cancel',
            handler: 'cancelButtonHandler'
        }
    ]
});