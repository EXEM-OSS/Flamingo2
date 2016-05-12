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
Ext.define('Flamingo2.view.monitoring.datanode.Decommissioningnodes', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.decommissioningNodes',

    requires: [
        'Flamingo2.view.monitoring.datanode.DatanodeController',
        'Flamingo2.view.monitoring.datanode.DatanodeModel'
    ],

    listeners: {
        afterrender: 'onDecommisioningNodesAfterrender'
    },

    bind: {
        store: '{decommisioningNodesStore}'
    },

    tools: [
        {
            type: 'refresh',
            handler: function (event, toolEl, panel) {
                var grid = query('decommissioningNodes');
                grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                grid.getStore().load({
                    callback: function (records, operation, success) {
                        grid.setTitle(format(message.msg('monitoring.datanode.msg.decommissioning_datanode'), this.getCount()))
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
            text: message.msg('monitoring.datanode.repl_block'),
            flex: 1,
            dataIndex: 'underreplicatedblocks',
            align: 'center'
        },
        {
            text: message.msg('monitoring.datanode.rep_blocks_infiles'),
            flex: 1,
            dataIndex: 'underrepblocksinfilesunderconstruction',
            align: 'center'
        },
        {
            text: message.msg('monitoring.datanode.decommissioning_node'),
            flex: 1,
            dataIndex: 'blockswithonlydecommissioningreplicas',
            align: 'center'
        }
    ],
    viewConfig: {
        deferEmptyText: false,
        emptyText: message.msg('monitoring.datanode.msg.no_decommissioned_node'),
        columnLines: true,
        enableTextSelection: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    }
});