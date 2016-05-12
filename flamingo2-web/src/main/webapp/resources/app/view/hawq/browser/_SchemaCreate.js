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
 * HAWQ > Browser > Connection Manager > Schema Menu > create menu click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._SchemaCreateController
 * @see Flamingo2.view.hawq.browser._SchemaCreateModel
 */
Ext.define('Flamingo2.view.hawq.browser._SchemaCreate', {
    extend: 'Ext.window.Window',
    alias: 'widget._hawqSchemaCreateWindow',

    controller: '_hawqSchemaCreateController',
    viewModel: {
        type: '_hawqSchemaCreateModel'
    },

    height: 300,
    width: 500,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('hawq.title.create.schema'),
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
        afterrender: 'schemaCreateWindowAfterrender'
    },

    items: [
        {
            xtype: 'form',
            layout: 'anchor',
            border: false,
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.label.schema.name'),
                    labelAlign: 'right',
                    name: 'schemaName',
                    allowBlank: false
                },
                {
                    xtype: 'combobox',
                    fieldLabel: message.msg('hawq.label.schema.author'),
                    labelAlign: 'right',
                    reference: 'usernameCombobox',
                    name: 'username',
                    editable: false,
                    bind: {
                        store: '{hawqUser}'
                    },
                    displayField: '_username',
                    valueField: '_username'
                },
                {
                    xtype: 'textareafield',
                    fieldLabel: message.msg('hawq.label.schema.element'),
                    labelAlign: 'right',
                    name: 'element',
                    anchor: '100%',
                    grow: true,
                    growMin: 100,
                    growMax: 150,
                    emptyText: message.msg('hawq.button.schema.createdesc')
                }
            ]
        }
    ],

    buttons: [
        {
            text: message.msg('hawq.button.create'),
            iconCls: 'common-complete',
            handler: 'schemaCreateButtonHandler'
        },
        {
            text: message.msg('hawq.button.cancel'),
            iconCls: 'common-cancel',
            handler: 'cancelButtonHandler'
        }
    ]
});