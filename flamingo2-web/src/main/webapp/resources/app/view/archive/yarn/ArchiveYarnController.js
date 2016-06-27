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
Ext.define('Flamingo2.view.archive.yarn.ArchiveYarnController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.archiveYarnViewController',

    requires: [
        'Flamingo2.view.designer.editor.AceEditor'
    ],

    /**
     * Engine Combobox Changed Event
     */
    onEngineChanged: function () {
        var me = this;

        me.onArchiveYarnAppSumChartAfterRender();
        me.onArchiveYarnAllAppsAfterRender();
    },

    /**
     * Remote Repository에 저장된 Yarn Application의 Summary Chart 정보를 가져온다.
     */
    onArchiveYarnAppSumChartAfterRender: function () {
        var yarnAppChart = query('archiveYarnAppSumChart #archiveYarnAppSumChart');

        setTimeout(function () {
            yarnAppChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            yarnAppChart.getStore().load();
        }, 10);

        // Table Layout의 colspan 적용시 cell간 간격 조정이되지 않는 문제를 해결하기 위해서 적용함
        setTableLayoutFixed(yarnAppChart);
    },

    /**
     * Remote Repository에 저장된 모든 Yarn Application 목록을 가져온다.
     */
    onArchiveYarnAllAppsAfterRender: function () {
        var yarnAllAppGrid = query('archiveYarnAllApplications');

        yarnAllAppGrid.setLoading(true);
        setTimeout(function () {
            yarnAllAppGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            yarnAllAppGrid.getStore().load({
                callback: function (records, operation, success) {
                    if (success) {
                        yarnAllAppGrid.setLoading(false);
                    } else {
                        yarnAllAppGrid.setLoading(false);
                    }
                }
            });
        }, 10);
    },

    /**
     * 조회조건에 맞는 Yarn 애플리케이션 통계 정보를 가져온다.
     * Filter -> All, Running, Finished, Failed, Killed
     */
    onArchiveYarnAppSumChartFindClick: function () {
        var archiveYarnAppSumChart = query('archiveYarnAppSumChart');
        var startDate = archiveYarnAppSumChart.down('#startDate').getValue();
        var endDate = archiveYarnAppSumChart.down('#endDate').getValue();
        var filter = archiveYarnAppSumChart.down('#filter').getValue();
        var convertedStartDate = '';
        var convertedEndDate = '';

        /**
         * 1. 시작일 미입력
         * 2. 종료일 미입력
         * 3. 시작일 and 종료일 미입력 -> ex. 1970-01-01 09:00:00 ~ Current Time
         * 4. 시작일 종료일 입력
         */
        if (!startDate && endDate) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('hdfs.audit.date.start.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        } else if (startDate && !endDate) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('hdfs.audit.date.end.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        } else if (!startDate && !endDate) {
            convertedStartDate = startDate;
            convertedEndDate = endDate;
        } else {
            var start = dateFormat(startDate, 'yyyy-MM-dd');
            var end = dateFormat(endDate, 'yyyy-MM-dd');

            // 시작일과 종료일 당일 일때 -> ex. 2015.05.05 00:00:00 ~ Current Time
            /**
             * 4.1 시작일과 종료일이 같을 때
             *  4.1.1 시작일과 종료일이 오늘일 때 -> ex. 2015.05.05 00:00:00 ~ Current Time
             *  4.1.2 시작일과 종료일이 특정 날짜일 때 -> ex. 2015.05.05 00:00:00 ~ 2015.05.05 23:59:59
             * 4.2 시작일이 종료일보다 빠를 때
             * 4.3 시작일이 종료일보다 늦을 때
             */
            if (start == end) {
                var today = dateFormat(new Date(), 'yyyy-MM-dd');

                convertedStartDate = startDate;
                convertedEndDate = start == today ? new Date() : new Date(endDate.setHours(23, 59, 59, 999));
            } else if (start > end) {
                Ext.MessageBox.show({
                    title: message.msg('common.notice'),
                    message: message.msg('hdfs.audit.date.start.msg'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
                return false;
            } else {
                convertedStartDate = startDate;
                convertedEndDate = endDate;
            }
        }

        var yarnAppChart = query('archiveYarnAppSumChart #archiveYarnAppSumChart');

        setTimeout(function () {
            yarnAppChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            yarnAppChart.getStore().getProxy().extraParams.startDate = convertedStartDate;
            yarnAppChart.getStore().getProxy().extraParams.endDate = convertedEndDate;
            yarnAppChart.getStore().getProxy().extraParams.filter = filter;
            yarnAppChart.getStore().load();
        }, 10);
    },

    /**
     * Yarn 애플리케이션 통계 정보의 조회조건을 초기화 한다.
     */
    onArchiveYarnAppSumChartResetClick: function () {
        var me = this;
        var startDateFields = query('archiveYarnAppSumChart #startDate');
        var endDateFields = query('archiveYarnAppSumChart #endDate');
        var filter = query('archiveYarnAppSumChart #filter');

        startDateFields.reset();
        endDateFields.reset();
        filter.reset();

        me.onArchiveYarnAppSumChartAfterRender();
    },

    /**
     * Yarn Application의 Summary Chart 정보를 업데이트한다.
     */
    onArchiveYarnAppSumChartRefreshClick: function () {
        var me = this;

        me.onArchiveYarnAppSumChartAfterRender();
    },

    /**
     * 조회조건에 맞는 모든 Yarn 애플리케이션 목록을 가져온다.
     */
    onArchiveYarnAllAppsFindClick: function () {
        var yarnAllAppGrid = query('archiveYarnAllApplications');
        var startDate = yarnAllAppGrid.down('#startDate').getValue();
        var endDate = yarnAllAppGrid.down('#endDate').getValue();
        var filter = yarnAllAppGrid.down('#filter').getValue();
        var appName = yarnAllAppGrid.down('#appName').getValue();
        var convertedStartDate = '';
        var convertedEndDate = '';

        /**
         * 1. 시작일 미입력
         * 2. 종료일 미입력
         * 3. 시작일 and 종료일 미입력 -> ex. 1970-01-01 09:00:00 ~ Current Time
         * 4. 시작일 종료일 입력
         */
        if (!startDate && endDate) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('hdfs.audit.date.start.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        } else if (startDate && !endDate) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('hdfs.audit.date.end.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        } else if (!startDate && !endDate) {
            convertedStartDate = startDate;
            convertedEndDate = endDate;
        } else {
            var start = dateFormat(startDate, 'yyyy-MM-dd');
            var end = dateFormat(endDate, 'yyyy-MM-dd');

            // 시작일과 종료일 당일 일때 -> ex. 2015.05.05 00:00:00 ~ Current Time
            /**
             * 4.1 시작일과 종료일이 같을 때
             *  4.1.1 시작일과 종료일이 오늘일 때 -> ex. 2015.05.05 00:00:00 ~ Current Time
             *  4.1.2 시작일과 종료일이 특정 날짜일 때 -> ex. 2015.05.05 00:00:00 ~ 2015.05.05 23:59:59
             * 4.2 시작일이 종료일보다 빠를 때
             * 4.3 시작일이 종료일보다 늦을 때
             */
            if (start == end) {
                var today = dateFormat(new Date(), 'yyyy-MM-dd');

                convertedStartDate = startDate;
                convertedEndDate = start == today ? new Date() : new Date(endDate.setHours(23, 59, 59, 999));
            } else if (start > end) {
                Ext.MessageBox.show({
                    title: message.msg('common.notice'),
                    message: message.msg('hdfs.audit.date.start.msg'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
                return false;
            } else {
                convertedStartDate = startDate;
                convertedEndDate = endDate;
            }
        }

        yarnAllAppGrid.setLoading(true);
        setTimeout(function () {
            yarnAllAppGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            yarnAllAppGrid.getStore().getProxy().extraParams.startDate = convertedStartDate;
            yarnAllAppGrid.getStore().getProxy().extraParams.endDate = convertedEndDate;
            yarnAllAppGrid.getStore().getProxy().extraParams.filter = filter;
            yarnAllAppGrid.getStore().getProxy().extraParams.appName = appName;
            yarnAllAppGrid.getStore().load({
                callback: function (records, operation, success) {
                    if (success) {
                        yarnAllAppGrid.setLoading(false);
                    } else {
                        yarnAllAppGrid.setLoading(false);
                    }
                }
            });
        }, 10);
    },

    /**
     * Yarn 애플리케이션 통계 정보의 조회조건을 초기화 한다.
     */
    onArchiveYarnAllAppsResetClick: function () {
        var me = this;
        var startDateFields = query('archiveYarnAllApplications #startDate');
        var endDateFields = query('archiveYarnAllApplications #endDate');
        var filter = query('archiveYarnAllApplications #filter');
        var appName = query('archiveYarnAllApplications #appName');

        startDateFields.reset();
        endDateFields.reset();
        filter.reset();
        appName.setValue('');

        me.onArchiveYarnAllAppsAfterRender();
    },

    /**
     * Yarn Application의 모든 Yarn Application 정보를 업데이트한다.
     */
    onArchiveYarnAllAppsRefreshClick: function () {
        var me = this;

        me.onArchiveYarnAllAppsAfterRender();
    },

    onDownloadLogClick: function () {
        var grid = query('archiveYarnAllApplications');
        var selection = grid.getSelectionModel().getSelection()[0];

        if (selection && (selection.get('yarnApplicationState') == 'FINISHED' ||
            selection.get('yarnApplicationState') == 'FAILED' ||
            selection.get('yarnApplicationState') == 'KILLED')) {

            var applicationId = selection.get('applicationId');
            var url = CONSTANTS.MONITORING.RM.APP_DOWNLOAD;

            Ext.core.DomHelper.append(document.body, {
                tag: 'iframe',
                id: 'testIframe' + new Date().getTime(),
                css: 'display:none;visibility:hidden;height:0px;',
                src: url + '?applicationId=' + applicationId + '&appOwner=' + selection.data.user + '&clusterName=' + ENGINE.id,
                frameBorder: 0,
                width: 0,
                height: 0
            });
        } else {
            Ext.MessageBox.show({
                title: message.msg('common.warn'),
                message: message.msg('monitoring.yarn.cannot_show_run_app_log'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    onShowApplicationLogClick: function () {
        var grid = query('archiveYarnAllApplications');
        var selection = grid.getSelectionModel().getSelection()[0];

        if (selection) {
            var applicationId = selection.get('applicationId');
            var url = CONSTANTS.MONITORING.RM.APP_LOG;
            var params = {
                applicationId: applicationId,
                appOwner: selection.data.user,
                clusterName: ENGINE.id
            };

            invokeGet(url, params,
                function (response) {
                    Ext.create('Ext.window.Window', {
                        title: message.msg('monitoring.yarn.app_log'),
                        modal: false,
                        width: 900,
                        height: 650,
                        layout: 'fit',
                        items: [
                            {
                                border: true,
                                layout: 'fit',
                                xtype: 'aceEditor',
                                parser: 'plain_text',
                                highlightActiveLine: false,
                                highlightGutterLine: false,
                                highlightSelectedWord: false,
                                forceFit: true,
                                theme: 'eclipse',
                                printMargin: false,
                                readOnly: true,
                                value: response.responseText,
                                listeners: {
                                    afterrender: function (comp) {
                                        // Hide toolbar
                                        comp.down('toolbar').setVisible(false);
                                    }
                                }
                            }
                        ]
                    }).center().show();
                },
                function () {
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            );
        }
    },

    onShowApplicationMasterClick: function () {
        var grid = query('archiveYarnAllApplications');
        var selection = grid.getSelectionModel().getSelection()[0];
        if (selection) {
            var url = selection.get('trackingUrl');

            // 애플리케이션 마스터 정보가 없으면 에러창을 표시한다.
            if (url && url == 'N/A') {
                Ext.MessageBox.show({
                    title: message.msg('common.warn'),
                    message: message.msg(''),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            } else {
                Ext.create('Ext.window.Window', {
                    title: message.msg('monitoring.application_master'),
                    modal: false,
                    maximizable: true,
                    resizable: true,
                    width: 800,
                    height: 600,
                    layout: 'fit',
                    items: [
                        {
                            xtype: 'panel',
                            flex: 1,
                            closable: false,
                            showCloseOthers: false,
                            showCloseAll: false,
                            type: 'help',
                            forceFit: true,
                            printMargin: true,
                            html: '<iframe style="overflow:auto;width:100%;height:100%;" frameborder="0"  src="' + url + '"></iframe>',
                            border: false,
                            autoScroll: true
                        }
                    ]
                }).center().show();
            }
        }
    },

    /**
     * Yarn Application에서 모든 Yarn Application 그리드에서 선택한 아이템의 정보를 가져온다.
     */
    onArchiveYarnAllAppsGridItemClick: function()  {
        var me = this;
        var tabPanel = query('archiveYarnApplication > tabpanel');

        me.onTabChanged(tabPanel, null);
    },

    /**
     * Yarn Application에서 모든 Yarn Application 그리드에서 선택한 아이템에 해당하는 각각의 탭 정보를 가져온다.
     *
     * @param tabPanel
     * @param tab
     */
    onTabChanged: function (tabPanel, tab) {
        var grid = query('archiveYarnAllApplications');
        var selection = grid.getSelectionModel().getSelection()[0];

        if (selection) {
            var applicationId = selection.data.applicationId;
            var activeTab = tabPanel.getActiveTab();
            var activeTabIndex = tabPanel.items.findIndex('id', activeTab.id);
            var url = '';
            var params = '';
            var start = '';
            var end = '';
            var diff = '';

            switch (activeTabIndex) {
                case 0:
                    url = CONSTANTS.ARCHIVE.YARN.GET_APP_REPORT;
                    params = {
                        applicationId: applicationId,
                        clusterName: ENGINE.id
                    };

                    invokeGet(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                // 실행중인 경우와 그렇지 않은 경우 elapsedTime을 별도로 계산한다.
                                if (obj.map.yarn_application_state == 'FINISHED' ||
                                    obj.map.yarn_application_state == 'FAILED' ||
                                    obj.map.yarn_application_state == 'KILLED') {
                                    start = new Date(obj.map.start_time);
                                    end = new Date(obj.map.finish_time);
                                    diff = (end.getTime() - start.getTime()) / 1000;

                                    obj.map.elapsed_time = toHumanReadableTime(Math.floor(diff));
                                } else {
                                    obj.map.elapsed_time = '';
                                }

                                // 이미 종료된 애플리케이션은 값이 -1이 나온다.
                                if (obj.map.needed_resources_vcores != -1) {
                                    obj.map.num_vcore =
                                        message.msg('common.total') + ' ' + toCommaNumber(obj.map.vcore_seconds) + message.msg('monitoring.yarn.tip.count') +
                                        ' / ' +
                                        message.msg('common.require') + ' ' + toCommaNumber(obj.map.needed_resources_vcores) + message.msg('monitoring.yarn.tip.count') +
                                        ' / ' +
                                        message.msg('common.use') + ' ' + toCommaNumber(obj.map.used_resources_vcores) + message.msg('monitoring.yarn.tip.count') +
                                        ' / ' +
                                        message.msg('common.reserve') + ' ' + toCommaNumber(obj.map.reserved_resources_vcores) + message.msg('monitoring.yarn.tip.count');
                                } else {
                                    obj.map.num_vcore = message.msg('common.use') + ' ' + ' ' + toCommaNumber(obj.map.vcore_seconds) + message.msg('monitoring.yarn.tip.count');
                                }

                                // 이미 종료된 애플리케이션은 값이 -1이 나온다.
                                if (obj.map.needed_resources_memory != -1) {
                                    obj.map.num_memory =
                                        message.msg('common.total') + ' ' + fileSize(obj.map.memory_seconds * 1024 * 1024) +
                                        ' / ' +
                                        message.msg('common.require') + ' ' + fileSize(obj.map.needed_resources_memory * 1024 * 1024) +
                                        ' / ' +
                                        message.msg('common.use') + ' ' + fileSize(obj.map.used_resources_memory * 1024 * 1024) +
                                        ' / ' +
                                        message.msg('common.reserve') + ' ' + fileSize(obj.map.reserved_resources_memory * 1024 * 1024);
                                } else {
                                    obj.map.num_memory = message.msg('common.use') + ' ' + ' ' + fileSize(obj.map.memory_seconds * 1024 * 1024);
                                }

                                // 이미 종료된 애플리케이션은 값이 -1이 나온다.
                                if (obj.map.num_used_containers != -1) {
                                    obj.map.num_container =
                                        message.msg('common.use') + ' ' + toCommaNumber(obj.map.num_used_containers) + message.msg('monitoring.yarn.tip.count') +
                                        ' / ' +
                                        message.msg('common.reserve') + ' ' + toCommaNumber(obj.map.num_reserved_containers) + message.msg('monitoring.yarn.tip.count');
                                } else {
                                    obj.map.num_container = '';
                                }

                                query('archiveYarnAppSummary').getForm().setValues(obj.map);
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.warn'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }

                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warn'),
                                message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                    break;
                case 1:
                    var logViewer = query('archiveYarnApplication #logviewer');

                    // 일단 선택하면 로그 패널의 내용을 모두 삭제한다.
                    logViewer.setValue('');

                    url = CONSTANTS.MONITORING.RM.APP_LOG;
                    params = {
                        applicationId: applicationId,
                        appOwner: selection.data.user,
                        clusterName: ENGINE.id
                    };

                    // 서버를 호출하여 애플리케이션 로그를 로그 패널에 추가한다.
                    invokeGet(url, params,
                        function (response) {
                            logViewer.setValue(response.responseText);
                        },
                        function (response) {
                            logViewer.setValue(response.responseText);
                        }
                    );
                    break;
                case 2:
                    if (selection.get('trackingUrl') && selection.get('trackingUrl') == 'N/A') {
                        query('archiveYarnApplication #applicationMaster').body.update('');
                    } else {
                        var html = '<iframe style="overflow:auto;width:100%;height:100%;" frameborder="0" src="' + selection.get('trackingUrl') + '"></iframe>'
                        var panel = query('archiveYarnApplication #applicationMaster');
                        panel.body.update(html);
                    }
                    break;
            }
        }
    }
});