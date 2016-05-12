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
Ext.define('Flamingo2.view.monitoring.namenode.NamenodeChart', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.namenodeChart',

    requires: [
        'Flamingo2.view.monitoring.namenode.BlockStatus',
        'Flamingo2.view.monitoring.namenode.DatanodeStatus',
        'Flamingo2.view.monitoring.namenode.CldbUsage',
        'Flamingo2.view.monitoring.namenode.JvmHeapUsage',
        'Flamingo2.view.monitoring.namenode.FileCount',
        'Flamingo2.view.monitoring.namenode.BlockCount'
    ],

    layout: {
        type: 'table',
        columns: 2,
        tableAttrs: {
            style: {
                width: '100%'
            }
        }
    },

    border: false,

    items: [
        {
            title: message.msg('monitoring.namenode.usage'),
            iconCls: 'common-view',
            height: 250,
            margin: '5 5 0 0',
            layout: 'fit',
            border: 1,
            xtype: 'hdfsUsage'
        },
        {
            title: message.msg('monitoring.namenode.jvm_heap'),
            iconCls: 'common-view',
            height: 250,
            margin: '5 0 0 5',
            layout: 'fit',
            border: 1,
            xtype: 'jvmHeapUsage'
        },
        {
            title: message.msg('monitoring.namenode.block_status'),
            iconCls: 'common-view',
            height: 250,
            margin: '5 0 0 0',
            layout: 'fit',
            border: 1,
            colspan: 2,
            xtype: 'blockStatus'
        },
        {
            title: message.msg('monitoring.namenode.total_file_count'),
            iconCls: 'common-view',
            height: 250,
            margin: '5 5 0 0',
            layout: 'fit',
            border: 1,
            xtype: 'fileCount'
        },
        {
            title: message.msg('monitoring.namenode.total_block_count'),
            iconCls: 'common-view',
            margin: '5 0 0 5',
            height: 250,
            layout: 'fit',
            border: 1,
            xtype: 'blockCount'
        },
        {
            title: message.msg('monitoring.namenode.datanode'),
            iconCls: 'common-view',
            height: 250,
            margin: '5 0 0 0',
            layout: 'fit',
            border: 1,
            colspan: 2,
            xtype: 'datanodeStatus'
        }
    ]
});