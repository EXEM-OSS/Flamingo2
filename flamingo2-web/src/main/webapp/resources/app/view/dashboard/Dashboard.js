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
Ext.define('Flamingo2.view.dashboard.Dashboard', {
    extend: 'Flamingo2.panel.Panel',

    requires: [
        'Flamingo2.view.dashboard.DashboardController',
        'Flamingo2.view.dashboard.DashboardSumChart',
        'Flamingo2.view.dashboard.DashboardModel',
        'Flamingo2.view.dashboard.WorkflowHistory'
    ],

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    controller: 'dashboardController',

    viewModel: {
        type: 'dashboardModel'
    },

    items: [
        {
            iconCls: 'common-view',
            title: message.msg('dashboard.wh.summary'),
            border: true,
            xtype: 'dashboardSumChart',
            status: 'ALL'
        },
        {
            title: message.msg('dashboard.wh.title'),
            margin: '5 0 0 0',
            height: 480,
            border: true,
            xtype: 'workflowHistory'
        }
    ]
});