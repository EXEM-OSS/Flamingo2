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
Ext.define('Flamingo2.view.terminal.TerminalLogin', {
    extend: 'Ext.form.Panel',
    alias: 'widget.terminalLogin',

    requires: [
        'Flamingo2.view.terminal.TerminalController'
    ],

    controller: 'terminalController',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [
        {
            xtype: 'textfield',
            name: 'password',
            inputType: 'password',
            emptyText: message.msg('terminal.form.emptyText.password'),
            allowBlank: false,
            listeners: {
                afterrender: 'onPasswordFieldAfterrender',
                specialkey: 'onEnterPasswordField'
            }
        },
        {
            xtype: 'panel',
            reference: 'status'
        }
    ],

    buttonAlign: 'center',

    buttons: [
        {
            text: message.msg('terminal.button.connect'),
            reference: 'submit',
            itemId: 'submitBtn',
            handler: 'onLoginSubmit'
        },
        {
            text: message.msg('terminal.button.reset'),
            reference: 'reset',
            handler: function () {
                this.up('form').getForm().reset();
            }
        }
    ]
});
