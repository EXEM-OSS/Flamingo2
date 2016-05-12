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
 * ViewController for Flamingo2.view.hawq.browser._TableCreate
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._TableCreate
 */
Ext.define('Flamingo2.view.hawq.browser._TableCreateController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller._hawqTableCreateController',

    tableCreateWindowAfterrender: function (window) {
        var me = this,
            refs = me.getReferences(),
            vm = me.getViewModel();

        setTimeout(function () {
            refs.cbxUser.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            refs.cbxUser.getStore().load();

            refs.databaseNameCombobox.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            refs.databaseNameCombobox.getStore().load();
            refs.databaseNameCombobox.setValue(vm.getData().databaseName);

            refs.schemaNameCombobox.getStore().getProxy().extraParams = {
                clusterName: ENGINE.id,
                databaseName: vm.getData().databaseName
            };
            refs.schemaNameCombobox.getStore().load();
            refs.schemaNameCombobox.setValue(vm.getData().schemaName);
        }, 300);
    },

    tableCreateButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            tableCreateWindow = button.up('window'),
            formPanel = tableCreateWindow.down('form'),
            form = formPanel.getForm(),
            fieldValues = form.getFieldValues(),
            hawqColumnsGridStore = refs.hawqColumnsGrid.getStore(),
            hawqPartitionRangeGridStore = refs.hawqPartitionRangeGrid.getStore(),
            hawqPartitionListGridStore = refs.hawqPartitionListGrid.getStore(),
            hawqTableCreateTabpanel = refs.hawqTableCreateTabpanel;

        if (form.isValid() &&
            fieldValues.otherTable || (hawqColumnsGridStore.getCount() >= 1 && hawqColumnsGridStore.isValid()) &&
            hawqPartitionRangeGridStore.isValid() &&
            hawqPartitionListGridStore.isValid()) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.create.table'),
                message: message.msg('hawq.msg.question.createtable'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        formPanel.setLoading(true);

                        var storeDataItems = hawqColumnsGridStore.getData().items,
                            columns = [],
                            partitionFieldValues = refs.hawqPartitionForm.getForm().getFieldValues(),
                            partitionRangeGridItems = refs.hawqPartitionRangeGrid.getStore().getData().items,
                            ranges = [],
                            partitionListGridItems = refs.hawqPartitionListGrid.getStore().getData().items,
                            lists = [];

                        for (var i = 0, columnLength = storeDataItems.length; i < columnLength; i++) {
                            columns.push(storeDataItems[i].data);
                        }

                        for (var j = 0, rangeLength = partitionRangeGridItems.length; j < rangeLength; j++) {
                            ranges.push(partitionRangeGridItems[j].data);
                        }

                        for (var k = 0, listLength = partitionListGridItems.length; k < listLength; k++) {
                            lists.push(partitionListGridItems[k].data);
                        }

                        partitionFieldValues = Ext.merge(partitionFieldValues, {
                            ranges: ranges,
                            lists: lists
                        });

                        fieldValues = Ext.merge(fieldValues, {
                            columns: columns,
                            with: refs.frmWith.getForm().getFieldValues(),
                            clusterName: ENGINE.id,
                            partition: partitionFieldValues
                        });

                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.CREATE_TABLE,
                            fieldValues,
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.createtable'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            formPanel.setLoading(false);
                                            tableCreateWindow.close();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.createtable') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
        } else if (!fieldValues.tableName) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidtablename'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    hawqTableCreateTabpanel.setActiveTab(0);
                    form.findField('tableName').focus(false, 20);
                }
            });
        } else if (!hawqColumnsGridStore.isValid()) {
            hawqTableCreateTabpanel.setActiveTab(0);
        } else if (!fieldValues.otherTable && hawqColumnsGridStore.getCount() < 1) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidcolumn'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    hawqTableCreateTabpanel.setActiveTab(0);
                }
            });
        } else if (hawqColumnsGridStore.getCount() < 1) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidcolumncount'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    hawqTableCreateTabpanel.setActiveTab(0);
                }
            });
        } else if (!hawqPartitionRangeGridStore.isValid() || !hawqPartitionListGridStore.isValid()) {
            hawqTableCreateTabpanel.setActiveTab(2);
        }
    },

    cancelButtonHandler: function (button) {
        button.up('window').close();
    },

    databaseNameComboboxSelect: function (combobox, record, eOpts) {
        var me = this,
            refs = me.getReferences(),
            schemaNameCombobox = refs.schemaNameCombobox;

        schemaNameCombobox.clearValue();
        schemaNameCombobox.getStore().reload({
            params: {
                clusterName: ENGINE.id,
                databaseName: combobox.getValue()
            }
        });
    },

    hawqOtherTableKeyup: function (textfield, e, eOpts) {
        this.getReferences().hawqColumnsGrid.setDisabled(!isBlank(trim(textfield.getValue())));
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

    blockSizeNumberfieldBlur: function (numberfield, e, eOpts) {
        if (numberfield.getValue() && numberfield.isValid()) {
            numberfield.setValue(Math.round(numberfield.getValue() / 8192) * 8192);
        }
    },

    hawqTablePartitionRadiogroupChange: function (radiogroup, newValue, oldValue, eOpts) {
        var me = this,
            refs = me.getReferences(),
            partitionRangeGrid = refs.hawqPartitionRangeGrid,
            partitionListGrid = refs.hawqPartitionListGrid;

        switch (newValue.type) {
            case 'RANGE':
                partitionRangeGrid.show();
                partitionListGrid.hide();
                break;
            case 'LIST':
                partitionRangeGrid.hide();
                partitionListGrid.show();
                break;
            default:
                break;
        }
    },

    hawqPartitionRangeGridAddButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqPartitionRangeGrid,
            cellediting = grid.getPlugin('createTablePartitionRangeGridCellediting'),
            count = grid.getStore().getCount(),
            partitionColumnCombobox = refs.partitionColumnNameCombobox,
            partitionColumnRecord = partitionColumnCombobox.getStore().findRecord('columnName', partitionColumnCombobox.getValue());

        if (partitionColumnRecord) {
            var dataType = partitionColumnRecord.get('dataType');
            cellediting.cancelEdit();
            grid.getStore().insert(count, {
                startType: dataType,
                endType: dataType
            });
            cellediting.startEdit(count, 0);
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidpartitioncolumn'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqPartitionRangeGridDeleteButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqPartitionRangeGrid,
            store = grid.getStore(),
            cellediting = grid.getPlugin('createTablePartitionRangeGridCellediting'),
            selection = grid.getSelectionModel().getSelection();

        cellediting.cancelEdit();

        if (selection.length) {
            store.remove(selection);
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.droppartition'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqTableCreateWithFormAfterrender: function (formPanel) {
        var partitionWith = formPanel.partitionWith;
        if (partitionWith) {
            formPanel.getForm().setValues(partitionWith);
        }
    },

    hawqPartitionWithActioncolumnHandler: function (grid, rowIndex, colIndex, item, e, record) {
        var withWindow = Ext.create('Ext.window.Window', {
            modal: true,
            title: message.msg('hawq.title.partition') + ' ' + message.msg('hawq.title.option'),
            closeAction: 'destroy',
            layout: 'fit',
            bodyPadding: 5,
            bodyStyle: {
                background: '#fff'
            },
            items: [
                {
                    xtype: '_hawqTableCreateWithForm',
                    partitionWith: record.getData().partitionWith
                }
            ],
            buttons: [
                {
                    text: message.msg('hawq.button.add'),
                    iconCls: 'common-add',
                    handler: function (button) {
                        record.set('partitionWith', button.up('window').down('form').getForm().getFieldValues());
                        withWindow.close();
                    }
                },
                {
                    text: message.msg('hawq.button.cancel'),
                    iconCls: 'common-cancel',
                    handler: function () {
                        withWindow.close();
                    }
                }
            ]
        }).center().show();
    },

    hawqPartitionListGridAddButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqPartitionListGrid,
            cellediting = grid.getPlugin('createTablePartitionListGridCellediting'),
            count = grid.getStore().getCount();

        cellediting.cancelEdit();
        grid.getStore().insert(count, {});
        cellediting.startEdit(count, 0);
    },

    hawqPartitionListGridDeleteButtonHandler: function (button) {
        var me = this,
            refs = me.getReferences(),
            grid = refs.hawqPartitionListGrid,
            store = grid.getStore(),
            cellediting = grid.getPlugin('createTablePartitionListGridCellediting'),
            selection = grid.getSelectionModel().getSelection();

        cellediting.cancelEdit();

        if (selection.length) {
            store.remove(selection);
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.droppartition'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    }
});