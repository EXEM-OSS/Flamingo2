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
Ext.define('Flamingo2.view.designer.Designer', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.workflowDesigner',

    requires: [
        'Flamingo2.view.designer.DesignerModel',
        'Flamingo2.view.designer.nodeList.NodeTab',
        'Flamingo2.view.designer.workflowTree.WorkflowTree',
        'Flamingo2.view.designer.canvas.Canvas',
        'Flamingo2.view.designer.variableGrid.VariableGrid',
        'Flamingo2.view.designer.workflowFolderTree.FolderTree',
        'Flamingo2.view.designer.ux.Toast'
    ],

    viewModel: {
        type: 'designerModel'
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    flex: 1,
    border: false,
    items: [
        {
            region: 'center',
            layout: 'fit',
            height: 160,
            collapseMode: 'mini',
            collapsible: true,
            split: false,
            header: false,
            items: {
                xtype: 'nodeTab'
            }
        },
        {
            region: 'south',
            layout: 'border',
            flex: 1,
            items: [
                {
                    region: 'center',
                    layout: 'fit',
                    items: {
                        border: true,
                        xtype: 'canvas'
                    }
                },
                {
                    title: message.msg('workflow.common.workflow.variable'),
                    region: 'east',
                    layout: 'fit',
                    width: 250,
                    minWidth: 250,
                    maxWidth: 500,
                    collapsible: true,
                    split: true,
                    border: true,
                    collapsed: true,
                    items: {
                        xtype: 'variableGrid'
                    }
                },
                {
                    title: message.msg('common.workflow'),
                    region: 'west',
                    layout: 'fit',
                    width: 250,
                    minWidth: 200,
                    maxWidth: 300,
                    collapsible: true,
                    collapsed: false,
                    border: true,
                    split: true,
                    items: {
                        xtype: 'workflowTree'
                    }
                }
            ]
        }
    ]
});
