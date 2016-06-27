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
Ext.define('Flamingo2.view.realtime.spark.streaming.SparkStreamingModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.sparkStreamingModel',

    data: {
        title: message.msg('spark.streaming.title')
    },

    stores: {
        ioTServiceTreeStore: {
            model: 'Flamingo2.model.realtime.spark.streaming.iot.IoTService',
            type: 'tree',
            autoLoad: false,
            rootVisible: true,
            useArrows: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.REALTIME.SPARK.STREAMING.GET_IOT_SERVICES,
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                },
                extraParams: {
                    clusterName: ENGINE.id,
                    depth: 0,
                    columnsType: '',
                    node: '',
                    serviceId: '',
                    serviceTypeId: '',
                    deviceTypeId: ''
                }
            },
            root: {
                text: 'IoT 서비스',
                id: 'IoT'
            }
        },
        ioTServiceColumnStore: {
            model: 'Flamingo2.model.realtime.spark.streaming.iot.IoTService',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.REALTIME.SPARK.STREAMING.GET_IOT_SERVICES,
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                },
                extraParams: {
                    clusterName: ENGINE.id,
                    depth: 0,
                    columnsType: '',
                    node: ''
                }
            }
        },
        ioTServiceCustomColumnStore: {
            model: 'Flamingo2.model.realtime.spark.streaming.iot.IoTService',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.REALTIME.SPARK.STREAMING.GET_IOT_SERVICES,
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                },
                extraParams: {
                    clusterName: ENGINE.id,
                    depth: 0,
                    columnsType: '',
                    node: ''
                }
            }
        },
        sparkStreamingJobsStore: {
            model: 'Flamingo2.model.realtime.spark.streaming.SparkStreaming',
            autoLoad: false,
            pageSize: 20,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.REALTIME.SPARK.STREAMING.GET_SPARK_STREAMING_APPS,
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
                    direction: 'desc'
                }
            ]
        },
        sparkStreamingSumChartStore: {
            fields: ['cpuUsage', 'time'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.REALTIME.SPARK.STREAMING.GET_SPARK_STREAMING_APP_SUMMARY,
                extraParams: {
                    clusterName: '',
                    server: '',
                    applicationId: ''
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
        }
    }
});