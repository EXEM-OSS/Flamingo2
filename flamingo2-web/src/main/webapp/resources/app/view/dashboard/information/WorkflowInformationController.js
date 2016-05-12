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
Ext.define('Flamingo2.view.dashboard.information.WorkflowInformationController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.workflowInformationController',

    /**
     * Flamingo2.view.component.WebSocketProxyController
     */
    listen: {
        controller: {
            websocketController: {
                workflowMessage: 'onWorkflowMessage'
            }
        }
    },

    /**
     * Workflow Monitoring Tree 목록에서 선택한 작업의 정보를 가져온다.
     *
     * @param tabPanel
     */
    onAfterRender: function (tabPanel) {
        var me = this;
        var refs = me.getReferences();
        var storeData = tabPanel.up().propertyData.data;

        refs.jobDetailForm.getForm().setValues(storeData);
        Ext.ComponentQuery.query('jobDetailForm aceEditor')[0].value = storeData.workflowXml;
        Ext.ComponentQuery.query('jobDetailForm aceEditor')[1].value = storeData.exception;
    },

    /**
     * Workflow Information Window에서 Action 탭의 Workflow Grid 목록을 가져온다.
     */
    onActionGridAfterRender: function () {
        var actionListGrid = query('actionDetailForm #actionListGrid');
        var identifier = query('jobDetailForm #identifier');

        setTimeout(function () {
            actionListGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    identifier: identifier.getValue()
                }
            });
        }, 10);
    },

    /**
     * Workflow Information Window에서 Action 탭의 Workflow Grid 목록을 갱신한다.
     */
    onActionGridRefreshButton: function () {
        var identifier = query('jobDetailForm #identifier');
        var actionListGrid = query('actionDetailForm #actionListGrid');

        setTimeout(function () {
            actionListGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    identifier: identifier.getValue()
                }
            })
        }, 10);
    },

    /**
     * ACE Editor의 toolbar를 숨긴다.
     *
     * @param comp ACE Editor
     */
    hide: function (comp) {
        comp.down('toolbar').setVisible(false);
    },

    /**
     * 워크플로우 실행 이력 Tree의 아이템을 더블클릭 시 처리하는 이벤트 핸들러.
     */
    onActionGridSelection: function (sm, selectedRecord) {
        var me = this;
        var refs = me.getReferences();
        var actionInfoForm = query('actionDetailForm #actionInfoForm');
        var startDate = selectedRecord[0].data.startDate;
        var endDate = selectedRecord[0].data.endDate;
        var duration = selectedRecord[0].data.duration;
        var identifier = selectedRecord[0].data.identifier;
        var taskId = selectedRecord[0].data.taskId;

        if (startDate > 0)
            selectedRecord[0].data.startDate = App.Util.Date.format(new Date(startDate), 'yyyy-MM-dd HH:mm:ss');

        if (endDate > 0)
            selectedRecord[0].data.endDate = App.Util.Date.format(new Date(endDate), 'yyyy-MM-dd HH:mm:ss');

        var diff;

        if (duration > 0) {
            var start = new Date(startDate);
            var end = new Date(startDate);
            diff = (end.getTime() - start.getTime()) / 1000;
            selectedRecord[0].data.duration = App.Util.Date.toHumanReadableTime(Math.floor(diff));
        } else {
            diff = duration / 1000;
            selectedRecord[0].data.duration = App.Util.Date.toHumanReadableTime(Math.floor(diff));
        }

        actionInfoForm.getForm().setValues(selectedRecord[0].data);

        var actionListGrid = query('actionDetailForm #actionListGrid');
        var selectedItem = actionListGrid.getSelectionModel().getLastSelected();

        if (selectedItem) {
            var tabpanel = refs.actionDetailForm.down('tabpanel');
            me.onActionSubTabChange(tabpanel);
        }
    },

    /**
     * Ace Editor의 Undo Button & Redo Button 렌터링 Event
     */
    onAceEditorReady: function () {
        var me = this;
        var refs = me.getReferences();

        if (refs.jobDetailForm) {
            var jobXmlToolbar = refs.jobXmlAceEditor.getDockedComponent(0);
            jobXmlToolbar.items.map.undoButton.hide();
            jobXmlToolbar.items.map.redoButton.hide();

            var jobErrorToolbar = refs.jobErrorAceEditor.getDockedComponent(0);
            jobErrorToolbar.items.map.undoButton.hide();
            jobErrorToolbar.items.map.redoButton.hide();
        }

        var logToolbar = refs.logAceEditor.getDockedComponent(0);
        logToolbar.items.map.undoButton.hide();
        logToolbar.items.map.redoButton.hide();

        var commandToolbar = refs.commandAceEditor.getDockedComponent(0);
        commandToolbar.items.map.undoButton.hide();
        commandToolbar.items.map.redoButton.hide();

        var scriptToolbar = refs.scriptAceEditor.getDockedComponent(0);
        scriptToolbar.items.map.undoButton.hide();
        scriptToolbar.items.map.redoButton.hide();

        var errorToolbar = refs.errorAceEditor.getDockedComponent(0);
        errorToolbar.items.map.undoButton.hide();
        errorToolbar.items.map.redoButton.hide();
    },

    /**
     * Workflow 실행 이력 Tree의 하위 목록에서 선택한 Sub Workflow 정보를 가져온다.
     */
    onSubWorkflowSelection: function (form) {
        var me = this;
        var refs = me.getReferences();
        var selectedItem = form.up().propertyData.data;
        var identifier = selectedItem.identifier;
        var taskId = selectedItem.taskId;
        var url = CONSTANTS.DASHBOARD.TASK.GET;
        var params = {
            clusterName: ENGINE.id,
            identifier: identifier,
            taskId: taskId
        };

        /**
         * 선택한 Sub Workflow의 상세 정보를 가져온다.
         */
        invokeGet(url, params,
            function (response) {
                var res = Ext.decode(response.responseText);

                if (res.success) {
                    var startDate = res.object.startDate;
                    var endDate = res.object.endDate;
                    var duration = res.object.duration;

                    if (startDate > 0)
                        res.object.startDate = App.Util.Date.format(new Date(startDate), 'yyyy-MM-dd HH:mm:ss');

                    if (endDate > 0)
                        res.object.endDate = App.Util.Date.format(new Date(endDate), 'yyyy-MM-dd HH:mm:ss');

                    var diff;

                    if (duration > 0) {
                        var start = new Date(startDate);
                        var end = new Date(startDate);
                        diff = (end.getTime() - start.getTime()) / 1000;
                        res.object.duration = App.Util.Date.toHumanReadableTime(Math.floor(diff));
                    } else {
                        diff = duration / 1000;
                        res.object.duration = App.Util.Date.toHumanReadableTime(Math.floor(diff));
                    }
                    refs.actionInfoForm.getForm().setValues(res.object);

                } else if (res.error.cause) {
                    error(message.msg('workflow.msg_get_action_list'), obj.error.cause);
                } else {
                    error(message.msg('workflow.msg_get_action_list'), message.msg('workflow.msg_get_action_list_fail'));
                }
            },
            function () {
                error(message.msg('workflow.msg_get_action_list'), message.msg('workflow.msg_get_action_list_fail'));
            }
        );

        // FIXME me.onLogTabSelection(selectedItem);
    },

    /**
     * 선택한 Sub Workflow 액션 정보에서 탭을 선택할 때 선택한 탭에 해당한 정보를 가져온다.
     */
    onActionSubTabChange: function (tabpanel) {
        var me = this;
        var refs = me.getReferences();
        var activeTab = tabpanel.getActiveTab();
        var activeTabIndex = tabpanel.items.findIndex('id', activeTab.id);
        var actionListGrid = query('actionDetailForm #actionListGrid');
        var selectedItem = actionListGrid.getSelectionModel().getLastSelected();
        var tabConditionKey;
        var msg;
        var params;

        if (selectedItem) {
            var identifier = selectedItem.get('identifier');
            var taskId = selectedItem.get('taskId');

            if (activeTabIndex == 4) {
                this.getViewModel().getStore('yarnApplicationIdStore').load({
                    params: {
                        clusterName: ENGINE.id,
                        identifier: identifier,
                        type: 'task'
                    }
                });

                this.getViewModel().getStore('mapReducdeIdStore').load({
                    params: {
                        clusterName: ENGINE.id,
                        identifier: identifier,
                        type: 'task'
                    }
                });
            } else {
                if (activeTabIndex == 0) {
                    tabConditionKey = 'log';
                    msg = message.msg('dashboard.wdetail.log.none');
                } else if (activeTabIndex == 1) {
                    tabConditionKey = 'command';
                    msg = message.msg('dashboard.wdetail.command.none');
                } else if (activeTabIndex == 2) {
                    tabConditionKey = 'script';
                    msg = message.msg('dashboard.wdetail.script.none');
                } else if (activeTabIndex == 3) {
                    tabConditionKey = 'error';
                    msg = message.msg('dashboard.wdetail.error.none');
                }

                params = {
                    clusterName: ENGINE.id,
                    identifier: identifier,
                    taskId: taskId,
                    tabConditionKey: tabConditionKey
                };

                invokeGet(CONSTANTS.DASHBOARD.GET_LOGS, params,
                    function (response) {
                        var res = Ext.decode(response.responseText);

                        if (res.success) {
                            if (activeTabIndex == 0) {
                                refs.logAceEditor.setValue(res.map.log);
                            } else if (activeTabIndex == 1) {
                                refs.commandAceEditor.setValue(res.map.command);
                            } else if (activeTabIndex == 2) {
                                refs.scriptAceEditor.setValue(res.map.script);
                            } else if (activeTabIndex == 3) {
                                refs.errorAceEditor.setValue(res.map.error);
                            }
                        } else if (res.error.cause) {
                            refs.errorAceEditor.setValue(res.error.cause);
                        } else {
                            refs.commandAceEditor.setValue(msg);
                        }
                    },
                    function () {
                        refs.commandAceEditor.setValue(msg);
                    }
                );
            }
        }
    },

    /**
     * Workflow Information Window에서 Action 탭의 실행 로그 필드 정보를 갱신한다.
     */
    onActionLogRefresh: function () {
        var me = this;
        var refs = me.getReferences();
        var actionListGrid = query('actionDetailForm #actionListGrid');
        var selectedItem = actionListGrid.getSelectionModel().getLastSelected();

        if (!selectedItem) {
            error(message.msg('workflow.msg_get_action_detail'), message.msg('workflow.msg_refresh_action_detail'));
            return false;
        }

        var url = CONSTANTS.DASHBOARD.GET_LOGS;
        var params = {
            clusterName: ENGINE.id,
            identifier: selectedItem.get('identifier'),
            taskId: selectedItem.get('taskId')
        };

        invokeGet(url, params,
            function (response) {
                var res = Ext.decode(response.responseText);

                if (res.success) {
                    refs.log.setValue(res.map.log);
                } else if (res.error.cause) {
                    refs.log.setValue(res.error.cause);
                } else {
                    refs.log.setValue(message.msg('dashboard.wdetail.log.none'));
                }
            },
            function () {
                refs.log.setValue(message.msg('dashboard.wdetail.log.none'));
            }
        );
    },

    /**
     * Sub Workflow Grid 목록이 없는 Action Tab에서 로그 정보를 갱신한다.
     */
    onSubActionLogRefresh: function () {
        var workflowHistoryTreePanel = query('workflowHistory');
        var selectedItem = workflowHistoryTreePanel.getSelectionModel().getLastSelected();
    },

    /**
     * 워크플로우 실행 이력 그리드의 아이템을 더블클릭시 CodeMirror Editor를 Resizing 처리 이벤트 핸들러.
     */
    onCodemirrorResize: function (editor, width, height) {
        editor.getEl().query('.CodeMirror')[0].CodeMirror.setSize('100%', height);
    },

    /**
     * 워크플로우 실행 이력 창에서 선택한 아이템의 정보를 갱신한다.
     * @param message
     */
    onWorkflowMessage: function (message) {
        var me = this;
        var data = Ext.decode(message.body);
        var store = me.getViewModel().getStore('workflowHistoryStore');

        if (data.command == 'taskHistories') {
            var taskhistories = data.data;
            for (var i = 0; i < taskhistories.length; i++) {
                var taskhistory = taskhistories[i];
                var identifier = taskhistory.identifier;
                var taskId = taskhistory.taskId;
                var status = taskhistory.status;

                var records = store.queryBy(function (record) {
                    return (record.get('identifier') == identifier && record.get('taskId') == taskId);
                });
                for (var r = 0; r < records.items.length; r++) {
                    var record = records.items[r];
                    record.set('status', status);
                    record.commit();
                }
            }
        }
        if (data.command == 'workflowHistory') {
            var workflowHistory = data.data;

            var sf_parentIdentifier = workflowHistory.sf_parentIdentifier;
            var sf_taskId = workflowHistory.sf_taskId;
            var jobStringId = workflowHistory.jobStringId;
            var status = workflowHistory.status;

            var updateTasks = store.queryBy(function (record) {
                return (record.get('identifier') == sf_parentIdentifier && record.get('taskId') == sf_taskId );
            });
            for (var r = 0; r < updateTasks.items.length; r++) {

                var progreses = Ext.ComponentQuery.query('progressbar[name=taskProgress][identifier=' + sf_parentIdentifier + '][taskId=' + sf_taskId + ']');
                Ext.each(progreses, function (progrese) {
                    progrese.destroy();
                });

                var record = updateTasks.items[r];
                workflowHistory.startDate = workflowHistory.startDateSimple;
                workflowHistory.endDate = workflowHistory.endDateSimple;
                workflowHistory.text = workflowHistory.workflowName;
                workflowHistory.cls = "folder";
                workflowHistory.leaf = false;
                workflowHistory.type = "workflow";
                workflowHistory.rowid = workflowHistory.id;
                delete workflowHistory.id;
                workflowHistory.identifier = null;
                workflowHistory.taskId = null;

                for (var key in workflowHistory) {
                    record.set(key, workflowHistory[key]);
                }
                var id = record.get('id');
                var list = id.split('/');
                var removeStr = list[list.length - 1];
                id = id.replace(removeStr, workflowHistory.jobStringId);
                record.set('id', id);
                record.commit();
            }

            var records = store.queryBy(function (record) {
                return (record.get('jobStringId') == jobStringId);
            });
            for (var r = 0; r < records.items.length; r++) {
                var record = records.items[r];
                record.set('status', status);

                record.set('startDate', workflowHistory.startDateSimple);
                record.set('endDate', workflowHistory.endDateSimple);
                record.set('elapsed', workflowHistory.elapsed);
                record.set('currentStep', workflowHistory.currentStep);
                record.set('totalStep', workflowHistory.totalStep);
                record.set('currentAction', workflowHistory.currentAction);
                record.commit();
            }
        }
    },

    /**
     * 워크플로우 실행 이력 창의 그리드 정보를 갱신한다.
     * @param identifier
     */
    updatetasks: function (identifier) {
        var me = this;
        var workflowGrid = query('workflowMonitoring #workflowGrid');

        me.destroyProgresses();

        setTimeout(function () {
            workflowGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    identifier: identifier
                }
            }, 10);
        })
    },

    destroyProgresses: function () {
        var progreses = Ext.ComponentQuery.query('progressbar[name=taskProgress]');
        Ext.each(progreses, function (progrese) {
            progrese.destroy();
        });
        $("[name=taskProgress]").remove();
    },

    /**
     * Workflow Detail Window를 종료한다.
     */
    onCloseWorkflowInformationWindow: function () {
        this.getView().close();
    },

    /**
     * 워크플로우 실행 이력 그리드의 목록을 가져오는 핸들러. (onJobDetailAfterRender 로 동작하던 코드)
     */
    onJobDetail: function () {
        var last = Ext.ComponentQuery.query('jobDetail')[0].job;
        var jobInfo = Ext.ComponentQuery.query('jobDetail #jobDetailForm')[0];

        var jobDetailModel = {};
        jobDetailModel.jobId = last.get('jobStringId');
        jobDetailModel.workflowId = last.get('workflowId');
        jobDetailModel.workflowName = last.get('workflowName');
        jobDetailModel.status = last.get('status');
        jobDetailModel.username = last.get('username');
        jobDetailModel.startDate = App.Util.Date.format(new Date(last.get('startDate')), 'yyyy-MM-dd HH:mm:ss');
        jobDetailModel.endDate = App.Util.Date.format(new Date(last.get('endDate')), 'yyyy-MM-dd HH:mm:ss');
        jobDetailModel.elapsed = App.Util.Date.toHumanReadableTime(Math.floor(last.get('elapsed') / 1000));
        jobDetailModel.currentStep = last.get('currentStep');
        jobDetailModel.totalStep = last.get('totalStep');

        jobInfo.getForm().setValues(jobDetailModel);

        query('jobDetail #workflowXml').setValue(last.get('workflowXml'));
        query('jobDetail #exception').setValue(last.get('exception'));
    }
});