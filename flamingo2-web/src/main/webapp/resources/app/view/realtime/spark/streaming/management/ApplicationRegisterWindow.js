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
Ext.define('Flamingo2.view.realtime.spark.steaming.management.ApplicationRegisterWindow', {
    extend: 'Ext.window.Window',

    requires: [
        'Flamingo2.view.realtime.spark.steaming.management.ApplicationRegisterForm'
    ],

    controller: 'sparkStreamingViewController',

    title: message.msg('spark.streaming.register.title'),
    height: 355,
    width: 540,
    modal: true,
    maximizable: false,
    resizable: false,
    closeAction: 'destroy',
    layout: 'fit',
    items: [
        {
            xtype: 'applicationRegisterForm'
        }
    ],
    buttonAlign: 'center',
    buttons: [
        {
            xtype: 'button',
            text: message.msg('common.regist'),
            iconCls: 'common-ok',
            handler: 'onApplicationRegisterOK'
        },
        {
            xtype: 'button',
            text: message.msg('common.cancel'),
            iconCls: 'common-cancel',
            handler: 'onApplicationRegisterCancel'
        }
    ]
});