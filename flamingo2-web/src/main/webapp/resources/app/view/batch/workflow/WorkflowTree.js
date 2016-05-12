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

Ext.define('Flamingo2.view.batch.workflow.WorkflowTree', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.workflowTreePanel',

    requires: [
        'Flamingo2.view.batch.workflow.WorkflowTreeModel',
        'Flamingo2.view.batch.workflow.WorkflowTreeController'
    ],

    controller: 'batchWorkflowtree',

    viewModel: {
        type: 'workflowTreeModel'
    },

    forceFit: true,

    bind: {
        store: '{workflowTreeStore}'
    },

    dockedItems: [
        {
            xtype: 'toolbar',
            items: [
                {
                    iconCls: 'common-expand',
                    text: message.msg('common.expand'),
                    tooltip: message.msg('common.tree.elapsedesc'),
                    handler: function () {
                        var panel = query('workflowTreePanel');
                        panel.expandAll();
                    }
                },
                {
                    iconCls: 'common-collapse',
                    text: message.msg('common.collapse'),
                    tooltip: message.msg('common.tree.collapsedesc'),
                    handler: function () {
                        var panel = query('workflowTreePanel');
                        panel.collapseAll();
                    }
                },
                '->',
                {
                    iconCls: 'common-refresh',
                    text: message.msg('common.refresh'),
                    tooltip: message.msg('common.tree.refreshdesc'),
                    handler: function () {
                        var panel = query('workflowTreePanel');
                        panel.getStore().load();
                    }
                }
            ]
        }
    ],
    listeners: {
        select: 'onSelect'
    }
});