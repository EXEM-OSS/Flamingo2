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
Ext.define('Flamingo2.view.hive.metastore._CreateDatabase', {
    extend: 'Ext.window.Window',
    alias: 'widget.hiveMetaCreateDatabase',

    requires: [
        'Flamingo2.view.hive.metastore._CreateDatabaseController'
    ],

    controller: 'createDatabaseController',

    viewModel: {
        type: 'hiveMetastoreModel'
    },

    width: 450,
    height: 350,
    resizable: false,
    modal: true,
    layout: 'fit',
    title: message.msg('hive.database.create'),
    buttonAlign: 'right',
    buttons: [
        {
            text: message.msg('common.create'),
            handler: 'onOKClick'
        },
        {
            text: message.msg('common.cancel'),
            handler: 'onCancelClick'
        }
    ],
    items: [
        {
            xtype: 'form',
            layout: 'anchor',
            reference: 'frmCreateDatabase',
            bodyPadding: 5,
            defaults: {
                labelWidth: 110,
                anchor: '100%',
                labelAlign: 'right'
            },
            items: [
                {
                    xtype: 'textfield',
                    itemId: 'dbTextField',
                    name: 'database',
                    fieldLabel: message.msg('hive.database'),
                    allowBlank: false
                },
                {
                    xtype: 'textfield',
                    itemId: 'commentTextField',
                    name: 'comment',
                    fieldLabel: message.msg('common.desc')
                },
                {
                    xtype: 'checkbox',
                    fieldLabel: message.msg('hive.external'),
                    reference: 'chkExternal',
                    name: 'external',
                    listeners: {
                        change: 'onCheckboxChange'
                    }
                },
                {
                    xtype: 'container',
                    itemId: 'fieldLocation',
                    reference: 'fcLocation',
                    layout: 'hbox',
                    disabled: true,
                    margin: '0 0 5 0',
                    style: {
                        backgroundColor: '#FFFFFF'
                    },
                    items: [
                        {
                            xtype: 'textfield',
                            reference: 'txLocation',
                            name: 'location',
                            fieldLabel: message.msg('hive.hdfsLocation'),
                            labelWidth: 110,
                            labelAlign: 'right',
                            flex: 1,
                            margin: '0 3 0 0'
                        },
                        {
                            xtype: 'button',
                            reference: 'browseButton',
                            itemId: 'browseButton',
                            text: message.msg('common.browse'),
                            width: 60,
                            handler: 'onBrowseClick'
                        }
                    ]
                },
                {
                    xtype: 'grid',
                    reference: 'grdProperty',
                    columnLines: true,
                    title: message.msg('hive.database.property'),
                    height: 165,
                    bind: {
                        store: '{dbproperties}'
                    },
                    tools: [{
                        type: 'plus',
                        handler: 'onPlusClick'
                    }, {
                        type: 'minus',
                        handler: 'onMinusClick'
                    }],
                    columns: [{
                        text: message.msg('common.key'),
                        dataIndex: 'key',
                        editor: 'textfield',
                        flex: 1
                    }, {
                        text: message.msg('common.value'),
                        dataIndex: 'value',
                        editor: 'textfield',
                        flex: 1
                    }],
                    plugins: {
                        ptype: 'cellediting',
                        clicksToEdit: 1
                    }
                }
            ]
        }
    ],
    listeners: {
        hdfsclose: 'onHdfsclose'
    }
});

