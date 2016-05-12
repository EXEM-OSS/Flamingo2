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
Ext.define('Flamingo2.view.system.authority.HdfsBrowserTree', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.hdfsBrowserTreePanel',

    bind: {
        store: '{hdfsTreeStore}'
    },

    border: true,
    title: message.msg('system.authority.hdfs'),
    width: 450,
    layout: 'fit',
    dockedItems: [
        {
            xtype: 'toolbar',
            items: [
                '->',
                {
                    text: message.msg('system.authority.common.add'),
                    iconCls: 'common-add',
                    tooltip: message.msg('system.authority.add.tip'),
                    handler: 'onAddHdfsBrowserAuth'
                },
                '-',
                {
                    text: message.msg('common.refresh'),
                    iconCls: 'common-refresh',
                    tooltip: message.msg('system.authority.refresh.tip'),
                    handler: 'onRefreshHdfsBrowserTree'
                }
            ]
        }
    ],
    listeners: {
        itemcontextmenu: function (tree, record, item, index, event) {
            event.stopEvent();
        },
        containercontextmenu: function (tree, event) {
            event.stopEvent();
        }
    }
});