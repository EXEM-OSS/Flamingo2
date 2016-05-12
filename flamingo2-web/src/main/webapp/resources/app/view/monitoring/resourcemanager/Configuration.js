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
Ext.define('Flamingo2.view.monitoring.resourcemanager.Configuration', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.resourceManagerConfiguration',

    columns: [
        {text: 'Key', width: 450, dataIndex: 'key'},
        {text: 'Value', flex: 1, dataIndex: 'value'}
    ],

    /*
     tools: [
     {
     type: 'save',
     tooltip: 'XML 파일로 다운로드',
     handler: function () {
     // show help here
     }
     }
     ],
     */

    multiColumnSort: true,

    initComponent: function () {
        var me = this;

        me.store = new Ext.data.Store({
            fields: [
                {name: 'key'},
                {name: 'value'}
            ],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.RM.CONFIGURATION,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            autoLoad: true,
            sorters: [
                {
                    property: 'file',
                    direction: 'DESC'
                },
                'file'
            ],
            listeners: {
                sort: me.updateSortTitle,
                scope: me
            }
        });

        me.callParent(arguments);

        me.updateSortTitle();
    },

    updateSortTitle: function () {
        var sortDetail = [];

        this.store.getSorters().each(function (sorter) {
            sortDetail.push(sorter.getProperty() + ' ' + sorter.getDirection());
        });
//        this.down('#order').update('Sorted By: ' + sortDetail.join(', '));
    }
});