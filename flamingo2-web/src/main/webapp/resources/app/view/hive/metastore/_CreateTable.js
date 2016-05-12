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
Ext.define('Flamingo2.view.hive.metastore._CreateTable', {
    extend: 'Ext.window.Window',
    alias: 'widget.createTable',

    requires: [
        'Flamingo2.view.hive.metastore._CreateTableController',
        'Flamingo2.view.hive.metastore.MetastoreModel',
        'Flamingo2.view.hive.metastore._ColumnGrid'
    ],

    config: {
        alter: false,
        database: null,
        table: null
    },
    hdfs: false,

    controller: 'metastoreCreateTableController',

    viewModel: {
        type: 'hiveMetastoreModel'
    },

    title: message.msg('hive.table.create'),
    modal: true,
    height: 600,
    width: 600,

    layout: 'border',

    items: [
        {
            xtype: 'form',
            region: 'north',
            reference: 'tableForm',
            bodyPadding: 5,
            layout: 'anchor',
            items: [
                {
                    xtype: 'fieldcontainer',
                    reference: 'containerDB',
                    hidden: true,
                    layout: 'fit',
                    anchor: '100%',
                    items: [
                        {
                            xtype: 'combo',
                            reference: 'comboDB',
                            fieldLabel: message.msg('hive.database'),
                            anchor: '100%',
                            editable: false,
                            displayField: 'database',
                            valueField: 'database',
                            allowBlank: false,
                            bind: {
                                store: '{databases}'
                            }
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'tableName',
                    reference: 'table_name',
                    itemId: 'tableTextField',
                    fieldLabel: message.msg('hive.table'),
                    allowBlank: false,
                    vtype: 'alphanum',
                    anchor: '100%'
                },
                {
                    xtype: 'textfield',
                    name: 'comment',
                    itemId: 'commentTextField',
                    fieldLabel: message.msg('common.desc'),
                    anchor: '100%'
                },
                {
                    xtype: 'fieldset',
                    title: message.msg('hive.table_type'),
                    layout: 'anchor',
                    itemId: 'tableField',
                    items: [
                        {
                            xtype: 'fieldcontainer',
                            reference: 'fieldTableType',
                            itemId: 'fieldTableType',
                            layout: 'hbox',
                            anchor: '100%',
                            defaultType: 'radiofield',
                            items: [
                                {
                                    boxLabel: 'Managed',
                                    reference: 'rdoManaged',
                                    itemId: 'rdoManaged',
                                    inputValue: 'MANAGED_TABLE',
                                    name: 'tableType',
                                    value: 'MANAGED_TABLE',
                                    flex: 1
                                },
                                {
                                    boxLabel: 'External',
                                    reference: 'rdoExternal',
                                    itemId: 'rdoExternal',
                                    inputValue: 'EXTERNAL_TABLE',
                                    name: 'tableType',
                                    flex: 1
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            layout: 'hbox',
                            reference: 'locationContainer',
                            disabled: true,
                            items: [
                                {
                                    xtype: 'textfield',
                                    name: 'location',
                                    reference: 'locationTextField',
                                    fieldLabel: message.msg('common.location'),
                                    margin: '0 3 0 0',
                                    flex: 1
                                },
                                {
                                    xtype: 'button',
                                    text: message.msg('common.browse'),
                                    width: 60,
                                    reference: 'browseButton',
                                    itemId: 'btnBrowse',
                                    handler: 'onBtnBrowseClick'
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    title: message.msg('hive.delimiter_type'),
                    layout: 'anchor',
                    items: [
                        {
                            xtype: 'fieldcontainer',
                            itemId: 'fieldRowformat',
                            layout: 'hbox',
                            anchor: '100%',
                            defaultType: 'radiofield',
                            items: [
                                {
                                    boxLabel: message.msg('hive.delimiter'),
                                    reference: 'rdoDelimiter',
                                    inputValue: 'delimiter',
                                    name: 'rowformat',
                                    value: 'delimiter',
                                    flex: 1
                                },
                                {
                                    boxLabel: 'SERDE',
                                    reference: 'rdoSerde',
                                    inputValue: 'serde',
                                    name: 'rowformat',
                                    flex: 1
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            reference: 'fieldDelimiter',
                            layout: {
                                type: 'vbox',
                                align: 'stretch'
                            },
                            items: [
                                {
                                    xtype: 'fieldcontainer',
                                    layout: 'hbox',
                                    anchor: '100%',
                                    border: false,
                                    flex: 1,
                                    items: [
                                        {
                                            xtype: 'textfield',
                                            reference: 'txField',
                                            labelWidth: 80,
                                            flex: 1,
                                            margin: '0 2 0 0',
                                            fieldLabel: message.msg('hive.delimiter_field'),
                                            name: 'field',
                                            editable: false
                                        },
                                        {
                                            xtype: 'button',
                                            iconCls: 'common-search',
                                            margin: '0 4 0 0',
                                            handler: 'onBtnDelimiterClick',
                                            target: 'field'
                                        },
                                        {
                                            xtype: 'textfield',
                                            reference: 'txLine',
                                            labelWidth: 80,
                                            flex: 1,
                                            margin: '0 2 0 4',
                                            fieldLabel: message.msg('hive.delimiter_line'),
                                            name: 'lines',
                                            editable: false
                                        },
                                        {
                                            xtype: 'button',
                                            iconCls: 'common-search',
                                            handler: 'onBtnDelimiterClick',
                                            target: 'line'
                                        }
                                    ]
                                },
                                {
                                    xtype: 'fieldcontainer',
                                    layout: 'hbox',
                                    anchor: '100%',
                                    flex: 1,
                                    items: [
                                        {
                                            xtype: 'textfield',
                                            reference: 'txMap',
                                            labelWidth: 80,
                                            flex: 1,
                                            margin: '0 2 0 0',
                                            fieldLabel: message.msg('hive.delimiter_map'),
                                            name: 'mapkeys',
                                            editable: false
                                        },
                                        {
                                            xtype: 'button',
                                            iconCls: 'common-search',
                                            margin: '0 4 0 0',
                                            target: 'map',
                                            handler: 'onBtnDelimiterClick'
                                        },
                                        {
                                            xtype: 'textfield',
                                            reference: 'txCollection',
                                            labelWidth: 80,
                                            flex: 1,
                                            padding: '0 2 0 4',
                                            fieldLabel: message.msg('hive.delimiter_collection'),
                                            name: 'collection',
                                            editable: false
                                        },
                                        {
                                            xtype: 'button',
                                            iconCls: 'common-search',
                                            target: 'collection',
                                            handler: 'onBtnDelimiterClick'
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            reference: 'fieldSerde',
                            hidden: true,
                            layout: 'anchor',
                            items: [
                                {
                                    xtype: 'textfield',
                                    anchor: '100%',
                                    reference: 'txSerde',
                                    name: 'serde'
                                },
                                {
                                    xtype: 'displayfield',
                                    value: message.msg('hive.msg.enter_serde_class')
                                }
                            ]
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    title: message.msg('hive.io_format'),
                    layout: 'anchor',
                    defaults: {
                        margin: '0 0 5 0'
                    },
                    items: [
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            items: [
                                {
                                    xtype: 'checkbox',
                                    margin: '0 5 0 0',
                                    listeners: {
                                        change: 'onCbxInputChange'
                                    }
                                },
                                {
                                    xtype: 'textfield',
                                    reference: 'txInputFormat',
                                    fieldLabel: message.msg('hive.input_format'),
                                    name: 'inputFormat',
                                    allowBlank: false,
                                    flex: 1,
                                    value: 'org.apache.hadoop.mapred.TextInputFormat',
                                    disabled: true
                                }
                            ]
                        },
                        {
                            xtype: 'container',
                            layout: 'hbox',
                            items: [
                                {
                                    xtype: 'checkbox',
                                    margin: '0 5 0 0',
                                    listeners: {
                                        change: 'onCbxOutputChange'
                                    }
                                },
                                {
                                    xtype: 'textfield',
                                    reference: 'txOutputFormat',
                                    fieldLabel: message.msg('hive.output_format'),
                                    name: 'outputFormat',
                                    allowBlank: false,
                                    flex: 1,
                                    value: 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat',
                                    disabled: true
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'tabpanel',
            region: 'center',
            flex: 1,
            layout: 'fit',
            border: false,
            items: [
                {
                    title: message.msg('common.columns'),
                    border: false,
                    layout: 'fit',
                    tbar: [
                        '->',
                        {
                            text: message.msg('common.add'),
                            handler: 'onColumnAddClick',
                            iconCls: 'common-add'
                        },
                        {
                            text: message.msg('common.delete'),
                            handler: 'onColumnRemoveClick',
                            iconCls: 'common-remove'
                        }
                    ],
                    items: [
                        {
                            reference: 'columnGrid',
                            xtype: 'metastoreColumngrid',
                            bind: {
                                store: '{columns}'
                            }
                        }
                    ]
                },
                {
                    title: message.msg('common.partition'),
                    border: false,
                    layout: 'fit',
                    tbar: [
                        '->',
                        {
                            text: message.msg('common.add'),
                            handler: 'onPartitionAddClick',
                            iconCls: 'common-add'
                        },
                        {
                            text: message.msg('common.delete'),
                            handler: 'onPartitionRemoveClick',
                            iconCls: 'common-remove'
                        }
                    ],
                    items: [
                        {
                            reference: 'partitionGrid',
                            xtype: 'metastoreColumngrid',
                            bind: {
                                store: '{partitions}'
                            }
                        }
                    ]
                },
                {
                    xtype: 'grid',
                    reference: 'grdProperty',
                    title: message.msg('hive.table.properties'),
                    border: false,
                    columnLines: true,
                    layout: 'fit',
                    bind: {
                        store: '{dbproperties}'
                    },
                    tbar: [
                        '->',
                        {
                            text: message.msg('common.add'),
                            handler: 'onPropertiesAddClick',
                            iconCls: 'common-add'
                        },
                        {
                            text: message.msg('common.delete'),
                            handler: 'onPropertiesRemoveClick',
                            iconCls: 'common-remove'
                        }
                    ],
                    columns: [{
                        text: message.msg('common.key'),
                        dataIndex: 'key',
                        editor: 'textfield',
                        flex: 1
                    }, {
                        text: message.msg('common.value'),
                        dataIndex: 'value',
                        editor: 'textfield',
                        flex: 1
                    }],
                    plugins: {
                        ptype: 'cellediting',
                        clicksToEdit: 1
                    }
                }
            ]
        }
    ],
    buttons: [
        {
            bind: {
                text: '{btnOKText}'
            },
            handler: 'onBtnCreateClick',
            iconCls: 'common-ok'
        },
        {
            text: message.msg('common.cancel'),
            handler: 'onBtnCancelClick',
            iconCls: 'common-cancel'
        }
    ],
    listeners: {
        afterrender: 'onAfterrender'
    }
});
