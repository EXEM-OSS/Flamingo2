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
/**
 * Created by Park on 15. 6. 30..
 */
Ext.define('Flamingo2.view.batch.register.JobModifyController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jobmodify',

    onAfterrender: function () {
        var me = this;
        var refs = me.getReferences();
        var i, tools = refs.variableGrid.query('tool');

        for (i = 0; i < tools.length; i++) {
            tools[i].setVisible(false);
        }

        var params = {
            clusterName: ENGINE.id,
            job_id: me.getView().job_id,
            username: me.getView().username
        }

        invokeGet('/batch/getJob', params,
            function (response) {
                var r = Ext.decode(response.responseText);

                if (r.success) {
                    me.getViewModel().setData({jobInfo: r.map});
                    refs.triggerCronExpression.setValue(r.map.cron);
                    refs.jobInformation.getForm().setValues(r.map);
                    refs.jobName.setValue(r.map.JOB_NAME);

                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
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

    onCancelClick: function () {
        this.getView().close();
    },

    onOKClick: function () {
        var me = this;
        var refs = me.getReferences();
        var jobName = refs.jobName.getValue();
        var cron = refs.triggerCronExpression.getValue();

        if (Ext.isEmpty(jobName)) {
            Ext.Msg.alert(message.msg('common.warn'), '작업명을 입력하시오.');
            return;
        }

        if (!refs.triggerCronExpression.isValid()) {
            Ext.Msg.alert(message.msg('common.warn'), 'Cron형식이 올바르지 않습니다.');
            return;
        }

        var params = Ext.merge(me.getViewModel().getData().jobInfo, {
            jobName: jobName,
            cron: cron,
            clusterName: ENGINE.id,
            username: me.getView().username
        });

        Ext.MessageBox.show({
            title: '배치작업 변경',
            message: '변경된 내용을 반영하시겠습니까?',
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn === 'yes') {
                    invokePostByMap('/batch/update', params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                info('배치 잡 변경', '배치 잡이 변경되었습니다.');

                                me.fireEvent('changeClose');
                                me.getView().close();
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.warn'),
                                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
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
                } else if (btn === 'no') {
                    return;
                }
            }
        });
    }
});