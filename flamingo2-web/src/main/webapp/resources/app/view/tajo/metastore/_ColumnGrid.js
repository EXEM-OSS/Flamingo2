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
Ext.define('Flamingo2.view.tajo.metastore._ColumnGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.tajoMetastoreColumngrid',

    requires: [
        'Ext.grid.plugin.CellEditing',
        'Ext.grid.selection.SpreadsheetModel',
        'Ext.grid.plugin.Clipboard',
        'Flamingo2.model.hive.StructType',
        'Flamingo2.model.hive.ColumnInfo'
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
            flex: 3,
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
            flex: 1,
            sortable: false,
            editor: {
                xtype: 'combo',
                allowBlank: false,
                editable: false,
                queryMode: 'local',
                bind: {
                    store: '{dataType}'
                },
                valueField: 'typeValue',
                displayField: 'typeString',
                listClass: 'x-combo-list-small'
            },
            renderer: function(value, metaData, record) {
                return this.dataType[value];
            }
        }
    ],
    plugins: {
        ptype: 'cellediting',
        clicksToEdit: 1
    },
    dataType: {
        'BOOLEAN': 'boolean',
        'INT1': 'tinyint',
        'INT2': 'smallint',
        'INT4': 'integer',
        'INT8': 'bigint',
        'INT8': 'real',
        'FLOAT4': 'float',
        'FLOAT8': 'double',
        'TEXT': 'text',
        'BLOB': 'blob',
        'DATE': 'date',
        'TIME': 'time',
        'TIMESTAMP': 'timestamp',
        'INET4': 'inet4'
    }

});