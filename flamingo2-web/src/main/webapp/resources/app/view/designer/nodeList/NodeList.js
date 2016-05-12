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
Ext.define('Flamingo2.view.designer.nodeList.NodeList', {
    extend: 'Ext.view.View',
    alias: 'widget.nodeList',

    requires: [
        'Flamingo2.view.designer.nodeList.NodeListController',
        'Flamingo2.view.designer.nodeList.NodeListModel'
    ],

    controller: 'nodeListController',

    viewModel: {
        type: 'nodeListModel'
    },

    bind: {
        store: '{nodemeta}'
    },

    cls: 'node-list',

    tpl: [
        '<div style="width: {[values.length * 80]}px;">',
        '<tpl for=".">',
        '<div class="{[values.disabled ? "thumb-wrap-disable" : "thumb-wrap"]}" id="{identifier}">',
        '<div class="thumb"><img src="{icon}" title="{description}"></div>',
        '<span style="font-size: 12px;word-wrap: break-word;">{name}</span>',
        '</div>',
        '</tpl>',
        '</div>'
    ],
    singleSelect: true,
    trackOver: true,
    autoScroll: true,
    overItemCls: 'x-item-over',
    itemSelector: 'div.thumb-wrap',
    selectedItemClass: 'x-item-selected',

    listeners: {
        beforerender: 'onNodeListBeforerender',
        render: 'onNodeListRender'
    }
});