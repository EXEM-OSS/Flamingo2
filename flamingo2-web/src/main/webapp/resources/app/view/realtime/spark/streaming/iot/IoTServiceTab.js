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
Ext.define('Flamingo2.view.realtime.spark.streaming.iot.IoTServiceTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.ioTServiceTab',

    items: [
        {
            xtype: 'grid',
            id: 'data',
            itemId: 'ioTServiceColumnGrid',
            title: '컬럼정보',
            bind: {
                store: '{ioTServiceColumnStore}'
            },
            border: true,
            columns: [
                {text: '컬럼명', dataIndex: 'columnName', width: 80, align: 'center'},
                {text: '자료형', dataIndex: 'columnType', width: 80, align: 'center'},
                {xtype: 'checkcolumn', text: '필터링', dataIndex: 'filtering', flex: 1},
                {xtype: 'checkcolumn', text: '마스킹', dataIndex: 'masking', flex: 1}
            ],
            viewConfig: {
                enableTextSelection: true,
                columnLines: true,
                stripeRows: true,
                getRowClass: function (b, e, d, c) {
                    return 'cell-height-30';
                }
            }
        },
        {
            xtype: 'grid',
            id: 'custom',
            itemId: 'ioTServiceCustomColumnGrid',
            title: '커스텀 컬럼정보',
            bind: {
                store: '{ioTServiceCustomColumnStore}'
            },
            border: true,
            columns: [
                {text: '컬럼명', dataIndex: 'columnName', width: 80, align: 'center'},
                {text: '자료형', dataIndex: 'columnType', width: 80, align: 'center'},
                {xtype: 'checkcolumn', text: '필터링', dataIndex: 'filtering', flex: 1},
                {xtype: 'checkcolumn', text: '마스킹', dataIndex: 'masking', flex: 1}
            ],
            viewConfig: {
                enableTextSelection: true,
                columnLines: true,
                stripeRows: true,
                getRowClass: function (b, e, d, c) {
                    return 'cell-height-30';
                }
            }
        }
    ],
    listeners: {
        tabchange: 'onIoTServiceTabChanged',
        containercontextmenu: function (tree, event) {
            event.stopEvent();
        }
    }
});