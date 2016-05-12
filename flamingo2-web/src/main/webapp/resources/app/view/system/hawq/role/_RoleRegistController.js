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
/**
 * ViewController for Flamingo2.view.system.hawq.role._RoleRegist
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.role._RoleRegist
 */
Ext.define('Flamingo2.view.system.hawq.role._RoleRegistController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.roleregist',

    onBtnSaveClick: function (owner, tool, event) {
        var me = this,
            refs = me.getReferences(),
            formPanel = refs.hawqRoleCreateForm,
            form = formPanel.getForm();

        if (form.isValid()) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.create.role'),
                message: message.msg('hawq.msg.question.createrole'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        formPanel.setLoading(true);

                        var fieldValues = form.getFieldValues();

                        fieldValues.clusterName = ENGINE.id;
                        fieldValues.denyFromTime = form.findField('denyFromTime').time;
                        fieldValues.denyToTime = form.findField('denyToTime').time;
                        if (fieldValues.validUntil) {
                            fieldValues.validUntil = dateFormat(fieldValues.validUntil.getTime(), 'yyyy-MM-dd');
                        }

                        invokePostByMap(
                            CONSTANTS.HAWQ.AUTH.CREATE_ROLE,
                            fieldValues,
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.createrole'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            formPanel.setLoading(false);
                                            me.fireEvent('refreshRole');
                                            me.getView().close();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.createrole') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING,
                                        fn: function () {
                                            formPanel.setLoading(false);
                                        }
                                    });
                                }
                            },
                            function (response) {
                                Ext.MessageBox.show({
                                    title: message.msg('hawq.title.fail'),
                                    message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.ERROR,
                                    fn: function () {
                                        formPanel.setLoading(false);
                                    }
                                });
                            }
                        );
                    }
                }
            });
        }
    },

    onBtnCancelClick: function () {
        this.getView().close();
    }
});