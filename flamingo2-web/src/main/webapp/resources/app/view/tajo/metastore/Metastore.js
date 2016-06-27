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
Ext.define('Flamingo2.view.tajo.metastore.Metastore', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.tajoMetastoreViewport',

    requires: [
        'Flamingo2.view.tajo.metastore.MetastoreController',
        'Flamingo2.view.tajo.metastore.MetastoreModel',
        'Flamingo2.view.tajo.metastore._CreateTable'
    ],

    controller: 'tajoMetastoreController',
    viewModel: {
        type: 'tajoMetastoreModel'
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    bodyPadding: 5,
    items: [{
        xtype: 'fieldcontainer',
        layout: 'hbox',
        items: [{
            xtype: 'combobox',
            fieldLabel: message.msg('common.database'),
            reference: 'cbxDatabase',
            labelWidth: 85,
            editable: false,
            queryMode: 'local',
            flex: 1,
            displayField: 'database',
            valueField: 'database',
            bind: {
                store: '{databases}'
            },
            listeners: {
                select: 'onDatabaseSelect'
            }
        }, {
            xtype: 'splitbutton',
            text: message.msg('common.menu'),
            margin: '0 0 0 5',
            menu: [{
                text: message.msg('common.refresh'),
                iconCls: 'common-refresh',
                handler: 'onRefreshClick'
            }, '-', {
                text: message.msg('hive.database.create'),
                iconCls: 'common-database-add',
                handler: 'onCreateDatabaseClick'
            }, {
                text: message.msg('hive.database.drop'),
                iconCls: 'common-database-remove',
                handler: 'onDropDatabaseClick'
            }]
        }]

    }, {
        xtype: 'grid',
        border: true,
        columnLines: true,
        reference: 'grdTable',
        flex: 1,
        bind: {
            store: '{tables}'
        },
        columns: [{
            text: message.msg('hive.table.name'),
            dataIndex: 'tableName',
            flex: 1
        }, {
            text: message.msg('hive.table.keyword'),
            dataIndex: 'tableType',
            width: 100,
            align: 'center'
        }],
        tbar: ['->', {
            xtype: 'button',
            text: message.msg('hive.table.create'),
            reference: 'btnTableAdd',
            iconCls: 'common-table-add',
            disabled: true,
            handler: 'onTableAddClick'
        }, {
            xtype: 'button',
            text: message.msg('hive.table.drop'),
            reference: 'btnTableRemove',
            iconCls: 'common-table-remove',
            disabled: true,
            handler: 'onTableRemoveClick'
        }],
        listeners: {
            rowclick: 'onGrdTableSelect',
            containercontextmenu: 'onGrdTableContainercontextmenu',
            rowcontextmenu: 'onGrdTableRowcontextmenu'
        },
        plugins: 'clipboard',
        selModel: {
            type: 'spreadsheet',
            // Disables sorting by header click, though it will be still available via menu
            columnSelect: true,
            rowNumbererHeaderWidth: 0,
            mode: 'SIMPLE'
        }
    }, {
        xtype: 'tabpanel',
        flex: 1,
        items: [{
            title: message.msg('common.columns'),
            xtype: 'grid',
            border: true,
            columnLines: true,
            emptyText: message.msg('hive.msg.does_not_have_column'),
            reference: 'grdColumns',
            bind: {
                store: '{columns}'
            },
            columns: [{
                text: message.msg('hive.column.name'),
                dataIndex: 'name',
                flex: 1
            }, {
                text: message.msg('hive.column.type'),
                dataIndex: 'type',
                align: 'center'
            }],
            plugins: 'clipboard',
            selModel: {
                type: 'spreadsheet',
                // Disables sorting by header click, though it will be still available via menu
                columnSelect: true,
                rowNumbererHeaderWidth: 0
            }
        }, {
            title: message.msg('common.partition'),
            xtype: 'grid',
            border: true,
            columnLines: true,
            emptyText: message.msg('hive.msg.does_not_have_partition'),
            reference: 'grdPartitions',
            bind: {
                store: '{partitions}'
            },
            columns: [{
                text: message.msg('hive.column.name'),
                dataIndex: 'name',
                flex: 1
            },{
                text: message.msg('hive.column.type'),
                align: 'center',
                dataIndex: 'type'
            }],
            plugins: 'clipboard',
            selModel: {
                type: 'spreadsheet',
                // Disables sorting by header click, though it will be still available via menu
                columnSelect: true,
                rowNumbererHeaderWidth: 0
            }
        }]
    }, {
        xtype: 'menu',
        reference: 'rowContextMenu',
        items: [{
            text: message.msg('common.refresh'),
            handler: 'onTableRefreshClick'
        }, {
            text: message.msg('tajo.table.create'),
            handler: 'onTableCreateClick'
        }, {
            text: message.msg('tajo.alter_table'),
            handler: 'onTableAlterClick'
        }, {
            text: message.msg('hawq.button.table.drop'),
            handler: 'onTableDropClick'
        }]
    }, {
        xtype: 'menu',
        reference: 'containerContextMenu',
        items: [{
            text: message.msg('common.refresh'),
            handler: 'onTableRefreshClick'
        }, {
            text: message.msg('tajo.table.create'),
            handler: 'onTableCreateClick'
        }]
    }],

    listeners: {
        afterrender: 'onAfterrender',
        hdfsclose: 'onHdfsclose'
    }
});

