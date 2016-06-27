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
Ext.define('Flamingo2.view.archive.mapreduce.ArchiveMapReduce', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.archiveMapReduce',

    requires: [
        'Flamingo2.view.archive.mapreduce.ArchiveMapReduceController',
        'Flamingo2.view.archive.mapreduce.ArchiveMapReduceModel',
        'Flamingo2.view.archive.mapreduce.ArchiveMapReduceSumChart',
        'Flamingo2.view.archive.mapreduce.ArchiveMapReduceJobs',
        'Flamingo2.view.archive.mapreduce.ArchiveMapReduceJobSummary',
        'Flamingo2.view.archive.mapreduce.ArchiveMapReduceJobCounters',
        'Flamingo2.view.archive.mapreduce.ArchiveMapReduceConfiguration',
        'Flamingo2.view.archive.mapreduce.ArchiveMapReduceTasks'
    ],

    controller: 'archiveMapReduceViewController',

    viewModel: {
        type: 'archiveMapReduceModel'
    },

    flex: 1,
    scrollable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [
        {
            iconCls: 'common-view',
            border: true,
            title: message.msg('monitoring.history.msg.finished_mr_job'),
            xtype: 'archiveMapReduceSumChart'
        },
        {
            iconCls: 'common-view',
            border: true,
            margin: '5 0',
            flex: 1,
            minHeight: 210,
            xtype: 'archiveMapReduceJobs'
        },
        {
            margin: '5 0',
            height: 343,
            border: true,
            xtype: 'tabpanel',
            items: [
                {
                    title: message.msg('monitoring.history.msg.mr_job_sum'),
                    iconCls: 'common-view',
                    xtype: 'archiveMapReduceJobSummary'
                },
                {
                    title: message.msg('monitoring.history.msg.mr_job_counter'),
                    iconCls: 'common-view',
                    xtype: 'archiveMapReduceJobCounters'
                },
                {
                    title: message.msg('monitoring.history.msg.mr_job_cofig'),
                    iconCls: 'common-view',
                    xtype: 'archiveMapReduceConfiguration'
                },
                {
                    title: message.msg('monitoring.history.msg.mr_job_task'),
                    iconCls: 'common-view',
                    xtype: 'archiveMapReduceTasks'
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