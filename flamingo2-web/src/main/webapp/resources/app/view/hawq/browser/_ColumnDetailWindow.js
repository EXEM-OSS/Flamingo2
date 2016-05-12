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
 * HAWQ > Browser > Table Informations > Column > grid item double click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser.HawqBrowser > hawqColumnsGrid
 */
Ext.define('Flamingo2.view.hawq.browser._ColumnDetailWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget._hawqColumnDetailWindow',

    requires: [
        'Flamingo2.view.hawq.browser.HawqBrowserController'
    ],

    controller: 'hawqBrowserController',

    height: 390,
    width: 300,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('hawq.title.detail.column'),
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
        afterrender: 'hawqColumnDetailWindowAfterrender'
    },

    items: [
        {
            xtype: 'form',
            layout: 'vbox',
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.column.database'),
                    labelAlign: 'right',
                    name: 'table_catalog',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.column.schema'),
                    labelAlign: 'right',
                    name: 'table_schema',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.column.tableview'),
                    labelAlign: 'right',
                    name: 'table_name',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.column.name'),
                    labelAlign: 'right',
                    name: 'column_name',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.column.ordinalposition'),
                    labelAlign: 'right',
                    name: 'ordinal_position',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.column.default'),
                    labelAlign: 'right',
                    name: 'column_default',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.column.null'),
                    labelAlign: 'right',
                    name: 'is_nullable',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.datatype'),
                    labelAlign: 'right',
                    name: 'data_type',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.column.length'),
                    labelAlign: 'right',
                    name: 'character_maximum_length',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('hawq.column.comment'),
                    labelAlign: 'right',
                    name: 'column_comment',
                    readOnly: true
                }
            ]
        }
    ],

    buttons: [
        {
            text: message.msg('hawq.button.cancel'),
            iconCls: 'common-cancel',
            handler: 'cancelButtonHandler'
        }
    ]
});