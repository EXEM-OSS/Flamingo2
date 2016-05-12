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
Ext.define('Flamingo2.view.setting.Preferences', {
    extend: 'Ext.form.Panel',
    alias: 'widget.preferences',

    bodyPadding: 10,
    border: false,
    autoScroll: true,
    reference: 'preferencesForm',

    items: [
        {
            xtype: 'fieldset',
            title: message.msg('setting.change_password'),
            items: [
                {
                    xtype: 'fieldcontainer',
                    labelWidth: 90,
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'textfield',
                            inputType: 'password',
                            flex: 1,
                            name: 'password1',
                            reference: 'password1',
                            margin: '0 5 0 0',
                            allowBlank: false,
                            minLength: 4,
                            maxLength: 20,
                            emptyText: message.msg('common.password')
                        },
                        {
                            xtype: 'textfield',
                            inputType: 'password',
                            flex: 1,
                            labelWidth: 70,
                            name: 'password2',
                            reference: 'password2',
                            margin: '0 0 0 5',
                            allowBlank: false,
                            minLength: 4,
                            maxLength: 20,
                            emptyText: message.msg('common.retype')
                        },
                        {
                            xtype: 'button',
                            margin: '0 0 0 10',
                            text: message.msg('common.change'),
                            handler: 'onChangePasswordClick'
                        }
                    ]
                }
            ]
        }
    ]
});
