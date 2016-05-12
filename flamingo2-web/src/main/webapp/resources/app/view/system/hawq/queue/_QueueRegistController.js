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
 * ViewController for Flamingo2.view.system.hawq.queue._QueueRegist
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.queue._QueueRegist
 */
Ext.define('Flamingo2.view.system.hawq.queue._QueueRegistController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.queueRegist',

    onBtnCancelClick: function () {
        this.getView().close();
    },

    onBtnSaveClick: function (owner, tool, event) {
        var me = this,
            refs = me.getReferences(),
            formPanel = refs.hawqRQueueCreateForm,
            form = formPanel.getForm(),
            fieldValues = form.getFieldValues();

        if (form.isValid() &&
            (fieldValues.activeStatements || fieldValues.maxCost)) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.create.rqueue'),
                message: message.msg('hawq.msg.question.createrqueue'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        formPanel.setLoading(true);

                        fieldValues.clusterName = ENGINE.id;

                        invokePostByMap(
                            CONSTANTS.HAWQ.AUTH.CREATE_RESOURCE_QUEUE,
                            fieldValues,
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.createrqueue'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            formPanel.setLoading(false);
                                            me.fireEvent('queueRefresh');
                                            me.getView().close();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.createrqueue') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
    }
});