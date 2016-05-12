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
 * ViewController for Flamingo2.view.hawq.browser._ExternalTableCreate
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._ExternalTableCreate
 */
Ext.define('Flamingo2.view.hawq.browser._ExternalTableCreateController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller._hawqExternalTableCreateController',

    extTableCreateWindowAfterrender: function (window) {
        var me = this;
        setTimeout(function () {
            var refs = me.getReferences(),
                databaseCombobox = refs.databaseNameCombobox,
                databaseStore = databaseCombobox.getStore(),
                schemaCombobox = refs.schemaNameCombobox,
                schemaStore = schemaCombobox.getStore(),
                data = me.getViewModel().getData(),
                databaseName = data.databaseName,
                schemaName = data.schemaName;

            databaseStore.getProxy().extraParams.clusterName = ENGINE.id;
            databaseStore.load();
            databaseCombobox.setValue(databaseName);

            schemaStore.getProxy().extraParams.clusterName = ENGINE.id;
            schemaStore.getProxy().extraParams.clusterName.databaseName = databaseName;
            schemaStore.load();
            schemaCombobox.setValue(schemaName);
        }, 20);
    },

    hawqExternalTableWritableCheckboxChange: function (checkbox, newValue, oldValue, eOpts) {
        var me = this,
            refs = me.getReferences(),
            columns = refs.hawqColumnsGrid.columnManager.headerCt.items.items;
        columns[4].setVisible(newValue).setDisabled(!newValue);
    },

    hawqOtherTableKeyup: function (textfield, e, eOpts) {
        this.getReferences().hawqColumnsGrid.setDisabled(!isBlank(trim(textfield.getValue())));
    },

    hawqExternalTableLocationGridAddButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqExternalTableLocationGrid,
            cellediting = grid.getPlugin('createExternalTableLocationGridCellediting'),
            count = grid.getStore().getCount();

        cellediting.cancelEdit();
        grid.getStore().insert(count, {});
        cellediting.startEdit(count, 0);
    },

    hawqExternalTableLocationGridDeleteButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqExternalTableLocationGrid,
            store = grid.getStore(),
            cellediting = grid.getPlugin('createExternalTableLocationGridCellediting'),
            selection = grid.getSelectionModel().getSelection();

        cellediting.cancelEdit();

        if (selection.length) {
            store.remove(selection);
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.droplocation'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqColumnsGridAddButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqColumnsGrid,
            cellediting = grid.getPlugin('createTableColumnsGridCellediting'),
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
            cellediting = grid.getPlugin('createTableColumnsGridCellediting'),
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

    hawqExternalTableLocationGridCelleditingEdit: function (editor, context, eOpts) {
        if (context.colIdx === 0) {
            var record = context.grid.getStore().getAt(context.rowIdx),
                hint = record.get('hint'),
                hintValue = '';

            switch (hint) {
                case 'gpfdist':
                    hintValue = 'gpfdist://filehost:8081/*.out';
                    break;
                case 'gpfdists':
                    hintValue = 'gpfdists://filehost:8081/*.out';
                    break;
                case 'file':
                    hintValue = 'file://seghost1/dbfast1/external/myfile.txt';
                    break;
                case 'web':
                    hintValue = 'http://intranet.mycompany.com/finance/expenses.csv';
                    break;
                case 'pxf(hdfs)':
                    hintValue = 'pxf://pivhdsne:50070/categories_dim.tsv.gz?profile=HdfsTextSimple';
                    break;
                case 'pxf(hbase)':
                    hintValue = 'pxf://pivhdsne:50070/categories_dim?PROFILE=hbase';
                    break;
                default:
                    break;
            }

            record.set('location', hintValue);
        }
    },

    hawqExternalTableFormatRadiogroup: function (radiogroup, newValue, oldValue, eOpts) {
        var me = this,
            refs = me.getReferences(),
            formatTextPanel = refs.formatTextPanel,
            formatCsvPanel = refs.formatCsvPanel;

        switch (newValue.type) {
            case 'TEXT':
                formatTextPanel.show().setDisabled(false);
                formatCsvPanel.hide().setDisabled(true);
                break;
            case 'CSV':
                formatTextPanel.hide().setDisabled(true);
                formatCsvPanel.show().setDisabled(false);
                break;
            default:
                break;
        }
    },

    externalTableCreateButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            externalTableCreateWindow = button.up('window'),
            formPanel = externalTableCreateWindow.down('form'),
            form = formPanel.getForm(),
            fieldValues = form.getFieldValues(),
            hawqColumnsGridStore = refs.hawqColumnsGrid.getStore(),
            hawqExternalTableLocationGridStore = refs.hawqExternalTableLocationGrid.getStore();

        if (form.isValid() &&
            ( fieldValues.otherTable || (hawqColumnsGridStore.getCount() >= 1 && hawqColumnsGridStore.isValid()) ) &&
            hawqExternalTableLocationGridStore.getCount() >= 1 && hawqExternalTableLocationGridStore.isValid()) {

            Ext.MessageBox.show({
                title: message.msg('hawq.title.create.exttable'),
                message: message.msg('hawq.msg.question.createexttable'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        var columnItems = hawqColumnsGridStore.getData().items,
                            hawqColumns = [];
                        for (var i = 0, columnLength = columnItems.length; i < columnLength; i++) {
                            hawqColumns.push(columnItems[i].data);
                        }

                        var locationItems = hawqExternalTableLocationGridStore.getData().items,
                            locations = [];
                        for (var j = 0, locationLength = locationItems.length; j < locationLength; j++) {
                            locations.push(locationItems[j].data.location);
                        }

                        fieldValues = Ext.merge(
                            fieldValues,
                            {
                                columns: hawqColumns,
                                locations: locations,
                                format: refs.formFormat.getForm().getFieldValues(),
                                clusterName: ENGINE.id
                            }
                        );

                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.CREATE_EXTERNAL_TABLE,
                            fieldValues,
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.createexttable'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            externalTableCreateWindow.close();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.createexttable') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
        } else if (!fieldValues.tableName) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidtablename'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    refs.hawqExternalTableCreateTabpanel.setActiveTab(0);
                    form.findField('tableName').focus(false, 20);
                }
            });
        } else if (!hawqColumnsGridStore.isValid()) {
            refs.hawqExternalTableCreateTabpanel.setActiveTab(0);
        } else if (!fieldValues.otherTable && hawqColumnsGridStore.getCount() < 1) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidcolumn'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    refs.hawqExternalTableCreateTabpanel.setActiveTab(0);
                }
            });
        } else if (!hawqExternalTableLocationGridStore.isValid()) {
            refs.hawqExternalTableCreateTabpanel.setActiveTab(1);
        } else if (hawqExternalTableLocationGridStore.getCount() < 1) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidlocation'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    refs.hawqExternalTableCreateTabpanel.setActiveTab(1);
                }
            });
        } else if (!fieldValues.format) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidfiletype'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    refs.hawqExternalTableCreateTabpanel.setActiveTab(2);
                    form.findField('type').focus(false, 20);
                }
            });
        }
    },

    cancelButtonHandler: function (button) {
        button.up('window').close();
    }
});