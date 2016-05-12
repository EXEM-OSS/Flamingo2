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
 * System > HAWQ > Lock Table
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.table.HawqLTableController
 * @see Flamingo2.view.system.hawq.HawqAuthModel
 */
Ext.define('Flamingo2.view.system.hawq.table.HawqLTable', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.hawqLTable',

    controller: 'hawqLTableController',
    viewModel: 'hawqAuthModel',
    layout: 'fit',

    title: message.msg('hawq.title.list.ltable'),

    listeners: {
        afterrender: 'hawqLTableAfterrender'
    },

    initComponent: function () {
        var me = this,
            lTableStore = me.getViewModel().getStore('hawqLTable');

        me.items = [
            {
                xtype: 'grid',
                reference: 'hawqLTableGrid',
                border: 1,
                store: lTableStore,
                viewConfig: {
                    stripeRows: true,
                    columnLines: true,
                    enableTextSelection: true
                },
                columns: [
                    {text: message.msg('hawq.label.ltable.locktype'), dataIndex: 'locktype', flex: 1, align: 'center'},
                    {text: message.msg('hawq.label.ltable.relation'), dataIndex: 'relation', flex: 1, align: 'center'},
                    {text: message.msg('hawq.label.ltable.mode'), dataIndex: 'mode', flex: 1, align: 'center'},
                    {
                        text: message.msg('hawq.label.ltable.waitingpid'),
                        dataIndex: 'waiting_pid',
                        flex: 1,
                        align: 'center',
                        renderer: 'hawqWaitingQueryRenderer'
                    },// 두번째 실행된 쿼리 pid
                    {
                        xtype: 'actioncolumn',
                        text: message.msg('hawq.session.kill'),
                        width: 40,
                        align: 'center',
                        iconCls: 'common-kill',
                        handler: 'waitingKillActioncolumnHandler'
                    },
                    {
                        text: message.msg('hawq.label.ltable.otherpid'),
                        dataIndex: 'other_pid',
                        flex: 1,
                        align: 'center',
                        renderer: 'hawqOtherQueryRenderer'
                    },// 첫번째 실행된 쿼리 pid
                    {
                        xtype: 'actioncolumn',
                        text: message.msg('hawq.session.kill'),
                        width: 40,
                        align: 'center',
                        iconCls: 'common-kill',
                        handler: 'otherKillActioncolumnHandler'
                    }
                ],
                bbar: [
                    {
                        xtype: 'pagingtoolbar',
                        store: lTableStore,
                        displayInfo: true
                    }
                ]
            }
        ];

        me.callParent(arguments);
    }
});