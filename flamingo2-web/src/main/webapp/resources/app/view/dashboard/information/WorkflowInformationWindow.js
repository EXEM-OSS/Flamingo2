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

Ext.define('Flamingo2.view.dashboard.information.WorkflowInformationWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.workflowInformationWindow',

    requires: [
        'Flamingo2.view.dashboard.information.WorkflowInformationController',
        'Flamingo2.view.dashboard.information.WorkflowInformationModel',
        'Flamingo2.view.dashboard.information.tab.JobDetailForm',
        'Flamingo2.view.dashboard.information.tab.ActionDetailForm'
    ],

    controller: 'workflowInformationController',

    viewModel: {
        type: 'workflowInformationModel'
    },

    title: message.msg('dashboard.workflowHistory.information'),
    width: 800,
    height: 650,
    layout: 'fit',
    hideCollapseTool: false,
    titleCollapse: false,
    modal: true,
    resizable: false,
    closable: true,
    closeAction: 'destroy',
    padding: 5,

    items: [
        {
            xtype: 'tabpanel',
            items: [
                {
                    xtype: 'actionDetailForm'
                },
                {
                    xtype: 'jobDetailForm'
                }
            ],
            listeners: {
                afterrender: 'onAfterRender',
                beforerender: 'onAceEditorReady'
            }
        }
    ],
    buttons: [
        {
            text: message.msg('common.ok'),
            handler: 'onCloseWorkflowInformationWindow'
        }
    ]
});