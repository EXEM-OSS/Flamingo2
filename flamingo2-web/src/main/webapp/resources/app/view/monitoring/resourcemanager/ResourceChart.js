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
Ext.define('Flamingo2.view.monitoring.resourcemanager.ResourceChart', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.resourceChart',

    requires: [
        'Flamingo2.view.monitoring.resourcemanager.JvmHeapUsage',
        'Flamingo2.view.monitoring.resourcemanager.NodeStatus',
        'Flamingo2.view.monitoring.resourcemanager.ContainerStatus',
        'Flamingo2.view.monitoring.resourcemanager.ApplicationStatus'
    ],

    listeners: {
        afterrender: function (view) {
            setTableLayoutFixed(view);
        }
    },

    layout: {
        type: 'vbox'
    },

    items: [{
        xtype: 'container',
        layout: 'hbox',
        width: '100%',
        items: [
            {
                title: message.msg('monitoring.clusternode.nodes.node_status'),
                iconCls: 'fa fa-server fa-fw',
                xtype: 'nodeStatus',
                margin: '5 0 0 0',
                flex: 1,
                border: 1
            },
            {
                title: message.msg('monitoring.rm.app_status'),
                iconCls: 'fa fa-server fa-fw',
                xtype: 'applicationStatus',
                margin: '5 0 0 5',
                flex: 1,
                border: 1
            }
        ]
    }, {
        xtype: 'container',
        layout: 'hbox',
        width: '100%',
        items: [
            {
                title: message.msg('monitoring.rm.container_status'),
                iconCls: 'fa fa-server fa-fw',
                xtype: 'containerStatus',
                margin: '5 0 0 0',
                flex: 1,
                border: 1
            },
            {
                title: message.msg('batch.jvm_heap_usage'),
                iconCls: 'fa fa-server fa-fw',
                xtype: 'rmJvmHeapUsage',
                margin: '5 0 0 5',
                flex: 1,
                border: 1
            }
        ]
    }
    ]
});