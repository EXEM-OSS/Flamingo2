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
 * System > HAWQ > Role > List grid > tbar add button click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.role._RoleRegistController
 * @see Flamingo2.view.system.hawq.HawqAuthModel
 */
Ext.define('Flamingo2.view.system.hawq.role._RoleRegist', {
    extend: 'Ext.window.Window',
    alias: 'widget.hawqRoleRegist',

    requires: [
        'Flamingo2.view.system.hawq.role._RoleRegistController',
        'Flamingo2.view.system.hawq.HawqAuthModel'
    ],

    controller: 'roleregist',
    viewModel: {
        type: 'hawqAuthModel'
    },

    title: message.msg('hawq.title.create.role'),
    layout: 'fit',

    bbar: [
        '->',
        {
            xtype: 'button',
            text: message.msg('hawq.button.save'),
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

    bodyStyle: {
        backgroundColor: '#FFFFFF'
    },
    bodyPadding: '0 5 0 0',

    items: [
        {
            xtype: 'form',
            reference: 'hawqRoleCreateForm',
            flex: 1,
            autoScroll: true,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            defaults: {
                labelWidth: 130
            },
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.role.name'),
                    labelAlign: 'right',
                    name: 'roleName',
                    allowBlank: false,
                    margin: '5 0 5 0'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.super'),
                    labelAlign: 'right',
                    name: 'superUser'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.createdb'),
                    labelAlign: 'right',
                    name: 'createDB'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.createrole'),
                    labelAlign: 'right',
                    name: 'createRole'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.createexttable'),
                    labelAlign: 'right',
                    name: 'createExtTable'
                },
                {
                    xtype: 'checkboxgroup',
                    hideEmptyLabel: false,
                    columns: 2,
                    reference: 'extTableTypes',
                    vertical: true,
                    items: [
                        {boxLabel: message.msg('hawq.role.rgpfdist'), name: 'rgpfdist'},
                        {boxLabel: message.msg('hawq.role.wgpfdist'), name: 'wgpfdist'},
                        {boxLabel: message.msg('hawq.role.rhttp'), name: 'rhttp'}
                    ]
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.inherit'),
                    labelAlign: 'right',
                    name: 'inherit'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.login'),
                    labelAlign: 'right',
                    name: 'canLogin'
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: message.msg('hawq.label.connlimit'),
                    labelAlign: 'right',
                    name: 'connLimit',
                    minValue: -1
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'textfield',
                            fieldLabel: message.msg('hawq.role.password'),
                            labelWidth: 130,
                            labelAlign: 'right',
                            name: 'password',
                            inputType: 'password'
                        },
                        {
                            xtype: 'checkboxfield',
                            fieldLabel: message.msg('hawq.role.encrypted'),
                            labelAlign: 'right',
                            name: 'encrypted'
                        }
                    ]
                },
                {
                    xtype: 'datefield',
                    fieldLabel: message.msg('hawq.role.validuntil'),
                    labelAlign: 'right',
                    name: 'validUntil',
                    minValue: new Date(),
                    format: 'Y-m-d'
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.role.inrole'),
                    labelAlign: 'right',
                    name: 'inRole',
                    multiSelect: true,
                    bind: {
                        store: '{hawqRole}'
                    },
                    displayField: 'rolname',
                    valueField: 'rolname'
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.role.role'),
                    labelAlign: 'right',
                    name: 'role',
                    multiSelect: true,
                    bind: {
                        store: '{hawqRole}'
                    },
                    displayField: 'rolname',
                    valueField: 'rolname'
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.role.admin'),
                    labelAlign: 'right',
                    name: 'admin',
                    multiSelect: true,
                    bind: {
                        store: '{hawqRole}'
                    },
                    displayField: 'rolname',
                    valueField: 'rolname'
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.role.rqueue'),
                    labelAlign: 'right',
                    name: 'resourceQueue',
                    bind: {
                        store: '{hawqRQueue}'
                    },
                    displayField: 'rsqname',
                    valueField: 'rsqname'
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'combobox',
                            fieldLabel: message.msg('hawq.role.denyfrom'),
                            labelAlign: 'right',
                            labelWidth: 130,
                            name: 'denyFromDay',
                            editable: false,
                            bind: {
                                store: '{hawqDenyDay}'
                            },
                            displayField: 'displ',
                            valueField: 'value'
                        },
                        {
                            xtype: 'timefield',
                            name: 'denyFromTime',
                            margin: '0 0 0 5',
                            increment: 30,
                            time: '',// submit 값
                            listeners: {
                                change: 'hawqRoleDenyTimeChange'
                            }
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'combobox',
                            fieldLabel: message.msg('hawq.role.denyto'),
                            labelAlign: 'right',
                            labelWidth: 130,
                            name: 'denyToDay',
                            editable: false,
                            bind: {
                                store: '{hawqDenyDay}'
                            },
                            displayField: 'displ',
                            valueField: 'value'
                        },
                        {
                            xtype: 'timefield',
                            name: 'denyToTime',
                            margin: '0 0 0 5',
                            increment: 30,
                            time: '',// submit 값
                            listeners: {
                                change: 'hawqRoleDenyTimeChange'
                            }
                        }
                    ]
                }
            ]
        }
    ]
});