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
Ext.define('Flamingo2.view.system.authority.HdfsBrowserAuthController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hdfsBrowserAuthViewController',

    /**
     * EngineComboBox Change Event
     */
    onEngineChanged: function () {
        var hdfsBrowserTreeItem = query('hdfsBrowserTreePanel');
        hdfsBrowserTreeItem.getStore().getProxy().extraParams.clusterName = ENGINE.id;
        this.onRefreshHdfsBrowserTree();
    },

    /**
     * HDFS Browser 경로에 설정된 상세 권한 필드를 ReadOnly 속성으로 변경한다.
     */
    onBeforeRender: function () {
        var me = this;
        var refs = me.getReferences();
        refs.hdfsBrowserAuthForm.getForm().getFields().each(function (field) {
            field.setReadOnly(1);
        });
    },

    /**
     * 디렉토리 트리를 화면에 표시한 후 서버에서 디렉토리 목록을 가져온다.
     *
     */
    onAfterRender: function () {
        var hdfsBrowserTree = query('hdfsBrowserTreePanel');
        setTimeout(function () {
            hdfsBrowserTree.getStore().proxy.extraParams.clusterName = ENGINE.id;
            hdfsBrowserTree.getStore().load({
                callback: function () {
                    hdfsBrowserTree.getRootNode().expand();
                    var rootNode = hdfsBrowserTree.getStore().getNodeById('root');
                    hdfsBrowserTree.getSelectionModel().select(rootNode);
                }
            });
        }, 500);

        var hdfsBrowserAuthGrid = query('hdfsBrowserAuthGridPanel');
        setTimeout(function () {
            hdfsBrowserAuthGrid.getStore().load();
        }, 10);
    },

    /**
     * HDFS Tree 목록을 갱신한다.
     */
    onRefreshHdfsBrowserTree: function () {
        var hdfsBrowserTreeItem = query('hdfsBrowserTreePanel');
        setTimeout(function () {
            hdfsBrowserTreeItem.getStore().proxy.extraParams.clusterName = ENGINE.id;
            hdfsBrowserTreeItem.getStore().load({
                callback: function () {
                    hdfsBrowserTreeItem.getRootNode().expand();
                    var rootNode = hdfsBrowserTreeItem.getStore().getNodeById('root');
                    hdfsBrowserTreeItem.getSelectionModel().select(rootNode);
                }
            });
        }, 500);
    },

    /**
     * HDFS Tree에서 선택한 경로에 권한을 추가한다.
     */
    onAddHdfsBrowserAuth: function () {
        var hdfsBrowserTreeItem = query('hdfsBrowserTreePanel');
        var selectedNode = hdfsBrowserTreeItem.getSelectionModel().getLastSelected();

        if (!selectedNode) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.authority.msg.add'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.create('Flamingo2.view.system.authority.HdfsAuthRegisterWindow', {
            buttons: [
                {
                    text: message.msg('common.add'),
                    iconCls: 'common-add',
                    handler: 'onAddHdfsAuthOK'
                },
                {
                    text: message.msg('common.cancel'),
                    iconCls: 'common-cancel',
                    handler: 'onAddHdfsAuthCancel'
                }
            ],
            listeners: {
                beforerender: function () {
                    var me = this;
                    var refs = me.getReferences();
                    var currentPath = selectedNode.get('id') == 'root' ? '/' : selectedNode.get('id');
                    refs.hdfsPathPattern.rawValue = currentPath;
                }
            }
        }).center().show();
    },

    /**
     * HDFS Auth Grid에서 선택한 패턴의 상세 정보를 사용 권한 정보폼에 보여준다.
     */
    onClickGridItem: function () {
        var me = this;
        var refs = me.getReferences();
        var hdfsBrowserAuthGrid = query('hdfsBrowserAuthGridPanel');
        var selectedItem = hdfsBrowserAuthGrid.getSelectionModel().getLastSelected();
        var url = CONSTANTS.SYSTEM.AUTHORITY.GET_HDFS_BROWSER_AUTH_DETAIL;
        var params = {
            hdfsPathPattern: selectedItem.get('hdfs_path_pattern'),
            authId: selectedItem.get('auth_id'),
            level: selectedItem.get('level')
        };

        invokePostByMap(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    refs.hdfsBrowserAuthForm.getForm().setValues(Ext.merge(obj.map));
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
                    title: message.msg('common.notice'),
                    message: format(message.msg('system.user.msg.delete.limit'), refs.orgCombo.selection.get('org_name')),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    },

    /**
     * HDFS Browser Authority Grid 목록을 갱신한다.
     */
    onRefreshHdfsBrowserAuthGrid: function () {
        var me = this;
        var refs = me.getReferences();
        var hdfsBrowserAuthGrid = query('hdfsBrowserAuthGridPanel');

        refs.hdfsBrowserAuthForm.getForm().reset();
        setTimeout(function () {
            hdfsBrowserAuthGrid.getStore().removeAll();
            hdfsBrowserAuthGrid.getStore().load();
        }, 10);
    },

    /**
     * HDFS 권한 목록에서 선택한 경로를 삭제한다.
     */
    onDeleteHdfsBrowserAuth: function () {
        var me = this;
        var hdfsBrowserAuthGrid = query('hdfsBrowserAuthGridPanel');
        var selectedItem = hdfsBrowserAuthGrid.getSelectionModel().getLastSelected();

        if (!selectedItem) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.authority.msg.delete'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.MessageBox.show({
            title: message.msg('system.authority.delete'),
            message: format(message.msg('system.authority.msg.delete.yesNo'), selectedItem.get('hdfs_path_pattern')),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    var url = CONSTANTS.SYSTEM.AUTHORITY.DELETE_HDFS_BROWSER_AUTH;
                    var params = {
                        hdfsPathPattern: selectedItem.get('hdfs_path_pattern'),
                        authId: selectedItem.get('auth_id'),
                        level: selectedItem.get('level')
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                me.onRefreshHdfsBrowserAuthGrid();
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
     * 선택한 HDFS Browser의 경로에 설정된 권한 정보를 수정한다.
     */
    onModifyHdfsBrowserAuth: function () {
        var me = this;
        var refs = me.getReferences();
        var hdfsBrowserAuthGrid = query('hdfsBrowserAuthGridPanel');
        var selectedItem = hdfsBrowserAuthGrid.getSelectionModel().getLastSelected();
        var hdfsAuthFormValues = refs.hdfsBrowserAuthForm.getValues();

        if (!selectedItem) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.authority.msg.modify'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.create('Flamingo2.view.system.authority.HdfsAuthModificationWindow', {
            buttons: [
                {
                    text: message.msg('common.save'),
                    iconCls: 'common-save',
                    handler: 'onSaveHdfsAuthOK'
                },
                {
                    text: message.msg('common.cancel'),
                    iconCls: 'common-cancel',
                    handler: 'onSaveHdfsAuthCancel'
                }
            ],
            listeners: {
                beforerender: function () {
                    var me = this;
                    var refs = me.getReferences();

                    refs.hdfsAuthModificationForm.getForm().setValues(Ext.merge(hdfsAuthFormValues));
                }
            }
        }).center().show();
    }
});