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
Ext.define('Flamingo2.view.system.user.register.UserRegisterController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.userRegisterViewController',

    /**
     * 사용자 등록창의 소속 목록을 가져온다.
     */
    onAfterRender: function () {
        var organizationCombo = query('userForm #orgCombo');
        setTimeout(function () {
            organizationCombo.getStore().load({
                params: {
                    condition: 'list'
                }
            })
        }, 10);

        var userLevelCombo = query('userForm #userLevelCombo');
        setTimeout(function () {
            userLevelCombo.getStore().load()
        }, 10);
    },

    /**
     * 사용자 목록을 갱신한다.
     */
    onRefreshUserClick: function (orgId) {
        var usersGrid = query('userGridPanel');
        var organizationCombo = query('userGridPanel #orgCombo');
        var lastOrgComboValue = organizationCombo.lastValue;

        /**
         * 추가한 소속 정보가 사용자 관리 홈 화면의 소속 콤보 박스에 선택된 값과 다를 경우
         * 추가한 사용자의 소속 정보로 사용자 그리드 업데이트
         */
        if (lastOrgComboValue != orgId && lastOrgComboValue != 'ALL') {
            organizationCombo.setValue(orgId);
        }

        setTimeout(function () {
            usersGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    orgId: lastOrgComboValue == 'ALL' ? 0 : orgId
                }
            });
        }, 10);
    },

    /**
     * 사용자 목록에 사용하는 소속 콤보 목록을 가져온다.
     */
    onRefreshOrgCombo: function () {
        var organizationCombo = query('userGridPanel #orgCombo');
        setTimeout(function () {
            organizationCombo.getStore().load({
                params: {
                    condition: 'all'
                }
            })
        }, 10);
    },

    /**
     * 사용자를 생성한다.
     */
    onAddUserOKClick: function () {
        var me = this;
        var refs = me.getReferences();
        var password = refs.password.getValue();
        var confirmPassword = refs.confirmPassword.getValue();

        if (!refs.userForm.getForm().isValid()) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.valid.user'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        if (password != confirmPassword) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.valid.pwd'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        // 소속 콤보에서 선택한 소속의 ID를 가져온다.
        var orgId = refs.orgCombo.selection.get('org_id');
        var url = CONSTANTS.SYSTEM.USER.ADD_USER;
        var params = {
            clusterName: ENGINE.id,
            username: refs.username.getValue(),
            password: password,
            name: refs.name.getValue(),
            email: refs.email.getValue(),
            orgId: orgId,
            authId: refs.isAdmin.getValue() ? 1 : 2,
            level: refs.userLevelCombo.getValue()
        };

        invokePostByMap(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    me.getView().close();
                    me.onRefreshUserClick(orgId);
                } else if (obj.error.cause) {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: format(message.msg('system.user.msg.add.user.failed'), obj.error.cause),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.INFO
                    });
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: format(message.msg('system.user.msg.add.user.failed'), obj.error.message),
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
     * 사용자 등록창을 종료한다.
     */
    onAddUserCancelClick: function () {
        this.getView().close();
    },

    /**
     * 사용자 정보를 수정한다.
     */
    onChangeUserOKClick: function () {
        var me = this;
        var refs = me.getReferences();
        var password = refs.password.getValue();
        var confirmPassword = refs.confirmPassword.getValue();

        if (!refs.userForm.getForm().isValid()) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.valid.user'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        if (password || confirmPassword) {
            if (password != confirmPassword) {
                Ext.MessageBox.show({
                    title: message.msg('common.notice'),
                    message: message.msg('system.user.msg.valid.pwd'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
                return false;
            }
        }

        // 소속 콤보에서 선택한 소속 ID를 가져온다.
        var orgId = refs.orgCombo.selection.get('org_id');
        var url = CONSTANTS.SYSTEM.USER.UPDATE_USER_INFO;
        var params = {
            clusterName: ENGINE.id,
            username: refs.username.getValue(),
            password: escape(refs.password.getValue()),
            name: refs.name.getValue(),
            email: refs.email.getValue(),
            orgId: orgId,
            authId: refs.isAdmin.getValue() ? 1 : 2,
            level: refs.userLevelCombo.getValue()
        };

        invokePostByMap(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    me.getView().close();
                    me.onRefreshUserClick(orgId);
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
     * 사용자 정보 수정창을 종료한다.
     */
    onChangeUserCancelClick: function () {
        this.getView().close();
    }
});