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
Ext.define('Flamingo2.view.monitoring.datanode.DatanodeModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.datanodeModel',

    data: {
        title: message.msg('monitoring.datanode.title')
    },

    stores: {
        deadNodesStore: {
            autoLoad: false,
            fields: [
                'hostname',
                'ipAddr',
                'decommissioned'
            ],
            proxy: {
                type: 'ajax',
                url: '/monitoring/datanode/deadnodes.json',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
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

        decommisioningNodesStore: {
            autoLoad: false,
            fields: [
                'hostname',
                'lastContact',
                'blockswithonlydecommissioningreplicas',
                'underrepblocksinfilesunderconstruction',
                'underreplicatedblocks',
                'startHour',
                'startMinute'
            ],
            proxy: {
                type: 'ajax',
                url: '/monitoring/datanode/decommisioningnodes.json',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
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

        liveNodesStore: {
            autoLoad: false,

            fields: [
                'nonDfsUsed',
                'ipAddr',
                'volumeFailures',
                'lastUpdate',
                'remainingPercent',
                'hostname',
                'blocks',
                'blockPoolUsedPercent',
                'adminState',
                'blockPoolUsed',
                'dfsUsed',
                'remaining',
                'dfsUsedPercent',
                'lastContact',
                'networkLocation',
                'capacity'
            ],

            proxy: {
                type: 'ajax',
                url: '/monitoring/datanode/livenodes.json',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
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
                    property: 'hostname',
                    direction: 'asc'
                }
            ]
        }
    }
});