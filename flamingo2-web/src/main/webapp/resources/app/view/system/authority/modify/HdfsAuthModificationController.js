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
Ext.define('Flamingo2.view.system.authority.HdfsAuthModificationController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hdfsAuthModificationViewController',

    /**
     * 사용자 권한 및 사용자 등급 목록을 가져온다.
     */
    onComboAfterRender: function () {
        var userAuthCombo = query('hdfsAuthModificationForm #userAuthCombo');
        setTimeout(function () {
            userAuthCombo.getStore().load()
        }, 10);

        var userLevelCombo = query('hdfsAuthModificationForm #userLevelCombo');
        setTimeout(function () {
            userLevelCombo.getStore().load()
        }, 10);
    },

    /**
     * 수정한 권한 정보를 저정한다.
     */
    onSaveHdfsAuthOK: function () {
        var me = this;
        var refs = me.getReferences();
        var hdfsBrowserAuthGrid = query('hdfsBrowserAuthGridPanel');
        var selectedItem = hdfsBrowserAuthGrid.getSelectionModel().getLastSelected();
        var hdfsPathPattern = selectedItem.get('hdfs_path_pattern');
        var authId = selectedItem.get('auth_id');
        var level = selectedItem.get('level');

        if (!refs.hdfsAuthModificationForm.getForm().isValid()) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.authority.msg.valid.add'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        if (refs.newHdfsPathPattern.lastValue) {
            if (refs.hdfsPathPattern.lastValue == hdfsPathPattern) {
                refs.newHdfsPathPattern.setValue('');
            }
        }

        if (refs.newUserAuth.lastValue) {
            if (refs.userAuthCombo.lastValue == authId) {
                refs.newUserAuth.setValue('');
            }
        }

        if (refs.newUserLevel.lastValue) {
            if (refs.userLevelCombo.lastValue == level) {
                refs.newUserLevel.setValue('');
            }
        }

        var hdfsAuthModFormValues = refs.hdfsAuthModificationForm.getForm().getValues();
        var url = CONSTANTS.SYSTEM.AUTHORITY.UPDATE_HDFS_BROWSER_AUTH;
        var params = {
            hdfsAuthModFormValues: hdfsAuthModFormValues
        };

        invokePostByMap(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    me.getView().close();
                    me.onRefreshHdfsBrowserAuthGrid();
                } else if (obj.error.cause) {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.INFO
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
     * 소속 삭제창을 종료한다.
     */
    onSaveHdfsAuthCancel: function () {
        this.getView().close();
    },

    /**
     * HDFS Browser Authority Grid 목록을 갱신한다.
     */
    onRefreshHdfsBrowserAuthGrid: function () {
        var hdfsBrowserAuthForm = query('hdfsBrowserAuthFormPanel');
        var hdfsBrowserAuthGrid = query('hdfsBrowserAuthGridPanel');

        hdfsBrowserAuthForm.getForm().reset();
        setTimeout(function () {
            hdfsBrowserAuthGrid.getStore().removeAll();
            hdfsBrowserAuthGrid.getStore().load();
        }, 10);
    }
});