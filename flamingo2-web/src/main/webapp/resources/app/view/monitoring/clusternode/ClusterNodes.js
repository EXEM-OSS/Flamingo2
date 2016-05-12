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
Ext.define('Flamingo2.view.monitoring.clusternode.ClusterNodes', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.clusterNodes',

    title: message.msg('monitoring.clusternode.title'),

    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('monitoring.clusternode.tooltip.update'),
            handler: 'onClusterNodesRefreshClick'
        }
    ],

    listeners: {
        afterrender: 'onClusterNodesAfterrender'
    },

    bind: {
        store: '{nodesStore}'
    },

    features: [
        {
            ftype: 'groupingsummary',
            groupHeaderTpl: message.msg('monitoring.clusternode.nodes.node_status') + ' : {name}',
            hideGroupedHeader: false,
            enableGroupingMenu: true
        }
    ],

    columns: [
        {
            text: message.msg('monitoring.clusternode.nodes.node_id'), dataIndex: 'nodeId', width: 170, align: 'center'
        },
        {
            text: message.msg('monitoring.clusternode.nodes.node_status'),
            dataIndex: 'nodeState',
            width: 100,
            align: 'center'
        },
        {
            text: message.msg('monitoring.clusternode.nodes.container'),
            dataIndex: 'numContainers',
            width: 80,
            align: 'center',
            summaryType: 'sum',
            summaryRenderer: function (value) {
                return ((value === 0 || value > 1) ? '(' + value + ' ' + message.msg('monitoring.clusternode.count') + ')' : '(1 ' + message.msg('monitoring.clusternode.count') + ')');
            }
        },
        {
            text: message.msg('monitoring.clusternode.nodes.mem'),
            dataIndex: 'capacityMemory',
            width: 100,
            align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return toCommaNumber(value) + ' MB';
            },
            summaryType: 'sum',
            summaryRenderer: function (value) {
                return ((value === 0 || value > 1) ? '(' + toCommaNumber(value) + ' ' + ' MB' + ')' : '(1 ' + ' MB' + ')');
            }
        },
        {
            text: message.msg('monitoring.clusternode.nodes.used_mem'),
            dataIndex: 'usedMemory',
            width: 100,
            align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return toCommaNumber(value) + ' MB';
            },
            summaryType: 'sum',
            summaryRenderer: function (value) {
                return ((value === 0 || value > 1) ? '(' + toCommaNumber(value) + ' ' + ' MB' + ')' : '(1 ' + ' MB' + ')');
            }
        },
        {
            text: message.msg('monitoring.clusternode.nodes.vcore'),
            dataIndex: 'capacityVcores',
            width: 95,
            align: 'center',
            summaryType: 'sum',
            summaryRenderer: function (value) {
                return ((value === 0 || value > 1) ? '(' + value + ' ' + message.msg('monitoring.clusternode.count') + ')' : '(1 ' + message.msg('monitoring.clusternode.count') + ')');
            }
        },
        {
            text: message.msg('monitoring.clusternode.nodes.used_vcore'),
            dataIndex: 'usedVcores',
            width: 90,
            align: 'center',
            summaryType: 'sum',
            summaryRenderer: function (value) {
                return ((value === 0 || value > 1) ? '(' + value + ' ' + message.msg('monitoring.clusternode.count') + ')' : '(1 ' + message.msg('monitoring.clusternode.count') + ')');
            }
        },
        {
            text: message.msg('monitoring.clusternode.nodes.rack'), dataIndex: 'rackName', width: 100, align: 'center'
        },
        {
            text: message.msg('monitoring.clusternode.nodes.http'),
            dataIndex: 'httpAddress',
            width: 170,
            align: 'center'
        },
        {
            text: message.msg('monitoring.clusternode.nodes.remarks'),
            dataIndex: 'healthReport',
            flex: 1,
            align: 'center'
        }
    ],

    viewConfig: {
        emptyText: message.msg('monitoring.clusternode.nodes.no_node'),
        deferEmptyText: false,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    }
});