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
Ext.define('Flamingo2.view.designer.monitoring.WorkflowMonitoringController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.workflowMonitoringController',

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
     * 워크플로우 실행 이력창에 실행 이력 정보를 출력한다.
     *
     * @param window
     */
    onAfterRender: function (window) {
        var workflowGrid = query('workflowMonitoring #workflowGrid');

        setTimeout(function () {
            workflowGrid.getStore().load({
                params: {
                    identifier: window.propertyData,
                    clusterName: ENGINE.id
                }
            }, 10);
        })
    },

    /**
     * 워크플로우 실행 이력창 그리드에서 선택한 아이템의 상세 정보를 가져온다.
     *
     * @param view
     * @param record
     * @param item
     * @param index
     * @param e
     */
    onClickWorkflowDetailItem: function (view, record, item, index, e) {
        var me = this;
        var refs = me.getReferences();

        invokeGet(CONSTANTS.DASHBOARD.TASK.LOG,
            {
                id: record.data.id
            },
            function (response) {
                var res = Ext.decode(response.responseText);
                if (res.success) {
                    var log = res.map['text'];
                    refs.aceEditor.editor.getSession().setValue(log);
                } else if (res.error.cause) {
                    refs.aceEditor.editor.getSession().setValue(res.error.cause);
                } else {
                    refs.aceEditor.editor.getSession().setValue(message.msg('dashboard.wdetail.log.none'));
                }
            },
            function () {
                refs.aceEditor.editor.getSession().setValues(message.msg('dashboard.wdetail.log.none'));
            }
        );
    },

    /**
     * 현재 입력되어 있는 조건으로 그리드 목록을 업데이트한다.
     *
     * @param event
     * @param toolEl
     * @param panel
     */
    onRefreshClick: function (event, toolEl, panel) {
        var workflowGrid = Ext.ComponentQuery.query('#workflowGrid')[0];
        var selected = workflowGrid.getSelectionModel().getSelection()[0];
        var aceEditor = Ext.ComponentQuery.query('#aceEditor')[0];

        if (selected) {
            invokeGet(CONSTANTS.DASHBOARD.TASK.LOG, {id: selected.id},
                function (response) {
                    var res = Ext.decode(response.responseText);
                    if (res.success) {
                        var log = res.map['text'];
                        aceEditor.editor.getSession().setValue(log);
                    } else if (res.error.cause) {
                        aceEditor.editor.getSession().setValue(res.error.cause);
                    } else {
                        aceEditor.editor.getSession().setValue(message.msg('dashboard.wdetail.log.none'));
                    }
                },
                function () {
                    aceEditor.editor.getSession().setValues(message.msg('dashboard.wdetail.log.none'));
                }
            );
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

    /**
     * 워크플로우 실행 이력 창의 그리드의 Progress 정보를 삭제한다.
     */
    destroyProgresses: function () {
        var progresses = Ext.ComponentQuery.query('progressbar[name=taskProgress]');
        Ext.each(progresses, function (progrese) {
            progrese.destroy();
        });
        $("[name=taskProgress]").remove();
    },

    /**
     * 워크플로우 실행 이력 창에서 선택한 아이템의 정보를 갱신한다.
     * @param message
     */
    onWorkflowMessage: function (message) {
        var me = this;
        var data = Ext.decode(message.body);
        var aceEditor = query('workflowMonitoring #aceEditor');

        if (data.command == 'logCasting') {
            if (data.type == 'workflow') {
                if (data.identifier && data.taskId) {
                    aceEditor.editor.setValue(data.data + '\n');
                }
            }
            if (data.type == 'bootstrap') {
                if (aceEditor.items.length > 0) {
                    aceEditor.editor.setValue(data.data + '\n');
                }
            }
        }
        if (data.command == 'taskHistories') {
            if (aceEditor.items.length > 0) {
                me.updatetasks(data.identifier);
            }
        }
    }
});