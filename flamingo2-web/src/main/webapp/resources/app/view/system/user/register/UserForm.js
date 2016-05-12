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
Ext.define('Flamingo2.view.system.user.register.UserForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.userForm',

    requires: [
        'Flamingo2.view.system.user.combo.OrganizationCombo',
        'Flamingo2.view.system.authority.combo.UserLevelCombo'
    ],

    bodyPadding: 15,
    defaults: {
        labelWidth: 90,
        labelAlign: 'right'
    },
    reference: 'userForm',
    items: [
        {
            xtype: 'textfield',
            reference: 'username',
            name: 'username',
            labelAlign: 'right',
            fieldLabel: message.msg('system.user.common.username'),
            anchor: '80%',
            allowBlank: false,
            allowOnlyWhitespace: false
        },
        {
            xtype: 'textfield',
            reference: 'password',
            anchor: '70%',
            fieldLabel: message.msg('system.user.common.password'),
            inputType: 'password',
            allowBlank: false
        },
        {
            xtype: 'textfield',
            reference: 'confirmPassword',
            anchor: '70%',
            fieldLabel: message.msg('system.user.common.password.confirm'),
            inputType: 'password',
            allowBlank: false
        },
        {
            xtype: 'textfield',
            reference: 'name',
            name: 'name',
            anchor: '70%',
            fieldLabel: message.msg('system.user.common.name'),
            allowBlank: false
        },
        {
            xtype: 'textfield',
            reference: 'email',
            name: 'email',
            anchor: '100%',
            fieldLabel: message.msg('system.user.common.email'),
            vtype: 'email',
            allowBlank: false
        },
        {
            xtype: 'organizationCombo',
            reference: 'orgCombo',
            itemId: 'orgCombo',
            anchor: '100%',
            fieldLabel: message.msg('system.user.common.org'),
            allowBlank: false,
            emptyText: message.msg('system.user.common.org.select'),
            bind: {
                store: '{orgComboStore}'
            }
        },
        {
            xtype: 'checkboxgroup',
            reference: 'securityRoles',
            fieldLabel: message.msg('system.user.common.authName'),
            name: 'security_roles',
            columns: 1,
            anchor: '70%',
            allowBlank: false,
            items: [
                {
                    xtype: 'checkboxfield',
                    itemId: 'adminUser',
                    inputValue: 'ROLE_ADMIN',
                    reference: 'isAdmin',
                    name: 'isAdmin',
                    uncheckedValue: '',
                    boxLabel: message.msg('system.user.common.manager'),
                    handler: function (checkbox) {
                        var userLevel = query('userForm #userLevelCombo');
                        if (checkbox.checked) {
                            userLevel.setValue(1);
                        } else
                            userLevel.setValue('');
                    }
                },
                {
                    xtype: 'checkboxfield',
                    itemId: 'roleUser',
                    inputValue: 'ROLE_USER',
                    name: 'isUser',
                    uncheckedValue: '',
                    boxLabel: message.msg('system.user.common.user'),
                    checked: true
                }
            ]
        },
        {
            xtype: 'userLevelCombo',
            reference: 'userLevelCombo',
            itemId: 'userLevelCombo',
            fieldLabel: message.msg('system.user.common.level'),
            emptyText: message.msg('system.user.common.level.select'),
            anchor: '80%',
            bind: {
                store: '{userLevelComboStore}'
            },
            renderer: function () {
                var me = this;
                var refs = me.getReferences();

                if (refs.securityRoles.getValue()) {
                    refs.userLevelCombo.setValue(1);
                }
            }
        }
    ]
});