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

Ext.define('Flamingo2.view.fs.hdfs.information.HdfsSummary', {
    extend: 'Ext.Panel',
    alias: 'widget.hdfsSummaryPanel',

    border: true,

    items: [
        {
            xtype: 'form',
            itemId: 'hdfsInformationForm',
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
    },
    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('common.refresh'),
            handler: 'onHdfsSummaryRefreshClick'
        }
    ]
});