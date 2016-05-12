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

Ext.define('Flamingo2.store.designer.property.browser.hdfs.DirectoryStore', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.directoryStoreForDesigner',

    autoLoad: false,

    root: {
        text: '/',
//        id: '/',
        expanded: false
    },

    folderSort: true,

    sorters: [
        {
            property: 'text',
            direction: 'ASC'
        }
    ],

    proxy: {
        type: 'ajax',
        url: '/fs/hdfs/directory.json',
        reader: {
            type: 'json',
            rootProperty: 'list'
        }
    },

    listeners: {
        beforeload: function (store, operation, eOpts) {
            this.proxy.setExtraParams({"clusterName": ENGINE.id});
        }
    }
});