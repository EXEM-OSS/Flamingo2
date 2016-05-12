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
 * HAWQ > Browser > Connection Manager > Database Menu > create menu click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._DatabaseCreateController
 * @see Flamingo2.view.hawq.browser._DatabaseCreateModel
 */
Ext.define('Flamingo2.view.hawq.browser._DatabaseCreate', {
    extend: 'Ext.window.Window',
    alias: 'widget._hawqDatabaseCreateWindow',

    controller: '_hawqDatabaseCreateController',
    viewModel: {
        type: '_hawqDatabaseCreateModel'
    },

    height: 260,
    width: 300,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('hawq.title.create.database'),
    titleCollapse: false,
    modal: true,
    closeAction: 'destroy',
    layout: 'fit',
    autoScroll: true,
    border: false,
    bodyPadding: 10,
    bodyStyle: {
        background: '#fff'
    },

    listeners: {
        afterrender: 'databaseCreateWindowAfterrender'
    },

    items: [
        {
            xtype: 'form',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            border: false,
            defaults: {
                labelWidth: 120
            },
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.label.database.name'),
                    labelAlign: 'right',
                    name: 'newDatabaseName',
                    allowBlank: false
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.label.database.owner'),
                    labelAlign: 'right',
                    reference: 'ownerCombobox',
                    name: 'owner',
                    bind: {
                        store: '{hawqUser}'
                    },
                    displayField: '_username',
                    valueField: '_username'
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.label.database.template'),
                    labelAlign: 'right',
                    name: 'template'
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.label.encoding'),
                    labelAlign: 'right',
                    name: 'encoding',
                    bind: {
                        store: '{hawqEncoding}'
                    },
                    displayField: 'displ',
                    valueField: 'value',
                    value: 'UTF8'
                },
                {
                    xtype: 'numberfield',
                    fieldLabel: message.msg('hawq.label.connlimit'),
                    labelAlign: 'right',
                    name: 'connlimit',
                    value: -1,
                    minValue: -1
                }
            ]
        }
    ],

    buttons: [
        {
            text: message.msg('hawq.button.create'),
            iconCls: 'common-complete',
            handler: 'databaseCreateButtonHandler'
        },
        {
            text: message.msg('hawq.button.cancel'),
            iconCls: 'common-cancel',
            handler: 'cancelButtonHandler'
        }
    ]
});