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
Ext.define('Flamingo2.view.hive.metastore._ColumnGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.metastoreColumngrid',

    requires: [
        'Ext.grid.plugin.CellEditing',
        'Ext.grid.selection.SpreadsheetModel',
        'Ext.grid.plugin.Clipboard',
        'Flamingo2.model.hive.StructType',
        'Flamingo2.model.hive.ColumnInfo',
        'Flamingo2.view.hive.metastore._PropStruct',
        'Flamingo2.view.hive.metastore._PropMap',
        'Flamingo2.view.hive.metastore._PropArray'
    ],

    viewConfig: {
        enableTextSelection: true,
        stripeRows: true,
        columnLines: true
    },
    columns: [
        {
            text: message.msg('hive.column_name'),
            dataIndex: 'name',
            flex: 1,
            sortable: false,
            editor: {
                vtype: 'alphanum',
                allowBlank: false,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        },
        {
            text: message.msg('hive.column.type'),
            dataIndex: 'type',
            width: 100,
            sortable: false,
            editor: {
                xtype: 'combo',
                allowBlank: false,
                editable: false,
                queryMode: 'local',
                store: Ext.create('Ext.data.Store', {
                    fields: ['typeId', 'typeString'],
                    data: [
                        ['tinyint', 'tinyint'],
                        ['smallint', 'smallint'],
                        ['int', 'int'],
                        ['bigint', 'bigint'],
                        ['boolean', 'boolean'],
                        ['float', 'float'],
                        ['double', 'double'],
                        ['string', 'string'],
                        ['timestamp', 'timestamp'],
                        ['binary', 'binary'],
                        ['struct', 'struct'],
                        ['map', 'map'],
                        ['array', 'array']
                    ]
                }),
                valueField: 'typeString',
                displayField: 'typeString',
                listClass: 'x-combo-list-small'
            }
        },
        {
            xtype: 'actioncolumn',
            itemId: 'collection',
            width: 25,
            items: [
                {
                    getClass: function (v, meta, record) {
                        var type = record.get('type');

                        if (type == 'struct' || type == 'map' || type == 'array') {
                            return 'common-search';
                        }
                    },
                    handler: 'onActionColumnClick'
                }
            ]
        },
        {
            text: message.msg('common.comment'),
            dataIndex: 'comment',
            flex: 1,
            sortable: false,
            editor: {
                allowBlank: true
            }
        }
    ],
    plugins: {
        ptype: 'cellediting',
        clicksToEdit: 1
    }

});