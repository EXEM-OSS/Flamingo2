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
Ext.define('Flamingo2.view.system.menu.Menu', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.systemMenu',
    controller: 'systemMenuViewController',
    viewModel: {
        type: 'menuModel'
    },
    flex: 1,
    layout: 'border',
    requires: [
        'Flamingo2.view.system.menu.MenuController',
        'Flamingo2.view.system.menu.MenuModel',

        'Flamingo2.model.system.Menu',
        'Flamingo2.model.system.MenuNode'
    ],

    listeners: {
        afterrender: 'onAfterrender'
    },

    items: [
        {
            xtype: 'treepanel',
            bind: {
                store: '{menu}'
            },
            border: true,
            region: 'center',
            reference: 'trpMenu',
            split: true,
            rootVisible: true,
            expanded: true,
            root: {
                text: 'Flatten',
                expanded: true,
                id: 'TOP'
            },
            flex: 1.2,
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'button',
                            text: message.msg('common.collapse'),
                            iconCls: 'common-collapse',
                            handler: 'onBtnCollapseClick'
                        },
                        {
                            xtype: 'button',
                            text: message.msg('common.expand'),
                            iconCls: 'common-expand',
                            handler: 'onBtnExpandClick'
                        }, '->',
                        {
                            xtype: 'button',
                            text: message.msg('common.refresh'),
                            iconCls: 'common-refresh',
                            handler: 'onBtnRefreshClick'
                        }
                    ]
                }
            ],
            listeners: {
                select: 'onTrpMenuSelect'
            }
        },
        {
            xtype: 'grid',
            reference: 'grdNode',
            region: 'east',
            flex: 4,
            split: true,
            columnLines: true,
            border: true,
            bind: {
                store: '{menuNode}'
            },
            columns: [
                {
                    text: message.msg('menu.id'),
                    align: 'center',
                    dataIndex: 'menu_id'
                },
                {
                    text: message.msg('menu.name'),
                    dataIndex: 'menu_nm',
                    width: 150,
                    align: 'center',
                    editor: 'textfield'
                },
                {
                    text: message.msg('menu.namespace'),
                    dataIndex: 'menu_ns',
                    flex: 1,
                    minWidth: 200,
                    align: 'center',
                    editor: 'textfield'
                },
                {
                    text: message.msg('menu.sort'),
                    dataIndex: 'sort_ordr',
                    align: 'center',
                    width: 40,
                    editor: 'textfield'
                },
                {
                    text: message.msg('menu.use'),
                    dataIndex: 'use_yn',
                    align: 'center',
                    width: 60,
                    editor: {
                        xtype: 'combobox',
                        valueField: 'value',
                        displayField: 'name',
                        store: Ext.create('Ext.data.Store', {
                            editable: false,
                            fields: ['value', 'name'],
                            data: [
                                {
                                    value: 'Y', name: 'Y'
                                },
                                {
                                    value: 'N', name: 'N'
                                }
                            ]
                        })
                    }
                },
                {
                    text: message.msg('menu.icon'),
                    dataIndex: 'icon_css_nm',
                    align: 'center',
                    editor: 'textfield'
                },
                {
                    text: message.msg('menu.name_ko_kr'),
                    dataIndex: 'menu_nm_ko_kr',
                    width: 150,
                    align: 'center',
                    editor: 'textfield'
                },
                {
                    text: message.msg('menu.name_en_us'),
                    dataIndex: 'menu_nm_en_us',
                    width: 150,
                    align: 'center',
                    editor: 'textfield'
                },
                {
                    text: message.msg('menu.name_ja_jp'),
                    dataIndex: 'menu_nm_ja_jp',
                    width: 150,
                    align: 'center',
                    editor: 'textfield'
                },
                {
                    text: message.msg('menu.name_zh_cn'),
                    dataIndex: 'menu_nm_zh_cn',
                    width: 150,
                    align: 'center',
                    editor: 'textfield'
                }
            ],
            plugins: {
                ptype: 'cellediting',
                clicksToEdit: 1
            },
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        '->',
                        {
                            xtype: 'button',
                            text: message.msg('common.add'),
                            iconCls: 'common-add',
                            handler: 'onBtnAddClick'
                        },
                        {
                            xtype: 'button',
                            text: message.msg('common.delete'),
                            iconCls: 'common-remove',
                            handler: 'onBtnRemoveClick'
                        },
                        {
                            xtype: 'button',
                            text: message.msg('common.save'),
                            iconCls: 'common-save',
                            handler: 'onBtnSaveClick'
                        }
                    ]
                }
            ]
        }
    ]
});