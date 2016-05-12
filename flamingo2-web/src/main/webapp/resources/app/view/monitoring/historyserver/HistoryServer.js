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
Ext.define('Flamingo2.view.monitoring.historyserver.HistoryServer', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.historyServer',

    controller: 'historyServerController',

    viewModel: {
        type: 'historyServerModel'
    },

    requires: [
        'Flamingo2.view.monitoring.historyserver.HistoryServerController',
        'Flamingo2.view.monitoring.historyserver.HistoryServerModel',

        'Flamingo2.view.monitoring.historyserver.MapReduceJobs',
        'Flamingo2.view.monitoring.historyserver.MapReduceSumChart',
        'Flamingo2.view.monitoring.historyserver.JobSummary',
        'Flamingo2.view.monitoring.historyserver.Configuration',
        'Flamingo2.view.monitoring.historyserver.JobCounters',
        'Flamingo2.view.monitoring.historyserver.Tasks',
        'Flamingo2.view.monitoring.historyserver.TaskCounters'
    ],

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [
        {
            iconCls: 'common-view',
            border: true,
            title: message.msg('monitoring.history.msg.finished_mr_job'),
            xtype: 'mapReduceSumChart'
        },
        {
            iconCls: 'common-view',
            border: true,
            margin: '5 0 0 0',
            height: 210,
            xtype: 'mapReduceJobs',
            reference: 'mapReduceJobs'
        },
        {
            margin: '5 0 0 0',
            height: 343,
            border: true,
            xtype: 'tabpanel',
            items: [
                {
                    title: message.msg('monitoring.history.msg.mr_job_sum'),
                    iconCls: 'common-view',
                    xtype: 'jobSummary'
                },
                {
                    title: message.msg('monitoring.history.msg.mr_job_counter'),
                    iconCls: 'common-view',
                    xtype: 'panel',
                    layout: 'fit',
                    items: [{
                        xtype: 'jobCounters',
                        reference: 'jobCounters'
                    }]
                },
                {
                    title: message.msg('monitoring.history.msg.mr_job_cofig'),
                    iconCls: 'common-view',
                    xtype: 'historyServerConfiguration'
                },
                {
                    title: message.msg('monitoring.history.msg.mr_job_task'),
                    iconCls: 'common-view',
                    xtype: 'tasks'
                }
            ],
            listeners: {
                tabchange: 'onTabChanged'
            }
        }
    ],

    listeners: {
        engineChanged: 'onEngineChanged'
    }

});