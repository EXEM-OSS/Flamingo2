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
Ext.define('Flamingo2.view.batch.Job', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.job',

    requires: [
        'Flamingo2.view.batch.JobController',
        'Flamingo2.view.batch.JobModel',
        'Flamingo2.view.batch.chart.EngineSummaryChart',
        'Flamingo2.view.batch.JobList'
    ],

    controller: 'jobController',

    viewModel: {
        type: 'jobModel'
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    flex: 1,

    items: [
        {
            xtype: 'engineSummaryChart',
            reference: 'batchChart',
            height: 250
        },
        {
            xtype: 'jobListGrid',
            reference: 'grdJobList',
            region: 'center',
            title: message.msg('batch.batch_list'),
            iconCls: 'common-view',
            flex: 1
        }
    ]
});