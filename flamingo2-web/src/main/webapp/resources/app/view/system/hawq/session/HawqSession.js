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
 * System > HAWQ > Session
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.session.HawqSessionController
 * @see Flamingo2.view.system.hawq.HawqAuthModel
 */
Ext.define('Flamingo2.view.system.hawq.session.HawqSession', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.hawqSession',

    controller: 'hawqSessionController',
    viewModel: 'hawqAuthModel',
    layout: 'fit',

    listeners: {
        afterrender: 'hawqSessionAfterrender'
    },

    initComponent: function () {
        var me = this,
            sessionStore = me.getViewModel().getStore('hawqSession');

        me.items = [
            {
                xtype: 'grid',
                reference: 'hawqSessionGrid',
                border: 1,
                store: sessionStore,
                viewConfig: {
                    stripeRows: true,
                    columnLines: true,
                    enableTextSelection: true
                },
                columns: [
                    {text: message.msg('common.number'), xtype: 'rownumberer', width: 50, align: 'center'},
                    {
                        xtype: 'actioncolumn',
                        text: message.msg('hawq.session.kill'),
                        width: 40,
                        align: 'center',
                        iconCls: 'common-kill',
                        handler: 'sessionKillActioncolumnHandler'
                    },
                    {text: message.msg('hawq.session.waiting'), dataIndex: 'waiting', width: 50, align: 'center'},
                    {text: message.msg('hawq.session.username'), dataIndex: '_username', width: 100, align: 'center'},
                    {text: message.msg('hawq.session.sessid'), dataIndex: 'sess_id', width: 80, align: 'center'},
                    {
                        text: message.msg('hawq.session.backendstart'),
                        dataIndex: 'backend_start',
                        width: 150,
                        align: 'center'
                    },
                    {
                        text: message.msg('hawq.session.currentquery'),
                        dataIndex: 'current_query',
                        width: 200,
                        align: 'center',
                        renderer: 'hawqCurrentqueryRenderer'
                    },
                    {text: message.msg('hawq.session.datname'), dataIndex: 'datname', width: 100, align: 'center'},
                    {text: message.msg('hawq.session.procpid'), dataIndex: 'procpid', width: 80, align: 'center'},
                    {
                        text: message.msg('hawq.session.clientaddr'),
                        dataIndex: 'client_addr',
                        width: 100,
                        align: 'center'
                    },
                    {
                        text: message.msg('hawq.session.appname'),
                        dataIndex: 'application_name',
                        width: 180,
                        align: 'center'
                    }
                ],
                bbar: [
                    {
                        xtype: 'pagingtoolbar',
                        store: sessionStore,
                        displayInfo: true
                    }
                ],
                listeners: {
                    itemdblclick: 'sessionGridItemdblclick'
                }
            }
        ];

        me.callParent(arguments);
    }
});