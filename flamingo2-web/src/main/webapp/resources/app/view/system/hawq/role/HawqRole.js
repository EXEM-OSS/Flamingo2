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
 * System > HAWQ > Role
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.role.HawqRoleController
 * @see Flamingo2.view.system.hawq.HawqAuthModel
 */
Ext.define('Flamingo2.view.system.hawq.role.HawqRole', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.hawqRole',

    controller: 'hawqRoleController',
    viewModel: {
        type: 'hawqAuthModel'
    },

    layout: 'border',
    split: true,
    border: 1,

    items: [
        {
            xtype: 'panel',
            region: 'west',
            split: true,
            flex: 1,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'grid',
                    title: message.msg('hawq.title.list.grouprole'),
                    reference: 'hawqGroupRoleGrid',
                    flex: 1,
                    bind: {
                        store: '{hawqGroupRole}'
                    },
                    tbar: [
                        '->',
                        {
                            xtype: 'button',
                            text: message.msg('hawq.button.add'),
                            handler: 'onBtnNewClick',
                            iconCls: 'common-add'
                        },
                        {
                            xtype: 'button',
                            text: message.msg('common.refresh'),
                            handler: 'onBtnRefreshClick',
                            iconCls: 'common-refresh'
                        }
                    ],
                    viewConfig: {
                        stripeRows: true,
                        columnLines: true,
                        enableTextSelection: true
                    },
                    columns: [
                        {text: message.msg('hawq.role.name'), dataIndex: 'rolname', flex: 1, align: 'center'},
                        {text: message.msg('hawq.role.super'), dataIndex: 'rolsuper', flex: 1, align: 'center'}
                    ],
                    listeners: {
                        itemclick: 'onRoleItemclick',
                        rowcontextmenu: 'hawqRoleGridRowcontextmenu'
                    }
                },
                {
                    xtype: 'grid',
                    title: message.msg('hawq.title.list.loginrole'),
                    reference: 'hawqLoginRoleGrid',
                    flex: 1,
                    bind: {
                        store: '{hawqLoginRole}'
                    },
                    tbar: [
                        '->',
                        {
                            xtype: 'button',
                            text: message.msg('hawq.button.add'),
                            handler: 'onBtnNewClick',
                            iconCls: 'common-add'
                        },
                        {
                            xtype: 'button',
                            text: message.msg('common.refresh'),
                            handler: 'onBtnRefreshClick',
                            iconCls: 'common-refresh'
                        }
                    ],
                    viewConfig: {
                        stripeRows: true,
                        columnLines: true,
                        enableTextSelection: true
                    },
                    columns: [
                        {text: message.msg('hawq.role.name'), dataIndex: 'rolname', flex: 1, align: 'center'},
                        {text: message.msg('hawq.role.super'), dataIndex: 'rolsuper', flex: 1, align: 'center'}
                    ],
                    listeners: {
                        itemclick: 'onRoleItemclick',
                        rowcontextmenu: 'hawqRoleGridRowcontextmenu'
                    }
                }
            ]
        },
        {
            xtype: 'form',
            region: 'center',
            split: true,
            title: message.msg('hawq.title.detail.role'),
            reference: 'hawqRoleViewForm',
            flex: 1,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            bodyPadding: '0 5 0 0',
            autoScroll: true,
            tbar: [
                '->',
                {
                    xtype: 'button',
                    text: message.msg('hawq.button.save'),
                    handler: 'onBtnSaveClick',
                    iconCls: 'common-save'
                }
            ],
            defaults: {
                labelWidth: 120
            },
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.role.name'),
                    labelAlign: 'right',
                    name: 'rolname',
                    allowBlank: false,
                    margin: '5 0 5 0'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.super'),
                    labelAlign: 'right',
                    name: 'rolsuper'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.createdb'),
                    labelAlign: 'right',
                    name: 'rolcreatedb'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.createrole'),
                    labelAlign: 'right',
                    name: 'rolcreaterole'
                },
                {
                    xtype: 'checkboxgroup',
                    hideEmptyLabel: false,
                    columns: 2,
                    vertical: true,
                    items: [
                        {boxLabel: message.msg('hawq.role.rgpfdist'), name: 'rolcreaterextgpfd'},
                        {boxLabel: message.msg('hawq.role.wgpfdist'), name: 'rolcreatewextgpfd'},
                        {boxLabel: message.msg('hawq.role.rhttp'), name: 'rolcreaterexthttp'},
                        {boxLabel: message.msg('hawq.role.rhdfs'), name: 'rolcreaterexthdfs'},
                        {boxLabel: message.msg('hawq.role.whdfs'), name: 'rolcreatewexthdfs'}
                    ]
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.inherit'),
                    labelAlign: 'right',
                    name: 'rolinherit'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.login'),
                    labelAlign: 'right',
                    name: 'rolcanlogin'
                },
                {
                    xtype: 'checkboxfield',
                    fieldLabel: message.msg('hawq.role.catupdate'),
                    labelAlign: 'right',
                    name: 'rolcatupdate'
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: message.msg('hawq.label.connlimit'),
                    labelAlign: 'right',
                    name: 'rolconnlimit',
                    minValue: -1
                },
                {
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'textfield',
                            fieldLabel: message.msg('hawq.role.password'),
                            labelAlign: 'right',
                            labelWidth: 120,
                            name: 'rolpassword',
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
                    name: 'rolvaliduntil',
                    format: 'Y-m-d'
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.role.config'),
                    labelAlign: 'right',
                    name: 'rolconfig'
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.role.rqueue'),
                    labelAlign: 'right',
                    name: 'rsqname',
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
                            labelWidth: 120,
                            labelAlign: 'right',
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
                            labelWidth: 120,
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