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
Ext.define('Flamingo2.view.uima.UimaModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.uimaModel',

    data: {
        title: 'UIMA'
    },

    stores: {
        uimaGridStore: {
            model: 'Flamingo2.model.uima.Uima',
            autoLoad: false,
            pageSize: 20,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.UIMA.GET_UIMA_LOGS,
                extraParams: {
                    clusterName: ''
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            sorters: [
                {
                    property: 'logDate',
                    direction: 'desc'
                }
            ]
        },
        uimaSumChartStore: {
            fields: ['time', 'sum'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.UIMA.GET_UIMA_SUMMARY,
                extraParams: {
                    clusterName: ''
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            remoteSort: true,
            sorters: [
                {
                    property: 'sum',
                    direction: 'ASC'
                }
            ]
        }
    }
});