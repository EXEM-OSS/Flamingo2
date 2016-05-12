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
Ext.define('Flamingo2.view.setting.PreferencesController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.preferencesController',

    onChangePasswordClick: function () {
        var me = this;
        var refs = me.getReferences();

        if (isBlank(trim(refs.password1.getValue())) || isBlank(trim(refs.password2.getValue()))) {
            Ext.MessageBox.show({
                title: message.msg('common.warn'),
                message: message.msg('setting.msg.enter_password'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        } else {
            if (trim(refs.password1.getValue()) != trim(refs.password2.getValue())) {
                Ext.MessageBox.show({
                    title: message.msg('common.warn'),
                    message: message.msg('setting.msg.not_match_password'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            } else {
                Ext.MessageBox.show({
                    title: message.msg('common.confirm'),
                    message: message.msg('setting.msg.change_password'),
                    buttons: Ext.MessageBox.YESNO,
                    icon: Ext.MessageBox.WARNING,
                    fn: function handler(btn) {
                        if (btn == 'yes') {
                            var params = {
                                clusterName: ENGINE.id,
                                password: escape(refs.password1.getValue())
                            };

                            invokePostByMap(CONSTANTS.PREFERENCES.CHANGE_PASSWORD, params,
                                function (response) {
                                    var obj = Ext.decode(response.responseText);
                                    if (obj.success) {
                                        me.getView().close();

                                        Ext.MessageBox.show({
                                            title: message.msg('common.confirm'),
                                            message: message.msg('setting.msg.password_chagned'),
                                            buttons: Ext.MessageBox.OK,
                                            icon: Ext.MessageBox.INFO
                                        });
                                    } else {
                                        Ext.MessageBox.show({
                                            title: message.msg('common.warn'),
                                            message: message.msg('setting.msg.can_not_change_password') + response.error.message,
                                            buttons: Ext.MessageBox.OK,
                                            icon: Ext.MessageBox.WARNING
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
                        }
                    }
                });
            }
        }
    },

    onClickPrefWindowClose: function () {
        this.getView().close();
    }
});