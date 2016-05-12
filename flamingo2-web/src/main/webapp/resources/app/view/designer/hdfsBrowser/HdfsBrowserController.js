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
Ext.define('Flamingo2.view.designer.HdfsBrowserController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.designerHdfsBrowserController',

    init: function () {
        this.control({
            'hdfsDirectoryPanelForDesigner': {
                itemclick: this.onDirectoryClick
            },
            'hdfsDirectoryPanelForDesigner #refreshButton': {
                click: this.onRefreshClick
            },
            'hdfsFilePanelForDesigner > grid': {
                itemclick: this.onFileClick
            },
            'hdfsFilePanelForDesigner #refreshButton': {
                click: this.onRefreshFileClick
            }
        });

        this.onLaunch();
    },

    onLaunch: function () {
        query('hdfsDirectoryPanelForDesigner').getStore().getRootNode().expand();
    },

    getWorkflowEngine: function () {
        return ENGINE.id;
    },

    /**
     * 디렉토리를 선택했을 때 파일 목록 정보를 업데이트한다.
     */
    onDirectoryClick: function (view, record, item, index, event, opts) {
        var directoryPanel = query('hdfsDirectoryPanelForDesigner');
        var fileStore = query('hdfsFilePanelForDesigner').getStore();
        var lastPathComp = directoryPanel.query('#lastPath')[0];
        lastPathComp.setValue(record.data.id);

        fileStore.load({
            params: {
                clusterName: ENGINE.id,
                node: record.data.id
            }
        });
    },

    /**
     * 현재 경로에서 선택한 파일 정보를 저장한다.
     */
    onFileClick: function (view, record, item, index, e, opts) {
        var directoryPanel = query('hdfsDirectoryPanelForDesigner');
        var lastPathComp = directoryPanel.query('#lastPath')[0];
        lastPathComp.setValue(record.data.id);
    },

    /**
     * Directory Panel의 Refresh 버튼을 눌렀을 경우 Root(/)를 기준으로 Tree와 Grid를 모두 갱신한다.
     */
    onRefreshClick: function () {
        this.updateDirectoryStore(this.getWorkflowEngine(), '/');
        this.updateFileStore(this.getWorkflowEngine(), '/');
    },

    /**
     * File Panel의 Refresh 버튼을 눌렀을 경우 Tree와 Grid를 모두 갱신한다.
     */
    onRefreshFileClick: function () {
        var directoryPanel = query('hdfsDirectoryPanelForDesigner');
        var lastPathComp = directoryPanel.query('#lastPath')[0];
        this.updateFileStore(this.getWorkflowEngine(), lastPathComp.getValue());
    },

    /**
     * 디렉토리 목록을 보여주는 디렉토리 목록을 갱신한다.
     */
    updateDirectoryStore: function (clusterName, path) {
        if (path == CONSTANTS.ROOT) {
            path = '/';
        }

        var directoryStore = query('hdfsDirectoryPanelForDesigner');
        var rootNode = directoryStore.getRootNode();
        var lengthOfChildNodes = rootNode.childNodes.length;

        if (lengthOfChildNodes > 0) {
            var childNodesArray = rootNode.childNodes;
            var idx = 0;
            Ext.each(childNodesArray, function () {
                rootNode.childNodes[idx].remove();
                idx++;
            });
            directoryStore.getStore().getRootNode().removeAll();
            directoryStore.getStore().load({
                params: {
                    clusterName: clusterName,
                    node: path
                }
            });
        }

        directoryStore.getRootNode().expand();
    },

    /**
     * 디렉토리 목록을 보여주는 디렉토리 목록을 갱신한다.
     */
    updateFileStore: function (clusterName, path) {
        if (path == CONSTANTS.ROOT) {
            path = '/';
        }

        var fileStore = query('hdfsFilePanelForDesigner').getStore();
        fileStore.removeAll();
        fileStore.load({
            params: {
                clusterName: clusterName,
                path: path
            }
        });
    }
});