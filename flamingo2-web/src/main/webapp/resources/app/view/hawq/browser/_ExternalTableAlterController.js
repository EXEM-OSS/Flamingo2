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
 * ViewController for Flamingo2.view.hawq.browser._ExternalTableAlter
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._ExternalTableAlter
 */
Ext.define('Flamingo2.view.hawq.browser._ExternalTableAlterController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller._hawqExternalTableAlterController',

    hawqUserStoreBeforeload: function (store, operation, eOpts) {
        store.getProxy().extraParams.clusterName = ENGINE.id;
    },

    hawqDatabaseStoreBeforeload: function (store, operation, eOpts) {
        store.getProxy().extraParams.clusterName = ENGINE.id;
    },

    hawqSchemaStoreBeforeload: function (store, operation, eOpts) {
        store.getProxy().extraParams.clusterName = ENGINE.id;
        store.getProxy().extraParams.databaseName = this.getReferences().databaseNameCombobox.getValue();
    },

    hawqColumnStoreBeforeload: function (store, operation, eOpts) {
        var refs = this.getReferences();
        store.getProxy().extraParams.clusterName = ENGINE.id;
        store.getProxy().extraParams.databaseName = refs.databaseNameCombobox.getValue();
        store.getProxy().extraParams.schemaName = refs.schemaNameCombobox.getValue();
        store.getProxy().extraParams.tableName = refs.tableNameTextfield.getValue();
    },

    hawqColumnStoreLoad: function (store, records, successful, eOpts) {
        // set distributed multi select combobox
        var refs = this.getReferences();
        if (successful) {
            var distributedCombobox = refs.distributedCombobox,
                distributedValues = [];

            distributedCombobox.getStore().loadData(records);

            Ext.each(records, function (record, index, allRecords) {
                if (record.get('distributed') === true) {
                    distributedValues.push(record.get('column_name'));
                }
            });

            distributedCombobox.setValue(distributedValues);
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.fail'),
                message: message.msg('hawq.msg.fail.getcolumn'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    _hawqTableAlterWindowAfterrender: function (window) {
        var me = this,
            refs = me.getReferences(),
            table = window.table,
            formPanel = window.down('form'),
            optionFormPanel = refs.hawqOptionForm,
            form = formPanel.getForm(),
            vm = me.getViewModel();

        formPanel.setLoading(true);
        optionFormPanel.setLoading(true);

        invokeGet(
            CONSTANTS.HAWQ.BROWSER.TABLE_DETAIL,
            {
                clusterName: ENGINE.id,
                databaseName: table.get('table_catalog'),
                schemaName: table.get('table_schema'),
                tableName: table.get('table_name')
            },
            function (response) {
                var result = Ext.decode(response.responseText);
                if (result.success) {
                    var original = result.object;

                    vm.setData({original: Ext.decode(response.responseText).object});

                    // Setting General
                    form.findField('tableName').setValue(original.tableName);

                    form.findField('tableComment').setValue(original.table_comment);

                    vm.getStore('hawqUser').reload();
                    refs.hawqOwnerCombobox.setValue(original.owner);

                    vm.getStore('hawqDatabase').reload();
                    form.findField('databaseName').setValue(original.databaseName);

                    vm.getStore('hawqSchema').reload();
                    form.findField('schemaName').setValue(original.schemaName);

                    vm.getStore('hawqColumn').reload();

                    formPanel.setLoading(false);

                    optionFormPanel.getForm().setValues(original);
                    optionFormPanel.setLoading(false);
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('hawq.title.fail'),
                        message: message.msg('hawq.msg.fail.gettable') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
    },

    tableAlterButtonHandler: function (button) {
        var me = this,
            vm = me.getViewModel(),
            refs = me.getReferences(),
            tableAlterWindow = button.up('window'),
            original = vm.getData().original,
            formPanel = tableAlterWindow.down('form'),
            form = formPanel.getForm(),
            fieldValues = form.getFieldValues(),
            hawqTableAlterTabpanel = refs.hawqTableAlterTabpanel;

        if (form.isValid()) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.alter'),
                message: message.msg('hawq.msg.question.altertable'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        formPanel.setLoading(true);

                        fieldValues = Ext.merge(fieldValues, {
                            clusterName: ENGINE.id,
                            original: original
                        });

                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.ALTER_TABLE,
                            fieldValues,
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.altertable'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            formPanel.setLoading(false);
                                            tableAlterWindow.table = new Ext.data.Model({
                                                table_catalog: fieldValues.databaseName,
                                                table_schema: fieldValues.schemaName,
                                                table_name: fieldValues.tableName
                                            });
                                            tableAlterWindow.fireEvent('afterrender', tableAlterWindow);// refresh
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.altertable') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
        } else if (!trim(fieldValues.tableName)) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidtablename'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    hawqTableAlterTabpanel.setActiveTab(0);
                    form.findField('tableName').focus(false, 20);
                }
            });
        }
    },

    cancelButtonHandler: function (button) {
        button.up('window').close();
    },

    hawqColumnsGridAddButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqColumnsGrid,
            cellediting = grid.getPlugin('alterTableColumnsGridCellediting'),
            count = grid.getStore().getCount();

        cellediting.cancelEdit();
        grid.getStore().insert(count, {});
        cellediting.startEdit(count, 0);
    },

    hawqColumnsGridDeleteButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqColumnsGrid,
            store = grid.getStore(),
            cellediting = grid.getPlugin('alterTableColumnsGridCellediting'),
            selection = grid.getSelectionModel().getSelection();

        cellediting.cancelEdit();

        if (selection.length) {
            store.remove(selection);
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.dropcolumn'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqColumnsGridSaveButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqColumnsGrid,
            store = grid.getStore();

        if (store.isValid()) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.alter'),
                message: message.msg('hawq.msg.question.altercolumn'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        var newRecordsModels = store.getNewRecords(),
                            newRecords = [],
                            removedRecordsModels = store.getRemovedRecords(),
                            removedRecords = [],
                            updatedRecordsModels = store.getUpdatedRecords(),
                            updatedRecords = [];

                        for (index in newRecordsModels) {
                            newRecords[index] = newRecordsModels[index].getData();
                        }

                        for (index in removedRecordsModels) {
                            removedRecords[index] = removedRecordsModels[index].getData();
                        }

                        for (index in updatedRecordsModels) {
                            var updatedRecord = updatedRecordsModels[index];
                            updatedRecords[index] = {
                                newColumn: updatedRecord.getData(),
                                oriColumn: updatedRecord.modified
                            };
                        }

                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.ALTER_COLUMN,
                            {
                                clusterName: ENGINE.id,
                                databaseName: refs.databaseNameCombobox.getValue(),
                                schemaName: refs.schemaNameCombobox.getValue(),
                                tableName: refs.tableNameTextfield.getValue(),
                                newRecords: newRecords,
                                removedRecords: removedRecords,
                                updatedRecords: updatedRecords
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.altercolumn'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            grid.getStore().reload();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.altercolumn') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });
                                }
                            },
                            function (response) {
                                Ext.MessageBox.show({
                                    title: message.msg('hawq.title.fail'),
                                    message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.ERROR
                                });
                            }
                        );
                    }
                }
            });
        } else if (store.getCount() < 1) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidcolumncount'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    refs.hawqTableAlterTabpanel.setActiveTab(0);
                }
            });
        }
    },

    hawqColumnsGridRefreshButtonHandler: function (button) {
        this.getReferences().hawqColumnsGrid.getStore().reload();
    }
});