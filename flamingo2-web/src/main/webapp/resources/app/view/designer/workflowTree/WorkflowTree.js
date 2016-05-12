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
Ext.define('Flamingo2.view.designer.workflowTree.WorkflowTree', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.workflowTree',

    layout: 'border',

    requires: [
        'Flamingo2.view.designer.workflowTree.WorkflowTreeController',
        'Flamingo2.view.designer.workflowTree.WorkflowTreeModel'
    ],

    controller: 'workflowTreeController',

    viewModel: {
        type: 'workflowTreeModel'
    },

    forceFit: true,

    items: [
        {
            itemId: 'workflowTreePanel',
            region: 'center',
            xtype: 'treepanel',
            bind: {
                store: '{workflowTreeStore}'
            },
            reference: 'treepanel',
            useArrows: true,
            dockedItems: [
                {
                    xtype: 'toolbar',
                    items: [
                        {
                            iconCls: 'common-expand',
                            text: message.msg('common.tree.elapse'),
                            tooltip: message.msg('common.tree.elapsedesc'),
                            handler: 'onWorkflowTreeExpand'
                        },
                        {
                            iconCls: 'common-collapse',
                            text: message.msg('common.tree.collapse'),
                            tooltip: message.msg('common.tree.collapsedesc'),
                            handler: 'onWorkflowTreeCollapse'
                        },
                        '->',
                        {
                            itemId: 'refreshButton',
                            text: message.msg('common.refresh'),
                            tooltip: message.msg('common.tree.refreshdesc'),
                            iconCls: 'common-refresh',
                            reference: 'refreshButton',
                            handler: 'onWorkflowTreeRefresh'
                        }
                    ]
                }
            ],
            listeners: {
                load: 'onWorkflowTreeLoad',
                render: 'onWorkflowTreeRender',
                itemappend: 'onWorkflowTreeItemappend',
                itemdblclick: 'onWorkflowTreeItemdblclick',
                itemcontextmenu: 'onWorkflowTreeItemcontextmenu'
            }
        }
    ]
});