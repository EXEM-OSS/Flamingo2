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
/**
 * Created by Park on 15. 8. 21..
 */
Ext.define('Flamingo2.view.component.timeline.Timeline', {
    extend: 'Ext.Component',
    alias: 'widget.timeline',

    mixins: [
        'Ext.util.StoreHolder'
    ],

    uses: ['Ext.data.StoreManager'],

    config:{
        store: 'ext-empty-store',
        deferInitialRefresh: false,
        timeline: null,
        groups: [],
        options: {}
    },

    cls: 'vis timeline',

    // private
    initComponent: function(){
        var me = this;

        me.store = Ext.data.StoreManager.lookup(me.store || 'ext-empty-store');

        if (!me.dataSource) {
            me.dataSource = me.store;
        }

        me.bindStore(me.dataSource, true, 'dataSource');
    },

    getStoreListeners: function() {
        var me = this;

        return {
            load: me.onLoad
        };
    },

    onLoad: function(store, records, successful, operation, eOpts ) {
        var me = this;
        var items = new vis.DataSet(me.toJson(records));
        var container = document.getElementById(me.id);

        me.timeline = new vis.Timeline(container);
        me.timeline.setItems(items);
        me.timeline.setOptions(me.options);

        if (me.groups.length > 0)
            me.timeline.setGroups(me.groups);
        else if (!Ext.isEmpty(store.proxy.reader.rawData.map.group))
            me.timeline.setGroups(store.proxy.reader.rawData.map.group);

        Ext.defer(function() {
            console.debug($("[data-toggle=tooltip]"));
            $("[data-toggle=tooltip]").tooltip({container: '#'+me.id,viewport: '#'+me.id});
        }, 300);

    },

    bindStore: function(store, initial, propName) {
        var me = this;

        me.mixins.storeholder.bindStore.apply(me, arguments);

        if (store && me.componentLayoutCounter && !me.preventRefresh) {
            me.doFirstRefresh(store, !initial);
        }
    },

    onBindStore: function(store, initial, propName, oldStore) {
        var me = this;

        // A BufferedStore has to know to reload the most recent visible zone if its View is preserveScrollOnReload
        if (me.store.isBufferedStore) {
            me.store.preserveScrollOnReload = me.preserveScrollOnReload;
        }
        if (oldStore && oldStore.isBufferedStore) {
            delete oldStore.preserveScrollOnReload;
        }

        // After the oldStore (.store) has been unbound/bound,
        // do the same for the old data source (.dataSource).
        if (!initial && propName === 'store') {
            // Block any refresh, since this means we're binding the store, which will kick off
            // a refresh.
            me.preventRefresh = true;
            // Ensure we have the this.store reference set correctly.
            me.store = store;
            me.bindStore(store, false, 'dataSource');
            me.preventRefresh = false;
        }
    },

    updateStore: function(store) {
        if (!this.isConfiguring) {
            // bindStore has various checks to see if the current store is the same, so
            // delete the property from our instance, it will be assigned during bindStore
            delete this.store;
            this.bindStore(store);
        }
    },


    doFirstRefresh: function(store, noDefer) {
        var me = this;

        // If we are configured to defer, and *NOT* called from the defer call below
        if (me.deferInitialRefresh && !noDefer) {
            Ext.defer(me.doFirstRefresh, 1, me, [store, true]);
        }

        else {
            if (store && !store.isLoading()) {
                me.refresh();
            }
        }
    },

    refresh: function() {

    },

    toJson : function(records) {
        var result = [];

        for (var j = 0; j < records.length; j++) {
            var models = {};
            var items = records[j].fields.items;
            for (var i = 0; i < items.length; i++) {
                var name = items[i].name;
                models[name] = records[j].get(name);
            }
            result.push(models);
        }

        return result;
    }
});