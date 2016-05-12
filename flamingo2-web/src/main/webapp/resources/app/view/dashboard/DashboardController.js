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
Ext.define('Flamingo2.view.dashboard.DashboardController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.dashboardController',

    init: function () {
        this.control({
            'workflowHistory #first': {
                click: this.onPagingClick
            },
            'workflowHistory #prev': {
                click: this.onPagingClick
            },
            'workflowHistory #next': {
                click: this.onPagingClick
            },
            'workflowHistory #last': {
                click: this.onPagingClick
            },
            'workflowHistory #findWorkflowButton': {
                click: this.onFindClick
            },
            'workflowHistory #clearWorkflowButton': {
                click: this.onClearClick
            },
            'workflowHistory #jobKillButton': {
                click: this.onJobKillClick
            }
        });
    },

    /**
     * 워크플로우 모니티링 Tree 목록을 가져온다.
     */
    onAfterRender: function () {
        this.getViewModel().getStore('workflowHistoryStore').load();
    },

    /**
     * 그리드의 아이템을 더블클릭시 처리하는 이벤트 핸들러.
     */
    onItemDoubleClick: function (view, record) {
        if (record.data.type == "workflow") { // Parent Workflow 선택 시
            Ext.create('Flamingo2.view.dashboard.information.WorkflowInformationWindow', {
                propertyData: record
            }).center().show();
        } else { // Sub Workflow 선택 시
            /*
             Ext.create('Flamingo2.view.dashboard.information.WorkflowInformationWindow', {
             height: 550,
             items: [
             {
             xtype: 'subActionDetailForm'
             }
             ],
             propertyData: record
             }).center().show();
             */
        }
    },

    onPagingClick: function () {
        var panel = query('workflowHistory');
        var startDate = panel.query('#startDate')[0];
        var endDate = panel.query('#endDate')[0];
        var status = panel.query('#status')[0];
        var workflowName = panel.query('#workflowName')[0];

        this.getViewModel().getStore('workflowHistoryStore').getProxy().setExtraParams({
            clusterName: ENGINE.id,
            startDate: App.Util.Date.formatExtJS(startDate.getValue(), 'Y-m-d'),
            endDate: App.Util.Date.formatExtJS(endDate.getValue(), 'Y-m-d'),
            status: status.getValue(),
            workflowName: workflowName.getValue()
        });
    },

    onClearClick: function () {
        var panel = query('workflowHistory');
        panel.query('#startDate')[0].setValue('');
        panel.query('#startDate')[0].setMaxValue();
        panel.query('#startDate')[0].setMinValue();

        panel.query('#endDate')[0].setValue('');
        panel.query('#endDate')[0].setMinValue();
        panel.query('#endDate')[0].setMaxValue();

        panel.query('#status')[0].setValue('ALL');
        panel.query('#workflowName')[0].setValue('');
    },

    onFindClick: function () {
        var panel = query('workflowHistory');
        var clusterName = ENGINE.id;
        var startDate = panel.query('#startDate')[0];
        var endDate = panel.query('#endDate')[0];
        var status = panel.query('#status')[0];
        var workflowName = panel.query('#workflowName')[0];

        if (clusterName) {
            if ((startDate.getValue() != null && endDate.getValue() == null)
                || endDate.getValue() != null && startDate.getValue() == null) {
                Ext.MessageBox.show({
                    title: message.msg('workflow.title_history_log'),
                    message: message.msg('workflow.msg_enter_dates'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING,
                    fn: function (e) {
                        return false;
                    }
                });
                return false;
            }

            if (startDate.getValue() != null || endDate.getValue() != null) {
                if (startDate.getValue().length <= 1) {
                    Ext.MessageBox.show({
                        title: message.msg('workflow.title_history_log'),
                        message: message.msg('workflow.msg_enter_exact'),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING,
                        fn: function (e) {
                            return false;
                        }
                    });
                    return false;
                }
                else if (endDate.getValue().length <= 1) {
                    Ext.MessageBox.show({
                        title: message.msg('workflow.title_history_log'),
                        message: message.msg('workflow.msg_enter_exact'),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING,
                        fn: function (e) {
                            return false;
                        }
                    });
                    return false;
                }
            }

            this.getViewModel().getStore('workflowHistoryStore').load({
                params: {
                    startDate: App.Util.Date.formatExtJS(startDate.getValue(), 'Y-m-d'),
                    endDate: App.Util.Date.formatExtJS(endDate.getValue(), 'Y-m-d'),
                    status: status.getValue(),
                    workflowName: workflowName.getValue(),
                    clusterName: ENGINE.id,
                    start: 0,
                    page: 1,
                    limit: 15
                }
            });

//            workflowHistoryPanel.getStore().currentPage = 1;
        } else {
            Ext.MessageBox.show({
                title: message.msg('workflow.title_history_log'),
                message: message.msg('workflow.msg_select_engine'),
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
     * 현재 입력한 조건으로 워크플로우 실행 이력 창 목록을 갱신한다.
     */
    onClickRefreshWorkflowButton: function () {
        var me = this;
        var refs = me.getReferences();
        var startDate = refs.startDate.getValue();
        var endDate = refs.endDate.getValue();
        var workflowHistory = query('workflowHistory');

        if (!startDate && endDate) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('hdfs.audit.date.start.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        if (startDate && !endDate) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('hdfs.audit.date.end.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        workflowHistory.getStore().load({
            params: {
                clusterName: ENGINE.id,
                startDate: startDate,
                endDate: endDate,
                status: refs.status.getValue(),
                workflowName: refs.workflowName.getValue()
            }
        });
    },

    /**
     * 워크플로우 실행 이력 창에서 실행 중인 잡을 종료한다.
     */
    onJobKillClick: function () {
        var workflowHistoryTreePanel = query('workflowHistory');
        var params = {
            clusterName: ENGINE.id,
            identifier: workflowHistoryTreePanel.getSelectionModel().getSelection()[0].get('identifier'),
            type: workflowHistoryTreePanel.getSelectionModel().getSelection()[0].get('type')
        };

        invokePostByXML(CONSTANTS.DASHBOARD.KILL, params,
            function (response) {
                var obj = Ext.decode(response.responseText);
                if (obj.success) {
                    /* FIXME 무슨 알람 창이 떠야하지 않을까?
                     Ext.create('Flamingo2.view.designer.Toast', {
                     title: obj.map.name,
                     position: 'tr',
                     width: 250,
                     jobId: obj.map.jobId,
                     slideInDelay: 600,
                     autoClose: false,
                     slideDownAnimation: 'easeIn'
                     }).show();
                     */
                } else {
                    /*
                     Ext.create('widget.uxNotification', {
                     title: message.msg('workflow.msg_fail_run'),
                     position: 'br',
                     paddingX: 50,
                     paddingY: 50,
                     width: 200,
                     autoCloseDelay: 1000,
                     slideInDelay: 100,
                     slideDownAnimation: 'easeIn',
                     html: '<div style="font-size:12px;">' + obj.error.cause + '</div>'
                     }).show();
                     */
                }
            },
            function (response) {
                /*
                 Ext.create('widget.uxNotification', {
                 title: message.msg('workflow.msg_fail_run'),
                 position: 'br',
                 paddingX: 50,
                 paddingY: 50,
                 width: 200,
                 autoCloseDelay: 1000,
                 slideInDelay: 100,
                 slideDownAnimation: 'easeIn',
                 html: '<div style="font-size:12px;">' + response.statusText + '</div>'
                 }).show();
                 */
            }
        );
    },

    destroyProgresses: function () {
        var progresses = Ext.ComponentQuery.query('progressbar[name=taskProgress]');
        Ext.each(progresses, function (progrese) {
            progrese.destroy();
        });
        $("[name=taskProgress]").remove();
    }
});