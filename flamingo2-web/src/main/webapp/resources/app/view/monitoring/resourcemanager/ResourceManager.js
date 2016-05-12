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
Ext.define('Flamingo2.view.monitoring.resourcemanager.ResourceManager', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.resourceManager',

    requires: [
        'Flamingo2.view.monitoring.resourcemanager.ResourceManagerController',
        'Flamingo2.view.monitoring.resourcemanager.ResourceManagerModel',
        'Flamingo2.view.monitoring.resourcemanager.RunningApplications',
        'Flamingo2.view.monitoring.resourcemanager.ResourceManagerSummary',
        'Flamingo2.view.monitoring.resourcemanager.ResourceChart',
        'Flamingo2.view.monitoring.resourcemanager.Configuration'
    ],

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    controller: 'resourcemanagerController',

    viewModel: {
        type: 'resourceManagerModel'
    },

    items: [
        {
            title: message.msg('monitoring.rm.rm_summary'),
            iconCls: 'common-view',
            border: true,
            xtype: 'resourceManagerSummary'
        },
        {
            title: message.msg('monitoring.rm.run_yarn_app'),
            iconCls: 'common-view',
            margin: '5 0 0 0',
            height: 150,
            border: true,
            xtype: 'runningApplications'
        },
        {
            xtype: 'resourceChart'
        },
        {
            title: message.msg('monitoring.rm.manager_config'),
            iconCls: 'common-view',
            margin: '5 0 0 0',
            height: 300,
            border: true,
            xtype: 'resourceManagerConfiguration'
        }
    ],

    listeners: {
        engineChanged: 'onEngineChanged'
    }
});