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
Ext.define('Flamingo2.view.hive.metastore._PropController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.metastorePorpController',

    /**
     * Array property popup show event
     */
    onPorpArrayShow: function (window) {
        var refs = this.getReferences();
        var record = window.record;
        var collection = record.get('collection');

        if (Ext.isEmpty(collection)) {
            refs.comboArray.setValue(null);
        }
        else {
            var data = Ext.decode(collection);
            refs.comboArray.setValue(data.key);
        }
    },

    /**
     * Array property popup "OK" button click event
     */
    onPropArrayBtnOkClick: function () {
        var me = this;
        var refs = me.getReferences();
        var value = refs.comboArray.getValue();
        if (Ext.isEmpty(value)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.select_array_type'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        var returnJson = Ext.encode({key: value});

        this.fireEvent('propArrayBtnOkClick', me.getView(), returnJson);
    },

    /**
     * Array property popup "Cancel" button click event
     */
    onPropArrayBtnCancelClick: function () {
        this.getView().close();
    },

    /**
     * Array property popup show event
     */
    onPorpMapShow: function (window) {
        var refs = this.getReferences();
        var record = window.record;
        var collection = record.get('collection');

        if (Ext.isEmpty(collection)) {
            refs.comboKey.setValue(null);
            refs.comboValue.setValue(null);
        }
        else {
            var jsonValue = Ext.decode(collection);
            refs.comboKey.setValue(jsonValue.key);
            refs.comboValue.setValue(jsonValue.value);
        }
    },

    /**
     * Map property popup "OK" button click event
     */
    onPropMapBtnOkClick: function () {
        var me = this;
        var refs = me.getReferences();
        var key = refs.comboKey.getValue();
        var value = refs.comboValue.getValue();

        if (Ext.isEmpty(key)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.select_key_type'),
                icon: Ext.MessageBox.INFO,
                buttons: Ext.MessageBox.OK
            });
            return;
        }

        if (Ext.isEmpty(value)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: message.msg('hive.msg.select_value_type'),
                icon: Ext.MessageBox.INFO,
                buttons: Ext.MessageBox.OK
            });
            return;
        }

        var returnJson = Ext.encode({key: key, value: value});
        me.fireEvent('propMapBtnOkClick', me.getView(), returnJson)
    },

    /**
     * Map property popup "Cancel" button click event
     */
    onPropMapBtnCancelClick: function () {
        this.getView().close();
    },

    /**
     * Struct property popup show event
     */
    onPorpStructShow: function (window) {
        var refs = this.getReferences();
        var record = window.record;
        var collection = record.get('collection');
        var gridStore = refs.structGrid.getStore();

        if (Ext.isEmpty(collection)) {
            gridStore.removeAll();
        }
        else {
            var jsonValue = Ext.decode(collection);

            gridStore.loadData(jsonValue);
        }
    },

    /**
     * Struct property "Add" button click event
     * */
    onPropStructAddClick: function () {
        var refs = this.getReferences();
        var model = {name: null, type: null};
        refs.structGrid.getStore().insert(refs.structGrid.getStore().getCount(), model);

    },

    /**
     * Struct property "Delete" button click event
     */
    onPropStructDeleteClick: function () {
        var refs = this.getReferences();
        var gridStore = refs.structGrid.getStore();
        var selection = refs.structGrid.getSelectionModel().getSelection()[0];

        if (Ext.isEmpty(selection)) {
            Ext.MessageBox.show({
                title: message.msg('hive.msg.notice'),
                message: 'Select Delete Row',
                icon: Ext.MessageBox.INFO,
                buttons: Ext.MessageBox.OK
            });
            return;
        }

        gridStore.remove(selection);
    },

    /**
     * Struct property popup "OK" button click event
     */
    onPropStructBtnOkClick: function () {
        var me = this;
        var refs = me.getReferences();

        var gridStore = refs.structGrid.getStore();
        var returnJson = [];

        for (var i = 0; i < gridStore.getCount(); i++) {
            var gridRecord = gridStore.getAt(i);
            if (!gridRecord.isValid()) {
                Ext.MessageBox.show({
                    title: message.msg('hive.msg.notice'),
                    message: 'Check Grid Row',
                    icon: Ext.MessageBox.INFO,
                    buttons: Ext.MessageBox.OK
                });
                return;
            }

            returnJson.push(gridRecord.data);
        }

        returnJson = Ext.encode(returnJson);

        me.fireEvent('propStructBtnOkClick', me.getView(), returnJson);
    },

    /**
     * Struct property popup "Cancel" button click event
     */
    onPropStructBtnCancelClick: function () {
        this.getView().close();
    }
});