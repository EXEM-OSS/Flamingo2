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
Ext.define('Flamingo2.view.tajo.metastore._CreateTable', {
    extend: 'Ext.window.Window',
    alias: 'widget.tajoCreateTable',

    requires: [
        'Flamingo2.view.tajo.metastore._CreateTableController',
        'Flamingo2.view.tajo.metastore.MetastoreModel',
        'Flamingo2.view.tajo.metastore._ColumnGrid'
    ],

    config: {
        alter: false,
        database: null,
        table: null
    },
    hdfs: false,

    controller: 'tajoMetastoreCreateTableController',

    viewModel: {
        type: 'tajoMetastoreModel'
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
                                    boxLabel: 'Base',
                                    reference: 'rdoManaged',
                                    inputValue: 'BASE_TABLE',
                                    name: 'tableType',
                                    disabled: true,
                                    flex: 1
                                },
                                {
                                    boxLabel: 'External',
                                    reference: 'rdoExternal',
                                    inputValue: 'EXTERNAL_TABLE',
                                    value: 'EXTERNAL_TABLE',
                                    name: 'tableType',
                                    flex: 1
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            layout: 'hbox',
                            reference: 'locationContainer',
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
                    title: message.msg('tajo.fileType'),
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [{
                        xtype: 'fieldcontainer',
                        defaultType: 'radiofield',
                        itemId: 'fileType',
                        defaults: {
                            flex: 1
                        },
                        layout: 'hbox',
                        items: [
                            {
                                boxLabel: 'Text',
                                name: 'storeType',
                                inputValue: 'TEXTFILE',
                                value: 'TEXTFILE'
                            },{
                                boxLabel: 'RCFile',
                                name: 'storeType',
                                inputValue: 'RCFILE'
                            },{
                                boxLabel: 'Parquet',
                                name: 'storeType',
                                inputValue: 'PARQUET'
                            },{
                                boxLabel: 'SequenceFile',
                                name: 'storeType',
                                inputValue: 'SEQUENCEFILE'
                            }
                        ]
                    },{
                        xtype: 'container',
                        reference: 'fileType',
                        layout: 'fit',
                        items: [{
                            xtype: 'tajoMetaTextFileFormat'
                        }]
                    }]
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
                            xtype: 'tajoMetastoreColumngrid',
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
                            xtype: 'tajoMetastoreColumngrid',
                            bind: {
                                store: '{partitions}'
                            }
                        }
                    ]
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
