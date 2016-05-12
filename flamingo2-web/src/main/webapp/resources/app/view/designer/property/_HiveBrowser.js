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
 * Inner Grid : KeyValue
 *
 * @class
 * @extends Flamingo2.view.designer.property._Grid
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.designer.property._HiveBrowser', {
    extend: 'Ext.panel.Panel',
    alias: 'widget._hiveBrowser',

    requires: [
        'Flamingo2.view.designer.property._HiveTableCreator'
    ],

    border: true,

    layout: 'border',

    initComponent: function () {
        this.items = [
            {
                region: 'west',
                border: false,
                collapsible: true,
                split: true,
                title: message.msg('workflow.common.hivedatabase'),
                width: 200,
                layout: 'fit',
                items: [
                    {
                        itemId: 'hiveBrowserTree',
                        border: false,
                        xtype: 'treepanel',
                        rootVisible: true,
                        useArrows: true,
                        store: Ext.create('Ext.data.TreeStore', {
                            autoLoad: false,
                            proxy: {
                                type: 'ajax',
                                url: CONSTANTS.DESIGNER.HIVE_DBS,
                                headers: {
                                    'Accept': 'application/json'
                                },
                                reader: {
                                    type: 'json',
                                    root: 'list'
                                }
                            },
                            root: {
                                text: 'Hive Databases',
                                id: '/',
                                expanded: true
                            },
                            folderSort: true,
                            sorters: [
                                {
                                    property: 'text',
                                    direction: 'ASC'
                                }
                            ]
                        }),
                        dockedItems: [
                            {
                                xtype: 'toolbar',
                                items: [
                                    {
                                        text: message.msg('common.create'),
                                        itemId: 'createButton',
                                        tooltip: message.msg('hive.table.create'),
                                        handler: function () {
                                            var win = Ext.create('Ext.window.Window', {
                                                title: message.msg('hive.table.create'),
                                                width: 500,
                                                height: 500,
                                                layout: 'fit',
                                                modal: true,
                                                closeAction: 'hide',
                                                buttons: [
                                                    {
                                                        text: message.msg('common.confirm'),

                                                        handler: function () {
                                                            var tableName = Ext.ComponentQuery.query('_hiveTableCreator #tableName')[0];
                                                            var comment = Ext.ComponentQuery.query('_hiveTableCreator #comment')[0];
                                                            var location = Ext.ComponentQuery.query('_hiveTableCreator #location')[0];
                                                            var delimiter = Ext.ComponentQuery.query('_hiveTableCreator #delimiter')[0];

                                                            var columnGrid = Ext.ComponentQuery.query('_hiveTableCreator #columnGrid')[0];
                                                            var partitionGrid = Ext.ComponentQuery.query('_hiveTableCreator #partitionGrid')[0];

                                                            var columns = [], partitions = [];

                                                            partitionGrid.getStore().each(function (record, idx) {
                                                                partitions.push({
                                                                    name: record.get('columnNames'),
                                                                    type: record.get('columnTypes'),
                                                                    comment: record.get('columnDescriptions')
                                                                });
                                                            });

                                                            columnGrid.getStore().each(function (record, idx) {
                                                                columns.push({
                                                                    name: record.get('columnNames'),
                                                                    type: record.get('columnTypes'),
                                                                    comment: record.get('columnDescriptions')
                                                                });
                                                            });

                                                            var body = {
                                                                tableName: tableName.getValue(),
                                                                comment: comment.getValue(),
                                                                location: location.getValue(),
                                                                delimiter: delimiter.query('#delimiterValue')[0].getRawValue(),
                                                                columns: columns,
                                                                partitions: partitions
                                                            };
                                                        }
                                                    },
                                                    {
                                                        text: message.msg('common.cancel'),

                                                        handler: function () {
                                                            win.close();
                                                        }
                                                    }
                                                ],
                                                items: [
                                                    {
                                                        xtype: '_hiveTableCreator'
                                                    }
                                                ]
                                            }).show();
                                        }
                                    },
                                    {
                                        text: message.msg('hive.table.drop'),
                                        itemId: 'dropButton',
                                        tooltip: 'Drop a table',
                                        handler: function () {
                                        }
                                    },
                                    '->',
                                    {
                                        text: message.msg('common.refresh'),
                                        iconCls: 'common_refresh',
                                        itemId: 'refreshButton',
                                        tooltip: message.msg('fs.hdfs.tip_file_refresh'),
                                        handler: function () {
                                            var treeStore = Ext.ComponentQuery.query('#hiveBrowserTree')[0].getStore();
                                            treeStore.load();

                                            var gridStore = Ext.ComponentQuery.query('#hiveBrowserListGrid')[0].getStore();
                                            gridStore.removeAll();
                                        }
                                    }
                                ]
                            }
                        ],
                        listeners: {
                            afterrender: function (comp, opts) {
                                var treeStore = Ext.ComponentQuery.query('#hiveBrowserTree')[0].getStore();
                                treeStore.load();
                            },
                            itemclick: function (view, node, item, index, event, opts) {
                                var gridStore = Ext.ComponentQuery.query('#hiveBrowserListGrid')[0].getStore();
                                gridStore.load(
                                    {
                                        scope: this,
                                        params: {
                                            'node': node.data.parentId,
                                            'table': node.data.text
                                        }
                                    }
                                );
                            }
                        }
                    }
                ]
            },
            {
                region: 'center',
                title: message.msg('common.column'),
                border: false,
                layout: 'fit',
                items: [
                    {
                        itemId: 'hiveBrowserListGrid',
                        xtype: 'grid',
                        border: false,
                        stripeRows: true,
                        columnLines: true,
                        viewConfig: {
                            enableTextSelection: true
                        },
                        columns: [
                            {text: 'Table ID', width: 60, dataIndex: 'columnId', hidden: true},
                            {text: 'Name', flex: 2, sortable: true, dataIndex: 'name'},
                            {text: 'Type', flex: 1, dataIndex: 'type', align: 'center'},
                            {text: 'Comment', flex: 2, dataIndex: 'comment'},
                            {text: 'Index', width: 50, dataIndex: 'index', align: 'center', hidden: true}
                        ],
                        store: Ext.create('Ext.data.Store', {
                            fields: ['columnId', 'name', 'type', 'comment', 'index'],
                            autoLoad: false,
                            proxy: {
                                type: 'ajax',
                                url: CONSTANTS.DESIGNER.HIVE_COLUMNS,
                                headers: {
                                    'Accept': 'application/json'
                                },
                                reader: {
                                    type: 'json',
                                    root: 'list'
                                }
                            }
                        })
                    }
                ]
            }
        ];

        this.callParent(arguments);
    }
});