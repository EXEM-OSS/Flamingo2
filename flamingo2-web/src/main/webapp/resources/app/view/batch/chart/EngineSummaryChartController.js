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
Ext.define('Flamingo2.view.batch.chart.EngineSummaryChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.enginesummarychart',

    onAfterrender: function () {
        var me = this;
        var refs = me.getReferences();

        Ext.defer(function () {
            refs.engineJvmHeapUsage.getStore().proxy.extraParams.clusterName = ENGINE.id;
            refs.jobs.getStore().proxy.extraParams.clusterName = ENGINE.id;

            refs.engineJvmHeapUsage.getStore().load();
            refs.jobs.getStore().load();
        }, 100);
    },

    /**
     * JVM Heap 사용량 정보를 업데이트한다.
     */
    onJVNHeapRefreshClick: function () {
        var jvmHeapUsageChart = query('engineJvmHeapUsage');

        setTimeout(function() {
            jvmHeapUsageChart.getStore().load();
        }, 100);
    },

    /**
     * 실행 중인 배치작업 현황 정보를 업데이트한다.
     */
    onRunningBatchJobsRefreshClick: function () {
        var runningBatchJobsChart = query('jobs');

        setTimeout(function() {
            runningBatchJobsChart.getStore().load();
        }, 100);
    }
});