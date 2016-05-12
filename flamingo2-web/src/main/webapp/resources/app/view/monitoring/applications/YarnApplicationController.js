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
Ext.define('Flamingo2.view.monitoring.applications.YarnApplicationController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.yarnApplicationController',

    requires: [
        'Flamingo2.view.fs.hdfs.File'
    ],

    onAfterrender: function (grid, opts) {
        setTimeout(function () {
            grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            grid.getStore().load({
                callback: function (records, operation, success) {
                    grid.setTitle(format(message.msg('monitoring.msg.all_yarn_app_total'), this.getCount()))
                }
            });
        }, 10);
    },

    onTabChanged: function (tabPanel, tab) {
        var grid = query('allApplications');
        var selection = grid.getSelectionModel().getSelection()[0];
        if (selection) {
            var applicationId = selection.data.applicationId;
            var activeTab = tabPanel.getActiveTab();
            var activeTabIndex = tabPanel.items.findIndex('id', activeTab.id);

            switch (activeTabIndex) {
                case 0:
                    invokeGet('/monitoring/resourcemanager/app/report.json', {
                            applicationId: applicationId,
                            clusterName: ENGINE.id
                        },
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {

                                // 실행중인 경우와 그렇지 않은 경우 elapsedTime을 별도로 계산한다.
                                if (obj.map.yarnApplicationState == 'RUNNING') {
                                    var start = new Date(obj.map.startTime);
                                    var end = new Date();
                                    var diff = (end.getTime() - start.getTime()) / 1000;
                                    obj.map.elapsedTime = toHumanReadableTime(Math.floor(diff));
                                } else if (
                                    obj.map.yarnApplicationState == 'FINISHED' ||
                                    obj.map.yarnApplicationState == 'FAILED' ||
                                    obj.map.yarnApplicationState == 'KILLED') {
                                    var start = new Date(obj.map.startTime);
                                    var end = new Date(obj.map.finishTime);
                                    var diff = (end.getTime() - start.getTime()) / 1000;
                                    obj.map.elapsedTime = toHumanReadableTime(Math.floor(diff));
                                } else {
                                    obj.map.elapsedTime = '';
                                }

                                // 이미 종료된 애플리케이션은 값이 -1이 나온다.
                                if (obj.map.neededResourcesVcores != -1) {
                                    obj.map.numVcore =
                                        message.msg('common.total') + ' ' + toCommaNumber(obj.map.vcoreSeconds) + message.msg('monitoring.yarn.tip.count') +
                                        ' / ' +
                                        message.msg('common.require') + ' ' + toCommaNumber(obj.map.neededResourcesVcores) + message.msg('monitoring.yarn.tip.count') +
                                        ' / ' +
                                        message.msg('common.use') + ' ' + toCommaNumber(obj.map.usedResourcesVcores) + message.msg('monitoring.yarn.tip.count') +
                                        ' / ' +
                                        message.msg('common.reserve') + ' ' + toCommaNumber(obj.map.reservedResourcesVcores) + message.msg('monitoring.yarn.tip.count');
                                } else {
                                    obj.map.numVcore = message.msg('common.use') + ' ' + ' ' + toCommaNumber(obj.map.vcoreSeconds) + message.msg('monitoring.yarn.tip.count');
                                }

                                // 이미 종료된 애플리케이션은 값이 -1이 나온다.
                                if (obj.map.neededResourcesMemory != -1) {
                                    obj.map.numMemory =
                                        message.msg('common.total') + ' ' + fileSize(obj.map.memorySeconds * 1024 * 1024) +
                                        ' / ' +
                                        message.msg('common.require') + ' ' + fileSize(obj.map.neededResourcesMemory * 1024 * 1024) +
                                        ' / ' +
                                        message.msg('common.use') + ' ' + fileSize(obj.map.usedResourcesMemory * 1024 * 1024) +
                                        ' / ' +
                                        message.msg('common.reserve') + ' ' + fileSize(obj.map.reservedResourcesMemory * 1024 * 1024);
                                } else {
                                    obj.map.numMemory = message.msg('common.use') + ' ' + ' ' + fileSize(obj.map.memorySeconds * 1024 * 1024);
                                }

                                // 이미 종료된 애플리케이션은 값이 -1이 나온다.
                                if (obj.map.numUsedContainers != -1) {
                                    obj.map.numContainer =
                                        message.msg('common.use') + ' ' + toCommaNumber(obj.map.numUsedContainers) + message.msg('monitoring.yarn.tip.count') +
                                        ' / ' +
                                        message.msg('common.reserve') + ' ' + toCommaNumber(obj.map.numReservedContainers) + message.msg('monitoring.yarn.tip.count');
                                }

                                query('applicationSummary').getForm().setValues(obj.map);
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.warn'),
                                    message: obj.error.message,
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
                    break;
                case 1:
                    var logviewer = query('yarnApplication #logviewer');
                    if (selection.data.yarnApplicationState == 'RUNNING') {
                        logviewer.setValue(message.msg(''));
                    } else {
                        // 일단 선택하면 로그 패널의 내용을 모두 삭제한다.
                        logviewer.setValue('');

                        // 서버를 호출하여 애플리케이션 로그를 로그 패널에 추가한다.
                        invokeGet('/monitoring/resourcemanager/app/log.json', {
                                applicationId: applicationId,
                                appOwner: selection.data.user,
                                clusterName: ENGINE.id
                            },
                            function (response) {
                                var logviewer = query('yarnApplication #logviewer');
                                logviewer.setValue(response.responseText);
                            },
                            function (response) {
                                var logviewer = query('yarnApplication #logviewer');
                                logviewer.setValue(response.responseText);
                            }
                        );
                    }
                    break;
                case 2:
                    if (selection.get('trackingUrl') && selection.get('trackingUrl') == 'N/A') {
                        query('yarnApplication #applicationMaster').body.update('');
                    } else {
                        var html = '<iframe style="overflow:auto;width:100%;height:100%;" frameborder="0" src="' + selection.get('trackingUrl') + '"></iframe>'
                        var panel = query('yarnApplication #applicationMaster');
                        panel.body.update(html);
                    }
                    break;
            }
        }
    },

    onDownloadLogClick: function () {
        var grid = query('allApplications');
        var selection = grid.getSelectionModel().getSelection()[0];
        if (selection && (
            selection.get('yarnApplicationState') == 'FINISHED' ||
            selection.get('yarnApplicationState') == 'FAILED' ||
            selection.get('yarnApplicationState') == 'KILLED')) {
            var applicationId = selection.get('applicationId');
            Ext.core.DomHelper.append(document.body, {
                tag: 'iframe',
                id: 'testIframe' + new Date().getTime(),
                css: 'display:none;visibility:hidden;height:0px;',
                src: '/monitoring/resourcemanager/app/download' + '?applicationId=' + applicationId + '&appOwner=' + selection.data.user + '&clusterName=' + ENGINE.id,
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
        var grid = query('allApplications');
        var selection = grid.getSelectionModel().getSelection()[0];
        if (selection) {
            var applicationId = selection.get('applicationId');

            if (selection.get('yarnApplicationState') == 'RUNNING') {
                Ext.MessageBox.show({
                    title: message.msg('common.warn'),
                    message: message.msg('monitoring.yarn.cannot_show_run_app_log'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            } else {
                invokeGet('/monitoring/resourcemanager/app/log.json', {
                        applicationId: applicationId,
                        appOwner: selection.data.user,
                        clusterName: ENGINE.id
                    },
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
                                    xtype: 'queryEditor',
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
                    function (response) {
                        Ext.MessageBox.show({
                            title: message.msg('common.warn'),
                            message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    }
                );
            }
        }
    },

    onShowApplicationMasterClick: function () {
        var grid = query('allApplications');
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

    onItemClick: function (view, record, item, index, e, opts) {
        var tabpanel = query('yarnApplication > tabpanel');
        this.onTabChanged(tabpanel, null);
    },

    onRefreshClick: function (event, toolEl, panel) {
        var grid = query('allApplications');
        var chart = query('applicationSumChart > cartesian');
        grid.getStore().load({
            callback: function (records, operation, success) {
                grid.setTitle(format(message.msg('monitoring.msg.all_yarn_app_total'), this.getCount()))
            }
        });

        chart.getStore().load();
    },

    /**
     * Engine Combobox Changed Event
     */
    onEngineChanged: function (engine) {
        var grid = query('allApplications');
        var chart = query('applicationSumChart > cartesian');

        grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
        grid.getStore().load({
            callback: function (records, operation, success) {
                grid.setTitle(format(message.msg('monitoring.msg.all_yarn_app_total'), this.getCount()))
            }
        });

        chart.getStore().load();
    },

    onApplicationSummaryAfterrender: function (view) {
        // Table Layout의 colspan 적용시 cell간 간격 조정이되지 않는 문제를 해결하기 위해서 적용함
        setTableLayoutFixed(view);
    },

    onItemContextMenu: function (grid, record, item, index, event) {
        var me = this;
        var applicationId = record.get('applicationId');

        event.stopEvent();

        if (me.contextMenu) {
            me.contextMenu.close();
            me.contextMenu = undefined;
        }
        me.contextMenu = new Ext.menu.Menu({
            items: [
                {
                    text: message.msg('common.kill'),
                    iconCls: 'common-kill',
                    handler: function () {
                        Ext.MessageBox.show({
                            title: message.msg('monitoring.application_kill'),
                            message: message.msg('monitoring.yarn.msg.kill_app'),
                            buttons: Ext.MessageBox.YESNO,
                            icon: Ext.MessageBox.WARNING,
                            fn: function handler(btn) {
                                if (btn == 'yes') {
                                    invokeGet('/monitoring/resourcemanager/app/kill.json', {
                                            applicationId: record.get('applicationId'),
                                            clusterName: ENGINE.id
                                        },
                                        function (response) {
                                            var obj = Ext.decode(response.responseText);
                                            if (obj.success) {
                                                Ext.MessageBox.show({
                                                    title: message.msg('common.confirm'),
                                                    message: message.msg('monitoring.yarn.msg.kill_app_finish'),
                                                    buttons: Ext.MessageBox.OK,
                                                    icon: Ext.MessageBox.INFO
                                                });
                                            } else {
                                                Ext.MessageBox.show({
                                                    title: message.msg('common.warn'),
                                                    message: obj.error.message,
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
                                }
                            }
                        });
                    }
                }, '-',
                {
                    text: message.msg('monitoring.application_move'),
                    iconCls: 'common-import',
                    menu: {
                        items: Ext.create('Flamingo2.view.monitoring.applications.Queue', {
                            title: message.msg('monitoring.application_move_queue'),
                            height: 200,
                            width: 200,
                            listeners: {
                                afterrender: function (comp, eOpts) {
                                    comp.getStore().load({
                                        params: {
                                            clusterName: ENGINE.id
                                        },
                                        callback: function (records, operation, success) {
                                            comp.getRootNode().expand();
                                        }
                                    });
                                },
                                itemclick: function (view, record, item, index, e, eOpts) {
                                    me.contextMenu.close();
                                    me.contextMenu = undefined;

                                    invokeGet('/monitoring/resourcemanager/app/move.json', {
                                            applicationId: applicationId,
                                            queue: record.get('queue'),
                                            clusterName: ENGINE.id
                                        },
                                        function (response) {
                                            var res = Ext.decode(response.responseText);
                                            if (res.success) {
                                                Ext.MessageBox.show({
                                                    title: message.msg('common.confirm'),
                                                    message: message.msg('monitoring.yarn.msg.app_moved'),
                                                    buttons: Ext.MessageBox.OK,
                                                    icon: Ext.MessageBox.INFO
                                                });
                                            } else {
                                                Ext.MessageBox.show({
                                                    title: message.msg('common.warn'),
                                                    message: obj.error.message,
                                                    buttons: Ext.MessageBox.OK,
                                                    icon: Ext.MessageBox.WARNING
                                                });
                                            }
                                        },
                                        function (response) {
                                            console.log(response);
                                            Ext.MessageBox.show({
                                                title: message.msg('common.warn'),
                                                message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                                                buttons: Ext.MessageBox.OK,
                                                icon: Ext.MessageBox.WARNING
                                            });
                                        }
                                    );
                                }
                            }
                        })
                    }
                }
            ]
        });

        if (record.get('yarnApplicationState') == 'RUNNING') {
            me.contextMenu.showAt(event.pageX - 5, event.pageY - 5);
        }
    }
});