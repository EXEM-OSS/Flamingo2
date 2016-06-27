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
Ext.define('Flamingo2.view.tajo.metastore._CreateTableController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.tajoMetastoreCreateTableController',

    requires: [
        'Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser',
        'Flamingo2.view.tajo.metastore._Delimiter',
        'Flamingo2.view.tajo.metastore._ParquetFormat',
        'Flamingo2.view.tajo.metastore._RcFileFormat',
        'Flamingo2.view.tajo.metastore._SequenceFileFormat',
        'Flamingo2.view.tajo.metastore._TextFileFormat'
    ],

    init: function () {
        this.control({
            'tajoCreateTable #fieldTableType radio': {
                change: 'onRadioChange'
            },
            'tajoCreateTable #fieldRowformat radio': {
                change: 'onRowformatRadioChange'
            },
            'tajoCreateTable #fileType radio': {
                change: 'onFileTypeRadioChange'
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
            'tajoMetastoreDelimiterController': {
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

        Ext.MessageBox.show({
            title: message.msg('hive.table.create'),
            message: message.msg('tajo.msg.create_table'),
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
        var database = Ext.isEmpty(me.getView().database) ? refs.comboDB.getValue() : me.getView().database;
        var columnGrid = refs.columnGrid;
        var columnGridStore = columnGrid.getStore();
        var columns = columnGridStore.records2Json(columnGridStore.getData().items);
        var partitionGrid = refs.partitionGrid;
        var partitionGridStore = partitionGrid.getStore();
        var partitions = partitionGridStore.records2Json(partitionGridStore.getData().items);

        tableForm.isValid();

        if (Ext.isEmpty(database)) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('hive.msg.select_database'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        if (Ext.isEmpty(formData.tableName)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.enter_table_name'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        if (formData.tableType == 'EXTERNAL_TABLE' && Ext.isEmpty(formData.location)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('tajo.msg.select_table_location'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        if (formData.storeType == 'TEXTFILE') {
            formData.delimiter = refs.delimiter.delimiter;
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
        });

        if (!storeValid) return false;

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
        });

        if (!storeValid) return false;

        var params = {
            clusterName: ENGINE.id,
            database: Ext.isEmpty(me.getView().database) ? refs.comboDB.getValue() : me.getView().database,
            columns: columns,
            partitions: partitions
        };

        params = Ext.merge(params, formData);

        invokePostByMap(
            CONSTANTS.TAJO.CREATE_TABLE,
            params,
            function (response) {
                var obj = Ext.decode(response.responseText);
                if (obj.success) {
                    info(message.msg('common.success'), params.tableName + ' ' + message.msg('hive.msg.created'));
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
        var refs = me.getReferences();
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
                message: message.msg('tajo.msg.delete_row'),
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
                message: message.msg('tajo.msg.delete_row'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        refs.partitionGrid.getStore().remove(selection);
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
            refs.txOutputFormat.setValue('org.apache.hadoop.tajo.ql.io.tajoIgnoreKeyTextOutputFormat');
        }
    },

    /**
     * Delimiter 버튼 클릭 이벤트
     * */
    onBtnDelimiterClick: function (button) {
        var me = this;
        var refs = me.getReferences();

        Ext.create('Flamingo2.view.tajo.metastore._Delimiter', {
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
            case 'delimiter':
                refs.delimiter.setValue(record.get('description'));
                refs.delimiter.delimiter = Ext.isEmpty(record.get('value')) ? '\\' + record.get('octal') : record.get('value');
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
            Ext.Msg.alert(message.msg('common.confirm'), message.msg('tajo.msg.delete_row'));
            return;
        }

        refs.grdProperty.getStore().remove(records);
    },

    /**
     * 파일형식 Radio Change 이벤트
     * */
    onFileTypeRadioChange: function(radio, newValue, oldValue) {
        var me = this;
        var refs = me.getReferences();

        if (newValue) {
            refs.fileType.removeAll();
            var view;
            switch (radio.inputValue) {
                case 'TEXTFILE':
                    view = Ext.create('Flamingo2.view.tajo.metastore._TextFileFormat');
                    break;
                case 'RCFILE':
                    view = Ext.create('Flamingo2.view.tajo.metastore._RcFileFormat');
                    break;
                case 'PARQUET':
                    view = Ext.create('Flamingo2.view.tajo.metastore._ParquetFormat');
                    break;
                case 'SEQUENCEFILE':
                    view = Ext.create('Flamingo2.view.tajo.metastore._SequenceFileFormat');
                    break;
            }

            refs.fileType.add(view);
        }
    }
});