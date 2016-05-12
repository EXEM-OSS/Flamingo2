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
Ext.define('Flamingo2.view.batch.chart.EngineSummaryChart', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.engineSummaryChart',

    requires: [
        'Flamingo2.view.batch.chart.EngineSummaryChartController',
        'Flamingo2.view.batch.chart.EngineSummaryChartModel',
        'Flamingo2.view.batch.chart.JvmHeapUsage',
        'Flamingo2.view.batch.chart.Jobs'
    ],

    controller: 'enginesummarychart',
    viewModel: {
        type: 'enginesummarychart'
    },

    layout: {
        type: 'hbox',
        align: 'stretch'
    },

    items: [
        {
            xtype: 'engineJvmHeapUsage',
            reference: 'engineJvmHeapUsage',
            title: message.msg('batch.jvm_heap_usage'),
            iconCls: 'common-view',
            border: true,
            flex: 1,
            padding: '0 5 5 0'
        },
        {
            xtype: 'jobs',
            reference: 'jobs',
            title: message.msg('batch.running_job'),
            iconCls: 'common-view',
            border: true,
            flex: 1,
            padding: '0 0 5 5'
        }
    ],

    listeners: {
        afterrender: 'onAfterrender'
    }
});