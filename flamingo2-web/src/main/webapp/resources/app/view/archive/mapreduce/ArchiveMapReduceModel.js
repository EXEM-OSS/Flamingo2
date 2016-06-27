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
Ext.define('Flamingo2.view.archive.mapreduce.ArchiveMapReduceModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.archiveMapReduceModel',

    data: {
        title: 'MapReduce'
    },

    stores: {
        archiveMRSearchKeyword: {
            fields: ['name', 'value'],
            data: [
                {name: 'All', value: 'ALL'},
                {name: 'Finished', value: 'FINISHED'},
                {name: 'Failed', value: 'FAILED'},
                {name: 'Killed', value: 'KILLED'}
            ]
        },

        archiveMRJobSumChartStore: {
            fields: ['time', 'sum'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.ARCHIVE.MAPREDUCE.GET_SUMMARY,
                extraParams: {
                    clusterName: '',
                    startDate: '',
                    endDate: '',
                    filter: ''
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            remoteSort: true,
            sorters: [
                {
                    property: 'num',
                    direction: 'ASC'
                }
            ]
        },

        archiveMRJobsStore: {
            model: 'Flamingo2.model.monitoring.historyserver.MapReduceJob',
            autoLoad: false,
            pageSize: 20,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.ARCHIVE.MAPREDUCE.GET_ALL_MR_JOBS,
                extraParams: {
                    clusterName: '',
                    startDate: '',
                    endDate: '',
                    filter: '',
                    mrJobName: ''
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
                    direction: 'desc'
                }
            ]
        },

        archiveMapReduceJobCounterStore: {
            type: 'tree',
            model: 'Flamingo2.model.monitoring.historyserver.JobCounters',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.ARCHIVE.MAPREDUCE.GET_MR_JOB_COUNTERS,
                extraParams: {
                    clusterName: '',
                    jobId: '',
                    state: ''
                }
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
        },

        archiveMapReduceConfStore: {
            autoLoad: false,
            remoteSort: false,
            fields: ['name', 'source', 'value'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.ARCHIVE.MAPREDUCE.GET_MR_JOB_CONFIGURATION,
                extraParams: {
                    clusterName: ''
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

        archiveMapReduceTasksStore: {
            autoLoad: false,
            model: 'Flamingo2.model.monitoring.historyserver.Tasks',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.ARCHIVE.MAPREDUCE.GET_MR_JOB_TASK,
                extraParams: {
                    clusterName: ''
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