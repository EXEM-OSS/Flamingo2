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
 * ViewController for Flamingo2.view.hawq.browser._SchemaCreate
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._SchemaCreate
 */
Ext.define('Flamingo2.view.hawq.browser._SchemaCreateController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller._hawqSchemaCreateController',

    schemaCreateWindowAfterrender: function (window) {
        var me = this;
        setTimeout(function () {
            var userStore = me.getReferences().usernameCombobox.getStore();
            userStore.getProxy().extraParams.clusterName = ENGINE.id;
            userStore.load();
        }, 300);
    },

    schemaCreateButtonHandler: function (button) {
        var formPanel = button.up('window').down('form'),
            form = formPanel.getForm();

        if (form.isValid()) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.create.schema'),
                message: message.msg('hawq.msg.question.createschema'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        formPanel.setLoading(true);
                        var fieldValues = form.getFieldValues(),
                            databaseName = query('hawqBrowser #hawqDatabaseCombobox').getValue();
                        fieldValues.clusterName = ENGINE.id;
                        fieldValues.databaseName = databaseName;
                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.CREATE_SCHEMA,
                            fieldValues,
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.createschema'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            formPanel.setLoading(false);
                                            button.up('window').close();
                                            var schemaStore = query('hawqBrowser #hawqSchemaCombobox').getStore();
                                            schemaStore.getProxy().extraParams = {
                                                clusterName: ENGINE.id,
                                                databaseName: databaseName
                                            };
                                            schemaStore.load();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.createschema') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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

    cancelButtonHandler: function (button) {
        button.up('window').close();
    }
});