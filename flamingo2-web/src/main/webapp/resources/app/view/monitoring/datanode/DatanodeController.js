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
Ext.define('Flamingo2.view.monitoring.datanode.DatanodeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.datanodeController',

    onDatanodeAfterrender: function (grid, opts) {
    },

    onLiveNodesAfterrender: function (grid, opts) {
        setTimeout(function () {
            var liveNodes = query('liveNodes');
            liveNodes.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            liveNodes.getStore().load({
                callback: function (records, operation, success) {
                    liveNodes.setTitle(format(message.msg('monitoring.datanode.msg.live_datanode'), this.getCount()))
                }
            });
        }, 10);
    },

    onDecommisioningNodesAfterrender: function (grid, opts) {
        setTimeout(function () {
            var decommissioningNodes = query('decommissioningNodes');
            decommissioningNodes.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            decommissioningNodes.getStore().load({
                callback: function (records, operation, success) {
                    decommissioningNodes.setTitle(format(message.msg('monitoring.datanode.msg.decommissioning_datanode'), this.getCount()))
                }
            });
        }, 10);
    },

    onDeadNodesAfterrender: function (grid, opts) {
        setTimeout(function () {
            var deadNodes = query('deadNodes');
            deadNodes.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            deadNodes.getStore().load({
                callback: function (records, operation, success) {
                    deadNodes.setTitle(format(message.msg('monitoring.datanode.msg.dead_datanode'), this.getCount()))
                }
            });
        }, 10);
    }
});