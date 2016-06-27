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
Ext.define('Flamingo2.view.monitoring.namenode.NamenodeModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.namenodeModel',

    data: {
        title: message.msg('monitoring.namenode.title')
    },

    stores: {
        hdfsUsageStore: {
            fields: ['num', 'total', 'used', 'free', 'reg_dt'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.NAMENODE.DFS_USAGE,
                extraParams: {
                    clusterName: ENGINE.id
                },
                remoteSort: true,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        },
        jvmHeapUsageStore: {
            fields: ['num', 'jvmMaxMemory', 'jvmTotalMemory', 'jvmUsedMemory', 'jvmFreeMemory', 'reg_dt'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.NAMENODE.DFS_USAGE,
                extraParams: {
                    clusterName: ENGINE.id
                },
                remoteSort: true,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        },
        blockStatusStore: {
            fields: ['num', 'corruptReplicaBlocks', 'pendingReplicationBlocks', 'scheduledReplicationBlocks', 'underReplicatedBlocks', 'missingBlocks', 'reg_dt'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.NAMENODE.DFS_USAGE,
                extraParams: {
                    clusterName: ENGINE.id
                },
                remoteSort: true,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        },
        fileCountStore: {
            fields: ['num', 'totalFiles', 'reg_dt'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.NAMENODE.DFS_USAGE,
                extraParams: {
                    clusterName: ENGINE.id
                },
                remoteSort: true,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        },
        blockCountStore: {
            fields: ['num', 'totalBlocks', 'reg_dt'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.NAMENODE.DFS_USAGE,
                extraParams: {
                    clusterName: ENGINE.id
                },
                remoteSort: true,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        },
        datanodeStatusStore: {
            fields: ['num', 'nodeAll', 'nodeDead', 'nodeLive', 'reg_dt'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.NAMENODE.DFS_USAGE,
                extraParams: {
                    clusterName: ENGINE.id
                },
                remoteSort: true,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        }
    }
});