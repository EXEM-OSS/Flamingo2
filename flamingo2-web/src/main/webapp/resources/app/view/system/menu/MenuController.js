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
Ext.define('Flamingo2.view.system.menu.MenuController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.systemMenuViewController',

    onAfterrender: function () {
    },

    onBtnRefreshClick: function () {
        var menu = this.lookupReference('trpMenu');

        menu.getStore().load({
            callback: function () {
                menu.expandAll();
            }
        });
    },

    onBtnCollapseClick: function () {
        this.lookupReference('trpMenu').collapseAll();
    },

    onBtnExpandClick: function () {
        this.lookupReference('trpMenu').expandAll();
    },

    onBtnAddClick: function () {
        var me = this;
        var refs = this.getReferences();
        var selection = refs.trpMenu.getSelectionModel().getSelection();
        var id = selection[0].get('id');
        var depth = selection[0].get('depth');
        var cnt = refs.grdNode.getStore().getCount();

        if (depth == 2) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('menu.msg.can_make_2depth'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        var params = {
            prnts_menu_id: selection[0].get('id'),
            sort_ordr: cnt + 1,
            use_yn: 'Y'
        };

        refs.grdNode.getStore().insert(cnt, params);
    },

    onBtnSaveClick: function () {
        var me = this;
        var refs = this.getReferences();
        var store = refs.grdNode.getStore();

        if (!store.isValid()) {
            return;
        }

        var params = store.getAllJson();

        invokePostByMap(
            CONSTANTS.SYSTEM.MENU.SAVE,
            params,
            function (response) {

                var obj = Ext.decode(response.responseText);
                if (obj.success) {
                    Ext.MessageBox.show({
                        title: message.msg('common.success'),
                        message: message.msg('menu.msg.save_menu'),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.INFO
                    });

                    refs.grdNode.getStore().removeAll();
                    me.onBtnRefreshClick();
                }
                else {
                    Ext.MessageBox.show({
                        title: message.msg('common.error'),
                        message: message.msg('menu.msg.save_error') + '<br>' + obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.INFO,
                        fn: function (e) {
                            return false;
                        }
                    });
                }
            },
            function (response) {
                Ext.MessageBox.show({
                    title: message.msg('common.warn'),
                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    },

    onBtnRemoveClick: function () {
        var me = this;
        var refs = me.getReferences();
        var selection = refs.grdNode.getSelectionModel().getSelection();

        if (!selection.length) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('menu.msg.select_delete_row'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        refs.grdNode.getStore().remove(selection[0]);
    },

    onTrpMenuSelect: function (rowModel, record, index) {
        var me = this;
        var refs = me.getReferences();
        var menu_id = record.get('id');

        refs.grdNode.getStore().load({
            params: {
                prnts_menu_id: menu_id
            }
        });
    }
});