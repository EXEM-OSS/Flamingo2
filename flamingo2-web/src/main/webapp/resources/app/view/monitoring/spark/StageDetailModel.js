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
 * Created by Park on 15. 8. 25..
 */
Ext.define('Flamingo2.view.monitoring.spark.StageDetailModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.stagedetail',

    requires: [
        'Flamingo2.model.monitoring.spark.StageDetail'
    ],

    stores: {
        executorDeserializeTime: {
            model: 'Flamingo2.model.monitoring.spark.StageDetail',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_STAGE_DEATIL_CHART,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            sorters: [{
                property: 'key',
                direction: 'ASC'
            }]
        },
        executorRunTime: {
            model: 'Flamingo2.model.monitoring.spark.StageDetail',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_STAGE_DEATIL_CHART,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            sorters: [{
                property: 'key',
                direction: 'ASC'
            }]
        },

        resultSerializationTime: {
            model: 'Flamingo2.model.monitoring.spark.StageDetail',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_STAGE_DEATIL_CHART,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            sorters: [{
                property: 'key',
                direction: 'ASC'
            }]
        },
        gettingResultTime: {
            model: 'Flamingo2.model.monitoring.spark.StageDetail',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_STAGE_DEATIL_CHART,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            sorters: [{
                property: 'key',
                direction: 'ASC'
            }]
        },
        jvmGCTime: {
            model: 'Flamingo2.model.monitoring.spark.StageDetail',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_STAGE_DEATIL_CHART,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            sorters: [{
                property: 'key',
                direction: 'ASC'
            }]
        }
    }
});