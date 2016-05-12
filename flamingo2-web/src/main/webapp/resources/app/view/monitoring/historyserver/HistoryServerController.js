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
Ext.define('Flamingo2.view.monitoring.historyserver.HistoryServerController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.historyServerController',

    onAfterrender: function (grid, opts) {
        setTimeout(function () {
            grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            grid.getStore().load({
                callback: function (records, operation, success) {
                    grid.setTitle(format(message.msg('monitoring.history.msg.finished_total'), this.getCount()));
                }
            });
        }, 10);
    },

    onTabChanged: function (tabPanel, tab) {
        var grid = query('mapReduceJobs');
        var selection = grid.getSelectionModel().getSelection()[0];
        if (selection) {
            var jobId = selection.get('id');
            var activeTab = tabPanel.getActiveTab();
            var activeTabIndex = tabPanel.items.findIndex('id', activeTab.id);

            switch (activeTabIndex) {
                case 0:
                    invokeGet(CONSTANTS.MONITORING.HS.JOB, {jobId: jobId, clusterName: ENGINE.id},
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                obj.map.durationStr = toHumanReadableTime(obj.map.duration);
                                obj.map.avgMapTimeStr = toCommaNumber(obj.map.avgMapTime);
                                obj.map.avgMergeTimeStr = toCommaNumber(obj.map.avgMergeTime);
                                obj.map.avgReduceTimeStr = toCommaNumber(obj.map.avgReduceTime < 0 ? -obj.map.avgReduceTime : obj.map.avgReduceTime); // TODO History Server BUG!!
                                obj.map.avgShuffleTimeStr = toCommaNumber(obj.map.avgShuffleTime);
                                obj.map.finishTimeStr = dateFormat2(obj.map.finishTime);
                                obj.map.startTimeStr = dateFormat2(obj.map.startTime);
                                obj.map.submitTimeStr = dateFormat2(obj.map.submitTime);
                                obj.map.failedReduceAttemptsStr = toCommaNumber(obj.map.failedReduceAttempts);
                                obj.map.killedReduceAttemptsStr = toCommaNumber(obj.map.killedReduceAttempts);
                                obj.map.failedMapAttemptsStr = toCommaNumber(obj.map.failedMapAttempts);
                                obj.map.killedMapAttemptsStr = toCommaNumber(obj.map.killedMapAttempts);
                                obj.map.reducesCompletedStr = toCommaNumber(obj.map.reducesCompleted);
                                obj.map.reducesTotalStr = toCommaNumber(obj.map.reducesTotal);
                                obj.map.mapsCompletedStr = toCommaNumber(obj.map.mapsCompleted);
                                obj.map.mapsTotalStr = toCommaNumber(obj.map.mapsTotal);
                                obj.map.successfulMapAttemptsStr = toCommaNumber(obj.map.successfulMapAttempts);
                                obj.map.successfulReduceAttemptsStr = toCommaNumber(obj.map.successfulReduceAttempts);
                                obj.map.failedMapAttemptStr = message.msg('common.fail') + ' ' + obj.map.failedMapAttemptsStr + message.msg('monitoring.yarn.tip.count') + ' / ' + message.msg('common.kill') + ' ' + obj.map.killedMapAttemptsStr + message.msg('monitoring.yarn.tip.count');
                                obj.map.failedReduceAttemptStr = message.msg('common.fail') + ' ' + obj.map.failedReduceAttemptsStr + message.msg('monitoring.yarn.tip.count') + ' / ' + message.msg('common.kill') + ' ' + obj.map.killedReduceAttemptsStr + message.msg('monitoring.yarn.tip.count');
                                obj.map.reducesStr = message.msg('common.complete') + ' ' + obj.map.reducesCompletedStr + message.msg('monitoring.yarn.tip.count') + ' / ' + message.msg('common.total') + ' ' + obj.map.reducesTotalStr + message.msg('monitoring.yarn.tip.count');
                                obj.map.mapsStr = message.msg('common.complete') + ' ' + obj.map.mapsCompletedStr + message.msg('monitoring.yarn.tip.count') + ' / ' + message.msg('common.total') + ' ' + obj.map.mapsTotalStr + message.msg('monitoring.yarn.tip.count');
                                obj.map.averageTime = 'Map' + ' ' + obj.map.avgMapTimeStr + 'ms ▶ ' + 'Shuffle' + ' ' + obj.map.avgShuffleTimeStr + 'ms ▶ ' + 'Merge' + ' ' + obj.map.avgMergeTimeStr + 'ms ▶ ' + 'Reduce' + ' ' + obj.map.avgReduceTimeStr + 'ms';

                                query('jobSummary').getForm().setValues(obj.map);
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
                    setTimeout(function () {
                        var counterGrid = query('jobCounters');
                        counterGrid.getStore().load({
                            params: {
                                jobId: jobId,
                                clusterName: ENGINE.id
                            }
                        });
                    }, 10);
                    break;
                case 2:
                    var configurationGrid = query('historyServerConfiguration');
                    configurationGrid.store.load({
                        params: {
                            jobId: jobId,
                            clusterName: ENGINE.id
                        }
                    });
                    break;
                case 3:
                    var tasksGrid = query('tasks');
                    tasksGrid.store.load({
                        params: {
                            jobId: jobId,
                            clusterName: ENGINE.id
                        }
                    });
                    break;
            }
        }
    },

    onItemClick: function (view, record, item, index, e, opts) {
        var tabpanel = query('historyServer > tabpanel');
        this.onTabChanged(tabpanel, null);
    },

    onRefreshClick: function (event, toolEl, panel) {
        var grid = query('mapReduceJobs');
        var chart = query('mapReduceSumChart > cartesian');
        grid.getStore().load({
            callback: function (records, operation, success) {
                grid.setTitle(format(message.msg('monitoring.history.msg.finished_total'), this.getCount()));
            }
        });

        chart.getStore().load();
    },

    /**
     * Engine Combobox Changed Event
     */
    onEngineChanged: function (engine) {
        var grid = query('mapReduceJobs');
        var chart = query('mapReduceSumChart > cartesian');
        grid.getStore().load({
            callback: function (records, operation, success) {
                grid.setTitle(format(message.msg('monitoring.history.msg.finished_total'), this.getCount()));
            }
        });

        chart.getStore().load();
    },

    onJobSummaryAfterrender: function (view) {
        // Table Layout의 colspan 적용시 cell간 간격 조정이되지 않는 문제를 해결하기 위해서 적용함
        setTableLayoutFixed(view);
    }
});