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
Ext.define('Flamingo2.view.hive.metastore._DelimiterController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.metastoreDelimiterController',

    onItemdblclick: function (view, record) {
        this.fireEvent('delimiterSelected', record, this.getView().target);
        this.getView().close();
    },

    onOkClick: function () {
        var me = this;
        var refs = me.getReferences();
        var selection = refs.grdDelimiter.getSelectionModel().getSelection();

        if (selection.length == 0) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('hive.msg.select_delimiter'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        me.fireEvent('delimiterSelected', selection[0], this.getView().target);
        me.getView().close();
    },

    onCancelClick: function () {
        this.getView().close();
    }
});