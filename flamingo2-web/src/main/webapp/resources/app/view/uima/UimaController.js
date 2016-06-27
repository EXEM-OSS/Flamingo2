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
Ext.define('Flamingo2.view.uima.UimaController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uimaController',

    /**
     * Engine Combo Box Changed Event
     */
    onEngineChanged: function () {
        var me = this;
        me.onUimaGridAfterRender();
    },

    /**
     * 등록된 UIMA Log 목록을 가져온다.
     */
    onUimaGridAfterRender: function () {
        var uimaManagementGrid = query('uimaManagementGrid');

        setTimeout(function () {
            uimaManagementGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            uimaManagementGrid.getStore().load({
                callback: function (records, operation, success) {
                    if (success) {
                        uimaManagementGrid.setLoading(false);
                    } else {
                        uimaManagementGrid.setLoading(false);
                    }
                }
            });
        }, 10);
    },

    /**
     * 선택한 UIMA Log 정보를 차트에 바인딩한다.
     */
    onUimaSumChartAfterRender: function () {
        var uimaSumChart = query('uimaSumChart');

        Ext.defer(function () {
            uimaSumChart.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    startDate: new Date(),
                    endDate: new Date()
                }
            });
        }, 10);
    },

    /**
     * UIMA Log 목록을 갱신한다.
     */
    onUimaGridRefreshClick: function () {
        this.onUimaGridAfterRender();
    },

    /**
     * UIMA Chart 정보를 갱신한다.
     */
    onUimaSumChartRefreshClick: function () {
        this.onUimaSumChartAfterRender();
    }

});