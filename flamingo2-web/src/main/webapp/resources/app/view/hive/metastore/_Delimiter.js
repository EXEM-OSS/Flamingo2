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
Ext.define('Flamingo2.view.hive.metastore._Delimiter', {
    extend: 'Ext.window.Window',
    requires: [
        'Flamingo2.view.hive.metastore._DelimiterController',
        'Flamingo2.view.hive.metastore.MetastoreModel'
    ],

    controller: 'metastoreDelimiterController',
    viewModel: {
        type: 'hiveMetastoreModel'
    },

    title: message.msg('hive.msg.select_delimiter'),
    modal: true,
    resizable: false,
    width: 570,
    height: 600,
    layout: 'fit',
    scrollable: 'vertical',
    bodyStyle: {
        backgroundColor: '#FFFFFF'
    },

    bbar: ['->', {
        xtype: 'button',
        text: message.msg('common.confirm'),
        handler: 'onOkClick'
    }, {
        xtype: 'button',
        text: message.msg('common.cancel'),
        handler: 'onCancelClick'
    }],

    items: [{
        xtype: 'grid',
        reference: 'grdDelimiter',
        columnLines: true,
        bind: {
            store: '{delimiter}'
        },
        columns: [{
            text: message.msg('hive.code_name'),
            dataIndex: 'description',
            style: 'text-align:center',
            flex: 1
        }, {
            text: message.msg('hive.octal_code'),
            dataIndex: 'octal',
            align: 'center'
        }, {
            text: message.msg('hive.symbol'),
            dataIndex: 'symbol',
            align: 'center'
        }],
        listeners: {
            itemdblclick: 'onItemdblclick'
        }
    }]
});