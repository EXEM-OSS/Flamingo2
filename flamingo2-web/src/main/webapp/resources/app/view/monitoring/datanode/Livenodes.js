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
Ext.define('Flamingo2.view.monitoring.datanode.Livenodes', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.liveNodes',

    requires: [
        'Flamingo2.view.monitoring.datanode.DatanodeController',
        'Flamingo2.view.monitoring.datanode.DatanodeModel'
    ],

    listeners: {
        afterrender: 'onLiveNodesAfterrender'
    },

    bind: {
        store: '{liveNodesStore}'
    },

    tools: [
        {
            type: 'refresh',
            handler: function (event, toolEl, panel) {
                var grid = query('liveNodes');
                grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                grid.getStore().load({
                    callback: function (records, operation, success) {
                        grid.setTitle(format(message.msg('monitoring.datanode.msg.live_datanode'), this.getCount()))
                    }
                });
            }
        }
    ],

    columns: [
        {
            text: message.msg('monitoring.datanode.host'),
            width: 140,
            dataIndex: 'hostname',
            align: 'center',
            lock: true
        },
        {
            text: message.msg('monitoring.datanode.ip_addr'),
            width: 140,
            dataIndex: 'ipAddr',
            align: 'center',
            lock: true
        },
        {
            text: message.msg('monitoring.datanode.last_contact'),
            width: 55,
            dataIndex: 'lastContact',
            align: 'center',
            hidden: true
        },
        {
            text: message.msg('common.status'), width: 70, dataIndex: 'adminState', align: 'center'
        },
        {
            text: message.msg('monitoring.datanode.capacity'), width: 80, dataIndex: 'capacity', align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return fileSize(value);
            }
        },
        {
            text: message.msg('monitoring.datanode.dfs_used'), width: 90, dataIndex: 'dfsUsed', align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return fileSize(value);
            }
        },
        {
            text: message.msg('monitoring.datanode.dfs_used_per'),
            width: 110,
            dataIndex: 'dfsUsedPercent',
            align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return Ext.String.format('<div class="x-progress x-progress-default x-border-box">' +
                    '<div class="x-progress-text x-progress-text-back" style="width: 95px;">{0}%</div>' +
                    '<div class="x-progress-bar x-progress-bar-default" role="presentation" style="width:{0}%">' +
                    '<div class="x-progress-text" style="width: 95px;"><div>{0}%</div></div></div></div>', Number(value).toFixed(2));
            }
        },
        {
            text: message.msg('monitoring.datanode.dfs_remaining'), width: 90, dataIndex: 'remaining', align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return fileSize(value);
            }
        },
        {
            text: message.msg('monitoring.datanode.dfs_remaining_per'),
            width: 110,
            dataIndex: 'remainingPercent',
            align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return Ext.String.format('<div class="x-progress x-progress-default x-border-box">' +
                    '<div class="x-progress-text x-progress-text-back" style="width: 95px;">{0}%</div>' +
                    '<div class="x-progress-bar x-progress-bar-default" role="presentation" style="width:{0}%">' +
                    '<div class="x-progress-text" style="width: 95px;"><div>{0}%</div></div></div></div>', Number(value).toFixed(2));
            }
        },
        {
            text: message.msg('monitoring.datanode.block'), width: 70, dataIndex: 'blocks', align: 'center',
            renderer: function (value) {
                return toCommaNumber(value);
            }
        },
        {
            text: message.msg('monitoring.datanode.level'), width: 80, dataIndex: 'level', align: 'center', hidden: true
        },
        {
            text: message.msg('monitoring.datanode.network_location'),
            width: 110,
            dataIndex: 'networkLocation',
            align: 'center'
        },
        {
            text: message.msg('monitoring.datanode.last_update'), width: 140, dataIndex: 'lastUpdate', align: 'center',
            renderer: function (value) {
                return dateFormat2(value);
            }
        },
        {
            text: message.msg('monitoring.datanode.etc'), flex: 1, dataIndex: 'etc', align: 'center'
        }
    ],
    viewConfig: {
        deferEmptyText: false,
        emptyText: message.msg('monitoring.datanode.msg.no_live_node'),
        enableTextSelection: true,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    }
});