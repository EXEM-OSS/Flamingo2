/*
 * Copyright (C) 2011  Flamingo Project (http://www.cloudine.io).
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

Ext.define('Flamingo2.view.fs.hdfs.simple.SimpleHdfsFileBrowserController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.simpleHdfsFileBrowserController',

    /**
     * 디렉토리 및 파일 목록을 가져온다.
     */
    onAfterRender: function () {
        var me = this;
        var directoryStore = me.getViewModel().getStore('directoryStore');

        directoryStore.proxy.extraParams.clusterName = ENGINE.id;
        directoryStore.load({
            callback: function () {
                directoryStore.getRootNode().expand();
            }
        });
    },

    /**
     * 디렉토리 패널에서 마우스 우클릭 이벤트를 처리한다.
     * @param tree
     * @param record
     * @param item
     * @param index
     * @param e
     */
    onTrpDirectoryItemcontextmenu: function (tree, record, item, index, e) {
        e.stopEvent();
    },

    /**
     * 디렉토리를 선택했을 때 파일 목록 정보를 업데이트한다.
     */
    onClickDirectoryItem: function (view, node) {
        var refs = this.getReferences();
        var fileStore = refs.hdfsFileGrid.getStore();

        fileStore.load({
            params: {
                node: node.data.id,
                clusterName: ENGINE.id
            }
        });
    },

    /**
     * 선택한 파일 정보를 부모 뷰컨트롤러에 전달한다.
     */
    onBtnOkClick: function () {
        var refs = this.getReferences();
        var record = refs.hdfsFileGrid.getSelectionModel().getSelection()[0];

        if (Ext.isEmpty(record)) {
            Ext.MessageBox.show({
                text: message.msg('common.ok'),
                message: message.msg('fs.hdfs.file.msg.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        this.fireEvent('hdfsFileClose', record);
        this.getView().close();
    },

    onBtnCancelClick: function () {
        this.getView().close();
    }
});