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
Ext.define('Flamingo2.view.monitoring.clusternode.ClusterNode', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.clusterNode',

    controller: 'clusterNodeController',

    viewModel: {
        type: 'clusterNodeModel'
    },

    requires: [
        'Flamingo2.view.monitoring.clusternode.ClusterNodeController',
        'Flamingo2.view.monitoring.clusternode.ClusterNodeModel',

        'Flamingo2.view.monitoring.clusternode.VCoreSumChart',
        'Flamingo2.view.monitoring.clusternode.MemorySumChart',
        'Flamingo2.view.monitoring.clusternode.ClusterNodes'
    ],
    flex: 1,

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [
        {
            iconCls: 'fa fa-tasks fa-fw',
            margin: '0 0 10 0',
            flex: 1,
            border: true,
            xtype: 'clusterNodes'
        }
    ],

    listeners: {
        engineChanged: 'onEngineChanged'
    }

});