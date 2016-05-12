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
Ext.define('Flamingo2.view.hive.metastore._CreateDatabaseController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.createDatabaseController',

    requires: [
        'Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser'
    ],

    listen: {
        controller: {
            'simpleHdfsBrowserController': {
                hdfsclose: 'onHdfsclose'
            }
        }
    },

    /**
     * CreateDatabase SimpleHDFSBrowser OK Click Event
     * */
    onOKClick: function () {
        var me = this;
        var refs = this.getReferences();
        var values = refs.frmCreateDatabase.getForm().getValues();
        var records = [], isValid = true;

        if (Ext.isEmpty(values.database)) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('hive.msg.enter_db_name'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        if (!Ext.isEmpty(values.external) && Ext.isEmpty(values.location)) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('hive.msg.select_db_location'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        values.external = Ext.isEmpty(values.external) ? false : true;

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

        values = Ext.merge(values, {properties: records2Json(records)});

        Ext.MessageBox.show({
            title: message.msg('hive.database.create'),
            message: message.msg('hive.msg.create_db'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn === 'yes') {
                    me.createDatabase(values);
                } else if (btn === 'no') {
                    return;
                }
            }
        });

    },

    /**
     * CreateDatabase Ajax Function
     * */
    createDatabase: function (values) {
        var me = this;
        var refs = this.getReferences();
        var params = Ext.merge(values, {clusterName: ENGINE.id});

        invokePostByMap(
            CONSTANTS.HIVE.METASTORE.CREATE_DATABASE,
            params,
            function (response) {

                var obj = Ext.decode(response.responseText);
                if (obj.success) {
                    info(message.msg('common.success'), message.msg('hive.msg.db_create_succ'));

                    me.fireEvent('databaseRefresh');
                    me.getView().close();
                    return;
                }
                else {
                    Ext.MessageBox.alert({
                        title: message.msg('hive.database.create'),
                        message: message.msg('hive.msg.system_error') + '<br>' + obj.error.message,
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
     * CreateDatabase Cancel Button Click Event
     * */
    onCancelClick: function () {
        this.getView().close();
    },

    /**
     * CreateDatabase Checkbox Change Event
     * */
    onCheckboxChange: function (field, newValue) {
        var me = this;
        var refs = me.getReferences();
        if (newValue) {
            refs.fcLocation.setDisabled(false);
        }
        else {
            refs.fcLocation.setDisabled(true);
            refs.txLocation.reset();
        }
    },

    /**
     * CreateDatabase Browse Button Click
     * */
    onBrowseClick: function () {
        var me = this;

        Ext.create('Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser').show();
    },

    /**
     * CreateDatabase SimpleHDFSBrowser Close Event
     * */
    onHdfsclose: function (record) {
        var me = this;
        var refs = this.getReferences();

        refs.txLocation.setValue(record.get('fullyQualifiedPath'));
    },

    /**
     * 데이터베이스 속성값 Plus 버튼 클릭 이벤트
     * */
    onPlusClick: function () {
        var me = this;
        var refs = this.getReferences();

        refs.grdProperty.getStore().insert(0, {key: '', value: ''});
    },

    /**
     * 데이터베이스 속성값 Minus 버튼 클릭 이벤트
     * */
    onMinusClick: function () {
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