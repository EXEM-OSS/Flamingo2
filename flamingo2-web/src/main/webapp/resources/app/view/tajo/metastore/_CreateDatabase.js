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
Ext.define('Flamingo2.view.tajo.metastore._CreateDatabase', {
    extend: 'Ext.window.Window',
    alias: 'widget.tajoMetaCreateDatabase',

    requires: [
        'Flamingo2.view.tajo.metastore._CreateDatabaseController'
    ],

    controller: 'tajoCreateDatabaseController',

    viewModel: {
        type: 'tajoMetastoreModel'
    },

    width: 450,
    height: 100,
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
                    fieldLabel: message.msg('tajo.database'),
                    allowBlank: false
                }
            ]
        }
    ],
    listeners: {
        hdfsclose: 'onHdfsclose'
    }
});

