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
        'Flamingo2.view.monitoring.namenode.HdfsUsage',
        'Flamingo2.view.monitoring.namenode.JvmHeapUsage',
        'Flamingo2.view.monitoring.namenode.BlockStatus',
        'Flamingo2.view.monitoring.namenode.FileCount',
        'Flamingo2.view.monitoring.namenode.BlockCount',
        'Flamingo2.view.monitoring.namenode.DatanodeStatus'
    ],

    border: false,

    items: [
        {
            xtype: 'container',
            margin: '5 0 0 0',
            flex: 2,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'hdfsUsage',
                    title: message.msg('monitoring.namenode.usage'),
                    iconCls: 'common-view',
                    margin: '0 5 0 0',
                    flex: 1,
                    border: true
                },
                {
                    xtype: 'jvmHeapUsage',
                    title: message.msg('monitoring.namenode.jvm_heap'),
                    iconCls: 'common-view',
                    margin: '0 0 0 5',
                    flex: 1,
                    border: true
                }
            ]
        },
        {
            xtype: 'blockStatus',
            title: message.msg('monitoring.namenode.block_status'),
            iconCls: 'common-view',
            margin: '5 0 0 0',
            border: true
        },
        {
            xtype: 'fileCount',
            title: message.msg('monitoring.namenode.total_file_count'),
            iconCls: 'common-view',
            margin: '5 0 0 0',
            border: true
        },
        {
            xtype: 'blockCount',
            title: message.msg('monitoring.namenode.total_block_count'),
            iconCls: 'common-view',
            margin: '5 0 0 0',
            border: true
        },
        {
            xtype: 'datanodeStatus',
            title: message.msg('monitoring.namenode.datanode'),
            iconCls: 'common-view',
            margin: '5 0 0 0',
            border: true
        }
    ],
    listeners: {
        afterrender: 'onNamenodeChartAfterRender'
    }
});