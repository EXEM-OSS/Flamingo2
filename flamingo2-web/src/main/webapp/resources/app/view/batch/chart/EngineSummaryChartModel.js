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
Ext.define('Flamingo2.view.batch.chart.EngineSummaryChartModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.enginesummarychart',

    stores: {
        jvm: {
            fields: ['id', 'total', 'running', 'jvmMaxMemory', 'jvmTotalMemory', 'jvmUsedMemory', 'jvmFreeMemory', 'time'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.BATCH.METRICS,
                extraParams: {
                    clusterName: ENGINE.id
                },
                remoteSort: true,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total',
                    idProperty: 'num'
                }
            }
        },
        jobs: {
            fields: ['id', 'total', 'running', 'jvmMaxMemory', 'jvmTotalMemory', 'jvmUsedMemory', 'jvmFreeMemory', 'time'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.BATCH.METRICS,
                extraParams: {
                    clusterName: ENGINE.id
                },
                remoteSort: true,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total',
                    idProperty: 'num'
                }
            }
        }
    },

    data: {
        /* This object holds the arbitrary data that populates the ViewModel and is then available for binding. */
    }
});