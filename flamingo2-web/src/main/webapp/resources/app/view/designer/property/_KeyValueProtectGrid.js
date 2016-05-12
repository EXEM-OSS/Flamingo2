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
Ext.define('Flamingo2.view.designer.property._KeyValueProtectGrid', {
    extend: 'Flamingo2.view.designer.property._Grid',
    alias: 'widget._keyValueProtectGrid',

    store: Ext.create('Ext.data.Store', {
        fields: [
            {name: 'keys'},
            {name: 'values'},
            {name: 'protected'}
        ],
        data: []
    }),
    selType: 'rowmodel',
    forceFit: true,
    columnLines: true,
    columns: [
        {
            text: message.msg('workflow.common.key'),
            dataIndex: 'keys',
            flex: 1,
            editor: {
                vtype: 'alphanum',
                allowBlank: true,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        },
        {
            text: message.msg('workflow.common.value'),
            dataIndex: 'values',
            flex: 1,
            editor: {
                allowBlank: true,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 30);
                    }
                }
            }
        },
        {
            xtype: 'checkcolumn',
            text: message.msg('workflow.common.fix.value'),
            dataIndex: 'protected',
            width: 50,
            editor: {}
        }
    ]
});