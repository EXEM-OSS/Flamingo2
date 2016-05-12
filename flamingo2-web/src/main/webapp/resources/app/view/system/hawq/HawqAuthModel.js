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
 * ViewModel for Flamingo2.view.system.hawq.HawqAuth
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.HawqAuth
 * @see Flamingo2.view.system.hawq.role.HawqRole
 * @see Flamingo2.view.system.hawq.queue.HawqRQueue
 * @see Flamingo2.view.system.hawq.session.HawqSession
 */
Ext.define('Flamingo2.view.system.hawq.HawqAuthModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.hawqAuthModel',

    data: {
        title: message.msg('hawq.title.main.auth')
    },

    stores: {
        hawqRQueue: {
            autoLoad: true,
            fields: [
                'queueid', 'rsqname', 'rsqcountlimit', 'rsqcountvalue', 'rsqcostlimit',
                'rsqcostvalue', 'rsqmemorylimit', 'rsqmemoryvalue', 'rsqwaiters', 'rsqholders'
            ],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.AUTH.RESOURCE_QUEUES,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            },
            listeners: {
                beforeload: function (store, operation, eOpts) {
                    store.getProxy().extraParams.clusterName = ENGINE.id;
                }
            }
        },

        hawqCostOvercommit: {
            fields: ['displ', 'value'],
            data: [
                {displ: 'TRUE', value: true},
                {displ: 'FALSE', value: false}
            ]
        },

        hawqPriority: {
            fields: ['displ', 'value'],
            data: [
                {displ: 'MIN', value: 'MIN'},
                {displ: 'LOW', value: 'LOW'},
                {displ: 'MEDIUM', value: 'MEDIUM'},
                {displ: 'HIGH', value: 'HIGH'},
                {displ: 'MAX', value: 'MAX'}
            ]
        },

        hawqMemoryUnit: {
            fields: ['displ', 'value'],
            data: [
                {displ: 'KB', value: 'KB'},
                {displ: 'MB', value: 'MB'},
                {displ: 'GB', value: 'GB'}
            ]
        },

        hawqGroupRole: {
            autoLoad: true,
            fields: [
                'rolname', 'rolsuper', 'rolinherit', 'rolcreaterole', 'rolcreatedb', 'rolcatupdate', 'rolcanlogin',
                'rolconnlimit', 'rolvaliduntil', 'rolconfig', 'rolresqueue', 'oid',
                'rolcreaterextgpfd', 'rolcreaterexthttp', 'rolcreatewextgpfd', 'rolcreaterexthdfs', 'rolcreatewexthdfs'
            ],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.AUTH.GROUP_ROLES,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            },
            listeners: {
                beforeload: function (store, operation, eOpts) {
                    store.getProxy().extraParams.clusterName = ENGINE.id;
                }
            }
        },

        hawqLoginRole: {
            autoLoad: true,
            fields: [
                'rolname', 'rolsuper', 'rolinherit', 'rolcreaterole', 'rolcreatedb', 'rolcatupdate', 'rolcanlogin',
                'rolconnlimit', 'rolvaliduntil', 'rolconfig', 'rolresqueue', 'oid',
                'rolcreaterextgpfd', 'rolcreaterexthttp', 'rolcreatewextgpfd', 'rolcreaterexthdfs', 'rolcreatewexthdfs'
            ],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.AUTH.LOGIN_ROLES,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            },
            listeners: {
                beforeload: function (store, operation, eOpts) {
                    store.getProxy().extraParams.clusterName = ENGINE.id;
                }
            }
        },

        hawqDenyDay: {
            fields: ['displ', 'value'],
            data: [
                {displ: message.msg('hawq.day.sunday'), value: 'Sunday'},
                {displ: message.msg('hawq.day.monday'), value: 'Monday'},
                {displ: message.msg('hawq.day.tuesday'), value: 'Tuesday'},
                {displ: message.msg('hawq.day.wednesday'), value: 'Wednesday'},
                {displ: message.msg('hawq.day.thursday'), value: 'Thursday'},
                {displ: message.msg('hawq.day.friday'), value: 'Friday'},
                {displ: message.msg('hawq.day.saturday'), value: 'Saturday'}
            ]
        },

        hawqSession: {
            pageSize: CONSTANTS.GRID_SIZE_PER_PAGE,
            fields: [
                'datid', 'datname', 'procpid', 'sess_id', 'usesysid', '_username', 'current_query', 'waiting',
                'query_start', 'backend_start', 'client_addr', 'client_port', 'application_name', 'xact_start'
            ],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.AUTH.SESSIONS,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            },
            listeners: {
                beforeload: function (store, operation, eOpts) {
                    store.getProxy().extraParams.clusterName = ENGINE.id;
                }
            }
        },

        hawqLTable: {
            pageSize: CONSTANTS.GRID_SIZE_PER_PAGE,
            fields: [
                'locktype', 'relation', 'mode', 'waiting_pid', 'waiting_query', 'other_pid', 'other_query'
            ],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.AUTH.LOCK_TABLES,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            },
            listeners: {
                beforeload: function (store, operation, eOpts) {
                    store.getProxy().extraParams.clusterName = ENGINE.id;
                }
            }
        }
    }
});