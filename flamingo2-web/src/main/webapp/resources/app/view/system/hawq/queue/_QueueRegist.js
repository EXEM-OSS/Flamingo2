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
/**
 * System > HAWQ > Resource Queue > List grid > tbar add button click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.queue._QueueRegistController
 * @see Flamingo2.view.system.hawq.HawqAuthModel
 */
Ext.define('Flamingo2.view.system.hawq.queue._QueueRegist', {
    extend: 'Ext.window.Window',
    alias: 'widget.queueRegist',
    title: message.msg('hawq.title.create.rqueue'),
    requires: [
        'Flamingo2.view.system.hawq.queue._QueueRegistController'
    ],

    controller: 'queueRegist',
    viewModel: {
        type: 'hawqAuthModel'
    },

    bodyStyle: {
        backgroundColor: '#FFFFFF'
    },

    bbar: [
        '->',
        {
            xtype: 'button',
            text: message.msg('hawq.button.add'),
            iconCls: 'common-save',
            handler: 'onBtnSaveClick'
        },
        {
            xtype: 'button',
            text: message.msg('hawq.button.cancel'),
            iconCls: 'common-cancel',
            handler: 'onBtnCancelClick'
        }
    ],

    items: [
        {
            xtype: 'form',
            reference: 'hawqRQueueCreateForm',
            flex: 1,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            bodyPadding: '0 5 0 0',
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.rqueue.name'),
                    labelAlign: 'right',
                    name: 'queueName',
                    allowBlank: false,
                    margin: '5 0 5 0'
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: message.msg('hawq.rqueue.count'),
                    labelAlign: 'right',
                    name: 'activeStatements',
                    minValue: -1,
                    value: 20
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: message.msg('hawq.rqueue.maxcost'),
                    labelAlign: 'right',
                    name: 'maxCost'
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.rqueue.costovercommit'),
                    labelAlign: 'right',
                    name: 'costOvercommit',
                    editable: false,
                    bind: {
                        store: '{hawqCostOvercommit}'
                    },
                    displayField: 'displ',
                    valueField: 'value'
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: message.msg('hawq.rqueue.mincost'),
                    labelAlign: 'right',
                    name: 'minCost'
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.rqueue.priority'),
                    labelAlign: 'right',
                    name: 'priority',
                    editable: false,
                    bind: {
                        store: '{hawqPriority}'
                    },
                    displayField: 'displ',
                    valueField: 'value'
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'numberfield',
                            fieldLabel: message.msg('hawq.rqueue.memorylimit'),
                            labelAlign: 'right',
                            name: 'memoryLimit',
                            flex: 1
                        },
                        {
                            xtype: 'combobox',
                            name: 'memoryUnit',
                            width: 50,
                            editable: false,
                            bind: {
                                store: '{hawqMemoryUnit}'
                            },
                            displayField: 'displ',
                            valueField: 'value'
                        }
                    ]
                }
            ]
        }
    ]
});