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
 * System > HAWQ > Resource Queue
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.queue.HawqRQueueController
 * @see Flamingo2.view.system.hawq.HawqAuthModel
 */
Ext.define('Flamingo2.view.system.hawq.queue.HawqRQueue', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.hawqRQueue',

    controller: 'hawqRQueueController',
    viewModel: {
        type: 'hawqAuthModel'
    },

    layout: 'border',
    split: true,
    border: 1,

    items: [
        {
            xtype: 'grid',
            region: 'west',
            split: true,
            title: message.msg('hawq.title.list.rqueue'),
            reference: 'hawqRQueueGrid',
            flex: 1,
            bind: {
                store: '{hawqRQueue}'
            },
            tbar: [
                '->',
                {
                    xtype: 'button',
                    text: message.msg('hawq.button.add'),
                    iconCls: 'common-add',
                    handler: 'onBtnNewClick'
                },
                {
                    xtype: 'button',
                    text: message.msg('common.refresh'),
                    iconCls: 'common-refresh',
                    handler: 'onBtnRefreshClick'
                }
            ],
            viewConfig: {
                stripeRows: true,
                columnLines: true,
                enableTextSelection: true
            },
            columns: [
                {text: message.msg('hawq.rqueue.name'), dataIndex: 'rsqname', flex: 1, align: 'center'},
                {text: message.msg('hawq.rqueue.countlimit'), dataIndex: 'rsqcountlimit', flex: 1, align: 'center'}
            ],
            listeners: {
                itemclick: 'hawqRQueueGridItemclick',
                rowcontextmenu: 'hawqRQueueGridRowcontextmenu'
            }
        },
        {
            xtype: 'form',
            region: 'center',
            split: true,
            title: message.msg('hawq.title.detail.rqueue'),
            reference: 'hawqRQueueViewForm',
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
                    name: 'rsqname',
                    readOnly: true,
                    margin: '5 0 5 0'
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.rqueue.countlimit'),
                    labelAlign: 'right',
                    name: 'rsqcountlimit',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.rqueue.count'),
                    labelAlign: 'right',
                    name: 'rsqcountvalue',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.rqueue.costlimit'),
                    labelAlign: 'right',
                    name: 'rsqcostlimit',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.rqueue.cost'),
                    labelAlign: 'right',
                    name: 'rsqcostvalue',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.rqueue.memorylimit'),
                    labelAlign: 'right',
                    name: 'rsqmemorylimit',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.rqueue.memory'),
                    labelAlign: 'right',
                    name: 'rsqmemoryvalue',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.rqueue.waiters'),
                    labelAlign: 'right',
                    name: 'rsqwaiters',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.rqueue.holders'),
                    labelAlign: 'right',
                    name: 'rsqholders',
                    readOnly: true
                }
            ]
        }
    ]
});