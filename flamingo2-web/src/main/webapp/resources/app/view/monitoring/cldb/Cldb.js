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
/**
 * Created by Park on 15. 6. 12..
 */
Ext.define('Flamingo2.view.monitoring.cldb.Cldb', {
    extend: 'Flamingo2.panel.Panel',

    requires: [
        'Flamingo2.view.monitoring.cldb.CldbSummary',
        'Flamingo2.view.monitoring.cldb.JvmHeapUsage',
        'Flamingo2.view.monitoring.cldb.CldbUsage',
        'Flamingo2.view.monitoring.cldb.FileCount',
        'Flamingo2.view.monitoring.cldb.VolumeCount',
        'Flamingo2.view.monitoring.cldb.FileServerCount'
    ],

    viewModel: {
        type: 'cldb'
    },
    controller: 'cldb',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [
        /*{
         iconCls: 'common-view',
         border: true,
         title: message.msg('monitoring.cldb.title.summary'),
         xtype: 'cldbSummary',
         margin: '0 0 0 0'
         },*/
        {
            xtype: 'container',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [{
                xtype: 'cldbUsage',
                title: message.msg('monitoring.cldb.title.usage'),
                margin: '5 5 0 0',
                height: 250,
                border: 1,
                flex: 1
            }, {
                xtype: 'cldbJvmHeapUsage',
                title: message.msg('monitoring.cldb.title.jvm_heap_usage'),
                margin: '5 0 0 5',
                height: 250,
                border: 1,
                flex: 1
            }]
        },
        {
            xtype: 'container',
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [{
                xtype: 'cldbFileCount',
                title: message.msg('monitoring.cldb.title.file_count'),
                margin: '5 5 0 0',
                height: 250,
                border: 1,
                flex: 1
            }, {
                xtype: 'cldbVolumeCount',
                title: message.msg('monitoring.cldb.title.volume_count'),
                margin: '5 0 0 5',
                height: 250,
                border: 1,
                flex: 1
            }]
        },
        {
            xtype: 'cldbFileServerCount',
            title: message.msg('monitoring.cldb.title.file_server_count'),
            margin: '5 0 0 0',
            height: 250,
            border: 1
        }
    ]
});