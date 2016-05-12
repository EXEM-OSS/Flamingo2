/*
 * Copyright (C) 2011  Flamingo Project (http://www.cloudine.io).
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
Ext.define('Flamingo2.view.system.authority.HdfsBrowserAuthGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.hdfsBrowserAuthGridPanel',

    bind: {
        store: '{hdfsBrowserAuthStore}'
    },

    title: message.msg('system.authority.pattern'),
    border: true,
    flex: 1,
    columns: [
        {
            xtype: 'rownumberer', width: 50, sortable: false
        },
        {
            text: message.msg('system.authority.common.patternId'),
            dataIndex: 'id',
            width: 50,
            align: 'center',
            hidden: true
        },
        {
            text: message.msg('common.path'), dataIndex: 'hdfs_path_pattern', flex: 0.5, align: 'center'
        },
        {
            text: message.msg('system.authority.common.authId'),
            dataIndex: 'auth_id',
            width: 50,
            align: 'center',
            hidden: true
        },
        {
            text: message.msg('system.authority.common.auth'), dataIndex: 'auth_name', width: 100, align: 'center'
        },
        {
            text: message.msg('system.authority.common.levelId'),
            dataIndex: 'level',
            width: 50,
            align: 'center',
            hidden: true
        },
        {
            text: message.msg('system.authority.common.level'), dataIndex: 'level_name', width: 80, align: 'center'
        }
    ],
    viewConfig: {
        enableTextSelection: true,
        columnLines: true,
        stripeRows: true,
        getRowClass: function () {
            return 'cell-height-30';
        }
    },
    dockedItems: [
        {
            xtype: 'toolbar',
            items: [
                {
                    xtype: 'tbfill'
                },
                {
                    text: message.msg('system.authority.common.delete'),
                    iconCls: 'common-delete',
                    tooltip: message.msg('system.authority.delete.tip'),
                    handler: 'onDeleteHdfsBrowserAuth'
                },
                '-',
                {
                    text: message.msg('common.refresh'),
                    iconCls: 'common-refresh',
                    tooltip: message.msg('system.authority.refresh.tip'),
                    handler: 'onRefreshHdfsBrowserAuthGrid'
                }
            ]
        }
    ],
    listeners: {
        select: 'onClickGridItem',
        itemcontextmenu: function (grid, record, item, index, event) {
            event.stopEvent();
        },
        containercontextmenu: function (grid, event) {
            event.stopEvent();
        }
    }
});