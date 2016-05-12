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
Ext.define('Flamingo2.view.system.license.LicenseController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.license',

    /**
     * Called when the view is created
     */
    init: function () {

    },

    onAfterrender: function () {
        var me = this;
        var refs = me.getReferences();
        var params = {
            clusterName: ENGINE.id
        };

        invokeGet('/system/license/licenseInfo', params,
            function (response) {
                var r = Ext.decode(response.responseText);

                if (r.success) {
                    refs.frmLicense.getForm().setValues(r.map);
                } else {
                    // FIXME
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

        invokeGet('/system/license/serverId', params,
            function (response) {
                var r = Ext.decode(response.responseText);

                if (r.success) {
                    refs.serverId.setValue(r.map.serverId);
                } else {
                    // FIXME
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

    onBtnRefreshClick: function () {
        var me = this;
        var refs = me.getReferences();

        if (Ext.isEmpty(refs.licenseKey.getValue())) {
            Ext.Msg.alert(message.msg('common.warn'), '라이센스 키를 입력하시오.');
            return;
        }
        Ext.MessageBox.show({
            title: message.msg('common.confirm'),
            message: 'Flamingo 라이센스 키를 갱신 하시겠습니까?',
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn === 'yes') {
                    me.renewLicense()
                } else if (btn === 'no') {
                    return;
                }
            }
        });
    },

    renewLicense: function () {
        var me = this;
        var refs = me.getReferences();

        me.getView().setLoading(true);
        invokePostByMap('/system/license/regist', {
                clusterName: ENGINE.id,
                license: refs.licenseKey.getValue()
            },
            function (response) {
                var result = Ext.decode(response.responseText);
                me.getView().setLoading(false);

                if (result.success) {
                    // 실행시 로그 창을 먼저 표시한다.
                    Ext.Msg.alert(message.msg('common.success'), 'Flamingo 라이센스 키를 갱신하였습니다.');
                    me.onAfterrender();
                    return;
                } else {
                    Ext.Msg.alert(message.msg('common.error'), 'Flamingo 라이센스 키가 올바르지 않습니다.');
                    return;
                }
            },
            function (response) {
                me.getView().setLoading(false);
                Ext.MessageBox.show({
                    title: message.msg('common.warn'),
                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    }
});