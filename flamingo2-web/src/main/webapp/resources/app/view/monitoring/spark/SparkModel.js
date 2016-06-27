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
 * Created by Park on 15. 8. 7..
 */
Ext.define('Flamingo2.view.monitoring.spark.SparkModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.sparkModel',

    requires: [
        'Flamingo2.model.monitoring.spark.Executors',
        'Flamingo2.model.monitoring.spark.Stages',
        'Flamingo2.model.monitoring.spark.Storage',
        'Flamingo2.model.monitoring.spark.Timeline'
    ],

    data: {
        title: 'Apache Spark'
    },

    stores: {
        timeSeriesStore: {
            fields: ['date', 'cnt'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_JOB_CHART,
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

        application: {
            fields: ['appid', 'appname', 'started', 'completed', 'user', 'duration'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_APPLICATIONS,
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

        jobs: {
            fields: ['appid', 'jobid', 'jobname', 'submitted', 'completed', 'duration', 'stage_rate', 'stages', 'stage_completed', 'stage_failed', 'task_rate', 'task_completed', 'task_skipped', 'task_failed', 'description', 'status', 'sorter'],
            autoLoad: false,
            groupField: 'sorter',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_JOBS,
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

        stages: {
            model: 'Flamingo2.model.monitoring.spark.Stages',
            autoLoad: false,
            groupField: 'sorter',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_STAGES,
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

        environment: {
            fields: ['key', 'value', 'type', 'sorter'],
            autoLoad: false,
            groupField: 'sorter',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_ENVIRONMENT,
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
                property: 'sorter',
                direction: 'ASC'
            },
            {
                property: 'key',
                direction: 'ASC'
            }]
        },

        executors: {
            model: 'Flamingo2.model.monitoring.spark.Executors',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_EXECUTORS,
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

        storage: {
            model: 'Flamingo2.model.monitoring.spark.Storage',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_STORAGE,
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

        appTimeline: {
            fields: ['id', 'content', 'start', 'end', 'className', 'group'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_JOBS_TIMELINE,
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

        jobTimeline: {
            fields: ['id', 'content', 'start', 'end', 'className', 'group'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_STAGES_TIMELINE,
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

        taskTimeline: {
            model: 'Flamingo2.model.monitoring.spark.Timeline',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SPARK.MONITORING.GET_TASK_TIMELINE,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        }
    }
});



