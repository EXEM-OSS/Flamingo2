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
Ext.define('Flamingo2.view.monitoring.datanode.Datanode', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.datanode',

    requires: [
        'Flamingo2.view.monitoring.datanode.DatanodeController',
        'Flamingo2.view.monitoring.datanode.DatanodeModel',

        'Flamingo2.view.monitoring.datanode.Livenodes',
        'Flamingo2.view.monitoring.datanode.Deadnodes',
        'Flamingo2.view.monitoring.datanode.Decommissioningnodes'
    ],

    flex: 1,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    controller: 'datanodeController',

    viewModel: {
        type: 'datanodeModel'
    },

    items: [
        {
            iconCls: 'common-view',
            border: true,
            xtype: 'liveNodes',
            title: message.msg('monitoring.datanode.live_datanode'),
            itemId: 'live',
            flex: 3
        },
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
                    iconCls: 'common-view',
                    border: true,
                    xtype: 'deadNodes',
                    title: message.msg('monitoring.datanode.dead_datanode'),
                    itemId: 'dead',
                    flex: 1,
                    margin: '0 5 0 0'
                },
                {
                    iconCls: 'common-view',
                    border: true,
                    xtype: 'decommissioningNodes',
                    title: message.msg('monitoring.datanode.decommission_datanode'),
                    flex: 1,
                    itemId: 'decommissioning'
                }
            ]
        }
    ]
});
