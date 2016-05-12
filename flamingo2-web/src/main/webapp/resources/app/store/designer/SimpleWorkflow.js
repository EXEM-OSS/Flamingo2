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

Ext.define('Flamingo2.store.designer.SimpleWorkflow', {
    extend: 'Ext.data.TreeStore',

    autoLoad: false,

    proxy: {
        type: 'ajax',
        url: '/designer/tree/get.json',//CONSTANTS.DESIGNER.GET_TREE,
        reader: {
            type: 'json',
            rootProperty: 'list'
        }
    },
    rootVisible: true,
    root: {
        text: message.msg('common.workflow'),
        id: '/',
        expanded: true
    },
    listeners: {
        beforeload: function (store, operation, eOpts) {
            this.proxy.setExtraParams({"clusterName": ENGINE.id, "type": 'WORKFLOW'});
        }
    }
});