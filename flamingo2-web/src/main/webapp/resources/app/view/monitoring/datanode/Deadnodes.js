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
Ext.define('Flamingo2.view.monitoring.datanode.Deadnodes', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.deadNodes',

    requires: [
        'Flamingo2.view.monitoring.datanode.DatanodeController',
        'Flamingo2.view.monitoring.datanode.DatanodeModel'
    ],

    listeners: {
        afterrender: 'onDeadNodesAfterrender'
    },

    bind: {
        store: '{deadNodesStore}'
    },

    tools: [
        {
            type: 'refresh',
            handler: function (event, toolEl, panel) {
                var grid = query('deadNodes');
                grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                grid.getStore().load({
                    callback: function (records, operation, success) {
                        grid.setTitle(format(message.msg('monitoring.datanode.msg.dead_datanode'), this.getCount()))
                    }
                });
            }
        }
    ],

    columns: [
        {
            text: message.msg('monitoring.datanode.host'), flex: 1, dataIndex: 'hostname', align: 'center'
        },
        {
            text: message.msg('monitoring.datanode.ip_addr'), flex: 1, dataIndex: 'ipAddr', align: 'center'
        },
        {
            text: message.msg('monitoring.datanode.decommission_node'),
            flex: 1,
            dataIndex: 'decommissioned',
            align: 'center'
        }
    ],

    viewConfig: {
        deferEmptyText: false,
        emptyText: message.msg('monitoring.datanode.msg.no_dead_node'),
        enableTextSelection: true,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    }
});