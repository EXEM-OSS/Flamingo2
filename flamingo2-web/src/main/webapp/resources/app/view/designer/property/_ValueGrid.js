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
Ext.define('Flamingo2.view.designer.property._ValueGrid', {
    extend: 'Flamingo2.view.designer.property._Grid',
    alias: 'widget._valueGrid',

    store: Ext.create('Ext.data.Store', {
        fields: [
            {name: 'variableValues'},
            {name: 'variableDescriptions'}
        ],
        data: []
    }),
    selType: 'rowmodel',
    forceFit: true,
    columnLines: true,
    columns: [
        {
            text: message.msg('common.value'),
            dataIndex: 'variableValues',
            width: 200,
            editor: {
                vtype: 'exceptcomma',
                allowBlank: true,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 30);
                    }
                }
            }
        },
        {
            text: message.msg('common.comment'),
            dataIndex: 'variableDescriptions',
            width: 200,
            editor: {
                vtype: 'exceptcomma',
                allowBlank: true
            }
        }
    ]
});