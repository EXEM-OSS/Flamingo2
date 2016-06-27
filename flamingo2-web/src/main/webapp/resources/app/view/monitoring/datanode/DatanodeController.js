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

    /**
     * 데이터노드에서 정상 동작하는 데이터 노드 정보를 가져온다.
     */
    onLiveNodesAfterRender: function () {
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

    /**
     * 장애 발생 데이터 노드 정보를 가져온다.
     */
    onDeadNodesAfterRender: function () {
        setTimeout(function () {
            var deadNodes = query('deadNodes');

            deadNodes.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            deadNodes.getStore().load({
                callback: function (records, operation, success) {
                    deadNodes.setTitle(format(message.msg('monitoring.datanode.msg.dead_datanode'), this.getCount()))
                }
            });
        }, 10);
    },

    /**
     * 해제된 데이터 노드 정보를 가져온다.
     */
    onDecommissionedNodesAfterRender: function () {
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

    /**
     * 정상 동작하는 데이터 노드 정보를 업데이트한다.
     */
    onLiveNodesRefreshClick: function () {
        var liveNodesGrid = query('liveNodes');

        liveNodesGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
        liveNodesGrid.getStore().load({
            callback: function (records, operation, success) {
                liveNodesGrid.setTitle(format(message.msg('monitoring.datanode.msg.live_datanode'), this.getCount()))
            }
        });
    },

    /**
     * 장애 발생 데이터 노드 정보를 업데이트한다.
     */
    onDeadNodesRefreshClick: function () {
        var deadNodesGrid = query('deadNodes');

        deadNodesGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
        deadNodesGrid.getStore().load({
            callback: function (records, operation, success) {
                deadNodesGrid.setTitle(format(message.msg('monitoring.datanode.msg.dead_datanode'), this.getCount()))
            }
        });
    },

    /**
     * 장애 발생 데이터 노드 정보를 업데이트한다.
     */
    onDecommissionedNodesRefreshClick: function () {
        var deadNodesGrid = query('decommissioningNodes');

        deadNodesGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
        deadNodesGrid.getStore().load({
            callback: function (records, operation, success) {
                deadNodesGrid.setTitle(format(message.msg('monitoring.datanode.msg.decommissioning_datanode'), this.getCount()))
            }
        });
    }
});