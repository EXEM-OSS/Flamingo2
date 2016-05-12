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
Ext.define('Flamingo2.view.designer.variableGrid.VariableGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.variableGrid',

    requires: [
        'Flamingo2.view.designer.variableGrid.VariableGridController',
        'Flamingo2.view.designer.variableGrid.VariableGridModel',

        'Ext.grid.plugin.RowEditing'
    ],

    controller: 'variableGridController',

    viewModel: {
        type: 'variableGridModel'
    },

    bind: {
        store: '{variable}'
    },

    selType: 'rowmodel',
    forceFit: true,
    columnLines: true,
    columns: [
        {
            text: message.msg('common.key'),
            dataIndex: 'name',
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
            text: message.msg('common.value'),
            dataIndex: 'value',
            editor: {
                vtype: 'exceptcomma',
                allowBlank: true,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        }
    ],
    plugins: [
        Ext.create('Ext.grid.plugin.RowEditing', {
            clicksToEdit: 2,
            pluginId: 'rowEditorPlugin',
            listeners: {
                canceledit: function (editor, e, eOpts) {
                    // Cancel Edit 시 유효하지 않으면 추가된 레코드를 삭제한다.
                    if (e.store.getAt(e.rowIdx)) {
                        if (!e.store.getAt(e.rowIdx).isValid()) {
                            e.store.removeAt(e.rowIdx);
                        }
                    }
                },
                edit: function (editor, e, eOpts) {
                    if (!e.store.getAt(e.rowIdx).isValid()) {
                        editor.startEdit(e.rowIdx, 0);
                    }
                }
            }
        })
    ],
    tools: [
        {
            type: 'plus',
            tooltip: message.msg('common.add'),
            handler: 'rowplus'
        },
        {
            type: 'minus',
            tooltip: message.msg('common.remove'),
            handler: 'rowminus'
        },
        {
            type: 'close',
            tooltip: message.msg('common.remove_all'),
            handler: 'rowclose'
        }
    ]
});

