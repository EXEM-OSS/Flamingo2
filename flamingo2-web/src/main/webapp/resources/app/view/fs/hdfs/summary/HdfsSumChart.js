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

Ext.define('Flamingo2.view.fs.hdfs.summary.HdfsSumChart', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.hdfsSumChartPanel',

    requires: [
        'Flamingo2.view.fs.hdfs.summary.HdfsSumChartController',
        'Flamingo2.view.fs.hdfs.summary.HdfsSumChartModel'
    ],

    controller: 'hdfsSumChartViewController',

    viewModel: {
        type: 'hdfsSumChartModel'
    },

    layout: {
        type: 'hbox',
        pack: 'start',
        align: 'stretch'
    },
    items: [
        {
            xtype: 'form',
            title: message.msg('fs.hdfs.chart.sum'),
            flex: 1,
            iconCls: 'fa fa-newspaper-o fa-fw',
            margin: '0 0 5 0',
            layout: 'fit',
            items: [
                {
                    xtype: 'form',
                    reference: 'hdfsSumChartForm',
                    layout: {
                        type: 'table',
                        columns: 2,
                        tableAttrs: {
                            style: {
                                width: '100%'
                            }
                        }
                    },
                    bodyPadding: '5',
                    itemId: 'summaryTable',
                    border: true,
                    autoScroll: true,
                    defaults: {
                        labelAlign: 'right',
                        anchor: '100%',
                        labelWidth: 150
                    },
                    items: [
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.host'),
                            name: 'hostName'
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.port'),
                            name: 'port'
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.total'),
                            name: 'total',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return fileSize(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.capacityUsedNonDFS'),
                            name: 'capacityUsedNonDFS',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return fileSize(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.used'),
                            name: 'used',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return fileSize(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.free'),
                            name: 'free',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return fileSize(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.defaultBlockSize'),
                            name: 'defaultBlockSize',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return fileSize(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.totalBlock'),
                            name: 'totalBlocks',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.totalFiles'),
                            name: 'totalFiles',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.all'),
                            name: 'all',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.liveNodes'),
                            name: 'live',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.stale'),
                            name: 'stale',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.unhealthyNodes'),
                            name: 'unhealthyNodes',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.deadNodes'),
                            name: 'dead',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.rebootedNodes'),
                            name: 'rebootedNodes',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.decommissionedNodes'),
                            name: 'decommissioning',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.threads'),
                            name: 'threads',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.missingBlocks'),
                            name: 'missingBlocks',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.corruptReplicatedBlocks'),
                            name: 'corruptReplicatedBlocks',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.underReplicatedBlocks'),
                            name: 'underReplicatedBlocks',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.pendingReplicationBlocks'),
                            name: 'pendingReplicationBlocks',
                            valueToRaw: function (value) {
                                if (value == undefined) return '';
                                return toCommaNumber(value);
                            }
                        },
                        {
                            xtype: 'displayfield',
                            fieldLabel: message.msg('fs.hdfs.chart.sum.scheduledReplicationBlocks'),
                            name: 'scheduledReplicationBlocks'
                        }
                    ]
                }
            ],
            viewConfig: {
                enableTextSelection: true,
                columnLines: true,
                stripeRows: true
            }
        },
        {
            title: message.msg('fs.hdfs.chart.pie'),
            iconCls: 'fa fa-pie-chart fa-fw',
            margin: '0 5 5 5',
            width: 300,
            layout: 'fit',
            items: [
                {
                    xtype: 'polar',
                    itemId: 'usage',
                    border: true,
                    shadow: true,
                    store: {
                        fields: ['name', 'value']
                    },
                    interactions: 'itemhighlight',
                    colors: ['#90ED7D', '#434348', '#7CB5EC'],
                    series: {
                        type: 'pie',
                        style: {
                            stroke: 'gray' // line color
                        },
                        rotation: 0,
                        angleField: 'value',
                        label: {
                            field: 'name',
                            font: '12px NanumGothic',
                            fontFamily: 'NanumGothic',
                            calloutLine: {
                                length: -1,
                                width: 0
                            }
                        },
                        tooltip: {
                            trackMouse: true,
                            renderer: function (storeItem) {
                                this.setHtml(fileSize(storeItem.get('value')));
                            }
                        }
                    }
                }
            ]
        },
        {
            title: message.msg('fs.hdfs.top5'),
            iconCls: 'fa fa-tasks fa-fw',
            width: 300,
            margin: '0 0 5 0',
            layout: 'fit',
            items: [
                {
                    xtype: 'grid',
                    itemId: 'hdfsTop5Grid',
                    border: true,
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    bind: {
                        store: '{top5Store}'
                    },
                    columns: [
                        {
                            locked: true,
                            text: message.msg('common.directory'),
                            width: 130,
                            sortable: false,
                            style: 'text-align:center;font-size:13px',
                            align: 'left',
                            dataIndex: 'path'
                        },
                        {
                            text: message.msg('fs.hdfs.top5.spaceConsumed'),
                            flex: 0.1,
                            sortable: false,
                            style: 'text-align:center;font-size:13px',
                            align: 'right',
                            dataIndex: 'spaceConsumed',
                            tip: message.msg('fs.hdfs.top5.spaceConsumed.tip'),
                            renderer: function (value) {
                                return fileSize(value.toFixed(0));
                            },
                            listeners: {
                                render: function (item) {
                                    Ext.create('Ext.tip.ToolTip', {
                                        target: item.getEl(),
                                        html: item.tip
                                    });
                                }
                            }
                        }
                    ],
                    viewConfig: {
                        enableTextSelection: true,
                        columnLines: true,
                        stripeRows: true,
                        getRowClass: function () {
                            return 'cell-height-25';
                        }
                    }
                }
            ]
        }
    ],
    listeners: {
        afterrender: 'onAfterRender'
    }
});