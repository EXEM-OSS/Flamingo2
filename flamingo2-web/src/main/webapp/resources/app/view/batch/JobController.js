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
Ext.define('Flamingo2.view.batch.JobController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jobController',

    listen: {
        controller: {
            jobRegisterWindowController: {
                batchRefresh: 'onBatchRefresh'
            },
            jobmodify: {
                changeClose: 'onBtnRefershClick'
            }
        }
    },

    /**
     * onBatchRefresh 배치작업 등록 후 등록창이 닫히면서 발생하는 이벤트
     * */
    onBatchRefresh: function () {
        var me = this;

        me.onBtnRefershClick();
    },

    /**
     * 실행 중인 배치 작업 목록을 가져온다.
     */
    onJobListAfterRender: function (grid) {
        setTimeout(function () {
            grid.getStore().proxy.extraParams.clusterName = ENGINE.id;
            grid.getStore().load();
        }, 300);
    },

    /**
     * 새로운 배치 작업을 등록할 수 있는 창을 보여준다.
     */
    onBtnRegistClick: function () {
        Ext.create('Flamingo2.view.batch.register.JobRegisterWindow').show();
    },

    /**
     * 갱신 버튼 클릭 이벤트
     * */
    onBtnRefershClick: function () {
        var me = this;
        var refs = me.getReferences();
        var chartRefs = refs.batchChart.getReferences();
        if (ENGINE.id) {
            refs.grdJobList.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    start: 0,
                    page: 1,
                    limit: CONSTANTS.GRID_SIZE_PER_PAGE
                }
            });

            refs.grdJobList.getStore().currentPage = 1;
            refs.stopJobButton.setDisabled(true);
            refs.suspendJobButton.setDisabled(true);
            refs.resumeJobButton.setDisabled(true);

            me.reload(chartRefs.engineJvmHeapUsage, ENGINE.id);
            me.reload(chartRefs.jobs, ENGINE.id);
        } else {
            Ext.MessageBox.show({
                title: message.msg('common.warn'),
                message: message.msg('batch.msg.select_workflow_engine'),
                width: 300,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                scope: this,
                fn: function (btn, text, eOpts) {
                    return false;
                }
            });
        }
    },

    /**
     * 잡 리스트 선택시 버튼 상태 변경
     * */
    onJobListSelect: function (grid, record, index) {
        var me = this;
        var refs = me.getReferences();

        switch (record.get('status')) {
            case 'RUNNING':
                refs.stopJobButton.setDisabled(false);
                refs.suspendJobButton.setDisabled(false);
                refs.resumeJobButton.setDisabled(true);
                refs.modifyJobButton.setDisabled(true);
                break;
            case 'STOPPED':
                refs.stopJobButton.setDisabled(true);
                refs.suspendJobButton.setDisabled(true);
                refs.resumeJobButton.setDisabled(true);
                refs.modifyJobButton.setDisabled(true);
                break;
            case 'RESUMED':
                refs.stopJobButton.setDisabled(false);
                refs.suspendJobButton.setDisabled(false);
                refs.resumeJobButton.setDisabled(true);
                refs.modifyJobButton.setDisabled(true);
                break;
            case 'PAUSED':
                refs.stopJobButton.setDisabled(false);
                refs.suspendJobButton.setDisabled(true);
                refs.resumeJobButton.setDisabled(false);
                refs.modifyJobButton.setDisabled(false);
                break;
        }
    },

    /**
     * 일시중지 버튼 클릭
     * */
    onBtnSuspendClick: function () {
        var me = this;
        var refs = this.getReferences();
        var selection = refs.grdJobList.getSelectionModel().getSelection()[0];
        var params = {
            clusterName: ENGINE.id,
            groupName: selection.data.groupName,
            name: selection.data.jobId,
            workflowId: selection.data.workflowId
        };

        Ext.MessageBox.show({
            title: message.msg('batch.pause'),
            message: message.msg('batch.msg.pause'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn === 'yes') {
                    invokePostByMap(CONSTANTS.BATCH.SUSPEND, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                info(message.msg('batch.msg.success_to_pause_batch_job'), message.msg('batch.msg.paused_batch_job'));

                                me.onBtnRefershClick();
                            } else {
                                error(message.msg('batch.msg.failed_batch_job_to_pause'), format(message.msg('batch.msg.cannot_pause_batch_job'), obj.error.message));
                            }
                        },
                        function (response) {
                            error(message.msg('batch.msg.failed_batch_job_to_pause'), format(message.msg('batch.msg.cannot_pause_batch_job'), response.statusText));
                        }
                    );
                }
            }
        });
    },

    /**
     * 다시시작 버튼 클릭 이벤트
     * */
    onBtnResumeClick: function () {
        var me = this;
        var refs = this.getReferences();
        var selection = refs.grdJobList.getSelectionModel().getSelection()[0];
        var params = {
            clusterName: ENGINE.id,
            groupName: selection.data.groupName,
            name: selection.data.jobId,
            workflowId: selection.data.workflowId
        };

        Ext.MessageBox.show({
            title: message.msg('batch.restart'),
            message: message.msg('batch.msg.restart'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn === 'yes') {
                    invokePostByMap(CONSTANTS.BATCH.RESUME, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                info(message.msg('batch.msg.success_to_resume_batch_job'), message.msg('batch.msg.resumed_batch_job'));

                                me.onBtnRefershClick();
                            } else {
                                error(message.msg('batch.msg.failed_to_resume_batch_job'), format(message.msg('batch.msg.cannot_resume_batch_job'), obj.error.message));
                            }
                        },
                        function (response) {
                            error(message.msg('batch.msg.failed_to_resume_batch_job'), format(message.msg('batch.msg.cannot_resume_batch_job'), response.statusText));
                        }
                    );
                }
            }
        });
    },

    /**
     * 정지버튼 클릭 이벤트
     * */
    onBtnStopClick: function () {
        var me = this;
        var refs = me.getReferences();
        Ext.MessageBox.show({
            title: message.msg('batch.stop'),
            message: message.msg('batch.msg_pause_batch_job_ok'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    var selection = refs.grdJobList.getSelectionModel().getSelection()[0];
                    var params = {
                        clusterName: ENGINE.id,
                        groupName: selection.data.groupName,
                        name: selection.data.jobId,
                        workflowId: selection.data.workflowId
                    };

                    invokePostByMap(CONSTANTS.BATCH.STOP, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                info(message.msg('batch.msg.success_to_stop_batch_job'), message.msg('batch.msg.stopped_batch_job'));

                                me.onBtnRefershClick();
                            } else {
                                error(message.msg('batch.msg.failed_to_stop_batch_job'), format(message.msg('batch.msg.cannot_to_stop_batch_job'), obj.error.message));
                            }
                        },
                        function (response) {
                            error(message.msg('batch.msg.failed_to_stop_batch_job'), format(message.msg('batch.msg.cannot_to_stop_batch_job'), response.statusText));
                        }
                    );
                }
            }
        });
    },

    /**
     * 수정버튼 클릭 이벤트
     * */
    onBtnModifyClick: function () {
        var me = this;
        var refs = me.getReferences();

        var record = refs.grdJobList.getSelectionModel().getSelection()[0];

        Ext.create('Flamingo2.view.batch.register.JobModify', {
            job_id: record.get('jobId'),
            username: record.get('username')
        }).show();
    },

    onJobListItemdblclick: function (view, record, item, index, e, opts) {
        var me = this;
        var refs = me.getReferences();
        var params = {
            clusterName: ENGINE.id,
            id: record.get('id'),
            jobId: record.get('jobId'),
            workflowId: record.get('workflowId')
        };

        invokePostByMap(CONSTANTS.BATCH.GET_WORKFLOW, params,
            function (response) {
                var obj = Ext.decode(response.responseText);
                var panel = Ext.create('Flamingo2.view.batch.register.JobRegisterWindow');
                var panelRefs = panel.getReferences();

                // Set values
                panelRefs.writer.setValue(obj.object.username);
                panelRefs.createDate.setValue(obj.object.createDate);
                panelRefs.jobName.setValue(obj.object.workflowName);
                panelRefs.workflowId.setValue(obj.object.workflowId);
                panelRefs.status.setValue(obj.object.status);
                panelRefs.identifier.setValue(obj.object.id);
                panelRefs.btnOk.setVisible(false);

                // Workflow Variables to Job Variables
                var vars = panelRefs.variableGrid;
                vars.getStore().removeAll();
                if (obj.object.variables && !isBlank(obj.object.variables)) {
                    var variables = Ext.decode(obj.object.variables);
                    var idx = 0;
                    Ext.each(variables, function (v) {
                        vars.getStore().insert(idx, {
                            name: v.name,
                            value: v.value
                        });
                        idx++;
                    });
                }

                panel.center().show();
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

    reload: function (chart, engineId) {
        chart.getStore().getProxy().extraParams.clusterName = engineId;
        chart.getStore().getProxy().extraParams.limit = parseInt(config.hadoop_monitoring_display_count);
        chart.getStore().removeAll();
        chart.getStore().load();
    }
});