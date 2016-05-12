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
 * HAWQ > Browser > Object Explorer > Table > Grid context menu > Create click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._TableCreateController
 * @see Flamingo2.view.hawq.browser._TableCreateModel
 */
Ext.define('Flamingo2.view.hawq.browser._TableCreate', {
    extend: 'Ext.window.Window',
    alias: 'widget._hawqTableCreateWindow',

    requires: [
        'Flamingo2.view.hawq.browser._TableCreateController',
        'Flamingo2.view.hawq.browser._TableCreateModel'
    ],

    controller: '_hawqTableCreateController',
    viewModel: {
        type: '_hawqTableCreateModel'
    },

    height: 500,
    width: 850,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('hawq.title.create.table'),
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
        afterrender: 'tableCreateWindowAfterrender'
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
                    reference: 'hawqTableCreateTabpanel',
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
                                            name: 'databaseName',
                                            editable: false,
                                            bind: {
                                                store: '{hawqDatabase}'
                                            },
                                            displayField: 'databaseName',
                                            valueField: 'databaseName',
                                            listeners: {
                                                select: 'databaseNameComboboxSelect'
                                            }
                                        },
                                        {
                                            xtype: 'combobox',
                                            fieldLabel: message.msg('hawq.label.schema.name'),
                                            reference: 'schemaNameCombobox',
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
                                            name: 'tableName',
                                            allowBlank: false,
                                            maxLength: 64
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.table.comment'),
                                            name: 'tableComment'
                                        },
                                        {
                                            xtype: 'combobox',
                                            reference: 'cbxUser',
                                            fieldLabel: message.msg('hawq.label.table.owner'),
                                            name: 'owner',
                                            editable: false,
                                            bind: {
                                                store: '{hawqUser}'
                                            },
                                            displayField: '_username',
                                            valueField: '_username'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.label.table.reference'),
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
                                            text: message.msg('hawq.column.default'),
                                            dataIndex: 'default',
                                            flex: 1,
                                            align: 'center',
                                            editor: 'textfield'
                                        },
                                        {
                                            text: message.msg('hawq.null'),
                                            dataIndex: 'isNull',
                                            flex: .5,
                                            align: 'center',
                                            editor: 'checkboxfield'
                                        },
                                        {
                                            text: message.msg('hawq.column.check'),
                                            dataIndex: 'check',
                                            flex: 1,
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
                                            editor: 'checkboxfield'
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
                                    xtype: '_hawqTableCreateWithForm',
                                    reference: 'frmWith',
                                    anchor: '50%',
                                    bodyPadding: 5
                                }
                            ]
                        },
                        {
                            xtype: 'form',
                            title: message.msg('hawq.title.partition'),
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            reference: 'hawqPartitionForm',
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
                                        width: '100%'
                                    },
                                    items: [
                                        {
                                            xtype: 'combobox',
                                            fieldLabel: message.msg('hawq.column.name'),
                                            labelAlign: 'right',
                                            reference: 'partitionColumnNameCombobox',
                                            name: 'partitionColumnName',
                                            editable: false,
                                            bind: {
                                                store: '{hawqColumn}'
                                            },
                                            displayField: 'columnName',
                                            valueField: 'columnName',
                                            queryMode: 'local'
                                        },
                                        {
                                            xtype: 'textfield',
                                            fieldLabel: message.msg('hawq.partition.default'),
                                            labelAlign: 'right',
                                            name: 'partitionDefault'
                                        },
                                        {
                                            xtype: 'radiogroup',
                                            colspan: 2,
                                            columns: 2,
                                            fieldLabel: message.msg('hawq.partition.type'),
                                            labelAlign: 'right',
                                            defaults: {
                                                margin: '0 25 0 0'
                                            },
                                            items: [
                                                {boxLabel: 'Range', name: 'type', inputValue: 'RANGE', checked: true},
                                                {boxLabel: 'List', name: 'type', inputValue: 'LIST'}
                                            ],
                                            listeners: {
                                                change: 'hawqTablePartitionRadiogroupChange'
                                            }
                                        }
                                    ]
                                },
                                {
                                    xtype: 'grid',
                                    flex: 1,
                                    reference: 'hawqPartitionRangeGrid',
                                    border: true,
                                    bind: {
                                        store: '{hawqPartitionRange}'
                                    },
                                    autoScroll: true,
                                    tbar: [
                                        '->',
                                        {
                                            text: message.msg('hawq.button.add'),
                                            iconCls: 'common-add',
                                            reference: 'hawqPartitionRangeGridAddButton',
                                            handler: 'hawqPartitionRangeGridAddButtonHandler'
                                        },
                                        {
                                            text: message.msg('hawq.button.drop'),
                                            iconCls: 'common-delete',
                                            reference: 'hawqPartitionRangeGridDeleteButton',
                                            handler: 'hawqPartitionRangeGridDeleteButtonHandler'
                                        }
                                    ],
                                    plugins: {
                                        ptype: 'cellediting',
                                        pluginId: 'createTablePartitionRangeGridCellediting',
                                        clicksToEdit: 1
                                    },
                                    columns: [
                                        {
                                            text: message.msg('hawq.partition.name'),
                                            dataIndex: 'name',
                                            autoSizeColumn: true,
                                            align: 'center',
                                            editor: 'textfield'
                                        },
                                        {
                                            text: message.msg('hawq.partition.start'),
                                            columns: [
                                                {
                                                    text: message.msg('hawq.partition.type'),
                                                    dataIndex: 'startType',
                                                    autoSizeColumn: true,
                                                    align: 'center'
                                                },
                                                {
                                                    text: message.msg('hawq.partition.value'),
                                                    dataIndex: 'startValue',
                                                    autoSizeColumn: true,
                                                    align: 'center',
                                                    editor: 'textfield'
                                                }
                                            ]
                                        },
                                        {
                                            text: message.msg('hawq.partition.end'),
                                            columns: [
                                                {
                                                    text: message.msg('hawq.partition.type'),
                                                    dataIndex: 'endType',
                                                    autoSizeColumn: true,
                                                    align: 'center'
                                                },
                                                {
                                                    text: message.msg('hawq.partition.value'),
                                                    dataIndex: 'endValue',
                                                    autoSizeColumn: true,
                                                    align: 'center',
                                                    editor: 'textfield'
                                                }
                                            ]
                                        },
                                        {
                                            xtype: 'actioncolumn',
                                            text: message.msg('hawq.title.option'),
                                            dataIndex: 'partitionWith',
                                            width: 50,
                                            align: 'center',
                                            items: [
                                                {
                                                    iconCls: 'common-add',
                                                    handler: 'hawqPartitionWithActioncolumnHandler'
                                                }
                                            ]
                                        }
                                    ],

                                    listeners: {
                                        resize: function (grid, width, height) {
                                            var columns = grid.columns,
                                                length = columns.length - 1,// with 제외
                                                columnWidth = (width - 52) / length;// 좌측테두리1 + with width 50 + 우측테두리1

                                            for (var i = 0; i < length; i++) {
                                                if (columns[i].autoSizeColumn) {
                                                    columns[i].setWidth(columnWidth);
                                                }
                                            }
                                        }
                                    }
                                },
                                {
                                    xtype: 'grid',
                                    flex: 1,
                                    reference: 'hawqPartitionListGrid',
                                    hidden: true,
                                    border: true,
                                    bind: {
                                        store: '{hawqPartitionList}'
                                    },
                                    autoScroll: true,
                                    tbar: [
                                        '->',
                                        {
                                            text: message.msg('hawq.button.add'),
                                            iconCls: 'common-add',
                                            reference: 'hawqPartitionListGridAddButton',
                                            handler: 'hawqPartitionListGridAddButtonHandler'
                                        },
                                        {
                                            text: message.msg('hawq.button.drop'),
                                            iconCls: 'common-delete',
                                            reference: 'hawqPartitionListGridDeleteButton',
                                            handler: 'hawqPartitionListGridDeleteButtonHandler'
                                        }
                                    ],
                                    plugins: {
                                        ptype: 'cellediting',
                                        pluginId: 'createTablePartitionListGridCellediting',
                                        clicksToEdit: 1
                                    },
                                    columns: [
                                        {
                                            text: message.msg('hawq.partition.listname'),
                                            dataIndex: 'name',
                                            flex: .5,
                                            align: 'center',
                                            editor: 'textfield'
                                        },
                                        {
                                            text: message.msg('hawq.partition.value'),
                                            dataIndex: 'value',
                                            flex: 1,
                                            align: 'center',
                                            editor: 'textfield'
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
            handler: 'tableCreateButtonHandler'
        },
        {
            text: message.msg('hawq.button.cancel'),
            iconCls: 'common-cancel',
            handler: 'cancelButtonHandler'
        }
    ]
});