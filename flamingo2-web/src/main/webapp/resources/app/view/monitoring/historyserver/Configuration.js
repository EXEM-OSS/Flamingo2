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
Ext.define('Flamingo2.view.monitoring.historyserver.Configuration', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.historyServerConfiguration',

    viewConfig: {
        enableTextSelection: true,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    },

    bind: {
        store: '{configurationStore}'
    },

    columns: [
        {
            text: message.msg('common.name'), dataIndex: 'name', flex: 1, style: 'text-align:center', sortable: true
        },
        {
            text: message.msg('common.value'), dataIndex: 'value', flex: 1, style: 'text-align:center', sortable: true,
            renderer: function (value, metaData, record, rowIdx, colIdx, store) {
                metaData.tdAttr = 'data-qtip="' + value + '"';
                return value;
            }
        },
        {
            text: message.msg('common.file'), dataIndex: 'source', flex: 1, style: 'text-align:center', hidden: true,
            renderer: function (value, metaData, record, rowIdx, colIdx, store) {
                metaData.tdAttr = 'data-qtip="' + value + '"';
                return value;
            }
        }
    ],

    initComponent: function () {
        var me = this;
        me.callParent(arguments);
    }
});