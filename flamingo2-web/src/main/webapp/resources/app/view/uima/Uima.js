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
Ext.define('Flamingo2.view.uima.Uima', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.uima',

    requires: [
        'Flamingo2.view.uima.UimaController',
        'Flamingo2.view.uima.UimaModel',
        'Flamingo2.view.uima.chart.UimaSumChart',
        'Flamingo2.view.uima.grid.UimaManagement'
    ],

    controller: 'uimaController',

    viewModel: {
        type: 'uimaModel'
    },

    height: 800,
    scrollable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [
        {
            xtype: 'uimaSumChart',
            title: 'UIMA Summary Chart',
            iconCls: 'common-view',
            flex: 1,
            border: true
        },
        {
            xtype: 'uimaManagementGrid',
            itemId: 'uimaManagementGrid',
            title: 'UIMA Log List',
            iconCls: 'common-view',
            flex: 1,
            border: true
        }
    ]
});