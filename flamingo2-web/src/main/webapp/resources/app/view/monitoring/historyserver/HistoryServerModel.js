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
Ext.define('Flamingo2.view.monitoring.historyserver.HistoryServerModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.historyServerModel',

    requires: [
        'Flamingo2.model.monitoring.historyserver.JobCounters'
    ],

    data: {
        title: 'MapReduce'
    },

    stores: {
        tasksStore: {
            autoLoad: false,
            model: 'Flamingo2.model.monitoring.historyserver.Tasks',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.HS.TASKS,
                extraParams: {
                    clusterName: ENGINE.id
                },
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'tasks.task',
                    totalProperty: 'total'
                }
            },
            sorters: [
                {
                    property: 'id',
                    direction: 'asc'
                }
            ]
        },

        configurationStore: {
            autoLoad: false,
            remoteSort: false,
            fields: ['name', 'source', 'value'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.HS.CONF,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'conf.property',
                    totalProperty: 'total'
                }
            },
            sorters: [
                {
                    property: 'name',
                    direction: 'desc'
                }
            ]
        },

        mapReduceJobsStore: {
            model: 'Flamingo2.model.monitoring.historyserver.MapReduceJob',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.HS.JOBS,
                reader: {
                    type: 'json',
                    rootProperty: 'jobs.job',
                    totalProperty: 'total'
                },
                extraParams: {
                    clusterName: ENGINE.id
                }
            },
            sorters: [
                {
                    property: 'id',
                    direction: 'desc'
                }
            ]
        },

        jobCounterStore: {
            type: 'tree',
            model: 'Flamingo2.model.monitoring.historyserver.JobCounters',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.HS.COUNTERS,
                headers: {'Accept': 'application/json'}
            },
            sorters: [
                {
                    property: 'name',
                    direction: 'asc'
                }
            ],
            rootVisible: false,
            root: {
                text: 'JobCounters',
                expanded: true,
                id: 'root'
            }
        }
    }
});