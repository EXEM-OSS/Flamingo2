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
Ext.define('Flamingo2.view.monitoring.resourcemanager.ResourceManagerController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.resourcemanagerController',

    onAfterrender: function (grid, opts) {
        setTimeout(function () {
            grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            grid.getStore().load({
                callback: function (records, operation, success) {
                    grid.setTitle(format(message.msg('monitoring.rm.total_run_app'), this.getCount()));
                }
            });
        }, 10);
    },

    onStoreAfterrender: function (comp, opts) {
        setTimeout(function () {
            comp.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            comp.getStore().load();
        }, 10);
    },

    onItemClick: function (view, record, item, index, e, opts) {
    },

    onRefreshClick: function (event, toolEl, panel) {
        var grid = query('runningApplications');
        grid.getStore().load({
            callback: function (records, operation, success) {
                grid.setTitle(format(message.msg('monitoring.rm.total_run_app'), this.getCount()));
            }
        });
    },

    /**
     * Engine Combobox Changed Event
     */
    onEngineChanged: function (engine) {
        var grid = query('runningApplications');
        grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
        grid.getStore().load({
            callback: function (records, operation, success) {
                grid.setTitle(format(message.msg('monitoring.rm.total_run_app'), this.getCount()));
            }
        });
    }
});
