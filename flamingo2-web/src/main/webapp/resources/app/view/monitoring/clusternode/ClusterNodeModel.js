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
Ext.define('Flamingo2.view.monitoring.clusternode.ClusterNodeModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.clusterNodeModel',

    data: {
        title: message.msg('monitoring.clusternode.title')
    },

    stores: {
        chartSumStore: {
            fields: ['vcoreSum', 'memorySum', {name: 'timestamp', convert: convertDateTime}],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.CLUSTERNODES.TIMESERIES,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        },

        nodesStore: {
            autoLoad: false,
            fields: [
                'rackName',
                'usedVcores',
                'httpAddress',
                'usedMemory',
                'nodeState',
                'capacityMemory',
                'lastHealthUpdated',
                'numContainers',
                'healthReport',
                'capacityVcores',
                'nodeId'
            ],
            groupField: 'nodeState',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.CLUSTERNODES.NODES,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            sorters: [
                {
                    property: 'id',
                    direction: 'asc'
                }
            ]
        }
    }
});