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
Ext.define('Flamingo2.view.batch.register.JobRegisterWindowController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jobRegisterWindowController',

    listen: {
        controller: {
            batchWorkflowtree: {
                workflowLoaded: 'onWorkflowLoaded'
            }
        }
    },

    /**
     * 배치 작업 등록 창에서 OK 버튼을 클릭할 경우 선택한 Job을 실행한다.
     */
    onRegisterClickOK: function () {
        var me = this;
        var refs = me.getReferences();
        var jobName = refs.jobName.getValue();
        var cronExpression = refs.triggerCronExpression.getValue();
        var workflowId = refs.workflowId.getValue();
        var wid = refs.identifier.getValue();
        var variableStore = query('variableGrid').getStore();
        var vars = [];

        if (Ext.isEmpty(workflowId)) {
            Ext.Msg.alert(message.msg('common.warn'), '워크플로우 ID가 없습니다.<br>워크플로우 목록을 선택하시오.');
            return;
        }

        if (Ext.isEmpty(jobName)) {
            Ext.Msg.alert(message.msg('common.warn'), '작업명을 입력하시오.');
            return;
        }

        if (!refs.triggerCronExpression.isValid()) {
            Ext.Msg.alert(message.msg('common.warn'), 'Cron형식이 올바르지 않습니다.');
            return;
        }

        Ext.each(variableStore.getRange(), function (item) {
            vars.push({
                name: item.data.name,
                value: item.data.value
            });
        });

        var params = {
            clusterName: ENGINE.id,
            wid: wid,
            cron: cronExpression,
            jobName: jobName,
            workflowId: workflowId,
            vars: vars.length == 0 ? '' : Ext.encode(vars)
        };

        invokePostByMap(CONSTANTS.BATCH.REGIST, params,
            function (response) {
                var obj = Ext.decode(response.responseText);
                if (obj.success) {
                    info(message.msg('batch.msg.job_register'), message.msg('batch.msg.registered_batch_job'));

                    me.fireEvent('batchRefresh');
                    me.getView().close();
                } else {
                    error(message.msg('batch.msg.job_register'), format(message.msg('batch.msg_cannot_register_batch_job'), obj.error.message));
                }
            },
            function (response) {
                error(message.msg('batch.msg.job_register'), format(message.msg('batch.msg_cannot_register_batch_job'), response.statusText));
            }
        );
    },

    /**
     * 배치 작업 등록 창을 종료한다.
     */
    onRegisterClickCancel: function () {
        this.getView().close();
    },

    /**
     저장된 워크플로우 Job을 선택할 수 있는 Tree 창을 보여준다.
     */
    onSelectWorkflowButton: function () {
        Ext.create('Flamingo2.view.batch.workflow.WorkflowTreeWindow').center().show();
    },

    /**
     * Cron Expression 정보를 등록할 수 있는 창을 보여준다.
     */
    onClickCronSetButton: function () {
        Ext.create('Flamingo2.view.batch.cron.CronExpressionWindow').center().show();
    },

    /**
     * Workflow loaded 이벤트 처리
     * 워크플로우 선택 창에서 선택 후 Ajax 호출이 완료되면 이벤트가 수행된다.
     * */
    onWorkflowLoaded: function (data) {
        var me = this;
        var refs = me.getReferences();

        refs.jobName.setValue(data.workflowName);
        refs.workflowId.setValue(data.workflowId);
        refs.writer.setValue(data.username);
        refs.createDate.setValue(data.createDate);
        refs.status.setValue(data.status);
        refs.identifier.setValue(data.id);

        var variables = Ext.decode(decodeURIComponent(data.variable));
        var global = variables.global;

        refs.variableGrid.getStore().removeAll();
        if (!Ext.isEmpty(global)) {
            var idx = 0;
            for (var key in global) {
                refs.variableGrid.getStore().insert(idx, {
                    name: key,
                    value: global[key]
                });
                idx++;
            }
        }
    }
});