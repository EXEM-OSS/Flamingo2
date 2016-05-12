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
 * System > HAWQ > Session > grid item double click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.session.HawqSession > hawqSessionGrid
 */
Ext.define('Flamingo2.view.system.hawq.session._SessionDetailWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget._hawqSessionDetailWindow',

    controller: 'hawqSessionController',

    height: 580,
    width: 500,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('hawq.title.detail.session'),
    titleCollapse: false,
    modal: true,
    closeAction: 'destroy',
    layout: 'fit',
    autoScroll: true,
    border: false,
    bodyPadding: 10,
    bodyStyle: {
        background: '#fff'
    },

    listeners: {
        afterrender: 'hawqSessionDetailWindowAfterrender'
    },

    items: [
        {
            xtype: 'form',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            defaultType: 'textfield',
            defaults: {
                labelAlign: 'right',
                readOnly: true
            },
            items: [
                {
                    fieldLabel: message.msg('hawq.session.datid'),
                    name: 'datid'
                },
                {
                    fieldLabel: message.msg('hawq.session.datname'),
                    name: 'datname'
                },
                {
                    fieldLabel: message.msg('hawq.session.procpid'),
                    name: 'procpid'
                },
                {
                    fieldLabel: message.msg('hawq.session.sessid'),
                    name: 'sess_id'
                },
                {
                    fieldLabel: message.msg('hawq.session.usesysid'),
                    name: 'usesysid'
                },
                {
                    fieldLabel: message.msg('hawq.session.username'),
                    name: '_username'
                },
                {
                    fieldLabel: message.msg('hawq.session.waiting'),
                    name: 'waiting'
                },
                {
                    fieldLabel: message.msg('hawq.session.querystart'),
                    name: 'query_start'
                },
                {
                    fieldLabel: message.msg('hawq.session.backendstart'),
                    name: 'backend_start'
                },
                {
                    fieldLabel: message.msg('hawq.session.clientaddr'),
                    name: 'client_addr'
                },
                {
                    fieldLabel: message.msg('hawq.session.clientport'),
                    name: 'client_port'
                },
                {
                    fieldLabel: message.msg('hawq.session.appname'),
                    name: 'application_name'
                },
                {
                    fieldLabel: message.msg('hawq.session.xactstart'),
                    name: 'xact_start'
                },
                {
                    xtype: 'textareafield',
                    fieldLabel: message.msg('hawq.session.currentquery'),
                    name: 'current_query',
                    grow: true,
                    growMax: 140
                }
            ]
        }
    ],

    buttons: [
        {
            text: message.msg('hawq.button.cancel'),
            iconCls: 'common-cancel',
            handler: 'cancelButtonHandler'
        }
    ]
});