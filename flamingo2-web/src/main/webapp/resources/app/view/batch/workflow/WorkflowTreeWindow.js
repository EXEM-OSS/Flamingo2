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

Ext.define('Flamingo2.view.batch.workflow.WorkflowTreeWindow', {
    extend: 'Ext.window.Window',

    requires: [
        'Flamingo2.view.batch.workflow.WorkflowTreeWindowController',
        'Flamingo2.view.batch.workflow.WorkflowTreeModel',
        'Flamingo2.view.batch.workflow.WorkflowTree'
    ],

    controller: 'workflowTreeWindowController',

    viewModel: {
        type: 'workflowTreeModel'
    },

    height: 450,
    width: 350,
    closable: true,
    title: message.msg('batch.workflow_list'),
    modal: true,
    closeAction: 'close',
    layout: 'fit',

    items: [
        {
            xtype: 'workflowTreePanel'
        }
    ],
    buttonAlign: 'center',
    buttons: [
        {
            text: message.msg('common.confirm'),
            handler: 'onWorkflowClickOK'
        },
        {
            text: message.msg('common.cancel'),
            handler: 'onWorkflowClickCancel'
        }
    ]
});