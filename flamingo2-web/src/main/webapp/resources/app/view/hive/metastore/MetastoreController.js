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
Ext.define('Flamingo2.view.hive.metastore.MetastoreController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hiveMetastoreController',

    requires: [
        'Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser',
        'Flamingo2.view.hive.metastore._CreateDatabase'
    ],

    listen: {
        controller: {
            'hiveMetastoreController': {
                tableRefreshClick: 'onTableRefreshClick',
                tableCreateClick: 'onTableCreateClick',
                tableAlterClick: 'onTableAlterClick',
                tableDropClick: 'onTableDropClick'
            },
            'metastoreCreateTableController': {
                refreshTable: 'onTableRefreshClick'
            },
            'createDatabaseController': {
                databaseRefresh: 'onRefreshClick'
            }
        }
    },

    /**
     * Afterender 이벤트
     * */
    onAfterrender: function (view) {
        var me = this;
        me.viewport = view;
        this.getViewModel().getStore('databases').load({
            params: {
                clusterName: ENGINE.id
            },
            callback: function (records, operation, success) {
                if (!success) {
                    error('Hive 오류', '데이터 베이스 목록을 가져오는데 실패하였습니다.');
                }
            }
        });
    },

    /**
     * Hive 데이터베이스 선택 이벤트
     * */
    onDatabaseSelect: function (combo, record) {
        var refs = this.getReferences();

        this.getViewModel().setData({database: record.get('database')});
        refs.grdTable.getStore().load({
            params: {
                clusterName: ENGINE.id,
                database: record.get('database')
            }
        });

        refs.grdColumns.getStore().removeAll();
        refs.grdPartitions.getStore().removeAll();
        refs.btnTableAdd.setDisabled(false);
        refs.btnTableRemove.setDisabled(true);
    },

    /**
     * Hive 데이터베이스 생성 이벤트
     * */
    onCreateDatabaseClick: function () {
        Ext.create('Flamingo2.view.hive.metastore._CreateDatabase').show();
    },

    /**
     * Drop Database Click Event
     * */
    onDropDatabaseClick: function () {
        var me = this;
        var refs = this.getReferences();

        var database = refs.cbxDatabase.getValue();

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
            title: message.msg('hive.database.drop'),
            message: message.msg('hive.msg.db_drop'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn === 'yes') {
                    me.dropDatabase(database);
                } else if (btn === 'no') {
                    return;
                }
            }
        });
    },

    /**
     * Drop Database Function
     * */
    dropDatabase: function (database) {
        var me = this;
        var params = {
            clusterName: ENGINE.id,
            database: database
        };

        invokePostByMap(
            CONSTANTS.HIVE.METASTORE.DROP_DATABASE,
            params,
            function (response) {

                var obj = Ext.decode(response.responseText);
                if (obj.success) {
                    info(message.msg('common.success'), message.msg('hive.msg.delete_database_succ'));
                    me.onRefreshClick();
                }
                else {
                    Ext.MessageBox.show({
                        title: message.msg('hive.error'),
                        message: message.msg('hive.error') + '<br>' + obj.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.INFO,
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
     * Database Refresh Click Event
     * */
    onRefreshClick: function (button, event) {
        var refs = this.getReferences();

        refs.cbxDatabase.reset();

        refs.cbxDatabase.getStore().load({
            params: {
                clusterName: ENGINE.id
            }
        });

        refs.grdTable.getStore().removeAll();
        refs.grdColumns.getStore().removeAll();
        refs.grdPartitions.getStore().removeAll();
        refs.btnTableAdd.setDisabled(true);
        refs.btnTableRemove.setDisabled(true);
    },

    //----------------------Table List Grid----------------------
    /**
     * Table List Grid row select event
     * */
    onGrdTableSelect: function (grid, record) {
        var me = this;
        var refs = this.getReferences();

        me.getViewModel().setData({table: record.get('tableName')});

        refs.grdColumns.getStore().load({
            params: {
                clusterName: ENGINE.id,
                database: me.getViewModel().getData().database,
                table: record.get('tableName')
            },
            callback: function (records, operation, success) {
                if (!success) {
                    Ext.MessageBox.show({
                        title: message.msg('common.error'),
                        message: this.proxy.reader.rawData.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.INFO
                    });
                }
            }
        });

        refs.grdPartitions.getStore().load({
            params: {
                clusterName: ENGINE.id,
                database: me.getViewModel().getData().database,
                table: record.get('tableName')
            },
            callback: function (records, operation, success) {
                if (!success) {
                    Ext.MessageBox.show({
                        title: message.msg('common.error'),
                        message: this.proxy.reader.rawData.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.INFO
                    });
                }
            }
        });

        refs.btnTableRemove.setDisabled(false);
    },

    /**
     * Table List Grid Container Context Event
     * */
    onGrdTableContainercontextmenu: function (grid, e, eOpts) {
        e.stopEvent();
        var me = this;
        var refs = this.getReferences();

        if (Ext.isEmpty(refs.cbxDatabase.getValue())) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('hive.msg.select_database'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        if (Ext.isEmpty(me.containerContextMenu)) {
            var menu = Ext.create('Ext.menu.Menu', {
                items: [{
                    text: message.msg('common.refresh'),
                    iconCls: 'common-refresh',
                    handler: function () {
                        me.fireEvent('tableRefreshClick');
                    }
                }, {
                    text: message.msg('hive.table.create'),
                    iconCls: 'common-table-add',
                    handler: function () {
                        me.fireEvent('tableCreateClick');
                    }
                }]
            });

            me.containerContextMenu = menu;
        }

        me.containerContextMenu.showAt(e.pageX - 5, e.pageY - 5);

    },

    /**
     * Table List Grid Row Context Event
     * */
    onGrdTableRowcontextmenu: function (grid, record, tr, rowIndex, e) {
        e.stopEvent();
        var me = this;
        var refs = me.getReferences();

        refs.grdTable.getSelectionModel().select([record]);

        if (Ext.isEmpty(me.rowContextMenu)) {
            var menu = Ext.create('Ext.menu.Menu', {
                items: [{
                    text: message.msg('common.refresh'),
                    iconCls: 'common-refresh',
                    handler: function () {
                        me.fireEvent('tableRefreshClick');
                    }
                }, {
                    text: message.msg('hive.table.create'),
                    iconCls: 'common-table-add',
                    handler: function () {
                        me.fireEvent('tableCreateClick');
                    }
                }, {
                    text: message.msg('hive.alter_table'),
                    iconCls: 'common-table-alter',
                    handler: function () {
                        me.fireEvent('tableAlterClick');
                    }
                }, {
                    text: message.msg('hive.table.drop'),
                    iconCls: 'common-table-remove',
                    handler: function () {
                        me.fireEvent('tableDropClick');
                    }
                }]
            });

            me.rowContextMenu = menu;
        }

        me.rowContextMenu.showAt(e.pageX - 5, e.pageY - 5);
    },

    /**
     * Table List on TableRefresh Click Event
     * */
    onTableRefreshClick: function () {
        var refs = query('hiveMetastoreViewport').getReferences();
        var database = refs.cbxDatabase.getValue();

        refs.grdTable.getStore().load({
            params: {
                clusterName: ENGINE.id,
                database: database
            }
        });

        refs.grdColumns.getStore().removeAll();
        refs.grdPartitions.getStore().removeAll();
    },

    /**
     * Table List on TableCreate Click Event
     * */
    onTableCreateClick: function () {
        var refs = query('hiveMetastoreViewport').getReferences();

        Ext.create('Flamingo2.view.hive.metastore._CreateTable', {
            database: refs.cbxDatabase.getValue()
        }).show();
    },

    /**
     * Table List on TableAlter Click Event
     * */
    onTableAlterClick: function () {
        var refs = query('hiveMetastoreViewport').getReferences();
        var record = refs.grdTable.getSelectionModel().getSelection()[0];

        Ext.create('Flamingo2.view.hive.metastore._CreateTable', {
            database: refs.cbxDatabase.getValue(),
            alter: true,
            table: record.get('tableName')
        }).show();
    },

    /**
     * Table List on TableDrop Click Event
     * */
    onTableDropClick: function () {
        var me = this;
        var refs = this.getReferences();
        var record = refs.grdTable.getSelectionModel().getSelection()[0];

        var params = {
            clusterName: ENGINE.id,
            database: refs.cbxDatabase.getValue(),
            table: record.get('tableName')
        };

        Ext.MessageBox.show({
            title: message.msg('hive.table.drop'),
            message: message.msg('hive.msg.wanna_delete_table'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn === 'yes') {
                    invokePostByMap(
                        CONSTANTS.HIVE.METASTORE.DROP_TABLE,
                        params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                info(message.msg('common.check'), message.msg('hive.msg.table_dropped'));
                                me.onTableRefreshClick();
                            }
                            else {
                                Ext.MessageBox.show({
                                    title: message.msg('hive.msg.drop_error'),
                                    message: message.msg('hive.msg.system_error') + '<br>' + obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.INFO,
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
                } else if (btn === 'no') {
                    return;
                }
            }
        });
    },

    /**
     * 테이블 생성 버튼 클릭 이벤트
     * */
    onTableAddClick: function () {
        var me = this;
        me.onTableCreateClick();
    },

    onTableRemoveClick: function () {
        var me = this;
        me.onTableDropClick();
    },

    /**
     * 하이브 데이터베이스 Load 이벤트
     * */
    onDatabasesLoad: function (store, records, successful) {
        if (!successful) {
            var rawData = store.proxy.reader.rawData;

            if (!Ext.isEmpty(rawData.error.message)) {
                error(message.msg('hive.error'), rawData.error.message);
            }
            else {
                error(message.msg('hive.error'), message.msg('hive.msg.system_error'));
            }
        }
    }
});