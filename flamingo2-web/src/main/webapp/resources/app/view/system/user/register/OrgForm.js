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
Ext.define('Flamingo2.view.system.user.register.OrgForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.orgForm',

    requires: [
        'Flamingo2.view.system.user.combo.OrganizationCombo'
    ],

    bodyPadding: 20,
    defaults: {
        labelWidth: 100,
        style: 'margin-right:20px;',
        labelAlign: 'right'
    },
    reference: 'orgForm',
    items: [
        {
            xtype: 'textfield',
            reference: 'orgCode',
            name: 'org_code',
            anchor: '100%',
            fieldLabel: message.msg('system.user.common.orgCode'),
            allowBlank: false
        },
        {
            xtype: 'textfield',
            reference: 'orgName',
            name: 'org_name',
            anchor: '100%',
            fieldLabel: message.msg('system.user.common.orgName'),
            allowBlank: false
        },
        {
            xtype: 'textfield',
            reference: 'orgDescription',
            name: 'org_description',
            anchor: '100%',
            fieldLabel: message.msg('system.user.common.orgDescription'),
            allowBlank: false
        },
        {
            xtype: 'organizationCombo',
            reference: 'orgCombo',
            itemId: 'orgCombo',
            anchor: '100%',
            fieldLabel: message.msg('system.user.common.orgList'),
            emptyText: message.msg('system.user.common.org.select'),
            bind: {
                store: '{orgRegisterComboStore}'
            },
            hidden: true
        }
    ]
});