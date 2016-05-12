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
Ext.define('Flamingo2.view.hive.metastore._PropMap', {
    extend: 'Ext.window.Window',
    requires: [
        'Flamingo2.view.hive.metastore._PropController'
    ],
    controller: 'metastorePorpController',
    viewModel: {
        type: 'hiveMetastoreModel'
    },
    modal: true,
    height: 125,
    bodyPadding: 5,
    title: message.msg('hive.msg.map_type_select'),
    resizable: false,
    layout: 'anchor',
    closeAction: 'hidden',
    items: [
        {
            xtype: 'combo',
            reference: 'comboKey',
            fieldLabel: message.msg('common.key'),
            labelWidth: 40,
            bind: {
                store: '{dataTypeWithoutComplex}'
            },
            editable: false,
            queryMode: 'local',
            valueField: 'typeString',
            displayField: 'typeString'
        },
        {
            xtype: 'combo',
            reference: 'comboValue',
            fieldLabel: message.msg('common.value'),
            labelWidth: 40,
            bind: {
                store: '{dataTypeWithoutComplex}'
            },
            editable: false,
            queryMode: 'local',
            valueField: 'typeString',
            displayField: 'typeString'
        }
    ],
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'bottom',
            items: ['->',
                {
                    xtype: 'button',
                    text: message.msg('common.confirm'),

                    handler: 'onPropMapBtnOkClick'
                }, {
                    xtype: 'button',
                    text: message.msg('common.cancel'),

                    handler: 'onPropMapBtnCancelClick'
                }]
        }
    ],
    listeners: {
        show: 'onPorpMapShow'
    }
});