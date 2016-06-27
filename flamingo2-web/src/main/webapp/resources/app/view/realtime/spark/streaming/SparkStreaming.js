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
Ext.define('Flamingo2.view.realtime.spark.streaming.SparkStreaming', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.sparkStreaming',

    requires: [
        'Flamingo2.view.realtime.spark.streaming.SparkStreamingController',
        'Flamingo2.view.realtime.spark.streaming.SparkStreamingModel',
        'Flamingo2.view.realtime.spark.streaming.iot.IoTServiceTree',
        'Flamingo2.view.realtime.spark.streaming.iot.IoTServiceTab',
        'Flamingo2.view.realtime.spark.streaming.chart.SparkStreamingSumChart',
        'Flamingo2.view.realtime.spark.streaming.management.SparkStreamingManagement'
    ],

    controller: 'sparkStreamingViewController',

    viewModel: {
        type: 'sparkStreamingModel'
    },

    height: 800,
    scrollable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [
        {
            xtype: 'panel',
            bodyStyle: {
                background: '#ffffff'
            },
            flex: 1,
            layout: 'border',
            items: [
                /*{
                    region: 'west',
                    split: true,
                    title: 'IoT 서비스 목록',
                    width: 300,
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'ioTServiceTree',
                            itemId: 'ioTServiceTreePanel',
                            border: true,
                            flex: 0.7
                        },
                        {
                            xtype: 'ioTServiceTab',
                            flex: 1.3
                        }
                    ],
                    tools: [
                        {
                            type: 'refresh',
                            tooltip: message.msg('common.refresh'),
                            handler: 'onIoTServiceTreeRefreshClick'
                        }
                    ]
                },*/
                {
                    region: 'center',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'sparkStreamingSumChart',
                            title: message.msg('spark.streaming.chart.title'),
                            iconCls: 'common-view',
                            flex: 1,
                            border: true
                        },
                        {
                            xtype: 'sparkStreamingManagementGrid',
                            itemId: 'sparkStreamingManagementGrid',
                            title: message.msg('spark.streaming.list.title'),
                            iconCls: 'common-view',
                            flex: 1,
                            border: true
                        }
                    ]
                }
            ]
        }
    ]
});