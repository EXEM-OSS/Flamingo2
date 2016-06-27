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
Ext.define('Flamingo2.view.realtime.spark.streaming.iot.IoTServiceTree', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.ioTServiceTree',

    bind: {
        store: '{ioTServiceTreeStore}'
    },
    columns: [
        {
            xtype: 'treecolumn',
            text: '명칭',
            dataIndex: 'text',
            width: 150,
            sortable: true
        },
        {
            text: '식별자',
            dataIndex: 'nodeName',
            flex: 1,
            sortable: true
        }
    ],
    listeners: {
        afterrender: 'onIoTServiceTreeAfterRender',
        itemclick: 'onIoTServiceTreeItemClick',
        beforeitemclick: 'onBeforeIoTServiceTreeItemClick',
        itemcontextmenu: function (tree, record, item, index, e, eOpts) {
            e.stopEvent();
        },
        containercontextmenu: function (tree, event) {
            event.stopEvent();
        }
    }
});