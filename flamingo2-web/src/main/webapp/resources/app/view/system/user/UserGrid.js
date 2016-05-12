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
Ext.define('Flamingo2.view.system.user.UserGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.userGridPanel',

    requires: [
        'Flamingo2.view.system.user.combo.OrganizationCombo'
    ],

    title: message.msg('system.user.list.title'),
    reference: 'userGridPanel',
    border: true,
    bind: {
        store: '{userStore}'
    },
    columns: [
        {
            text: message.msg('system.user.common.userId'),
            dataIndex: 'id',
            align: 'center',
            hidden: true
        },
        {
            text: message.msg('system.user.common.username'),
            dataIndex: 'username',
            align: 'center',
            width: 100,
            summaryType: 'count',
            summaryRenderer: function (value) {
                return format(message.msg('system.user.total'), toCommaNumber(value))
            }
        },
        {
            text: message.msg('system.user.common.name'),
            dataIndex: 'name',
            align: 'center',
            width: 100
        },
        {
            text: message.msg('system.user.common.email'),
            dataIndex: 'email',
            align: 'center',
            flex: 0.5
        },
        {
            text: message.msg('system.user.common.authId'),
            dataIndex: 'auth_id',
            align: 'center',
            width: 80,
            hidden: true
        },
        {
            text: message.msg('system.user.common.authName'),
            dataIndex: 'auth_name',
            align: 'center',
            width: 80
        },
        {
            text: message.msg('system.user.common.level'),
            dataIndex: 'level',
            align: 'center',
            width: 50
        },
        {
            text: message.msg('system.user.common.orgId'),
            dataIndex: 'org_id',
            align: 'center',
            width: 150,
            hidden: true
        },
        {
            text: message.msg('system.user.common.orgCode'),
            dataIndex: 'org_code',
            align: 'center',
            flex: 0.2
        },
        {
            text: message.msg('system.user.common.orgName'),
            dataIndex: 'org_name',
            align: 'center',
            flex: 0.1,
            hidden: true
        },
        {
            text: message.msg('system.user.common.orgDescription'),
            dataIndex: 'org_description',
            align: 'center',
            width: 150,
            hidden: true
        },
        {
            text: message.msg('system.user.common.linuxUserHome'),
            dataIndex: 'linux_user_home',
            name: 'linux_user_home',
            align: 'center',
            flex: 0.3
        },
        {
            text: message.msg('system.user.common.hdfsUserHome'),
            dataIndex: 'hdfs_user_home',
            name: 'hdfs_user_home',
            align: 'center',
            flex: 0.3
        },
        {
            text: message.msg('common.group'),
            dataIndex: 'user_group',
            align: 'center',
            width: 150,
            hidden: true
        },
        {
            text: message.msg('system.user.common.registerDate'),
            dataIndex: 'register_date',
            name: 'register_date',
            align: 'center',
            width: 150
        },
        {
            text: message.msg('system.user.common.updateDate'),
            dataIndex: 'update_date',
            name: 'update_date',
            align: 'center',
            width: 150
        },
        {
            text: message.msg('system.user.common.userDescription'),
            dataIndex: 'user_description',
            align: 'center',
            width: 150,
            hidden: true
        },
        {
            text: message.msg('system.user.common.status'),
            itemId: 'status',
            dataIndex: 'enabled',
            align: 'center',
            width: 80,
            renderer: function (value) {
                return value ? message.msg('system.user.common.status.ack')
                    : "<span style='color:red;font-weight:bold' >" + message.msg('system.user.common.status.standby') + "</span>";
            }
        }
    ],
    viewConfig: {
        enableTextSelection: true,
        stripeRows: true,
        columnLines: true,
        getRowClass: function () {
            return 'cell-height-30';
        }
    },
    /*,
     features: [
     {
     ftype: 'groupingsummary',
     groupHeaderTpl: format('소속명 : {0}', '{[values.rows[0].data.org_name]}'),
     hideGroupedHeader: false,
     enableGroupingMenu: true
     }
     ]*/
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    xtype: 'tbtext',
                    text: message.msg('system.user.common.searchCondition')
                },
                {
                    xtype: 'combo',
                    name: 'condition_key',
                    reference: 'conditionKey',
                    itemId: 'conditionKey',
                    editable: true,
                    queryMode: 'local',
                    typeAhead: true,
                    selectOnFocus: true,
                    displayField: 'name',
                    valueField: 'value',
                    width: 80,
                    value: 'NAME',
                    store: Ext.create('Ext.data.Store', {
                        fields: ['name', 'value'],
                        data: [
                            {name: message.msg('system.user.common.name'), value: 'NAME'},
                            {name: message.msg('system.user.common.org'), value: 'ORG'},
                            {name: message.msg('system.user.common.email'), value: 'EMAIL'},
                            {name: message.msg('system.user.common.username'), value: 'USERNAME'}
                        ]
                    })
                },
                {
                    xtype: 'textfield',
                    reference: 'condition',
                    itemId: 'condition',
                    emptyText: message.msg('system.user.common.username'),
                    width: 150
                },
                {
                    xtype: 'button',
                    itemId: 'findUserButton',
                    formBind: true,
                    text: message.msg('system.user.common.search'),
                    iconCls: 'common-search',
                    labelWidth: 50,
                    handler: 'onFindUserButton'
                },
                {
                    xtype: 'button',
                    itemId: 'clearUserButton',
                    formBind: true,
                    text: message.msg('system.user.common.clear'),
                    iconCls: 'common-search-clear',
                    labelWidth: 50,
                    handler: 'onClearUserButton'
                },
                '->',
                {
                    xtype: 'tbtext',
                    text: message.msg('system.user.common.org')
                },
                {
                    xtype: 'organizationCombo',
                    itemId: 'orgCombo',
                    reference: 'orgCombo',
                    value: 'ALL',
                    bind: {
                        store: '{organizationStore}'
                    },
                    listeners: {
                        select: 'onSelectComboValue'
                    }
                },
                {
                    xtype: 'splitbutton',
                    text: message.msg('system.user.common.management'),
                    margin: '0 0 0 5',
                    menu: {
                        items: [
                            {
                                text: message.msg('system.user.common.org.add'),
                                iconCls: 'common-group-add',
                                handler: 'onAddOrgClick'
                            },
                            {
                                text: message.msg('system.user.common.org.delete'),
                                iconCls: 'common-group-remove',
                                handler: 'onDeleteOrgClick'
                            },
                            {
                                text: message.msg('system.user.common.org.modify'),
                                iconCls: 'common-group-modify',
                                handler: 'onChangeOrgClick'
                            },
                            '-',
                            {
                                text: message.msg('system.user.common.user.add'),
                                iconCls: 'common-user-add',
                                handler: 'onAddUserClick'
                            },
                            {
                                text: message.msg('system.user.common.user.ack'),
                                iconCls: 'common-user-auth',
                                handler: 'onAckUserClick'
                            },
                            {
                                text: message.msg('system.user.common.user.delete'),
                                iconCls: 'common-user-remove',
                                handler: 'onDeleteUserClick'
                            },
                            {
                                text: message.msg('system.user.common.user.modify'),
                                iconCls: 'common-user-modify',
                                handler: 'onChangeUserClick'
                            },
                            '-',
                            {
                                text: message.msg('system.user.management.refresh'),
                                handler: 'onRefreshUserClick',
                                iconCls: 'common-refresh'
                            }
                        ]
                    }
                }
            ]
        }
    ],
    listeners: {
        itemcontextmenu: function (grid, record, item, index, event) {
            event.stopEvent();
        },
        containercontextmenu: function (grid, event) {
            event.stopEvent();
        }
    }
});