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
Ext.define('Flamingo2.view.batch.WorkflowEngineHeap', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workflowEngineHeap',

    requires: ['Ext.chart.series.Area'],

    border: false,

    layout: {
        type: 'fit'
    },

    initComponent: function () {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'chart',
                    flex: 1,
                    store: Ext.create('Flamingo2.store.batch.JobStore', {
                        clusterName: ENGINE.id
                    }),
                    series: [
                        {
                            type: 'area',
                            dataIndex: 'jvmMaxMemory',
                            name: 'Max'
                        },
                        {
                            type: 'area',
                            dataIndex: 'jvmTotalMemory',
                            name: 'Total'
                        },
                        {
                            type: 'area',
                            dataIndex: 'jvmFreeMemory',
                            name: 'Free'
                        }
                    ],
                    xField: 'date',
                    chartConfig: {
                        colors: ['#BF0B23', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
                        chart: {
                            marginRight: 30,
                            zoomType: 'x'
                        },
                        title: {
                            text: message.msg('batch.jvm_heap'),
                            x: 20,
                            align: 'left'
                        },
                        xAxis: [
                            {
                                title: {
                                    text: '',
                                    margin: 20
                                },
                                labels: {
                                    rotation: 270,
                                    y: 35,
                                    formatter: function () {
                                        return '';
                                    }
                                }
                            }
                        ],
                        yAxis: {
                            min: 0,
                            title: {
                                text: message.msg('batch.txt_size')
                            },
                            plotLines: [
                                {
                                    value: 0,
                                    width: 1,
                                    color: '#808080'
                                }
                            ],
                            labels: {
                                formatter: function () {
                                    return toCommaNumber(this.value) + "M";
                                }
                            }
                        },
                        tooltip: {
                            formatter: function () {
                                return '<b>' + this.series.name + '</b><br/>Date : ' + Flamingo.Util.Date.format(new Date(this.x), 'yyyy-MM-dd HH:mm:ss') + '<br/>Size : ' + toCommaNumber(this.y) + "M";
                            }
                        },
                        legend: {
                            align: 'right',
                            verticalAlign: 'top',
                            floating: true,
                            borderWidth: 0
                        },
                        credits: {
                            enabled: false
                        }
                    }
                }
            ]
        });

        this.callParent(arguments);
    }

});