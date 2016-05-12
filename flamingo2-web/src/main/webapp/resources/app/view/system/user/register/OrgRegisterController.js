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
Ext.define('Flamingo2.view.system.user.register.OrgRegisterController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.orgRegisterViewController',

    /**
     * 사용자 목록에 사용하는 소속 콤보 목록을 업데이트 한다(추가된 소속 포함).
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
     * 소속을 삭제할 경우 사용자 목록 및 사용자 그리드의 소속 콤보 정보도 모두 업데이트한다.
     * 사용자 그리드의 경우는 콤보박스에서 선택한 소속을 삭제할 경우에는 전체 목록을 기준으로 업데이트한다.
     */
    onRefreshAll: function (orgId) {
        var usersGrid = query('userGridPanel');
        setTimeout(function () {
            usersGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id
                }
            });
        }, 10);

        var organizationCombo = query('userGridPanel #orgCombo');

        if (orgId == organizationCombo.lastValue) {
            orgId = 'ALL';
        }

        setTimeout(function () {
            organizationCombo.getStore().load({
                params: {
                    condition: orgId == 'ALL' ? 0 : orgId
                }
            })
        }, 10);
    },

    /**
     * 입력한 소속 정보를 서버로 전송한다.
     */
    onAddOrgOKClick: function () {
        var me = this;
        var refs = me.getReferences();

        if (!refs.orgForm.getForm().isValid()) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.valid.org'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var url = CONSTANTS.SYSTEM.USER.ADD_ORGANIZATION;
        var params = {
            clusterName: ENGINE.id,
            orgCode: refs.orgCode.getValue(),
            orgName: refs.orgName.getValue(),
            orgDescription: refs.orgDescription.getValue()
        };

        invokePostByMap(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    me.getView().close();
                    me.onRefreshOrgCombo();
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
     * 소속 추가창을 종료한다.
     */
    onAddOrgCancelClick: function () {
        this.getView().close();
    },

    /**
     * 선택한 소속을 삭제한다.
     */
    onDeleteOrgOKClick: function () {
        var me = this;
        var refs = me.getReferences();

        if (!refs.orgCombo.selection) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.delete.org'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var orgId = refs.orgCombo.selection.get('org_id');
        var url = CONSTANTS.SYSTEM.USER.DELETE_ORGANIZATION;
        var params = {
            clusterName: ENGINE.id,
            id: orgId
        };

        invokePostByMap(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    me.getView().close();
                    me.onRefreshAll(orgId);
                } else if (obj.error.cause) {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.INFO
                    });
                } else if (obj.error.message) {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: obj.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
                else {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: format(message.msg('system.user.msg.delete.limit'), refs.orgCombo.selection.get('org_name')),
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
    onDeleteOrgCancelClick: function () {
        this.getView().close();
    },

    /**
     * 수정한 소속 정보를 서버로 전송한다.
     */
    onChangeOrgOKClick: function () {
        var me = this;
        var refs = me.getReferences();

        if (!refs.orgCombo.selection) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.modify.org'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        if (!refs.orgForm.getForm().isValid()) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('system.user.msg.valid.orgAll'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        // 소속 콤보에서 선택한 소속의 ID를 가져온다.
        var orgId = refs.orgCombo.selection.get('org_id');
        var url = CONSTANTS.SYSTEM.USER.UPDATE_ORG_INFO;
        var params = {
            clusterName: ENGINE.id,
            id: orgId,
            orgCode: refs.orgCode.getValue(),
            orgName: refs.orgName.getValue(),
            orgDescription: refs.orgDescription.getValue()
        };

        invokePostByMap(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    me.getView().close();
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
    },

    /**
     * 소속 정보 수정창을 종료한다.
     */
    onChangeOrgCancelClick: function () {
        this.getView().close();
    }
});