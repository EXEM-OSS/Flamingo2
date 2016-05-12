/*
 * Copyright (C) 2011  Flamingo Project (http://www.cloudine.io).
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

Ext.define('Flamingo2.view.batch.JobModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.jobModel',

    data: {
        title: message.msg('batch.title')
    },

    stores: {
        metricsStore: {
            fields: ['id', 'total', 'running', 'jvmMaxMemory', 'jvmTotalMemory', 'jvmUsedMemory', 'jvmFreeMemory', 'time'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.BATCH.METRICS,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total',
                    idProperty: 'jobId'
                }
            }
        },

        scheduledJobsStore: {
            autoLoad: false,
            fields: ['apps', 'list', 'running', 'date'],
            pageSize: CONSTANTS.GRID_SIZE_PER_PAGE,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.BATCH.WORKFLOW_ENGINE,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total',
                    idProperty: 'jobId'
                },
                extraParams: {
                    clusterName: ENGINE.id,
                    limit: 40
                }
            }
        },

        jobListStore: {
            autoLoad: false,
            fields: 'Flamingo2.model.batch.Job',
            pageSize: CONSTANTS.GRID_SIZE_PER_PAGE,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.BATCH.LIST,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                },
                extraParams: {
                    clusterName: ENGINE.id
                }
            }
        }
    }
});