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
Ext.define('Flamingo2.view.hive.metastore._CreateTableController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.metastoreCreateTableController',

    requires: [
        'Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser',
        'Flamingo2.view.hive.metastore._PropController',
        'Flamingo2.view.hive.metastore._Delimiter'
    ],

    init: function () {
        this.control({
            'createTable #fieldTableType radio': {
                change: 'onRadioChange'
            },
            'createTable #fieldRowformat radio': {
                change: 'onRowformatRadioChange'
            }
        });
    },

    listen: {
        controller: {
            'simpleHdfsBrowserController': {
                hdfsclose: 'onHdfsClose'
            },
            'metastorePorpController': {
                propArrayBtnOkClick: 'onPropBtnOkClick',
                propMapBtnOkClick: 'onPropBtnOkClick',
                propStructBtnOkClick: 'onPropBtnOkClick'
            },
            'metastoreDelimiterController': {
                delimiterSelected: 'onDelimiterSelected'
            }
        }
    },

    /**
     * Create table popup window afterrender Event
     * */
    onAfterrender: function (window) {
        var me = this;
        var refs = me.getReferences();
        var viewModel = me.getViewModel();

        viewModel.setData({btnOKText: message.msg('common.create')});

        if (window.getAlter()) {
            me.getView().setLoading(true);
            var database = window.getDatabase();
            var table = window.getTable();
            var params = {
                clusterName: ENGINE.id,
                database: database,
                table: table
            };
            viewModel.getStore('columns').load({
                params: params
            });

            viewModel.getStore('partitions').load({
                params: params
            });

            invokeGet(CONSTANTS.HIVE.METASTORE.TABLE_INFO, params,
                function (response) {
                    var res = Ext.decode(response.responseText);
                    if (res.success) {

                        res.map.field = Ext.isEmpty(res.map['field.delim']) ? null : res.map['field.delim'];
                        res.map.lines = Ext.isEmpty(res.map['line.delim']) ? null : res.map['line.delim'];
                        res.map.mapkeys = Ext.isEmpty(res.map['mapkey.delim']) ? null : res.map['mapkey.delim'];
                        res.map.colelction = Ext.isEmpty(res.map['colelction.delim']) ? null : res.map['colelction.delim'];

                        refs.txField.delimiter = res.map.field;
                        refs.txCollection.delimiter = res.map.lines;
                        refs.txMap.delimiter = res.map.mapkeys;
                        refs.txLine.delimiter = res.map.colelction;

                        refs.tableForm.getForm().setValues(res.map);

                        if (res.map.tableType == 'EXTERNAL_TABLE') {
                            refs.locationTextField.setValue(res.map.location);
                        }

                        if (res.map.serializationLib != 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe') {
                            refs.rdoSerde.setValue('serde');
                            refs.txSerde.setValue(res.map.serializationLib);
                        }

                        var propKeys = Object.keys(res.map.tableParameter);
                        var keys = [], name;
                        for (name in propKeys) {
                            if (propKeys.hasOwnProperty(name)) {
                                refs.grdProperty.getStore().insert(0, {
                                    key: propKeys[name],
                                    value: res.map.tableParameter[propKeys[name]]
                                });
                            }
                        }

                        var tableForm = refs.tableForm;
                        var formData = tableForm.getForm().getValues();

                        viewModel.setData({orgTable: formData});
                        me.getView().setLoading(false);
                    } else {
                        // TODO 에러 처리
                        me.getView().setLoading(false);
                    }
                },
                function () {
                    me.getView().setLoading(false);
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            );
        }
    },

    /**
     * Browse button click event
     * */
    onBtnBrowseClick: function () {
        var popWindow = Ext.create('Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser');

        popWindow.center().show();
    },

    /**
     * Create button click event
     * */
    onBtnCreateClick: function () {
        var me = this;
        var refs = this.getReferences();
        var database = Ext.isEmpty(me.getView().database) ? refs.comboDB.getValue() : me.getView().database;

        if (Ext.isEmpty(database)) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('hive.msg.select_database'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        if (me.getView().getAlter()) {
            Ext.MessageBox.show({
                title: message.msg('hive.alter_table'),
                message: message.msg('hive.msg.alter_table'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn === 'yes') {
                        me.alterTable();
                    } else if (btn === 'no') {
                        return;
                    }
                }
            });
        }
        else {
            Ext.MessageBox.show({
                title: message.msg('hive.table.create'),
                message: message.msg('hive.msg.create_table'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn === 'yes') {
                        me.createTable();
                    } else if (btn === 'no') {
                        return;
                    }
                }
            });
        }
    },

    /**
     * 테이블 생성 함수
     * */
    createTable: function () {
        var me = this;
        var refs = this.getReferences();
        //입력값 유효성 검사.
        //var tableCreator = popWindow.down('tableCreator');
        var tableForm = refs.tableForm;
        var formData = tableForm.getForm().getValues();
        var tableName = formData.tableName;
        var tableType = formData.tableType;
        var comment = Ext.isEmpty(formData.comment) ? null : formData.comment;
        var location = formData.location == undefined ? null : formData.location;
        var columnGrid = refs.columnGrid;
        var columnGridStore = columnGrid.getStore();
        var columns = columnGridStore.getNewJson();
        var partitionGrid = refs.partitionGrid;
        var partitionGridStore = partitionGrid.getStore();
        var partitions = partitionGridStore.getNewJson();
        var inputFormat = Ext.isEmpty(formData.inputFormat) ? 'org.apache.hadoop.mapred.TextInputFormat' : formData.inputFormat;
        var outputFormat = Ext.isEmpty(formData.outputFormat) ? 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat' : formData.outputFormat;
        var database = Ext.isEmpty(me.getView().database) ? refs.comboDB.getValue() : me.getView().database;
        var records = [], isValid = true;

        if (Ext.isEmpty(database)) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('hive.msg.select_database'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        if (Ext.isEmpty(tableName)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.enter_table_name'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        if (Ext.isEmpty(tableType)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.select_table_type'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }
        else if (tableType == 'EXTERNAL_TABLE') {
            if (Ext.isEmpty(location)) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.select_table_location'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                return;
            }
        }

        if (columnGridStore.getRange().length == 0) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.define_column'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        var storeValid = true;

        Ext.each(columnGridStore.getRange(), function (item, idx, a) {
            if (Ext.isEmpty(item.get('name'))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.enter_column_name'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }
            else if (Ext.isEmpty(item.get('type'))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.select_column_type'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }

            if ((item.get('type') == 'struct' || item.get('type') == 'map' || item.get('array') == 'array') && (Ext.isEmpty(item.get('collection')))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.define_object_type'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }
        });

        if (!storeValid) return false;

        storeValid = true;

        Ext.each(partitionGridStore.getRange(), function (item, idx, a) {
            if (Ext.isEmpty(item.get('name'))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.enter_column_name'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }
            else if (Ext.isEmpty(item.get('type'))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.select_column_type'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }

            if ((item.get('type') == 'struct' || item.get('type') == 'map' || item.get('array') == 'array') && (Ext.isEmpty(item.get('collection')))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.define_object_type'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }
        });

        if (!storeValid) return false;

        var delimiter, serde;

        if (formData.rowformat == 'delimiter') {
            delimiter = Ext.encode({
                field: Ext.isEmpty(refs.txField.delimiter) ? '' : refs.txField.delimiter,
                collection: Ext.isEmpty(refs.txCollection.delimiter) ? '' : refs.txCollection.delimiter,
                mapkeys: Ext.isEmpty(refs.txMap.delimiter) ? '' : refs.txMap.delimiter,
                lines: Ext.isEmpty(refs.txLine.delimiter) ? '' : refs.txLine.delimiter
            });
            serde = null;
        }
        else {
            delimiter = Ext.encode({field: null, collection: null, mapkeys: null, lines: null});
            serde = formData.serde;
        }

        refs.grdProperty.getStore().each(function (record) {
            if (Ext.isEmpty(record.get('key'))) {
                isValid = false;
                return false;
            }

            if (Ext.isEmpty(record.get('value'))) {
                isValid = false;
                return false;
            }
            records.push(record);
        });

        if (!isValid) {
            Ext.Msg.alert(message.msg('common.check'), message.msg('hive.msg.enter_key_value'));
            return;
        }


        var params = {
            username: SESSION.USERNAME,
            clusterName: ENGINE.id,
            database: Ext.isEmpty(me.getView().database) ? refs.comboDB.getValue() : me.getView().database,
            table: tableName,
            tableType: tableType,
            comment: comment,
            location: location,
            columns: columns,
            partitions: partitions,
            delimiter: delimiter,
            serde: serde,
            inputFormat: inputFormat,
            outputFormat: outputFormat,
            properties: records2Json(records)
        };

        invokePostByMap(
            CONSTANTS.HIVE.METASTORE.CREATE_TABLE,
            params,
            function (response) {
                var obj = Ext.decode(response.responseText);
                if (obj.success) {
                    info(message.msg('common.success'), tableName + ' ' + message.msg('hive.msg.created'));
                    me.fireEvent('refreshTable');
                    me.getView().close();
                } else if (obj.error.cause) {
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('hive.msg.notice'),
                        message: message.msg('hive.msg.table_create_fail') + '<br>' + message.msg('common.cause') + ': ' + obj.error.message,
                        buttons: Ext.MessageBox.OK,
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

    alterTable: function () {
        var me = this;
        var refs = this.getReferences();
        //입력값 유효성 검사.
        //var tableCreator = popWindow.down('tableCreator');
        var tableForm = refs.tableForm;
        var formData = tableForm.getForm().getValues();
        var tableName = formData.tableName;
        var tableType = formData.tableType;
        var comment = Ext.isEmpty(formData.comment) ? null : formData.comment;
        var location = formData.location == undefined ? null : formData.location;
        var columnGrid = refs.columnGrid;
        var columnGridStore = columnGrid.getStore();
        var columns = columnGridStore.records2Json(columnGridStore.getData().items);
        var partitionGrid = refs.partitionGrid;
        var partitionGridStore = partitionGrid.getStore();
        var partitions = partitionGridStore.records2Json(partitionGridStore.getData().items);
        var inputFormat = Ext.isEmpty(formData.inputFormat) ? 'org.apache.hadoop.mapred.TextInputFormat' : formData.inputFormat;
        var outputFormat = Ext.isEmpty(formData.outputFormat) ? 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat' : formData.outputFormat;
        var records = [], isValid = true;

        if (Ext.isEmpty(tableName)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.enter_table_name'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        if (Ext.isEmpty(tableType)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.select_table_type'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }
        else if (tableType == 'EXTERNAL_TABLE') {
            if (Ext.isEmpty(location)) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.select_table_location'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                return;
            }
        }

        if (columnGridStore.getRange().length == 0) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.define_column'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        var storeValid = true;

        Ext.each(columnGridStore.getRange(), function (item, idx, a) {

            if (Ext.isEmpty(item.get('name'))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.enter_column_name'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }
            else if (Ext.isEmpty(item.get('type'))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.select_column_type'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }

            if ((item.get('type') == 'struct' || item.get('type') == 'map' || item.get('array') == 'array') && (Ext.isEmpty(item.get('collection')))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.define_object_type'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }
        });

        if (!storeValid) return false;

        storeValid = true;

        Ext.each(partitionGridStore.getRange(), function (item, idx, a) {

            if (Ext.isEmpty(item.get('name'))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.enter_column_name'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }
            else if (Ext.isEmpty(item.get('type'))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.select_column_type'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }

            if ((item.get('type') == 'struct' || item.get('type') == 'map' || item.get('array') == 'array') && (Ext.isEmpty(item.get('collection')))) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: message.msg('hive.msg.define_object_type'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                storeValid = false;
                return;
            }
        });

        if (!storeValid) return false;

        var delimiter, serde;

        if (formData.rowformat == 'delimiter') {
            delimiter = Ext.encode({
                field: Ext.isEmpty(refs.txField.delimiter) ? '' : refs.txField.delimiter,
                collection: Ext.isEmpty(refs.txCollection.delimiter) ? '' : refs.txCollection.delimiter,
                mapkeys: Ext.isEmpty(refs.txMap.delimiter) ? '' : refs.txMap.delimiter,
                lines: Ext.isEmpty(refs.txLine.delimiter) ? '' : refs.txLine.delimiter
            });
            serde = null;
        }
        else {
            delimiter = Ext.encode({field: null, collection: null, mapkeys: null, lines: null});
            serde = formData.serde;
        }

        refs.grdProperty.getStore().each(function (record) {
            if (Ext.isEmpty(record.get('key'))) {
                isValid = false;
                return false;
            }

            if (Ext.isEmpty(record.get('value'))) {
                isValid = false;
                return false;
            }
            records.push(record);
        });

        if (!isValid) {
            Ext.Msg.alert(message.msg('common.check'), message.msg('hive.msg.enter_key_value'));
            return;
        }

        var params = {
            username: SESSION.USERNAME,
            clusterName: ENGINE.id,
            database: me.getView().database,
            table: tableName,
            tableType: tableType,
            comment: comment,
            location: location,
            columns: columns,
            partitions: partitions,
            delimiter: delimiter,
            serde: serde,
            inputFormat: inputFormat,
            outputFormat: outputFormat,
            orgTable: me.getViewModel().getData().orgTable,
            properties: records2Json(records)
        };

        invokePostByMap(
            CONSTANTS.HIVE.METASTORE.ALTER_TABLE,
            params,
            function (response) {
                var obj = Ext.decode(response.responseText);
                if (obj.success) {

                    info(message.msg('common.success'), tableName + ' ' + message.msg('hive.msg.table_changed'));
                    me.fireEvent('refreshTable');
                    me.getView().close();
                }
                else {
                    Ext.MessageBox.show({
                        title: message.msg('hive.msg.notice'),
                        message: message.msg('hive.msg.table_create_fail') + '<br>' + message.msg('common.cause') + ': ' + obj.error.message,
                        buttons: Ext.MessageBox.OK,
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

    /**
     * Cancel button click event
     * */
    onBtnCancelClick: function () {
        this.getView().close();
    },

    /**
     * Simple HDFS Browser에서 선택한 Record를 Location Textfiled에 셋팅
     * @param {Object}
     * **/
    onHdfsClose: function (record) {
        var refs = this.getReferences();

        if (!Ext.isEmpty(record)) {
            refs.locationTextField.setValue(record.get('fullyQualifiedPath'));
        }
    },

    /**
     * TableType radio change evnet
     * */
    onRadioChange: function (radio, newValue, oldValue) {
        var me = this;

        var refs = query('createTable').getReferences();
        var values = refs.tableForm.getForm().getValues();

        if (values.tableType == 'EXTERNAL_TABLE') {
            refs.locationContainer.setDisabled(false);
        }
        else {
            refs.locationContainer.setDisabled(true);
            refs.tableForm.getForm().setValues({location: ''});
        }
    },

    /**
     * Delimiter radio change Event
     * */
    onRowformatRadioChange: function (radio, newValue, oldValue) {
        var me = this;
        var refs = me.getReferences();
        var values = refs.tableForm.getForm().getValues();

        if (values.rowformat == 'serde') {
            refs.fieldDelimiter.setVisible(false);
            refs.fieldSerde.setVisible(true);
        }
        else {
            refs.fieldDelimiter.setVisible(true);
            refs.fieldSerde.setVisible(false);
        }
    },

    /**
     * Column grid "Add" button Click Event
     * */
    onColumnAddClick: function () {
        var me = this;
        var refs = me.getReferences();

        refs.columnGrid.getStore().add({name: null, type: null, comment: null});
    },

    /**
     * Column grid "Remove" button Click Event
     * */
    onColumnRemoveClick: function () {
        var me = this;
        var refs = me.getReferences();
        var selection = refs.columnGrid.getSelectionModel().getSelection();

        if (Ext.isEmpty(selection)) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('hive.msg.delete_row'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        refs.columnGrid.getStore().remove(selection);
    },

    /**
     * Partition grid "Add" button Click Event
     * */
    onPartitionAddClick: function () {
        var me = this;
        var refs = me.getReferences();

        refs.partitionGrid.getStore().add({name: null, type: null, comment: null});
    },

    /**
     * Partition grid "Remove" button Click Event
     * */
    onPartitionRemoveClick: function () {
        var me = this;
        var refs = me.getReferences();
        var selection = refs.partitionGrid.getSelectionModel().getSelection();

        if (Ext.isEmpty(selection)) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('hive.msg.delete_row'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        refs.partitionGrid.getStore().remove(selection);
    },

    /**
     * Action Column Click Event
     * */
    onActionColumnClick: function (view, rowIndex, colIndex, item, e, record) {
        if (Ext.isEmpty(record)) {
            return;
        }
        var refs = this.getReferences();
        var me = this;
        var type = record.get('type');
        var winPopup;

        refs.columnGrid.getSelectionModel().select([record]);

        switch (type) {
            case 'struct':
                if (Ext.isEmpty(me.propStruct)) {
                    me.propStruct = Ext.create('Flamingo2.view.hive.metastore._PropStruct', {
                        record: record,
                        reference: 'propStruct'
                    });
                }

                me.propStruct.record = record;
                winPopup = me.propStruct;
                break;
            case 'map':
                if (Ext.isEmpty(me.propMap)) {
                    me.propMap = Ext.create('Flamingo2.view.hive.metastore._PropMap', {
                        record: record,
                        reference: 'propMap'
                    });
                }

                me.propMap.record = record;
                winPopup = me.propMap;
                break;
            case 'array':
                if (Ext.isEmpty(me.propArray)) {
                    me.propArray = Ext.create('Flamingo2.view.hive.metastore._PropArray', {
                        record: record,
                        reference: 'propArray'
                    });
                }
                me.propArray.record = record;
                winPopup = me.propArray;
                break;
        }

        winPopup.show();
    },

    /**
     * property popup "OK" button click event
     * */
    onPropBtnOkClick: function (window, value) {
        var me = this;
        var refs = me.getReferences();
        var record = refs.columnGrid.getSelectionModel().getSelection()[0];

        record.set('collection', value);
        window.close();
    },

    /**
     * Input format checkbox change event
     * */
    onCbxInputChange: function (checkbox, newValue) {
        var refs = this.getReferences();
        if (newValue) {
            refs.txInputFormat.setDisabled(false);
            refs.txInputFormat.setValue(null);
        }
        else {
            refs.txInputFormat.setDisabled(true);
            refs.txInputFormat.setValue('org.apache.hadoop.mapred.TextInputFormat');
        }
    },

    /**
     * Output format checkbox change event
     * */
    onCbxOutputChange: function (checkbox, newValue) {
        var refs = this.getReferences();
        if (newValue) {
            refs.txOutputFormat.setDisabled(false);
            refs.txOutputFormat.setValue(null);
        }
        else {
            refs.txOutputFormat.setDisabled(true);
            refs.txOutputFormat.setValue('org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat');
        }
    },

    /**
     * Delimiter 버튼 클릭 이벤트
     * */
    onBtnDelimiterClick: function (button) {
        var me = this;
        var refs = me.getReferences();

        Ext.create('Flamingo2.view.hive.metastore._Delimiter', {
            target: button.target
        }).show();
    },

    /**
     * Delimiter 선택 완료 이벤트
     * 화면에서 선택한 Delimiter 값을 textfiled에 설정한다.
     * */
    onDelimiterSelected: function (record, target) {
        var refs = this.getReferences();

        switch (target) {
            case 'field':
                refs.txField.setValue(record.get('description'));
                refs.txField.delimiter = Ext.isEmpty(record.get('value')) ? '\\' + record.get('octal') : record.get('value');
                break;
            case 'line':
                refs.txLine.setValue(record.get('description'));
                refs.txLine.delimiter = Ext.isEmpty(record.get('value')) ? '\\' + record.get('octal') : record.get('value');
                break;
            case 'map':
                refs.txMap.setValue(record.get('description'));
                refs.txMap.delimiter = Ext.isEmpty(record.get('value')) ? '\\' + record.get('octal') : record.get('value');
                break;
            case 'collection':
                refs.txCollection.setValue(record.get('description'));
                refs.txCollection.delimiter = Ext.isEmpty(record.get('value')) ? '\\' + record.get('octal') : record.get('value');
                break;
        }
    },

    /**
     * Properties 추가 버튼 클릭 이벤트
     * */
    onPropertiesAddClick: function () {
        var me = this;
        var refs = this.getReferences();

        refs.grdProperty.getStore().insert(0, {key: '', value: ''});
    },

    /**
     * Properties 삭제 버튼 클릭 이벤트
     * */
    onPropertiesRemoveClick: function () {
        var me = this;
        var refs = this.getReferences();
        var records = refs.grdProperty.getSelectionModel().getSelection()

        if (Ext.isEmpty(records)) {
            Ext.Msg.alert(message.msg('common.confirm'), message.msg('hive.msg.delete_row'));
            return;
        }

        refs.grdProperty.getStore().remove(records);
    }
});