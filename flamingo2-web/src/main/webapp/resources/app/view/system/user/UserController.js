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
Ext.define('Flamingo2.view.system.user.UserController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.userViewController',

    /**
     * 사용자 목록과 소속 목록을 조회한다.
     */
    onAfterRender: function () {
        var usersGrid = query('userGridPanel');
        setTimeout(function () {
            usersGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id
                }
            });
        }, 10);

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
     * 현재 선택한 소속의 사용자 목록을 갱신한다.
     */
    onRefreshUserClick: function () {
        var me = this;
        var refs = me.getReferences();
        var usersGrid = query('userGridPanel');
        var orgId = refs.orgCombo.lastValue;

        setTimeout(function () {
            usersGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    orgId: orgId == 'ALL' ? 0 : orgId
                }
            });
        }, 10);
    },

    /**
     * 입력한 모든 값이 초기화되고 모든 사용자가 표시된다.
     */
    onClearUserButton: function () {
        var me = this;
        var refs = me.getReferences();

        refs.conditionKey.setValue('NAME');
        refs.condition.setValue('');
        refs.orgCombo.setValue('ALL');

        me.onRefreshUserClick();
    },

    /**
     * 입력한 조건값에 해당하는 사용자를 검색한다.
     */
    onFindUserButton: function () {
        var me = this;
        var refs = me.getReferences();
        var usersGrid = query('userGridPanel');
        var conditionKey = refs.conditionKey.getValue();
        var condition = refs.condition.getValue();

        setTimeout(function () {
            usersGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    conditionKey: conditionKey,
                    condition: condition
                }
            });
        }, 10);
    },

    /**
     * 소속 콤보박스에서 선택한 소속에 포함된 사용자 목록을 가져온다.
     */
    onSelectComboValue: function (combo) {
        var usersGrid = query('userGridPanel');
        var orgId = combo.lastValue;

        setTimeout(function () {
            usersGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    orgId: orgId
                }
            });
        }, 10);
    },

    /**
     * 관리자가 사용자를 등록한다.
     */
    onAddUserClick: function () {
        Ext.create('Flamingo2.view.system.user.register.UserRegisterWindow', {
            buttons: [
                {
                    text: message.msg('common.add'),
                    iconCls: 'common-add',
                    handler: 'onAddUserOKClick'
                },
                {
                    text: message.msg('common.cancel'),
                    iconCls: 'common-cancel',
                    handler: 'onAddUserCancelClick'
                }
            ]
        }).center().show();
    },

    /**
     * 사용자를 삭제한다.
     */
    onDeleteUserClick: function () {
        var me = this;
        var usersGrid = query('userGridPanel');
        var selectedUser = usersGrid.getSelectionModel().getLastSelected();

        // User Grid Panel에서 사용자를 선택하지 않았을 경우
        if (!selectedUser) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.delete.user'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.MessageBox.show({
            title: message.msg('system.user.common.user.delete'),
            message: format(message.msg('system.user.msg.delete.yesNo'), selectedUser.get('username'), selectedUser.get('username')),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    var url = CONSTANTS.SYSTEM.USER.DELETE_USER;
                    var params = {
                        clusterName: ENGINE.id,
                        username: selectedUser.get('username'),
                        status: selectedUser.get('enabled'),
                        hdfsUserHome: selectedUser.get('hdfs_user_home'),
                        authId: selectedUser.get('auth_id'),
                        level: selectedUser.get('level')
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                me.onRefreshUserClick();
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
                }
            }
        });
    },

    /**
     * 관리자가 승인대기 상태인 사용자를 승인완료한다.
     */
    onAckUserClick: function () {
        var me = this;
        var usersGrid = query('userGridPanel');
        var selectedUser = usersGrid.getSelectionModel().getLastSelected();

        // User Grid Panel에서 사용자를 선택하지 않았을 경우
        if (!selectedUser) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.ack'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        // 사용자가 승인 상태 검사
        if (selectedUser.get('enabled')) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.stanby'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.MessageBox.show({
            title: message.msg('common.notice'),
            message: format(message.msg('system.user.msg.ack.yesNo'), selectedUser.get('username')),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    var url = CONSTANTS.SYSTEM.USER.ADD_ACKNOWLEDGE;
                    var params = {
                        clusterName: ENGINE.id,
                        name: selectedUser.get('name'),
                        username: selectedUser.get('username'),
                        authId: selectedUser.get('auth_id'),
                        level: selectedUser.get('level'),
                        userGroup: selectedUser.get('user_group')
                    };

                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                me.onRefreshUserClick();
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
                }
            }
        });
    },

    /**
     * 선택한 사용자의 정보를 수정한다.
     */
    onChangeUserClick: function () {
        var usersGrid = query('userGridPanel');
        var selectedUser = usersGrid.getSelectionModel().getLastSelected();

        // User Grid Panel에서 사용자를 선택하지 않았을 경우
        if (!selectedUser) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.modify.user'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var params = {
            username: selectedUser.get('username'),
            name: selectedUser.get('name'),
            email: selectedUser.get('email'),
            org_id: selectedUser.get('org_id'),
            displayField: selectedUser.get('org_name'),
            security_roles: selectedUser.get('auth_id'),
            isAdmin: selectedUser.get('auth_id') == 1 ? 1 : 0,
            isUser: selectedUser.get('auth_id') == 1 ? 0 : 1,
            auth_name: selectedUser.get('auth_name'),
            level: selectedUser.get('level')
        };

        // 사용자 수청창을 생성한다.
        Ext.create('Flamingo2.view.system.user.register.UserRegisterWindow', {
            title: message.msg('system.user.common.user.modify'),
            buttons: [
                {
                    text: message.msg('common.save'),
                    iconCls: 'common-save',
                    handler: 'onChangeUserOKClick'
                },
                {
                    text: message.msg('common.cancel'),
                    iconCls: 'common-cancel',
                    handler: 'onChangeUserCancelClick'
                }
            ],
            listeners: {
                beforerender: function () {
                    var me = this;
                    var refs = me.getReferences();

                    refs.userForm.getForm().setValues(Ext.merge(params));
                    refs.username.setReadOnly(true);
                    refs.password.emptyText = '********';
                    refs.password.allowBlank = true;
                    refs.confirmPassword.emptyText = '********';
                    refs.confirmPassword.allowBlank = true;
                }
            }
        }).center().show();
    },

    /**ㄱ
     * 소속을 추가창을 생성한다.
     */
    onAddOrgClick: function () {
        Ext.create('Flamingo2.view.system.user.register.OrgRegisterWindow', {
            buttons: [
                {
                    text: message.msg('common.add'),
                    iconCls: 'common-add',
                    handler: 'onAddOrgOKClick'
                },
                {
                    text: message.msg('common.cancel'),
                    iconCls: 'common-cancel',
                    handler: 'onAddOrgCancelClick'
                }
            ]
        }).center().show();
    },

    /**
     * 소속 삭제창을 생성한다.
     */
    onDeleteOrgClick: function () {
        Ext.create('Flamingo2.view.system.user.register.OrgRegisterWindow', {
            title: message.msg('system.user.common.org.delete'),
            buttons: [
                {
                    text: message.msg('common.ok'),
                    iconCls: 'common-ok',
                    handler: 'onDeleteOrgOKClick'
                },
                {
                    text: message.msg('common.cancel'),
                    iconCls: 'common-cancel',
                    handler: 'onDeleteOrgCancelClick'
                }
            ],
            listeners: {
                beforerender: function () {
                    var me = this;
                    var refs = me.getReferences();
                    refs.orgCode.setHidden(true);
                    refs.orgCode.setDisabled(true);
                    refs.orgName.setHidden(true);
                    refs.orgName.setDisabled(true);
                    refs.orgDescription.setDisabled(true);
                    refs.orgDescription.setHidden(true);
                    refs.orgCombo.setHidden(false);
                },
                afterrender: function () {
                    var organizationCombo = query('orgForm #orgCombo');
                    setTimeout(function () {
                        organizationCombo.getStore().load({
                            params: {
                                condition: 'list'
                            }
                        })
                    }, 10);
                }
            }
        }).center().show();
    },

    /**
     * 소속 정보창을 생성한다.
     */
    onChangeOrgClick: function () {
        Ext.create('Flamingo2.view.system.user.register.OrgRegisterWindow', {
            title: message.msg('system.user.common.org.modify'),
            buttons: [
                {
                    text: message.msg('common.save'),
                    iconCls: 'common-save',
                    handler: 'onChangeOrgOKClick'
                },
                {
                    text: message.msg('common.cancel'),
                    iconCls: 'common-cancel',
                    handler: 'onChangeOrgCancelClick'
                }
            ],
            listeners: {
                beforerender: function () {
                    var me = this;
                    var refs = me.getReferences();
                    refs.orgCombo.setHidden(false);
                    refs.orgCombo.setListeners({
                        select: function (combo, record) {
                            refs.orgForm.getForm().setValues(Ext.merge(record.data));
                        }
                    });
                },
                afterrender: function () {
                    var organizationCombo = query('orgForm #orgCombo');
                    setTimeout(function () {
                        organizationCombo.getStore().load({
                            params: {
                                condition: 'list'
                            }
                        })
                    }, 10);
                }
            }
        }).center().show();
    }
});