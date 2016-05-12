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
Ext.define('Flamingo2.view.designer.variableGrid.VariableGridController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.variableGridController',

    rowplus: function (event, toolEl, panel) {
        var grid = panel.up('variableGrid'),
            store = grid.getStore(),
            rowEditor = grid.getPlugin('rowEditorPlugin');
        rowEditor.cancelEdit();
        store.add({name: '', value: ''});
        rowEditor.startEdit(store.getCount() - 1, 0);
    },

    rowminus: function (event, toolEl, panel) {
        var grid = panel.up('variableGrid'),
            store = grid.getStore(),
            selectionModel = grid.getSelectionModel();
        store.remove(selectionModel.getSelection());
    },


    rowclose: function (event, toolEl, panel) {
        var grid = panel.up('variableGrid'),
            store = grid.getStore();
        store.removeAll();
    }
});