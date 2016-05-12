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

Ext.define('Flamingo2.view.designer.monitoring.WorkflowMonitoringWindow', {
    extend: 'Ext.window.Window',

    requires: [
        'Flamingo2.view.designer.monitoring.WorkflowMonitoringController',
        'Flamingo2.view.designer.monitoring.WorkflowMonitoringModel',
        'Flamingo2.view.designer.monitoring.WorkflowMonitoring'
    ],

    controller: 'workflowMonitoringController',

    viewModel: {
        type: 'workflowMonitoringModel'
    },

    title: message.msg('dashboard.wh.title'),
    width: 850,
    height: 500,
    layout: 'fit',
    modal: true,
    closeAction: 'destroy',
    resizable: false,
    items: [
        {
            xtype: 'workflowMonitoring'
        }
    ],

    listeners: {
        afterrender: 'onAfterRender',
        beforedestroy: 'destroyProgresses'
    }
});