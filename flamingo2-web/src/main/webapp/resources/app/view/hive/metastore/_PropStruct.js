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
Ext.define('Flamingo2.view.hive.metastore._PropStruct', {
    extend: 'Ext.window.Window',
    requires: [
        'Flamingo2.view.hive.metastore._PropController'
    ],
    controller: 'metastorePorpController',
    viewModel: {
        type: 'hiveMetastoreModel'
    },
    modal: true,
    height: 300,
    width: 300,
    title: message.msg('hive.msg.struct_type_define'),
    resizable: false,
    layout: 'fit',
    closeAction: 'hidden',
    items: [
        {
            xtype: 'grid',
            reference: 'structGrid',
            bind: {
                store: '{structType}'
            },
            columnLines: true,
            columns: [
                {
                    text: message.msg('hive.struct_name'),
                    dataIndex: 'name',
                    flex: 1,
                    editor: {xtype: 'textfield', allowBlank: false}
                },
                {
                    text: message.msg('fs.hdfs.type'),
                    dataIndex: 'type',
                    flex: 1,
                    editor: Ext.create('Ext.form.field.ComboBox', {
                        bind: {
                            store: '{dataTypeWithoutComplex}'
                        },
                        queryMode: 'local',
                        editable: false,
                        valueField: 'typeString',
                        displayField: 'typeString'
                    })
                }
            ],
            plugins: {
                ptype: 'cellediting',
                clicksToEdit: 1
            }
        }
    ],
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: ['->', {
                xtype: 'button',
                text: message.msg('common.add'),
                iconCls: 'common-add',
                handler: 'onPropStructAddClick'
            }, {
                xtype: 'button',
                text: message.msg('common.delete'),
                iconCls: 'common-delete',
                handler: 'onPropStructDeleteClick'
            }]
        },
        {
            xtype: 'toolbar',
            dock: 'bottom',
            items: ['->',
                {
                    xtype: 'button',
                    text: message.msg('common.confirm'),

                    handler: 'onPropStructBtnOkClick'
                }, {
                    xtype: 'button',
                    text: message.msg('common.cancel'),

                    handler: 'onPropStructBtnCancelClick'
                }]
        }
    ],
    listeners: {
        show: 'onPorpStructShow'
    }
});