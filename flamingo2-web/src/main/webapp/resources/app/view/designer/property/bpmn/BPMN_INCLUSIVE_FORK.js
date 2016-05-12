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
Ext.define('Flamingo2.view.designer.property.bpmn.BPMN_INCLUSIVE_FORK', {
    extend: 'Flamingo2.view.designer.property._NODE_SHELL',
    alias: 'widget.BPMN_INCLUSIVE_FORK',

    width: 850,
    height: 550,

    requires: [
        'Flamingo2.view.designer.property.bpmn.BPMN_controller',
        'Flamingo2.view.designer.property._InclusiveGrid',
        'Flamingo2.view.designer.property._ParallelGrid',
        'Flamingo2.view.designer.property._NodeValueGrid',
        'Flamingo2.view.designer.editor.WorkEditor'
    ],

    controller: 'designer.bpmn_controller',

    items: [
        {
            title: message.msg('workflow.etc.inclusivefork.flows.title'),
            xtype: '_parallelGrid',
            reference: '_parallelGrid'
        },
        {
            mainform: null,
            title: message.msg('workflow.etc.inclusivefork.condition.title'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'hidden',
                    name: 'sequenceData',
                    reference: 'sequenceData',
                    value: '',
                    allowBlank: true
                },
                {
                    xtype: 'hidden',
                    name: 'conditions',
                    reference: 'conditions',
                    value: '',
                    allowBlank: true
                },
                {
                    xtype: 'panel',
                    flex: 1,
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: '_inclusiveGrid',
                            title: message.msg('workflow.etc.inclusivefork.condition.script.inclusive.title'),
                            reference: '_inclusiveGrid',
                            flex: 1,
                            margin: '0 5 5 0',
                            listeners: {
                                select: 'onInclusiveGridSelect'
                            }
                        },
                        {
                            xtype: '_nodeValueGrid',
                            title: message.msg('workflow.common.workflow.variable'),
                            flex: 1,
                            margin: '0 0 5 0',
                            layout: {
                                type: 'hbox',
                                align: 'stretch'
                            },
                            search: 'variable',
                            editor: 'BPMN_INCLUSIVE_FORK [name=json]'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    title: message.msg('workflow.etc.inclusivefork.condition.script.title'),
                    flex: 2,
                    layout: {
                        type: 'hbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            itemId: 'json',
                            name: 'json',
                            xtype: 'workflowEditor',
                            reference: 'workflowEditor',
                            forceFit: true,
                            layout: 'fit',
                            flex: 5,
                            theme: 'eclipse',
                            printMargin: true,
                            parser: 'javascript',
                            closable: false,
                            value: ''
                        }
                    ]
                }

            ]
        }
    ],
    listeners: {
        afterrender: 'onINCLUSIVE_FORKafterrender'
    }
});