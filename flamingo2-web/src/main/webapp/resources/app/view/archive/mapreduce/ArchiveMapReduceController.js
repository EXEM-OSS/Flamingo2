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
Ext.define('Flamingo2.view.archive.mapreduce.ArchiveMapReduceController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.archiveMapReduceViewController',

    /**
     * Engine Combobox Changed Event
     */
    onEngineChanged: function () {
        var me = this;

        me.onArchiveMapReduceSumChartAfterRender();
        me.onArchiveMapReduceJobsAfterRender();
    },

    /**
     * 실행이 완료된 MapReduce Job의 Summary Chart 정보를 가져온다.
     */
    onArchiveMapReduceSumChartAfterRender: function () {
        var mapReduceSumChart = query('archiveMapReduceSumChart #archiveMRSumChart');

        setTimeout(function () {
            mapReduceSumChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            mapReduceSumChart.getStore().load();
        }, 10);

        // Table Layout의 colspan 적용시 cell간 간격 조정이되지 않는 문제를 해결하기 위해서 적용함
        setTableLayoutFixed(mapReduceSumChart);
    },

    /**
     * 실행이 완료된 MapReduce Job 정보를 가져온다.
     */
    onArchiveMapReduceJobsAfterRender: function () {
        var mapReduceJobsGrid = query('archiveMapReduceJobs');

        mapReduceJobsGrid.setLoading(true);
        setTimeout(function () {
            mapReduceJobsGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            mapReduceJobsGrid.getStore().load({
                callback: function (records, operation, success) {
                    if (success) {
                        mapReduceJobsGrid.setLoading(false);
                    } else {
                        mapReduceJobsGrid.setLoading(false);
                    }
                }
            });
        }, 10);
    },

    /**
     * 조회조건에 맞는 실행이 완료된 MapReduce Jobs 통계 정보를 가져온다.
     * Filter -> All, Running, Finished, Failed, Killed
     */
    onArchiveMRJobSumChartFindClick: function () {
        var archiveMapReduceSumChart = query('archiveMapReduceSumChart');
        var startDate = archiveMapReduceSumChart.down('#startDate').getValue();
        var endDate = archiveMapReduceSumChart.down('#endDate').getValue();
        var filter = archiveMapReduceSumChart.down('#filter').getValue();
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

        var archiveMRSumChart = query('archiveMapReduceSumChart #archiveMRSumChart');

        setTimeout(function () {
            archiveMRSumChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            archiveMRSumChart.getStore().getProxy().extraParams.startDate = convertedStartDate;
            archiveMRSumChart.getStore().getProxy().extraParams.endDate = convertedEndDate;
            archiveMRSumChart.getStore().getProxy().extraParams.filter = filter;
            archiveMRSumChart.getStore().load();
        }, 10);
    },

    /**
     * 실행이 완료된 MapReduce Jobs 통계 정보의 조회조건을 초기화 한다.
     */
    onArchiveMRJobSumChartResetClick: function () {
        var me = this;
        var startDateFields = query('archiveMapReduceSumChart #startDate');
        var endDateFields = query('archiveMapReduceSumChart #endDate');
        var filter = query('archiveMapReduceSumChart #filter');

        startDateFields.reset();
        endDateFields.reset();
        filter.reset();

        me.onArchiveMapReduceSumChartAfterRender();
    },

    /**
     * 실행이 완료된 MapReduce Job 통계 정보를 업데이트한다.
     */
    onArchiveMRJobSumChartRefreshClick: function () {
        var me = this;

        me.onArchiveMapReduceSumChartAfterRender();
    },

    /**
     * 조회조건에 맞는 실행이 완료된 모든 MapReduce Jobs 목록을 가져온다.
     */
    onArchiveMRJobsFindClick: function () {
        var archiveMapReduceJobsGrid = query('archiveMapReduceJobs');
        var startDate = archiveMapReduceJobsGrid.down('#startDate').getValue();
        var endDate = archiveMapReduceJobsGrid.down('#endDate').getValue();
        var filter = archiveMapReduceJobsGrid.down('#filter').getValue();
        var mrJobName = archiveMapReduceJobsGrid.down('#mrJobName').getValue();
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

        archiveMapReduceJobsGrid.setLoading(true);
        setTimeout(function () {
            archiveMapReduceJobsGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            archiveMapReduceJobsGrid.getStore().getProxy().extraParams.startDate = convertedStartDate;
            archiveMapReduceJobsGrid.getStore().getProxy().extraParams.endDate = convertedEndDate;
            archiveMapReduceJobsGrid.getStore().getProxy().extraParams.filter = filter;
            archiveMapReduceJobsGrid.getStore().getProxy().extraParams.mrJobName = mrJobName;
            archiveMapReduceJobsGrid.getStore().load({
                callback: function (records, operation, success) {
                    if (success) {
                        archiveMapReduceJobsGrid.setLoading(false);
                    } else {
                        archiveMapReduceJobsGrid.setLoading(false);
                    }
                }
            });
        }, 10);
    },

    /**
     * 실행이 완료된 MapReduce Jobs 정보의 조회조건을 초기화 한다.
     */
    onArchiveMRJobsResetClick: function () {
        var me = this;
        var startDateFields = query('archiveMapReduceJobs #startDate');
        var endDateFields = query('archiveMapReduceJobs #endDate');
        var filter = query('archiveMapReduceJobs #filter');
        var mrJobName = query('archiveMapReduceJobs #mrJobName');

        startDateFields.reset();
        endDateFields.reset();
        filter.reset();
        mrJobName.setValue('');

        query("archiveMapReduceJobs #filter").reset();
        query("archiveMapReduceJobs #mrJobName").setValue('');

        me.onArchiveMapReduceJobsAfterRender();
    },

    /**
     * 실행이 완료된 MapReduce Jobs 목록을 갱신한다.
     */
    onArchiveMapReduceJobsRefreshClick: function () {
        var me = this;

        me.onArchiveMapReduceJobsAfterRender();
    },

    /**
     * 실행이 완료된 MapReduce Job 그리드에서 선택한 아이템의 상세 정보를 활성화된 탭 필드에 보여준다.
     */
    onArchiveMRJobGridItemClick: function () {
        var me = this;
        var tabPanel = query('archiveMapReduce > tabpanel');

        me.onTabChanged(tabPanel, null);
    },

    /**
     * MapReduce에서 선택한 각 탭의 정보를 가져온다.
     *
     * @param tabPanel
     * @param tab
     */
    onTabChanged: function (tabPanel, tab) {
        var archiveMapReduceJobsGrid = query('archiveMapReduceJobs');
        var selection = archiveMapReduceJobsGrid.getSelectionModel().getSelection()[0];

        if (selection) {
            var jobId = selection.get('jobId');
            var state = selection.get('state');
            var activeTab = tabPanel.getActiveTab();
            var activeTabIndex = tabPanel.items.findIndex('id', activeTab.id);

            switch (activeTabIndex) {
                case 0:
                    var url = CONSTANTS.ARCHIVE.MAPREDUCE.GET_MR_JOB_REPORT;
                    var params = {
                        jobId: jobId,
                        state: state,
                        clusterName: ENGINE.id
                    };

                    invokeGet(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                obj.map.jobId = obj.map.job_id;
                                obj.map.durationStr = toHumanReadableTime(obj.map.duration);
                                obj.map.avgMapTimeStr = toCommaNumber(obj.map.avg_map_time);
                                obj.map.avgMergeTimeStr = toCommaNumber(obj.map.avg_merge_time);
                                obj.map.avgReduceTimeStr = toCommaNumber(obj.map.avg_reduce_time < 0 ? -obj.map.avg_reduce_time : obj.map.avg_reduce_time); // TODO History Server BUG!!
                                obj.map.avgShuffleTimeStr = toCommaNumber(obj.map.avg_shuffle_time);
                                obj.map.finishTimeStr = dateFormat2(obj.map.finish_time);
                                obj.map.startTimeStr = dateFormat2(obj.map.start_time);
                                obj.map.submitTimeStr = dateFormat2(obj.map.submit_time);
                                obj.map.failedReduceAttemptsStr = toCommaNumber(obj.map.failed_reduce_attempts);
                                obj.map.killedReduceAttemptsStr = toCommaNumber(obj.map.killed_reduce_attempts);
                                obj.map.failedMapAttemptsStr = toCommaNumber(obj.map.failed_map_attempts);
                                obj.map.killedMapAttemptsStr = toCommaNumber(obj.map.killed_map_attempts);
                                obj.map.reducesCompletedStr = toCommaNumber(obj.map.reduces_completed);
                                obj.map.reducesTotalStr = toCommaNumber(obj.map.reduces_total);
                                obj.map.mapsCompletedStr = toCommaNumber(obj.map.maps_completed);
                                obj.map.mapsTotalStr = toCommaNumber(obj.map.maps_total);
                                obj.map.successfulMapAttemptsStr = toCommaNumber(obj.map.successful_map_attempts);
                                obj.map.successfulReduceAttemptsStr = toCommaNumber(obj.map.successful_reduce_attempts);
                                obj.map.failedMapAttemptStr = message.msg('common.fail') + ' ' + obj.map.failedMapAttemptsStr + message.msg('monitoring.yarn.tip.count') + ' / ' + message.msg('common.kill') + ' ' + obj.map.killedMapAttemptsStr + message.msg('monitoring.yarn.tip.count');
                                obj.map.failedReduceAttemptStr = message.msg('common.fail') + ' ' + obj.map.failedReduceAttemptsStr + message.msg('monitoring.yarn.tip.count') + ' / ' + message.msg('common.kill') + ' ' + obj.map.killedReduceAttemptsStr + message.msg('monitoring.yarn.tip.count');
                                obj.map.reducesStr = message.msg('common.complete') + ' ' + obj.map.reducesCompletedStr + message.msg('monitoring.yarn.tip.count') + ' / ' + message.msg('common.total') + ' ' + obj.map.reducesTotalStr + message.msg('monitoring.yarn.tip.count');
                                obj.map.mapsStr = message.msg('common.complete') + ' ' + obj.map.mapsCompletedStr + message.msg('monitoring.yarn.tip.count') + ' / ' + message.msg('common.total') + ' ' + obj.map.mapsTotalStr + message.msg('monitoring.yarn.tip.count');
                                obj.map.averageTime = 'Map' + ' ' + obj.map.avgMapTimeStr + 'ms ▶ ' + 'Shuffle' + ' ' + obj.map.avgShuffleTimeStr + 'ms ▶ ' + 'Merge' + ' ' + obj.map.avgMergeTimeStr + 'ms ▶ ' + 'Reduce' + ' ' + obj.map.avgReduceTimeStr + 'ms';

                                query('archiveMapReduceJobSummary').getForm().setValues(obj.map);
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
                    var archiveMRJobCounterTree = query('archiveMapReduceJobCounters');

                    setTimeout(function () {
                        archiveMRJobCounterTree.getStore().proxy.extraParams.clusterName = ENGINE.id;
                        archiveMRJobCounterTree.getStore().proxy.extraParams.jobId = jobId;
                        archiveMRJobCounterTree.getStore().proxy.extraParams.state = state;
                        archiveMRJobCounterTree.getStore().load({
                            callback: function () {
                                archiveMRJobCounterTree.getRootNode().expand();
                                var rootNode = archiveMRJobCounterTree.getStore().getNodeById('root');
                                archiveMRJobCounterTree.getSelectionModel().select(rootNode);
                            }
                        });
                    }, 100);
                    break;
                case 2:
                    var configurationGrid = query('archiveMapReduceConfiguration');

                    configurationGrid.store.load({
                        params: {
                            clusterName: ENGINE.id,
                            jobId: jobId,
                            state: state
                        }
                    });
                    break;
                case 3:
                    var tasksGrid = query('archiveMapReduceTasks');

                    tasksGrid.store.load({
                        params: {
                            clusterName: ENGINE.id,
                            jobId: jobId,
                            state: state
                        }
                    });
                    break;
            }
        }
    }
});