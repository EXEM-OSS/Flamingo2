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
Ext.define('Flamingo2.view.designer.property._HiveTableCreator', {
    extend: 'Ext.panel.Panel',
    alias: 'widget._hiveTableCreator',

    requires: [
        'Flamingo2.view.designer.property._HiveDelimiterSelCmbField',
        'Flamingo2.view.designer.property._HiveColumnGrid'
    ],

    layout: 'border',

    border: false,

    initComponent: function () {
        this.items = [
            {
                xtype: 'form',
                region: 'north',
                bodyPadding: 5,
                items: [
                    {
                        xtype: 'fieldcontainer',
                        layout: 'vbox',
                        defaults: {
                            width: 340
                        },
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'tableName',
                                itemId: 'tableName',
                                fieldLabel: 'Table Name',
                                allowBlank: false,
                                vtype: 'alphanum',
                                value: ''
                            },
                            {
                                xtype: 'textfield',
                                name: 'comment',
                                itemId: 'comment',
                                fieldLabel: 'Comment'
                            },
                            {
                                itemId: 'delimiter',
                                xtype: '_hiveDelimiterSelCmbField'
                            }
                        ]
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        defaults: {
                            width: 340
                        },
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'location',
                                itemId: 'location',
                                fieldLabel: 'Location',
                                vtype: 'alphanum'
                            },
                            {
                                xtype: 'button',
                                text: 'Browse',
                                width: 60,
                                handler: function () {
                                }
                            }
                        ]
                    }
                ]
            },
            {
                title: message.msg('common.column'),
                region: 'center',
                border: false,
                layout: 'fit',
                items: [
                    {
                        itemId: 'columnGrid',
                        xtype: '_hiveColumnGrid'
                    }
                ]
            },
            {
                title: message.msg('common.partition'),
                region: 'south',
                height: 150,
                border: false,
                layout: 'fit',
                items: [
                    {
                        itemId: 'partitionGrid',
                        xtype: '_hiveColumnGrid'
                    }
                ]
            }
        ];

        this.callParent(arguments);
    }
});