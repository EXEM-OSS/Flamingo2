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
Ext.define('Flamingo2.view.monitoring.clusternode.ClusterNodeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.clusterNodeController',

    onClusterNodesAfterrender: function (grid, opts) {
        var update = this.updateAll;
        setTimeout(function () {
            update();
        }, 10);
    },

    updateAll: function () {
        var grid = query('clusterNodes');
        var params = {
            clusterName: ENGINE.id
        };

        grid.getStore().load({
            params: params,
            callback: function (records, operation, success) {
                grid.setTitle(message.msg('monitoring.clusternode.title') + ' (' + message.msg('monitoring.clusternode.total') + ' ' + this.getCount() + message.msg('monitoring.clusternode.count') + ')')
            }
        });
    },

    onClusterNodesRefreshClick: function (event, toolEl, panel) {
        this.updateAll();
    },

    /**
     * Engine Combobox Changed Event
     */
    onEngineChanged: function (engine) {
        this.updateAll();
    }

});