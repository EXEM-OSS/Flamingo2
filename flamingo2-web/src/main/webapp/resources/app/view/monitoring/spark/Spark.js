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
 * Created by Park on 15. 8. 7..
 */
Ext.define('Flamingo2.view.monitoring.spark.Spark', {
    extend: 'Flamingo2.panel.Panel',

    requires: [
        'Flamingo2.view.monitoring.spark.SparkModel',
		'Flamingo2.view.monitoring.spark.SparkController',
        'Flamingo2.view.monitoring.spark.JobSumChart',
        'Flamingo2.view.monitoring.spark.ApplicationGrid',
        'Flamingo2.view.monitoring.spark.JobGrid',
        'Flamingo2.view.monitoring.spark.StageGrid',
        'Flamingo2.view.monitoring.spark.EnvironmentGrid',
        'Flamingo2.view.monitoring.spark.ExecutorGrid',
        'Flamingo2.view.monitoring.spark.StorageGrid'
    ],

    viewModel: {
        type: 'sparkModel'
    },

    controller: 'sparkController',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    flex: 1,
    scrollable: true,

    defaults: {
        margin: '0 0 5 0'
    },

    items: [
        {
            xtype: 'sparkJobSumChart',
            title: 'Spark Job 통계',
            border: true,
            height: 210
        },
        {
            xtype: 'sparkAppGrid',
            reference: 'appGrid',
            title: '모든 Spark 애플리케이션',
            border: true,
            minHeight: 210,
            flex: 1
        },
        {
            xtype: 'tabpanel',
            reference: 'detailTab',
            height: 350,
            items: [{
                xtype: 'sparkJobGrid',
                reference: 'jobGrid',
                title: 'Jobs'
            },{
                xtype: 'sparkStageGrid',
                reference: 'stageGrid',
                title: 'Stages'
            },{
                xtype: 'sparkStorageGrid',
                title: 'Storage'
            },{
                xtype: 'sparkEnvironmentGrid',
                title: 'Environment'
            },{
                xtype: 'sparkExecutorGrid',
                title: 'Executors'
            }],
            listeners: {
                tabchange: 'onDetailTabchange'
            }
        }
    ]
});