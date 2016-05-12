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
 * HAWQ > Browser > Object Explorer > Table > Grid context menu > Alter click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._TableAlterController
 * @see Flamingo2.view.hawq.browser._TableAlterModel
 */
Ext.define('Flamingo2.view.hawq.browser._TableAlter', {
    extend: 'Ext.window.Window',
    alias: 'widget._hawqTableAlterWindow',

    requires: [
        'Flamingo2.view.hawq.browser._TableAlterController',
        'Flamingo2.view.hawq.browser._TableAlterModel'
    ],

    controller: '_hawqTableAlterController',
    viewModel: {
        type: '_hawqTableAlterModel'
    },

    height: 500,
    width: 850,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('hawq.title.alter.table'),
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
        afterrender: '_hawqTableAlterWindowAfterrender'
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
                    reference: 'hawqTableAlterTabpanel',
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
                                            reference: 'databaseNameCombobox',
                                            itemId: 'databaseNameCombobox',
                                            name: 'databaseName',
                                            editable: false,
                                            readOnly: true,
                                            bind: {
                                                store: '{hawqDatabase}'
                                            },
                                            displayField: 'databaseName',
                                            valueField: 'databaseName'
                                        },
                                        {
                                            xtype: 'combobox',
                                            fieldLabel: message.msg('hawq.label.schema.name'),
                                            reference: 'schemaNameCombobox',
                                            itemId: 'schemaNameCombobox',
                                            name: 'schemaName',
                                            editable: false,
                                            bind: {
                                                store: '{hawqSchema}'
                                            },
                                            displayField: 'schemaName',
                                            valueField: 'schemaName'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.table.name'),
                                            reference: 'tableNameTextfield',
                                            itemId: 'tableNameTextfield',
                                            name: 'tableName',
                                            allowBlank: false
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.table.comment'),
                                            name: 'tableComment'
                                        },
                                        {
                                            xtype: 'combobox',
                                            fieldLabel: message.msg('hawq.label.table.owner'),
                                            reference: 'hawqOwnerCombobox',
                                            name: 'owner',
                                            editable: false,
                                            bind: {
                                                store: '{hawqUser}'
                                            },
                                            displayField: '_username',
                                            valueField: '_username'
                                        },
                                        {
                                            xtype: 'combobox',
                                            fieldLabel: message.msg('hawq.column.distributed'),
                                            editable: false,
                                            itemId: 'distributedCombobox',
                                            reference: 'distributedCombobox',
                                            name: 'distributed',
                                            multiSelect: true,
                                            queryMode: 'local',
                                            bind: {
                                                store: '{hawqDistributed}'
                                            },
                                            displayField: 'column_name',
                                            valueField: 'column_name'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'grid',
                                    flex: 1,
                                    title: message.msg('hawq.column'),
                                    border: true,
                                    itemId: 'hawqColumnsGrid',
                                    reference: 'hawqColumnsGrid',
                                    bind: {
                                        store: '{hawqColumn}'
                                    },
                                    autoScroll: true,
                                    tbar: [
                                        '->',
                                        {
                                            text: message.msg('hawq.button.add'),
                                            iconCls: 'common-column-add',
                                            handler: 'hawqColumnsGridAddButtonHandler'
                                        },
                                        {
                                            text: message.msg('hawq.button.drop'),
                                            iconCls: 'common-column-add',
                                            handler: 'hawqColumnsGridDeleteButtonHandler'
                                        },
                                        {
                                            text: message.msg('hawq.button.save'),
                                            iconCls: 'common-save',
                                            tooltip: message.msg('hawq.button.save.columntooltip'),
                                            handler: 'hawqColumnsGridSaveButtonHandler'
                                        },
                                        {
                                            text: message.msg('common.refresh'),
                                            iconCls: 'common-refresh',
                                            handler: 'hawqColumnsGridRefreshButtonHandler'
                                        }
                                    ],
                                    plugins: {
                                        ptype: 'cellediting',
                                        pluginId: 'alterTableColumnsGridCellediting',
                                        clicksToEdit: 1
                                    },
                                    columns: [
                                        {
                                            text: message.msg('hawq.column.name'),
                                            dataIndex: 'column_name',
                                            flex: 1,
                                            align: 'center',
                                            editor: {
                                                xtype: 'textfield',
                                                allowBlank: false
                                            }
                                        },
                                        {
                                            text: message.msg('hawq.datatype'),
                                            dataIndex: 'data_type',
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
                                            dataIndex: 'character_maximum_length',
                                            flex: .5,
                                            align: 'center',
                                            editor: 'textfield'
                                        },
                                        {
                                            text: message.msg('hawq.column.default'),
                                            dataIndex: 'column_default',
                                            flex: 1,
                                            align: 'center',
                                            editor: 'textfield'
                                        },
                                        {
                                            text: message.msg('hawq.null'),
                                            dataIndex: 'is_nullable',
                                            flex: .5,
                                            align: 'center',
                                            editor: {
                                                xtype: 'checkboxfield',
                                                inputValue: 'YES'
                                            }
                                        },
                                        {
                                            text: message.msg('hawq.column.comment'),
                                            dataIndex: 'column_comment',
                                            flex: 1,
                                            align: 'center',
                                            editor: 'textfield'
                                        },
                                        {
                                            text: message.msg('hawq.column.distributed'),
                                            dataIndex: 'distributed',
                                            width: 80,
                                            align: 'center'
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            title: message.msg('hawq.title.option'),
                            layout: 'anchor',
                            items: [
                                {
                                    xtype: '_hawqTableAlterWithForm',
                                    reference: 'frmWith',
                                    anchor: '50%',
                                    bodyPadding: 5
                                }
                            ]
                        },
                        {
                            xtype: 'form',
                            title: message.msg('hawq.partition'),
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            reference: 'hawqPartitionForm',
                            bodyPadding: 5,
                            items: [
                                {
                                    xtype: 'textareafield',
                                    fieldLabel: message.msg('hawq.partition.definition'),
                                    labelAlign: 'right',
                                    name: 'partitiondef',
                                    readOnly: true,
                                    flex: 1
                                }
                            ]
                        },
                        {
                            xtype: 'grid',
                            title: message.msg('hawq.title.constraint'),
                            border: true,
                            itemId: 'hawqConstraintsGrid',
                            reference: 'hawqConstraintsGrid',
                            bind: {
                                store: '{hawqConstraint}'
                            },
                            autoScroll: true,
                            tbar: [
                                '->',
                                {
                                    text: message.msg('hawq.button.add'),
                                    iconCls: 'common-add',
                                    handler: 'hawqConstraintsGridAddButtonHandler'
                                },
                                {
                                    text: message.msg('hawq.button.drop'),
                                    iconCls: 'common-delete',
                                    handler: 'hawqConstraintGridDeleteButtonHandler'
                                },
                                {
                                    text: message.msg('hawq.button.save'),
                                    iconCls: 'common-save',
                                    tooltip: message.msg('hawq.button.save.constrainttooltip'),
                                    handler: 'hawqConstraintsGridSaveButtonHandler'
                                },
                                {
                                    text: message.msg('common.refresh'),
                                    iconCls: 'common-refresh',
                                    handler: 'hawqConstraintsGridRefreshButtonHandler'
                                }
                            ],
                            plugins: {
                                ptype: 'cellediting',
                                pluginId: 'alterTableConstraintsGridCellediting',
                                clicksToEdit: 1
                            },
                            columns: [
                                {
                                    text: message.msg('hawq.constraint.name'),
                                    dataIndex: 'conname',
                                    flex: .5,
                                    align: 'center',
                                    editor: {
                                        xtype: 'textfield',
                                        allowBlank: false
                                    }
                                },
                                {
                                    text: message.msg('hawq.constraint.keys'),
                                    dataIndex: 'conkeys',
                                    flex: .5,
                                    align: 'center'
                                },
                                {
                                    text: message.msg('hawq.constraint.source'),
                                    dataIndex: 'consrc',
                                    flex: 1,
                                    align: 'center',
                                    editor: {
                                        xtype: 'textfield',
                                        allowBlank: false
                                    }
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
            text: message.msg('hawq.button.alter'),
            iconCls: 'common-complete',
            handler: 'tableAlterButtonHandler'
        },
        {
            text: message.msg('hawq.button.cancel'),
            iconCls: 'common-cancel',
            handler: 'cancelButtonHandler'
        }
    ]
});