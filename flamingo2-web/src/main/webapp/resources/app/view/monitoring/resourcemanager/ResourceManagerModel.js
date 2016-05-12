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
Ext.define('Flamingo2.view.monitoring.resourcemanager.ResourceManagerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.resourceManagerModel',

    data: {
        title: message.msg('monitoring.rm.title') // FIXME
    },

    stores: {
        nodeStatusStore: {
            fields: ['name', 'value'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.RM.NODESTATUS,
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

        applicationStatusStore: {
            fields: ['name', 'value'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.RM.APPSTATUS,
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

        containerStatusStore: {
            fields: ['name', 'value'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.RM.CONTAINERSTATUS,
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

        queueMemoryStore: {
            fields: ['name', 'value'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.RM.QUEUEMEMORY,
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

        jvmHeapStore: {
            fields: ['name', 'value'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.RM.JVMHEAP,
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

        runningApplicationsStore: {
            model: 'Flamingo2.model.monitoring.resourcemanager.Application',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.RM.APP_RUNNING,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            remoteSort: false,  // groupField가 설정되면 groupField 기준으로 정렬이 됨
            sorters: [
                {
                    property: 'startTime',
                    direction: 'DESC'
                }
            ]
        }
    }
});