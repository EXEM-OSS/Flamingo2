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
Ext.define('Flamingo2.view.fs.hdfs.BrowserController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.browserViewController',

    listen: {
        controller: {
            'simpleHdfsBrowserController': {
                copyDirectoryBeforeClose: 'onCopyDirectoryBeforeClose',
                moveDirectoryBeforeClose: 'onMoveDirectoryBeforeClose',
                copyFileBeforeClose: 'onCopyFileBeforeClose',
                moveFileBeforeClose: 'onMoveFileBeforeClose'
            }
        }
    },

    /**
     * EngineComboBox Change Event
     */
    onEngineChanged: function () {
        var me = this;
        var directoryPanel = query('hdfsDirectoryPanel');

        directoryPanel.getStore().getProxy().extraParams.clusterName = ENGINE.id;
        me.updateDirectoryStore('/');
        me.updateFileStore('/');
    },

    /**
     * 디렉토리 트리를 화면에 표시한 후 서버에서 디렉토리 목록을 가져온다.
     *
     * @param tree 디렉토리 목록 Tree
     */
    onDirectoryAfterRender: function (tree) {
        var statusBar = query('browser > _statusBar');

        setTimeout(function () {
            tree.getStore().proxy.extraParams.clusterName = ENGINE.id;
            tree.getStore().load({
                callback: function () {
                    tree.getRootNode().expand();
                    var rootNode = tree.getStore().getNodeById('root');
                    tree.getSelectionModel().select(rootNode);
                }
            });
        }, 500);

        statusBar.setStatus(message.msg('common.path') + ' : ' + '/');
    },

    /**
     * 파일 그리드를 화면에 표시한 후 서버에서 파일 목록을 가져온다.
     *
     * @param grid 파일 목록 Grid
     */
    onFileAfterRender: function (grid) {
        setTimeout(function () {
            grid.getStore().load({
                params: {
                    clusterName: ENGINE.id
                }
            });
        }, 10);
    },

    /**
     * Refresh 버튼을 누르면 루트(/) 노드를 기준으로 디렉토리와 파일 패널, 파일 경로를 업데이트한다.
     */
    onDirectoryRefreshClick: function () {
        var me = this;
        var treeItem = query('hdfsDirectoryPanel');
        var statusBar = query('browser > _statusBar');

        me.updateDirectoryStore('/');
        me.updateFileStore('/');

        statusBar.setStatus(message.msg('common.path') + ' : ' + '/');
    },

    /**
     * 루트(/) 노드 기준으로 디렉토리 목록을 갱신한다.
     */
    updateDirectoryStore: function (path) {
        if (!path || path == 'root') {
            path = '/';
        }

        var treeItem = query('hdfsDirectoryPanel');
        treeItem.getStore().load({
            params: {
                clusterName: ENGINE.id,
                node: path
            },
            callback: function () {
                var rootNode = treeItem.getStore().getNodeById('root');
                treeItem.getSelectionModel().select(rootNode);
            }
        });
    },

    /**
     * 파일 목록 정보를 갱신한다.
     */
    updateFileStore: function (path) {
        if (!path || path == 'root') {
            path = '/';
        }

        var filePanel = query('hdfsFilePanel');

        filePanel.getStore().load({
            params: {
                clusterName: ENGINE.id,
                node: path
            }
        });
    },

    /**
     * HDFS Tree에서 디렉토리를 선택했을 때 파일 목록 정보를 업데이트한다.
     */
    onClickDirectoryItem: function (view, node) {
        var fileStore = query('hdfsFilePanel').getStore();

        fileStore.load({
            params: {
                node: node.data.id == 'root' ? '/' : node.data.id,
                clusterName: ENGINE.id
            }
        });

        var statusBar = query('browser > _statusBar');

        if (node.data.id == 'root') {
            statusBar.setStatus(message.msg('common.path') + ' : ' + '/');
        } else {
            statusBar.setStatus(message.msg('common.path') + ' : ' + node.data.fullyQualifiedPath);
        }
    },

    /**
     * 선택한 현재 디렉토리 경로만 갱신한다.
     */
    updateCurrentDirectoryStore: function (parentNode) {
        var treeItem = query('hdfsDirectoryPanel');

        if (!parentNode.data.expanded) {
            parentNode.data.expanded = true;
        }

        treeItem.getStore().load({
            clusterName: ENGINE.id,
            node: parentNode
        });
    },

    /**
     * 하위 디렉토리 목록을 갱신한다.
     */
    updateSubDirectoryStore: function (srcParentNode, dstParentNode) {
        var treeItem = query('hdfsDirectoryPanel');

        if (!dstParentNode.data.expanded) {
            dstParentNode.data.expanded = true;
        }

        treeItem.getStore().load({
            clusterName: ENGINE.id,
            node: srcParentNode
        });

        treeItem.getStore().load({
            clusterName: ENGINE.id,
            node: dstParentNode
        });
    },

    /**
     * 마지막 선택한 디렉토리를 가지고 와서 업데이트 한다.
     */
    onFileRefreshBtn: function () {
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var filePanel = query('hdfsFilePanel');
        var currentPath = selectedNode.get('id') == 'root' ? '/' : selectedNode.get('id');

        filePanel.getStore().load({
            params: {
                clusterName: ENGINE.id,
                node: currentPath
            }
        });
    },

    /**
     * 디렉토리에서 마우스 오른쪽 버튼을 누르는 경우 Context Menu를 표시한다.
     */
    onDirectoryItemContextMenu: function (view, record, item, index, event) {
        var me = this;
        event.stopEvent();

        /**
         * 노드가 펼쳐진 상태에서 우클릭 했을 때 해당 노드를 강제 선택함.
         *
         * @type {Array}
         */
        var records = [];
        records.push(record);
        view.getSelectionModel().select(records);

        if (Ext.isEmpty(me.contextDirectoryMenu)) {
            me.contextDirectoryMenu = new Ext.menu.Menu({
                controller: 'browserViewController',
                items: [
                    {
                        text: message.msg('fs.hdfs.common.create'),
                        iconCls: 'common-directory-add',
                        itemId: 'createDirectoryMenu',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.create'),
                        handler: 'onClickCreateDirectory'
                    },
                    '-',
                    {
                        text: message.msg('fs.hdfs.common.copy'),
                        iconCls: 'common-directory-copy',
                        itemId: 'copyDirectoryMenu',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.copy'),
                        handler: 'onClickCopyDirectory'
                    },
                    {
                        text: message.msg('fs.hdfs.common.move'),
                        iconCls: 'common-directory-move',
                        itemId: 'moveDirectoryMenu',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.move'),
                        handler: 'onClickMoveDirectory'
                    },
                    {
                        text: message.msg('fs.hdfs.common.rename'),
                        iconCls: 'common-directory-remove',
                        itemId: 'renameDirectoryMenu',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.rename'),
                        handler: 'onClickRenameDirectory'
                    },
                    {
                        text: message.msg('fs.hdfs.common.delete'),
                        iconCls: 'common-database-remove',
                        itemId: 'deleteDirectoryMenu',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.delete'),
                        handler: 'onClickDeleteDirectory'
                    },
                    '-',
                    {
                        text: message.msg('fs.hdfs.common.upload'),
                        iconCls: 'common-upload',
                        itemId: 'uploadFileMenu',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.upload'),
                        handler: 'onClickUploadFile'
                    },
                    '-',
                    {
                        text: message.msg('fs.hdfs.common.merge'),
                        iconCls: 'common-directory-merge',
                        itemId: 'mergeFileMenu',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.merge'),
                        handler: 'onClickMergeFile'
                    },
                    '-',
                    {
                        text: message.msg('fs.hdfs.common.directory.information'),
                        iconCls: 'common-information',
                        itemId: 'getInfoMenu',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.info'),
                        handler: 'onClickDirectoryInfo'
                    },
                    '-',
                    {
                        text: message.msg('common.refresh'),
                        iconCls: 'common-refresh',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.refresh'),
                        handler: 'onClickRefresh'
                    },
                    '-',
                    {
                        text: message.msg('fs.hdfs.common.setPermission'),
                        iconCls: 'common-user-auth',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.permission'),
                        handler: 'onClickPermission'
                    },
                    '-',
                    {
                        text: message.msg('fs.hdfs.common.createHiveDB'),
                        iconCls: 'common-database-add',
                        itemId: 'createHiveDB',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.hiveDB'),
                        handler: 'onClickCreateHiveDB'
                    },
                    {
                        text: message.msg('fs.hdfs.common.createHiveTable'),
                        iconCls: 'common-table-add',
                        itemId: 'createHiveTable',
                        tooltip: message.msg('fs.hdfs.directory.menu.tip.hiveTable'),
                        handler: 'onClickCreateHiveTable'
                    }
                ]
            });
        }
        me.contextDirectoryMenu.showAt(event.pageX - 5, event.pageY - 5);

        var statusBar = query('browser > _statusBar');

        if (record.data.id == 'root' || record.data.id == '/') {
            statusBar.setStatus(message.msg('common.path') + ' : ' + '/');
        } else {
            statusBar.setStatus(message.msg('common.path') + ' : ' + record.data.fullyQualifiedPath);
        }
    },

    /**
     * 선택한 경로에 디렉토리를 생성한다.
     */
    onClickCreateDirectory: function () {
        var me = this;
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');

        Ext.MessageBox.show({
                title: message.msg('fs.hdfs.directory.title.create'),
                message: message.msg('fs.hdfs.directory.msg.create'),
                width: 300,
                prompt: true,
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.INFO,
                multiline: false,
                value: 'folder',
                fn: function (btn, text) {
                    if (App.Util.String.isBlank(text)) {
                        Ext.MessageBox.show({
                            title: message.msg('common.notice'),
                            message: message.msg('fs.hdfs.directory.msg.create'),
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                        return false;
                    }

                    if (btn == 'yes') {
                        var url = CONSTANTS.FS.HDFS_CREATE_DIRECTORY;
                        var params = {
                            clusterName: ENGINE.id,
                            currentPath: currentPath == 'root' ? '/' : currentPath, // 디렉토리를 생성할 경로
                            directoryName: text // 생성할 디렉토리명
                        };

                        invokePostByMap(url, params,
                            function (response) {
                                var obj = Ext.decode(response.responseText);

                                if (obj.success) {
                                    if (!ENGINE.id) {
                                        Ext.MessageBox.show({
                                            title: message.msg('common.notice'),
                                            message: message.msg('fs.hdfs.common.engine'),
                                            buttons: Ext.MessageBox.OK,
                                            icon: Ext.MessageBox.WARNING
                                        });
                                        return;
                                    }

                                    var parentNode = treeItem.getStore().getNodeById(currentPath);
                                    // 생성된 디렉토리의 현재 경로만 업데이트
                                    me.updateCurrentDirectoryStore(parentNode);
                                } else if (obj.error.cause) {
                                    error(message.msg('common.notice'), obj.error.cause);
                                } else {
                                    error(message.msg('common.notice'), obj.error.message);
                                }
                            },
                            function () {
                                Ext.MessageBox.show({
                                    title: message.msg('common.warning'),
                                    message: format(message.msg('common.failure'), config['system.admin.email']),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        );
                    }
                }
            }
        );
    },

    /**
     * 현재 디렉토리를 선택한 경로로 복사한다.
     */
    onClickCopyDirectory: function () {
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');

        if (currentPath == 'root') {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.copy.root'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        // 복사할 경로를 선택하기 위한 윈도를 생성하고 화면에 보여준다.
        Ext.create('Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser', {
            beforeCloseEvent: 'copyDirectoryBeforeClose'
        }).show();
    },

    /**
     * Directory Tree Panel에서 복사를 선택했을때 Simple Hdfs Browser의 이벤트 처리
     * @window {Object} Simple HDFS Browser
     * @record{Object} Simple HDFS Browser에서 선택한 record
     */
    onCopyDirectoryBeforeClose: function (window, record) {
        // Simple HDFS Browser에서 복사할 경로를 선택하지 않은 경우
        if (Ext.isEmpty(record)) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.copy.target'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var me = this;
        var treeItem = query('hdfsDirectoryPanel');
        var srcNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = srcNode.get('id');
        var selectedNodeName = srcNode.get('filename');
        var srcParentPath = srcNode.get('parentId');
        var dstPath = record.id == 'root' ? '/' : record.id;
        var fullyQualifiedPath;

        if (dstPath == '/') {
            fullyQualifiedPath = dstPath + selectedNodeName;
        } else {
            fullyQualifiedPath = dstPath + '/' + selectedNodeName;
        }

        if (currentPath == fullyQualifiedPath) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.copy.samePath'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        /*        if (record.id != 'root' && record.data.fullyQualifiedPath == currentPath) {
         Ext.MessageBox.show({
         title: message.msg('common.notice'),
         message: message.msg('fs.hdfs.directory.msg.copy.samePath'),
         buttons: Ext.MessageBox.OK,
         icon: Ext.MessageBox.WARNING
         });
         return false;
         }*/

        Ext.MessageBox.show({
            title: message.msg('fs.hdfs.directory.title.copy'),
            message: format(message.msg('fs.hdfs.directory.msg.copy.confirm'), selectedNodeName, dstPath),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: message.msg('fs.hdfs.file.msg.directory.copying'),
                        width: 300,
                        wait: true,
                        waitConfig: {interval: 200},
                        progress: true,
                        closable: true
                    });

                    var url = CONSTANTS.FS.HDFS_COPY_DIRECTORY;
                    var params = {
                        clusterName: ENGINE.id,
                        currentPath: currentPath, // 복사할 디렉토리의 현재 경로 (디렉토리명 포함)
                        dstPath: dstPath // 복사될 디렉토리의 목적지 경로 (디렉토리명 포함)
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                Ext.MessageBox.hide();
                                window.close();

                                if (!ENGINE.id) {
                                    Ext.MessageBox.show({
                                        title: message.msg('common.notice'),
                                        message: message.msg('fs.hdfs.common.engine'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });
                                    return;
                                }

                                var srcParentNode = '';
                                var dstParentNode = '';

                                /**
                                 * Copy directories from source path to destination path
                                 *
                                 * Case 1. /dir > /../dir
                                 * Case 2. /../dir1> /dir
                                 * Case 3. /../../dir > /../../dir
                                 */
                                if (srcParentPath == 'root') {
                                    me.updateDirectoryStore('root');
                                } else if (dstPath == '/') {
                                    me.updateDirectoryStore('root');
                                } else {
                                    srcParentNode = treeItem.getStore().getNodeById(srcParentPath);
                                    dstParentNode = treeItem.getStore().getNodeById(record.id);

                                    me.updateSubDirectoryStore(srcParentNode, dstParentNode);
                                }
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
        return false;
    },

    /**
     * 선택한 디렉토리를 다른 경로로 이동시킨다.
     */
    onClickMoveDirectory: function () {
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var srcPath = selectedNode.get('id');

        if (srcPath == 'root') {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.move.root'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        // 이동할 경로를 선택하기 위한 윈도를 생성하고 화면에 보여준다.
        Ext.create('Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser', {
            beforeCloseEvent: 'moveDirectoryBeforeClose'
        }).show();
    },

    /**
     * 선택한 디렉토리를 다른 경로로 이동시킨다.
     * Simple HDFS Browser BeforeClose Event
     */
    onMoveDirectoryBeforeClose: function (window, record) {
        // Simple HDFS Browser에서 이동할 경로를 선택하지 않은 경우
        if (Ext.isEmpty(record)) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.move.target'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var me = this;
        var treeItem = query('hdfsDirectoryPanel');
        var srcNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = srcNode.get('id');
        var selectedNodeName = srcNode.get('filename');
        var srcParentPath = srcNode.get('parentId');
        var dstPath = record.id == 'root' ? '/' : record.id;

        // 선택한 디렉토리와 이동시킬 경로가 동일한 경우
        if (currentPath == record.id || srcParentPath == record.id) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.move.samePath'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.MessageBox.show({
            title: message.msg('fs.hdfs.directory.title.move'),
            message: format(message.msg('fs.hdfs.directory.msg.move.confirm'), selectedNodeName, record.id),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    var url = CONSTANTS.FS.HDFS_MOVE_DIRECTORY;
                    var params = {
                        clusterName: ENGINE.id,
                        currentPath: currentPath, // 이동할 디렉토리의 현재 경로 (디렉토리명 포함)
                        dstPath: dstPath // 이동될 디렉토리의 전체 경로
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                window.close();

                                if (!ENGINE.id) {
                                    Ext.MessageBox.show({
                                        title: message.msg('common.notice'),
                                        message: message.msg('fs.hdfs.common.engine'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });
                                    return false;
                                }

                                var srcParentNode = '';
                                var dstParentNode = '';

                                /**
                                 * Move directories from source path to destination path
                                 *
                                 * Case 1. /dir > /../dir
                                 * Case 2. /../dir > /dir
                                 * Case 3. /../../dir > /../../dir
                                 */
                                if (srcParentPath == 'root') {
                                    me.updateDirectoryStore('root');
                                } else if (dstPath == '/') {
                                    me.updateDirectoryStore('root');
                                } else {
                                    srcParentNode = treeItem.getStore().getNodeById(srcParentPath);
                                    dstParentNode = treeItem.getStore().getNodeById(record.id);

                                    me.updateSubDirectoryStore(srcParentNode, dstParentNode);
                                }
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                                return false;
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                                return false;
                            }
                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                            return false;
                        }
                    );
                }
            }
        });
        return false;
    },

    /**
     * 현재 디렉토리명을 변경한다.
     */
    onClickRenameDirectory: function () {
        var me = this;
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');

        if (currentPath == 'root') {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.rename.root'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var selectedNodeName = selectedNode.get('text');

        Ext.MessageBox.show({
            title: message.msg('fs.hdfs.directory.title.rename'),
            message: message.msg('fs.hdfs.directory.msg.rename.confirm'),
            width: 300,
            prompt: true,
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            multiline: false,
            value: selectedNodeName,
            fn: function (btn, text) {
                // 입력한 이름 뒤에 공백 제거
                if (trim(text) == selectedNodeName) {
                    return;
                }

                if (btn == 'yes' && selectedNodeName != text && !Ext.isEmpty(text)) {
                    var url = CONSTANTS.FS.HDFS_RENAME_DIRECTORY;
                    var params = {
                        clusterName: ENGINE.id,
                        currentPath: currentPath, // 변경할 디렉토리명이 포함된 전체 경로
                        directoryName: text // 변경할 디렉토리명
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                if (!ENGINE.id) {
                                    Ext.MessageBox.show({
                                        title: message.msg('common.notice'),
                                        message: message.msg('fs.hdfs.common.engine'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });
                                    return;
                                }

                                var parentNode = treeItem.getStore().getNodeById(selectedNode.get('parentId'));
                                // 이름변경된 디렉토리의 상위 경로만 업데이트
                                me.updateCurrentDirectoryStore(parentNode);
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                } else {
                    me.getView().close();
                }
            }
        });
    },

    /**
     * 선택한 디렉토리를 삭제한다.
     */
    onClickDeleteDirectory: function () {
        var me = this;
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');
        var parentPath = selectedNode.get('parentId') == 'root' ? '/' : selectedNode.get('parentId');

        if (currentPath == 'root') {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.delete.root'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.MessageBox.show({
            title: message.msg('fs.hdfs.directory.title.delete'),
            message: format(message.msg('fs.hdfs.directory.msg.delete.confirm'), currentPath),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    var url = CONSTANTS.FS.HDFS_DELETE_DIRECTORY;
                    var params = {
                        clusterName: ENGINE.id,
                        currentPath: currentPath, // 삭제할 디렉토리 경로 (디렉토리명 포함)
                        parentPath: parentPath // 삭제할 디렉토리의 상위 경로
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                if (!ENGINE.id) {
                                    Ext.MessageBox.show({
                                        title: message.msg('common.notice'),
                                        message: message.msg('fs.hdfs.common.engine'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });
                                    return;
                                }

                                var parentNode = '';

                                if (parentPath == '/') {
                                    parentNode = treeItem.getStore().getNodeById('root');
                                } else {
                                    parentNode = treeItem.getStore().getNodeById(parentPath);
                                }

                                // 삭제된 디렉토리의 상위 경로만 업데이트
                                me.updateCurrentDirectoryStore(parentNode);
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 선택한 디렉토리에 존재하는 모든 파일을 병합해서 상위 경로에 저장한다.
     * 만약 선택한 디렉토리가 root일 경우 '/mergedFile' 형태로 저장된다.
     */
    onClickMergeFile: function () {
        var me = this;
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var selectedNodePath = selectedNode.get('id');
        var filenameToMerge = selectedNodePath == 'root' ? 'mergedFile' : selectedNode.get('filename');

        Ext.MessageBox.show({
            title: message.msg('fs.hdfs.directory.title.merge'),
            message: message.msg('fs.hdfs.directory.msg.merge'),
            width: 300,
            prompt: true,
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.INFO,
            multiline: false,
            value: filenameToMerge,
            fn: function (btn, text) {
                if (App.Util.String.isBlank(text)) {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: message.msg('fs.hdfs.directory.msg.merge'),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                    return false;
                }

                if (btn == 'yes') {
                    var parentNodePath = '';
                    var mergedFilePath = '';
                    var selectedNodeName = '';

                    // 현재 노드가 루트(/)일 때 병합된 파일의 저장 위치는 루트(/)로 설정
                    if (selectedNodePath == 'root') {
                        mergedFilePath = CONSTANTS.ROOT + text;
                        selectedNodeName = CONSTANTS.ROOT;
                    } else { // 병합된 파일의 저장 경로는 상위 부모 디렉토리(mergedFile)에 저장함.
                        parentNodePath = selectedNode.get('parentId');
                        mergedFilePath = parentNodePath == 'root' ? '/' + text : parentNodePath + '/' + text;
                        selectedNodeName = selectedNodePath;
                    }

                    var url = CONSTANTS.FS.HDFS_GET_MERGE_FILE;
                    var params = {
                        clusterName: ENGINE.id,
                        currentPath: selectedNodeName,
                        dstPath: mergedFilePath
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                if (!ENGINE.id) {
                                    Ext.MessageBox.show({
                                        title: message.msg('common.notice'),
                                        message: message.msg('fs.hdfs.common.engine'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });
                                    return;
                                }

                                // 병합된 디렉토리의 현재 경로만 업데이트
                                me.updateFileStore(selectedNodePath);
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 선택한 디렉토리의 정보를 보여준다.
     */
    onClickDirectoryInfo: function () {
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');

        /*
         if (selectedNode.data.text == '/') {
         Ext.MessageBox.show({
         title: message.msg('common.notice'),
         message: message.msg('fs.hdfs.directory.msg.info.root'),
         buttons: Ext.MessageBox.OK,
         icon: Ext.MessageBox.WARNING
         });
         return false;
         }
         */

        var url = CONSTANTS.FS.HDFS_GET_DIRECTORY_INFO;
        var params = {
            clusterName: ENGINE.id,
            currentPath: currentPath == 'root' ? '/' : currentPath
        };

        invokeGet(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    Ext.create('Flamingo2.view.fs.hdfs.property.HdfsPropertyWindow', {
                        title: message.msg('fs.hdfs.property.title.Directory'),
                        propertyData: obj.map
                    }).show();
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function () {
                Ext.MessageBox.show({
                    title: message.msg('common.warning'),
                    message: format(message.msg('common.failure'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    },

    /**
     * 선택한 디렉토리에 포함된 하위 디렉토리만 업데이트한다.
     */
    onClickRefresh: function () {
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');
        var node = treeItem.getStore().getNodeById(currentPath);

        if (!node.data.expanded) {
            node.data.expanded = true;
        }

        if (ENGINE.id) {
            treeItem.getStore().load({
                clusterName: ENGINE.id,
                node: node
            });
        }

        var fileStore = query('hdfsFilePanel').getStore();

        fileStore.load({
            params: {
                node: node.data.id == 'root' ? '/' : node.data.id,
                clusterName: ENGINE.id
            }
        });

        var statusBar = query('browser > _statusBar');

        if (node.data.id == 'root') {
            statusBar.setStatus(message.msg('common.path') + ' : ' + '/');
        } else {
            statusBar.setStatus(message.msg('common.path') + ' : ' + node.data.fullyQualifiedPath);
        }
    },

    /**
     * 선택한 디렉토리의 접근 권한을 설정한다.
     */
    onClickPermission: function () {
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');

        if (selectedNode.data.text == '/') {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.permission.root'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var url = CONSTANTS.FS.HDFS_GET_DIRECTORY_INFO;
        var params = {
            clusterName: ENGINE.id,
            currentPath: currentPath == 'root' ? '/' : currentPath
        };

        invokeGet(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    // 사용자의 접근 권한을 설정할 수 있는 창을 생성하고 화면에 보여준다.
                    Ext.create('Flamingo2.view.fs.hdfs.permission.HdfsPermissionWindow', {
                        permissionData: obj.map,
                        fileStatus: obj.map['isFile']
                    }).show();
                } else if (obj.error.cause) {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function () {
                Ext.MessageBox.show({
                    title: message.msg('common.warning'),
                    message: format(message.msg('common.failure'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    },

    /**
     * 선택한 디렉토리를 Hive Database로 변환한다.
     */
    onClickCreateHiveDB: function () {
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');
        var database = selectedNode.get('filename');

        if (currentPath == 'root') {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.hiveDB.root'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.create("Flamingo2.view.hive.metastore._CreateDatabase", {
            listeners: {
                afterrender: function () {
                    var me = this;
                    var refs = me.getReferences();
                    var params = {
                        database: database,
                        external: true,
                        location: currentPath == 'root' ? '/' : currentPath
                    };

                    refs.frmCreateDatabase.getForm().setValues(params);
                    refs.chkExternal.readOnly = true;
                    refs.txLocation.setReadOnly(true);
                    refs.browseButton.setDisabled(true);
                }
            }
        }).show();
    },

    /**
     * 선택한 디렉토리에 Hive Table을 생성한다.
     */
    onClickCreateHiveTable: function () {
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');
        var tableName = selectedNode.get('filename');

        if (currentPath == 'root') {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.hiveTable.root'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.create("Flamingo2.view.hive.metastore._CreateTable", {
            hdfs: true,
            listeners: {
                afterrender: function () {
                    var me = this;
                    var refs = me.getReferences();
                    var params = {
                        tableName: tableName,
                        tableType: 'EXTERNAL_TABLE',
                        location: currentPath
                    };

                    refs.containerDB.setVisible(true);
                    refs.tableForm.getForm().setValues(params);
                    refs.rdoManaged.readOnly = true;
                    refs.rdoExternal.readOnly = true;
                    refs.locationTextField.setReadOnly(true);
                    refs.browseButton.setDisabled(true);

                    Ext.defer(function () {
                        refs.comboDB.getStore().proxy.extraParams.clusterName = ENGINE.id;
                    }, 300);
                }
            }
        }).show();
    },

    /**
     * 파일을 다른 디렉토리로 복사한다.
     */
    onClickCopyFile: function () {
        var selectedFiles = this.getSelectedItemIds();

        // File Grid Panel에서 파일을 선택하지 않았을 경우
        if (selectedFiles.length == 0) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.file.msg.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var fromItems = [];

        for (var i = 0; i < selectedFiles.length; i++) {
            fromItems[i] = selectedFiles[i].id;
        }

        // 복사할 경로를 선택하기 위한 창을 생성하고 화면에 보여준다.
        Ext.create("Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser", {
            beforeCloseEvent: 'copyFileBeforeClose'
        }).show();
    },

    /**
     * File Grid Panel에서 복사를 선택했을때 Simple HDFS Browser의 이벤트 처리
     * @window {Object} Simple HDFS Browser
     * @record {Object} Simple HDFS Browser에서 선택한 record
     */
    onCopyFileBeforeClose: function (window, record) {
        // Simple HDFS Browser에서 복사할 파일을 선택하지 않은 경우
        if (Ext.isEmpty(record)) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.copy.target'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var dstPath = record.id == 'root' ? '/' : record.id;
        var selectedFiles = this.getSelectedItemIds();

        // 파일을 복사할 경로에 동일한 파일명이 존재하는지 확인
        if (selectedFiles[0].path == dstPath) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.copy.samePath'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var filesToCopy = [];

        for (var i = 0; i < selectedFiles.length; i++) {
            filesToCopy[i] = selectedFiles[i].id;
        }

        Ext.MessageBox.show({
            title: message.msg('fs.hdfs.file.title.copy'),
            message: format(message.msg('fs.hdfs.file.msg.copy'), selectedFiles.length, dstPath),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.INFO,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: message.msg('fs.hdfs.file.msg.copying'),
                        width: 300,
                        wait: true,
                        waitConfig: {interval: 200},
                        progress: true,
                        closable: true
                    });

                    var url = CONSTANTS.FS.HDFS_COPY_FILE;
                    var params = {
                        clusterName: ENGINE.id,
                        currentPath: selectedFiles[0].path,
                        files: filesToCopy.join(), // 복사할 파일이 있는 경로(파일명 포함)
                        dstPath: dstPath // 파일이 복사될 목적지 경로
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            Ext.MessageBox.hide();
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                window.close();
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.hide();
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 파일을 다른 디렉토리로 이동한다.
     */
    onClickMoveFile: function () {
        var selectedFiles = this.getSelectedItemIds();

        // File Grid Panel에서 파일을 선택하지 않았을 경우
        if (selectedFiles.length == 0) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.file.msg.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var fromItems = [];

        for (var i = 0; i < selectedFiles.length; i++) {
            fromItems[i] = selectedFiles[i].id;
        }

        // 이동할 경로를 선택하기 위한 윈도를 생성하고 화면에 보여준다.
        Ext.create('Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowser', {
            beforeCloseEvent: 'moveFileBeforeClose'
        }).show();
    },

    /**
     * File Grid Panel에서 이동을 선택했을때 Simple HDFS Browser의 이벤트 처리
     * @window {Object} Simple HDFS Browser
     * @record {Object} Simple HDFS Browser에서 선택한 record
     */
    onMoveFileBeforeClose: function (window, record) {
        // Simple HDFS Browser에서 이동할 파일을 선택하지 않은 경우
        if (Ext.isEmpty(record)) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.move.target'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var me = this;
        var targetPath = record.id == 'root' ? '/' : record.id;
        var selectedFiles = this.getSelectedItemIds();
        var compareToValidPath = selectedFiles[0].path == '/' ? 'root' : selectedFiles[0].path;

        // 파일을 이동할 경로에 동일한 파일명이 존재하는지 확인
        if (compareToValidPath == record.id) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.move.samePath'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var filesToMove = [];

        for (var i = 0; i < selectedFiles.length; i++) {
            filesToMove[i] = selectedFiles[i].id;
        }

        Ext.MessageBox.show({
            title: message.msg('fs.hdfs.file.title.move'),
            message: format(message.msg('fs.hdfs.file.msg.move'), filesToMove.length, record.id),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.INFO,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: message.msg('fs.hdfs.file.msg.moving'),
                        width: 300,
                        wait: true,
                        waitConfig: {interval: 200},
                        progress: true,
                        closable: true
                    });

                    var url = CONSTANTS.FS.HDFS_MOVE_FILE;
                    var params = {
                        clusterName: ENGINE.id,
                        currentPath: selectedFiles[0].path,
                        files: filesToMove.join(), // 이동할 파일이 있는 경로(파일명 포함)
                        dstPath: targetPath // 파일이 이동될 목적지 경로
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            Ext.MessageBox.hide();
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                window.close();

                                var treeItem = query('hdfsDirectoryPanel');
                                var srcParentNode = '';
                                var dstParentNode = '';

                                /**
                                 * Move files from source path destination path
                                 *
                                 * Case 1. /files > /../files
                                 * Case 2. /../files > /files
                                 * Case 3. /../../files > /../../files
                                 */
                                if (targetPath == '/') {
                                    me.updateDirectoryStore('root');
                                } else if (record.id == 'root') {
                                    me.updateDirectoryStore('root');
                                } else {
                                    // 이동된 파일이 있던 현재 경로만 업데이트
                                    srcParentNode = treeItem.getStore().getNodeById(compareToValidPath);
                                    dstParentNode = treeItem.getStore().getNodeById(record.id);
                                    me.updateSubDirectoryStore(srcParentNode, dstParentNode);
                                    me.updateFileStore(compareToValidPath);
                                }
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.hide();
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 선택한 파일의 이름을 변경한다.
     */
    onClickRenameFile: function () {
        var me = this;
        var gridItem = query('hdfsFilePanel');
        var selectedFiles = this.getSelectedItemIds();
        var selectedFile = gridItem.getSelectionModel().getLastSelected();

        if (selectedFiles.length < 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.file.msg.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        } else if (selectedFiles.length > 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.common.file.limit'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var selectedFilename = selectedFile.get('filename');

        Ext.MessageBox.show({
            title: message.msg('fs.hdfs.file.title.rename'),
            message: message.msg('fs.hdfs.file.msg.rename.input'),
            width: 300,
            prompt: true,
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            multiline: false,
            closable: false,
            value: selectedFilename,
            fn: function (btn, text) {
                // 입력한 이름 뒤에 공백 제거
                if (trim(text) == selectedFilename) return;

                if (btn == 'yes' && selectedFilename != text && !Ext.isEmpty(text)) {
                    var url = CONSTANTS.FS.HDFS_RENAME_FILE;
                    var params = {
                        clusterName: ENGINE.id,
                        srcPath: selectedFile.get('id'),
                        dirPath: selectedFile.get('path'), // 현재 경로에 대한 권한 검사에 사용
                        filename: text
                    };

                    invokePostByMap(url, params, function (response) {
                        var obj = Ext.decode(response.responseText);

                        if (obj.success) {
                            me.updateFileStore(params.dirPath);
                        } else if (obj.error.cause) {
                            Ext.MessageBox.show({
                                title: message.msg('common.notice'),
                                message: obj.error.cause,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: message.msg('common.notice'),
                                message: obj.error.message,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    }, function () {
                        Ext.MessageBox.show({
                            title: message.msg('common.warning'),
                            message: format(message.msg('common.failure'), config['system.admin.email']),
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    });
                }
            }
        });
    },

    /**
     * 선택한 파일을 삭제한다.
     */
    onClickDeleteFile: function () {
        var me = this;
        var selectedFiles = this.getSelectedItemIds();

        // File Grid Panel에서 파일을 선택하지 않았을 경우
        if (selectedFiles.length == 0) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.file.msg.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var fromItems = [];

        for (var i = 0; i < selectedFiles.length; i++) {
            fromItems[i] = selectedFiles[i].id;
        }

        Ext.MessageBox.show({
            title: message.msg('fs.hdfs.file.title.delete'),
            message: format(message.msg('fs.hdfs.file.msg.delete'), selectedFiles.length),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: message.msg('fs.hdfs.file.msg.deleting'),
                        width: 300,
                        wait: true,
                        waitConfig: {interval: 200},
                        progress: true,
                        closable: true
                    });

                    var url = CONSTANTS.FS.HDFS_DELETE_FILE;
                    var params = {
                        clusterName: ENGINE.id,
                        srcPath: selectedFiles[0].path,
                        files: fromItems.join()
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                Ext.MessageBox.hide();
                                me.updateFileStore(params.srcPath);
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 선택한 파일의 상세 정보를 표시한다.
     */
    onClickFileInfo: function () {
        var selectedFiles = this.getSelectedItemIds();

        if (selectedFiles.length > 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.common.file.limit'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var gridItem = query('hdfsFilePanel');
        var selectedFile = gridItem.getSelectionModel().getLastSelected();
        var filePath = selectedFile.get('id');
        var url = CONSTANTS.FS.HDFS_GET_FILE_INFO;
        var params = {
            clusterName: ENGINE.id,
            filePath: filePath
        };

        invokeGet(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    Ext.create('Flamingo2.view.fs.hdfs.property.HdfsPropertyWindow', {
                        title: message.msg('fs.hdfs.property.title.File'),
                        propertyData: obj.map
                    }).show();
                } else if (obj.error.cause) {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function () {
                Ext.MessageBox.show({
                    title: message.msg('common.warning'),
                    message: format(message.msg('common.failure'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        )
    },

    /**
     * 선택한 경로에 파일을 업로드한다.
     */
    onClickUploadFile: function () {
        var me = this;
        var treeItem = query('hdfsDirectoryPanel');
        var selectedNode = treeItem.getSelectionModel().getLastSelected();
        var currentPath = selectedNode.get('id');

        Ext.create('Ext.window.Window', {
            title: message.msg('fs.hdfs.file.title.upload'),
            layout: 'fit',
            border: false,
            modal: true,
            closeAction: 'destroy',
            items: [
                Ext.create('Flamingo2.view.fs.hdfs.MultiFileUpload', {
                    uploadPath: currentPath == 'root' ? '/' : currentPath
                })
            ],
            listeners: {
                close: function () {
                    me.updateFileStore(currentPath);
                }
            }
        }).center().show();
    },

    /**
     * File Grid Panel에서 선택한 파일을 로컬로 다운로드한다.
     */
    onClickDownloadFile: function () {
        var selectedFiles = this.getSelectedItemIds();

        if (!selectedFiles) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.file.msg.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        if (selectedFiles.length > 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.common.file.limit'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        if (selectedFiles[0].length > config['file.download.max.size']) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: format(message.msg('fs.hdfs.file.msg.download.max'), Ext.util.Format.fileSize(config['file.download.max.size'])),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var params = {
            clusterName: ENGINE.id,
            username: SESSION.USERNAME,
            srcPath: selectedFiles[0].path, // 다운로드 할 파일이 위치한 경로
            fullyQualifiedPath: selectedFiles[0].id // 다운로드 할 파일이 위치한 경로 (파일명 포함)
        };

        Ext.dom.Helper.append(document.body, {
                tag: 'iframe',
                id: 'testIframe' + new Date().getTime(),
                css: 'display:none;visibility:hidden;height:0px;',
                src: CONSTANTS.FS.HDFS_DOWNLOAD_FILE
                + "?&clusterName=" + params.clusterName
                + "&username=" + params.username
                + "&srcPath=" + params.srcPath
                + "&fullyQualifiedPath=" + params.fullyQualifiedPath,
                frameBorder: 0,
                width: 0,
                height: 0
            }
        );
    },

    /**
     * 선택한 파일의 내용을 지정된 크기만큼 페이지 단위로 보여준다.
     */
    onClickViewFile: function () {
        var selectedFiles = this.getSelectedItemIds();

        if (selectedFiles.length < 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.file.msg.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        } else if (selectedFiles.length > 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.common.file.limit'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var currentPath = selectedFiles[0].path;
        var filePath = selectedFiles[0].id;
        var fileExtension = config['hdfs.viewFile.limit.type'];
        var extensionPattern = new RegExp(fileExtension, 'g');

        if (filePath.match(extensionPattern)) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.file.msg.viewFile'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var url = CONSTANTS.FS.HDFS_GET_DEFAULT_FILE_CONTENTS;
        var params = {
            clusterName: ENGINE.id,
            currentPath: currentPath, // 파일이 위치한 경로
            filePath: filePath, // 파일이 위치한 경로 (파일명 포함)
            chunkSizeToView: config['hdfs.viewFile.default.chunkSize'], // DEFAULT_CHUNK_SIZE = 10000
            startOffset: 0,
            currentPage: 0,
            buttonType: 'defaultPage',
            bestNode: ''
        };

        Ext.MessageBox.show({
            title: message.msg('common.notice'),
            message: message.msg('fs.hdfs.file.msg.view'),
            width: 300,
            wait: true,
            waitConfig: {interval: 200},
            progress: true,
            closable: true
        });

        invokePostByMap(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    Ext.MessageBox.hide();

                    // 파일 내용 보기 창을 화면에 표시한다.
                    Ext.create('Flamingo2.view.fs.hdfs.viewer.FileViewerWindow', {
                        propertyData: obj.map,
                        emptyPageData: {
                            total: obj.map['totalPage'],
                            currentPage: obj.map['currentPage']
                        }
                    }).center().show();
                } else if (obj.error.cause) {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function () {
                Ext.MessageBox.show({
                    title: message.msg('common.warning'),
                    message: format(message.msg('common.failure'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    },

    /**
     * 선택한 파일 들의 소유권 및 권한 정보를 수정한다.
     */
    onClickFilePermission: function () {
        var selectedFiles = this.getSelectedItemIds();

        if (selectedFiles.length < 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.file.msg.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var gridItem = query('hdfsFilePanel');
        var selectedFile = gridItem.getSelectionModel().getLastSelected();
        var filePath = selectedFile.get('id');

        if (filePath == '/' || filePath == 'root') {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('fs.hdfs.directory.msg.permission.root'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        // 단일 파일만 권한을 변경할 경우 파일의 권한 정보를 가져온다.
        if (selectedFiles.length == 1) {
            var url = CONSTANTS.FS.HDFS_GET_FILE_INFO;
            var params = {
                clusterName: ENGINE.id,
                filePath: filePath
            };

            invokeGet(url, params,
                function (response) {
                    var obj = Ext.decode(response.responseText);

                    if (obj.success) {
                        Ext.create('Flamingo2.view.fs.hdfs.permission.HdfsPermissionWindow', {
                            permissionData: obj.map,
                            fileStatus: obj.map['isFile'],
                            height: 320,
                            listeners: {
                                beforerender: function () {
                                    var me = this;
                                    var refs = me.getReferences();

                                    refs.group.setStyle('margin-bottom', '10px');
                                    refs.otherCheckGroup.setStyle('margin-bottom', '15px');
                                    refs.recursiveOwner.setHidden(true);
                                    refs.recursivePermission.setHidden(true);
                                }
                            }
                        }).show();
                    } else if (obj.error.cause) {
                        Ext.MessageBox.show({
                            title: message.msg('common.notice'),
                            message: obj.error.cause,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: message.msg('common.notice'),
                            message: obj.error.message,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    }
                },
                function () {
                    Ext.MessageBox.show({
                        title: message.msg('common.warning'),
                        message: format(message.msg('common.failure'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            )
        } else {
            Ext.create('Flamingo2.view.fs.hdfs.permission.HdfsPermissionWindow', {
                permissionData: selectedFiles.join(),
                fileStatus: true,
                height: 320,
                listeners: {
                    beforerender: function () {
                        var me = this;
                        var refs = me.getReferences();

                        refs.group.setStyle('margin-bottom', '10px');
                        refs.otherCheckGroup.setStyle('margin-bottom', '15px');
                        refs.recursiveOwner.setHidden(true);
                        refs.recursivePermission.setHidden(true);
                    }
                }
            }).show();
        }
    },

    /**
     * File 목록에서 파일을 마우스 우클릭 한 경우 Context Menu를 표시한다.
     */
    onFileItemContextMenu: function (grid, record, item, index, event) {
        var me = this;
        event.stopEvent();

        if (Ext.isEmpty(me.contextFileMenu)) {
            me.contextFileMenu = new Ext.menu.Menu({
                items: [
                    {
                        text: message.msg('fs.hdfs.common.viewFile'),
                        iconCls: 'common-file-view',
                        itemId: 'viewFileContents',
                        handler: 'onClickViewFile',
                        scope: me,
                        record: record,
                        tooltip: message.msg('fs.hdfs.file.panel.tip.viewFile')
                    },
                    {
                        text: message.msg('fs.hdfs.common.file.information'),
                        iconCls: 'common-information',
                        itemId: 'fileInfo',
                        handler: 'onClickFileInfo',
                        scope: me,
                        record: record,
                        tooltip: message.msg('fs.hdfs.file.panel.tip.info')
                    }
                ]
            });
        }
        me.contextFileMenu.showAt(event.pageX - 5, event.pageY - 5);
        me.onFileItemClick(item, record);
    },

    /**
     * 파일을 선택했을 때 파일의 전체경로를 상태바에 표시한다.
     * @param view
     * @param record
     */
    onFileItemClick: function (view, record) {
        var fullyQualifiedFilename = '';
        var statusBar = query('browser > _statusBar');

        if (record.get('path') == '/') {
            fullyQualifiedFilename = message.msg('common.path') + ' : /' + record.get('filename');
        } else {
            fullyQualifiedFilename = message.msg('common.path') + ' : ' + record.get('path') + '/' + record.get('filename');
        }

        statusBar.setStatus(fullyQualifiedFilename);
    },

    /**
     * 파일 목록에서 선택한 모든 파일 목록을 반환한다.
     */
    getSelectedItemIds: function () {
        var gridItem = query('hdfsFilePanel');
        var checkedFiles = gridItem.getSelectionModel().getSelection();
        var list = [];

        for (var i = 0; i <= checkedFiles.length - 1; i++) {
            var file = {};
            file.id = checkedFiles[i].get('id');
            file.path = checkedFiles[i].get('path');
            file.name = checkedFiles[i].get('filename');
            file.length = checkedFiles[i].get('length');

            list.push(file);
        }

        return list;
    }
});
