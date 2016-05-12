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
 * ViewController for Flamingo2.view.hawq.browser.HawqBrowser
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser.HawqBrowser
 */
Ext.define('Flamingo2.view.hawq.browser.HawqBrowserController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hawqBrowserController',

    listen: {
        controller: {
            'hawqBrowserController': {
                hawqCreateTableClick: 'hawqCreateTableClick',
                hawqAlterTableClick: 'hawqAlterTableClick',
                hawqDropTableClick: 'hawqDropTableClick',
                hawqCreateExternalTableClick: 'hawqCreateExternalTableClick',
                hawqAlterExternalTableClick: 'hawqAlterExternalTableClick',
                hawqDropExternalTableClick: 'hawqDropExternalTableClick',
                hawqCreateViewClick: 'hawqCreateViewClick',
                hawqDropViewClick: 'hawqDropViewClick',
                hawqCreateFunctionClick: 'hawqCreateFunctionClick',
                hawqDropFunctionClick: 'hawqDropFunctionClick',
                hawqViewPartitionDetailClick: 'hawqViewPartitionDetailClick'
            }
        }
    },

    hawqBrowserAfterrender: function (panel) {
        var me = this;
        setTimeout(function () {
            me.hawqConnect(panel);
        }, 50);
    },

    hawqConnectionRefresh: function (owner, tool) {
        this.hawqConnect(tool.up('panel').up('panel'));
    },

    hawqConnect: function (panel) {
        var me = this,
            refs = me.getReferences();
        invokePostByMap(
            CONSTANTS.HAWQ.BROWSER.CONNECT,
            {clusterName: ENGINE.id},
            function (response) {
                var result = Ext.decode(response.responseText);
                if (result.success) {
                    if (!result.object) {
                        Ext.MessageBox.show({
                            title: message.msg('hawq.title.fail'),
                            message: message.msg('hawq.msg.fail.invalidcluster'),
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                        return;
                    }

                    // Connection Manager 아래의 fieldcontainer setting
                    var hawqDatabaseCombobox = refs.hawqDatabaseCombobox,
                        hawqSchemaCombobox = refs.hawqSchemaCombobox,
                        hawqMetadata = result.object;

                    hawqDatabaseCombobox.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                    hawqDatabaseCombobox.getStore().load();
                    hawqDatabaseCombobox.setValue(hawqMetadata.databaseName);

                    hawqSchemaCombobox.getStore().getProxy().extraParams = {
                        clusterName: ENGINE.id,
                        databaseName: hawqDatabaseCombobox.getValue()
                    };
                    hawqSchemaCombobox.getStore().load();
                    hawqSchemaCombobox.setValue(hawqMetadata.defaultSchema);

                    // HAWQ Editor 가장 바깥 panel 의 bbar setting
                    var hawqPanelRefs = panel.up('panel').getReferences();
                    hawqPanelRefs.autoCommitDisplayfield.setValue(hawqMetadata.autoCommit);
                    hawqPanelRefs.encodingDisplayfield.setValue(hawqMetadata.encoding);
                    hawqPanelRefs.userNameDisplayfield.setValue(hawqMetadata.userName);
                    hawqPanelRefs.hawqVersionDisplayfield.setValue(hawqMetadata.databaseProductVersion);

                    // Object Explorer setting
                    var databaseName = hawqDatabaseCombobox.getValue(),
                        schemaName = hawqSchemaCombobox.getValue();

                    Ext.each(
                        refs.objectExplorerTabpanel.items.items,
                        function (grid, index, allGrids) {
                            grid.getStore().reload({
                                params: {
                                    clusterName: ENGINE.id,
                                    databaseName: databaseName,
                                    schemaName: schemaName
                                }
                            });
                        }
                    );
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('hawq.title.fail'),
                        message: message.msg('hawq.msg.fail.connect') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function (response) {
                Ext.MessageBox.show({
                    title: message.msg('hawq.title.fail'),
                    message: response.timedout ? response.statusText + ': Timeout.' : format(message.msg('hawq.msg.warning.servererror'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.ERROR
                });
            }
        );
    },

    hawqDatabaseComboboxSelect: function (combo, records, eOpts) {
        var me = this,
            refs = me.getReferences(),
            databaseName = combo.getValue();

        invokeGet(
            CONSTANTS.HAWQ.BROWSER.CHANGE_DATABASE,
            {
                clusterName: ENGINE.id,
                databaseName: databaseName
            },
            function (response) {
                var result = Ext.decode(response.responseText);
                if (result.success) {
                    var schemaName = result.object;// defaultSchema
                    refs.hawqSchemaCombobox.getStore().getProxy().extraParams = {
                        clusterName: ENGINE.id,
                        databaseName: databaseName
                    };
                    refs.hawqSchemaCombobox.getStore().load();
                    refs.hawqSchemaCombobox.setValue(schemaName);

                    Ext.each(
                        refs.objectExplorerTabpanel.items.items,
                        function (grid, index, allGrids) {
                            grid.getStore().reload({
                                params: {
                                    clusterName: ENGINE.id,
                                    databaseName: databaseName,
                                    schemaName: schemaName
                                }
                            });
                        }
                    );
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('hawq.title.fail'),
                        message: message.msg('hawq.msg.fail.changedb') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
    },

    refreshObjectExplorer: function (tabpanel, tool, event) {
        var me = this,
            refs = me.getReferences(),
            databaseName = refs.hawqDatabaseCombobox.getValue(),
            schemaName = refs.hawqSchemaCombobox.getValue();

        Ext.each(
            tabpanel.items.items,
            function (grid, index, allGrids) {
                grid.getStore().reload({
                    params: {
                        clusterName: ENGINE.id,
                        databaseName: databaseName,
                        schemaName: schemaName
                    }
                });
            }
        );
    },

    hawqConnectionManagerConfigShowMenu: function (button, e) {
        button.showMenu({});
    },

    hawqConnectionManagerDatabaseRefresh: function (menuItem, e) {
        this.getReferences().hawqDatabaseCombobox.getStore().reload({
            params: {
                clusterName: ENGINE.id
            }
        });
    },

    hawqConnectionManagerDatabaseCreate: function (menuItem, e) {
        Ext.create('Flamingo2.view.hawq.browser._DatabaseCreate').center().show();
    },

    hawqConnectionManagerDatabaseDrop: function (menuItem, e) {
        var me = this,
            refs = me.getReferences(),
            hawqDatabaseCombobox = refs.hawqDatabaseCombobox,
            databaseName = hawqDatabaseCombobox.getValue();

        if (databaseName) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.drop'),
                message: format(message.msg('hawq.msg.question.dropdb'), databaseName),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.DROP_DATABASE,
                            {
                                clusterName: ENGINE.id,
                                dropDatabaseName: databaseName
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.dropdb'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            hawqDatabaseCombobox.clearValue();
                                            hawqDatabaseCombobox.getStore().reload({
                                                params: {
                                                    clusterName: ENGINE.id
                                                }
                                            });
                                            hawqDatabaseCombobox.setValue(result.object);// set default database
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.dropdb') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.dropdb'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqConnectionManagerSchemaRefresh: function () {
        var refs = this.getReferences();
        refs.hawqSchemaCombobox.getStore().reload({
            params: {
                clusterName: ENGINE.id,
                databaseName: refs.hawqDatabaseCombobox.getValue()
            }
        });
    },

    hawqConnectionManagerSchemaCreate: function (menuItem, e) {
        Ext.create('Flamingo2.view.hawq.browser._SchemaCreate').center().show();
    },

    hawqConnectionManagerSchemaDrop: function (menuItem, e) {
        var me = this,
            refs = me.getReferences(),
            hawqSchemaCombobox = refs.hawqSchemaCombobox,
            schemaName = hawqSchemaCombobox.getValue();

        if (schemaName) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.drop'),
                message: format(message.msg('hawq.msg.question.dropschema'), schemaName),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.DROP_SCHEMA,
                            {
                                clusterName: ENGINE.id,
                                databaseName: refs.hawqDatabaseCombobox.getValue(),
                                schemaName: schemaName
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.dropschema'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            hawqSchemaCombobox.clearValue();
                                            hawqSchemaCombobox.getStore().reload({
                                                params: {
                                                    clusterName: ENGINE.id,
                                                    databaseName: refs.hawqDatabaseCombobox.getValue()
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.dropschema') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.dropschema'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqSchemaComboboxSelect: function (combo, records, eOpts) {
        var me = this,
            refs = me.getReferences(),
            databaseName = refs.hawqDatabaseCombobox.getValue(),
            schemaName = combo.getValue();

        Ext.each(
            refs.objectExplorerTabpanel.items.items,
            function (grid, index, allGrids) {
                grid.getStore().reload({
                    params: {
                        clusterName: ENGINE.id,
                        databaseName: databaseName,
                        schemaName: schemaName
                    }
                });
            }
        );
    },

    hawqTableSizeRenderer: function (value, metaData, record) {
        return record.get('size') || value;
    },

    hawqObjectExplorerGridItemclick: function (view, record, item, index, e, eOpts) {
        if (!record.get('root')) {
            var me = this,
                refs = me.getReferences(),
                databaseName = refs.hawqDatabaseCombobox.getValue(),
                schemaName = record.get('table_schema') || record.get('nspname'),
                tableName = record.get('table_name'),
                oid = record.get('oid'),
                tableType = record.get('table_type'),
                storage = record.get('relstorage'),
                objectType = record.get('object_type'),
                params = {
                    clusterName: ENGINE.id,
                    databaseName: databaseName,
                    schemaName: schemaName,
                    objectType: objectType,
                    oid: oid,
                    tableName: tableName,
                    storage: storage
                },
                defField = refs.hawqTableDefForm.getForm().findField('def');

            defField.setLoading(true);

            if (view.panel.reference !== 'hawqFunctionsGrid') {
                refs.hawqColumnsGrid.getStore().reload({
                    params: params
                });

                if (view.panel.reference !== 'hawqPartitionTree') {
                    var tree = refs.hawqPartitionTree,
                        treeStore = tree.getStore();
                    treeStore.load({
                        params: params,
                        callback: function () {
                            tree.getRootNode().expand();
                        }
                    });
                }
            }

            refs.hawqObjectMetadatasGrid.getStore().reload({
                params: params,
                callback: function (records, operation, success) {
                    if (tableType === 'VIEW') {
                        var databaseName = '',
                            schemaName = '',
                            viewName = '',
                            owner = '',
                            viewDef = '';
                        Ext.each(records, function (metadata, index, allMetadatas) {
                            switch (metadata.get('key')) {
                                case 'Database':
                                    databaseName = metadata.get('value');
                                    break;
                                case 'Schema':
                                    schemaName = metadata.get('value');
                                    break;
                                case 'Table Name':
                                    viewName = metadata.get('value');
                                    break;
                                case 'Owner':
                                    owner = metadata.get('value');
                                    break;
                                case 'View Definition':
                                    viewDef = metadata.get('value');
                                    break;
                                default:
                                    break;
                            }
                        });
                        var dbSchemaView = databaseName + '.' + schemaName + '.' + viewName;

                        defField.setValue(
                            'CREATE OR REPLACE VIEW ' + dbSchemaView + ' AS' +
                            '\n    ' + viewDef +
                            '\nALTER TABLE ' + dbSchemaView + ' OWNER TO ' + owner + ';'
                        );
                        defField.setLoading(false);
                    }
                }
            });

            if (tableType !== 'VIEW') {
                invokeGet(
                    CONSTANTS.HAWQ.BROWSER.OBJECT_DEF,
                    params,
                    function (response) {
                        var result = Ext.decode(response.responseText);
                        if (result.success) {
                            defField.setValue(result.object);
                            defField.setLoading(false);
                        } else {
                            Ext.MessageBox.show({
                                title: message.msg('hawq.title.fail'),
                                message: message.msg('hawq.msg.fail.getobjectdef') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING,
                                fn: function () {
                                    defField.setLoading(false);
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
                                defField.setLoading(false);
                            }
                        });
                    }
                );
            }
        }
    },

    /**
     * 테이블 그리드 컨텍스트 메뉴 이벤트
     * */
    onTableContainercontextmenu: function (view, e) {
        e.stopEvent();
        var me = this,
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.table.create'),
                        iconCls: 'common-table-add',
                        handler: function (item, e) {
                            me.fireEvent('hawqCreateTableClick', me.getReferences());
                        }
                    }
                ]
            });

        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    /**
     * 테이블 그리드 행 컨텍스트 메뉴 이벤트
     * */
    onTableRowcontextmenu: function (grid, record, tr, rowIndex, e) {
        e.stopEvent();
        var me = this,
            refs = me.getReferences(),
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.table.create'),
                        iconCls: 'common-table-add',
                        handler: function (item, e) {
                            me.fireEvent('hawqCreateTableClick', refs);
                        }
                    },
                    {
                        text: message.msg('hawq.button.table.alter'),
                        iconCls: 'common-table-alter',
                        handler: function (item, e) {
                            me.fireEvent('hawqAlterTableClick', grid);
                        }
                    },
                    {
                        text: message.msg('hawq.button.table.drop'),
                        iconCls: 'common-table-remove',
                        handler: function (item, e) {
                            me.fireEvent('hawqDropTableClick', grid, refs);
                        }
                    }
                ]
            });
        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    /**
     * 뷰 그리드 컨텍스트 메뉴 이벤트
     * */
    onViewContainercontextmenu: function (view, e) {
        e.stopEvent();
        var me = this,
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.view.create'),
                        iconCls: 'common-add',
                        handler: function (item, e) {
                            me.fireEvent('hawqCreateViewClick', me.getReferences());
                        }
                    }
                ]
            });
        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    /**
     * 뷰 그리드 행 컨텍스트 메뉴 이벤트
     * */
    onViewRowcontextmenu: function (grid, record, tr, rowIndex, e) {
        e.stopEvent();
        var me = this,
            refs = me.getReferences(),
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.view.create'),
                        iconCls: 'common-add',
                        handler: function (item, e) {
                            me.fireEvent('hawqCreateViewClick', refs);
                        }
                    },
                    {
                        text: message.msg('hawq.button.view.drop'),
                        iconCls: 'common-delete',
                        handler: function (item, e) {
                            me.fireEvent('hawqDropViewClick', grid, refs);
                        }
                    }
                ]
            });
        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    /**
     * 외부테이블 그리드 컨텍스트 메뉴 이벤트
     * */
    onExternalContainercontextmenu: function (view, e) {
        e.stopEvent();
        var me = this,
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.exttable.create'),
                        iconCls: 'common-external-add',
                        handler: function (item, e) {
                            me.fireEvent('hawqCreateExternalTableClick', me.getReferences());
                        }
                    }
                ]
            });
        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    /**
     * 외부테이블 그리드 행 컨텍스트 메뉴 이벤트
     * */
    onExternalRowcontextmenu: function (grid, record, tr, rowIndex, e, eOpts) {
        e.stopEvent();
        var me = this,
            refs = me.getReferences(),
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.exttable.create'),
                        iconCls: 'common-external-add',
                        handler: function (item, e) {
                            me.fireEvent('hawqCreateExternalTableClick', refs);
                        }
                    },
                    {
                        text: message.msg('hawq.button.exttable.alter'),
                        iconCls: 'common-table-alter',
                        handler: function (item, e) {
                            me.fireEvent('hawqAlterExternalTableClick', grid);
                        }
                    },
                    {
                        text: message.msg('hawq.button.exttable.drop'),
                        iconCls: 'common-external-remove',
                        handler: function (item, e) {
                            me.fireEvent('hawqDropExternalTableClick', grid, refs);
                        }
                    }
                ]
            });
        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    /**
     * 함수 컨텍스트 메뉴 이벤트
     * */
    onFunctionContainercontextmenu: function (view, e) {
        e.stopEvent();
        var me = this,
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.function.create'),
                        iconCls: 'common-function-add',
                        handler: function (item, e) {
                            me.fireEvent('hawqCreateFunctionClick');
                        }
                    }
                ]
            });
        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    /**
     * 함수 행 컨텍스트 메뉴 이벤트
     * */
    onFunctionRowcontextmenu: function (grid, record, tr, rowIndex, e, eOpts) {
        e.stopEvent();
        var me = this,
            refs = me.getReferences(),
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.function.create'),
                        iconCls: 'common-function-add',
                        handler: function (item, e) {
                            me.fireEvent('hawqCreateFunctionClick');
                        }
                    },
                    {
                        text: message.msg('hawq.button.function.drop'),
                        iconCls: 'common-function-remove',
                        handler: function (item, e) {
                            me.fireEvent('hawqDropFunctionClick', grid, refs);
                        }
                    }
                ]
            });
        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    hawqCreateTableClick: function (refs) {
        var tableCreateWindow = Ext.create('Flamingo2.view.hawq.browser._TableCreate').center().show();
        tableCreateWindow.getViewModel().setData({
            databaseName: refs.hawqDatabaseCombobox.getValue(),
            schemaName: refs.hawqSchemaCombobox.getValue()
        });
    },

    hawqAlterTableClick: function (grid) {
        var table = grid.getSelectionModel().getSelection()[0];
        if (table) {
            Ext.create('Flamingo2.view.hawq.browser._TableAlter', {
                table: table
            }).center().show();
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.altertable'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqDropTableClick: function (grid, references) {
        var record = grid.getSelectionModel().getSelection()[0];
        if (record) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.drop'),
                message: format(message.msg('hawq.msg.question.droptable'), record.get('table_name')),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        var databaseName = references.hawqDatabaseCombobox.getValue(),
                            schemaName = references.hawqSchemaCombobox.getValue();
                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.DROP_TABLE,
                            {
                                clusterName: ENGINE.id,
                                databaseName: databaseName,
                                schemaName: record.get('table_schema'),
                                tableName: record.get('table_name')
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.droptable'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            references.hawqTablesGrid.getStore().reload({
                                                params: {
                                                    clusterName: ENGINE.id,
                                                    databaseName: databaseName,
                                                    schemaName: schemaName
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.droptable') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.droptable'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqCreateExternalTableClick: function (refs) {
        var extTableCreateWindow = Ext.create('Flamingo2.view.hawq.browser._ExternalTableCreate').center().show();
        extTableCreateWindow.getViewModel().setData({
            databaseName: refs.hawqDatabaseCombobox.getValue(),
            schemaName: refs.hawqSchemaCombobox.getValue()
        });
    },

    hawqAlterExternalTableClick: function (grid) {
        var table = grid.getSelectionModel().getSelection()[0];
        if (table) {
            Ext.create('Flamingo2.view.hawq.browser._ExternalTableAlter', {
                table: table
            }).center().show();
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.altertable'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqDropExternalTableClick: function (grid, references) {
        var record = grid.getSelectionModel().getSelection()[0];
        if (record) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.drop'),
                message: format(message.msg('hawq.msg.question.dropexttable'), record.get('table_name')),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        var databaseName = references.hawqDatabaseCombobox.getValue(),
                            schemaName = references.hawqSchemaCombobox.getValue();
                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.DROP_EXTERNAL_TABLE,
                            {
                                clusterName: ENGINE.id,
                                databaseName: databaseName,
                                schemaName: record.get('table_schema'),
                                externalTableName: record.get('table_name')
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.dropexttable'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            references.hawqExternalTablesGrid.getStore().reload({
                                                params: {
                                                    clusterName: ENGINE.id,
                                                    databaseName: databaseName,
                                                    schemaName: schemaName
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.dropexttable') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.dropexttable'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqCreateViewClick: function (refs) {
        var me = this,
            editorSession = query('hawqViewport #hawqEditorTab').getActiveTab().getReferences().queryEditor.editor.getSession();

        editorSession.insert(
            {row: 0, column: 0},
            'CREATE OR REPLACE VIEW ' + refs.hawqSchemaCombobox.getValue() + '.view_name\n\t' +
            'AS SELECT column FROM schema.table;\n'
        );
        editorSession.setScrollTop(0);
    },

    hawqDropViewClick: function (grid, references) {
        var record = grid.getSelectionModel().getSelection()[0];
        if (record) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.drop'),
                message: format(message.msg('hawq.msg.question.dropview'), record.get('table_name')),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        var databaseName = references.hawqDatabaseCombobox.getValue(),
                            schemaName = references.hawqSchemaCombobox.getValue();
                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.DROP_VIEW,
                            {
                                clusterName: ENGINE.id,
                                databaseName: databaseName,
                                schemaName: record.get('table_schema'),
                                viewName: record.get('table_name')
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.dropview'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            references.hawqViewsGrid.getStore().reload({
                                                params: {
                                                    clusterName: ENGINE.id,
                                                    databaseName: databaseName,
                                                    schemaName: schemaName
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.dropview') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.dropview'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqCreateFunctionClick: function () {
        var me = this,
            refs = me.getReferences(),
            editorSession = query('hawqViewport #hawqEditorTab').getActiveTab().getReferences().queryEditor.editor.getSession();

        editorSession.insert({row: 0, column: 0},
            'CREATE OR REPLACE FUNCTION ' + refs.hawqSchemaCombobox.getValue() + '.function_name(\n' +
            '\targname argtype,\n' +
            '\targname argtype\n' +
            ')\n' +
            'RETURNS rettype AS $$\n' +
            'BEGIN\n' +
            '\tdefinition\n' +
            'END;\n' +
            '$$ LANGUAGE langname;\n'
        );
        editorSession.setScrollTop(0);
    },

    hawqDropFunctionClick: function (grid, references) {
        var record = grid.getSelectionModel().getSelection()[0];
        if (record) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.drop'),
                message: format(message.msg('hawq.msg.question.dropfunc'), record.get('proname')),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        var databaseName = references.hawqDatabaseCombobox.getValue(),
                            schemaName = references.hawqSchemaCombobox.getValue();
                        invokePostByMap(
                            CONSTANTS.HAWQ.BROWSER.DROP_FUNCTION,
                            {
                                clusterName: ENGINE.id,
                                databaseName: databaseName,
                                schemaName: record.get('nspname'),
                                functionName: record.get('proname'),
                                proargtypes: record.get('proargtypes')
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.dropfunc'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            references.hawqFunctionsGrid.getStore().reload({
                                                params: {
                                                    clusterName: ENGINE.id,
                                                    databaseName: databaseName,
                                                    schemaName: schemaName
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.dropfunc') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.dropfunc'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqTableInformationsGridItemdblclick: function (grid, record, item, index, e, eOpts) {
        Ext.create('Flamingo2.view.hawq.browser._ColumnDetailWindow', {
            detail: record.getData()
        }).center().show();
    },

    hawqColumnDetailWindowAfterrender: function (window) {
        window.down('form').getForm().setValues(window.detail);
    },

    hawqPartitionTreeItemcontextmenu: function (treepanel, record, item, index, e, eOpts) {
        e.stopEvent();
        if (!record.get('root')) {
            var me = this,
                refs = me.getReferences(),
                menu = Ext.create('Ext.menu.Menu', {
                    items: [
                        {
                            text: message.msg('hawq.title.detail.partition'),
                            iconCls: 'common-information',
                            handler: function (item, e) {
                                me.fireEvent('hawqViewPartitionDetailClick', record);
                            }
                        }
                    ]
                });
            menu.showAt(e.pageX - 5, e.pageY - 5);
        }
    },

    hawqPartitionTreeContainercontextmenu: function (treepanel, e, eOpts) {
        e.stopEvent();
    },

    hawqViewPartitionDetailClick: function (record) {
        Ext.create('Flamingo2.view.hawq.browser._PartitionDetailWindow', {
            params: record.getData()
        }).center().show();
    },

    hawqPartitionDetailWindowAfterrender: function (window) {
        var formPanel = window.down('form');
        formPanel.setLoading(true);
        invokeGet(
            CONSTANTS.HAWQ.BROWSER.PARTITION_DETAIL,
            {
                clusterName: ENGINE.id,
                schemaName: window.params.nspname,
                tableName: window.params.relname// relname || table_name || text
            },
            function (response) {
                var result = Ext.decode(response.responseText);
                if (result.success) {
                    formPanel.getForm().setValues(result.object);
                    formPanel.setLoading(false);
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('hawq.title.fail'),
                        message: message.msg('hawq.msg.fail.getpartitiondetail') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
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

    hawqPartitionTreeBeforeload: function (store, operation, eOpts) {
        store.getProxy().extraParams.clusterName = ENGINE.id;
    },

    cancelButtonHandler: function (button) {
        button.up('window').close();
    }
});