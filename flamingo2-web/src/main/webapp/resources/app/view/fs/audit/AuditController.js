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
Ext.define('Flamingo2.view.fs.audit.AuditController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.auditController',

    /**
     * 현재 날짜를 기준으로 해당되는 달의 첫번째 날짜 정보를 가져온다.
     * @returns {Date}
     */
    onGetFirstDay: function () {
        var today = new Date();
        return new Date(today.getFullYear(), today.getMonth(), 1);
    },

    /**
     * Audit Top 10 목록을 화면에 출력한다.
     * 기본 페이지에 출력되는 범위는 매달 1일 ~ 현재 날짜를 기준으로 출력됨.
     */
    onAuditTop10AfterRender: function () {
        var me = this;
        var firstDay = me.onGetFirstDay();
        var auditTop10Grid = query('auditTop10');

        setTimeout(function () {
            auditTop10Grid.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    startDate: firstDay,
                    endDate: new Date(),
                    auditType: 'ACT'
                }
            });
        }, 10);
    },

    /**
     * Audit Now Status 정보를 화면에 출력한다.
     * 기본 페이지에 출력되는 범위는 매달 1일 ~ 현재 날짜를 기준으로 출력됨.
     */
    onAuditNowStatusAfterRender: function () {
        var me = this;
        var firstDay = me.onGetFirstDay();
        var auditNowStatusChart = query('auditNowStatus');

        setTimeout(function () {
            auditNowStatusChart.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    startDate: firstDay,
                    endDate: new Date(),
                    auditType: 'ACT'
                }
            });
        }, 10);
    },

    /**
     * Audit Trend 정보를 화면에 출력한다.
     * 기본 페이지에 출력되는 범위는 매달 1일 ~ 현재 날짜를 기준으로 출력됨.
     */
    onAuditTrendAfterRender: function (chart) {
        var me = this;
        var firstDay = me.onGetFirstDay();

        setTimeout(function () {
            chart.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    startDate: firstDay,
                    endDate: new Date(),
                    searchType: 'ACT'
                }
            });
        }, 10);

        var series = [];

        /**
         * Audit Type 개수 만큼 Y 필드 생성.
         */
        for (var i = 0; i < 11; i++) {
            series.push({
                type: 'bar',
                axis: 'left',
                xField: 'time',
                yField: 'data' + (i),
                marker: {
                    opacity: 0,
                    scaling: 0.01,
                    fx: {
                        duration: 200,
                        easing: 'easeOut'
                    }
                },
                highlight: {
                    strokeStyle: 'black',
                    fillStyle: 'gold',
                    lineDash: [5, 3]
                },
                tooltip: {
                    trackMouse: true,
                    style: 'background: #fff',
                    renderer: function (storeItem, item) {
                        var obj = new Object(storeItem.store.proxy.reader.rawData.map);
                        var keys = Object.keys(obj);

                        for (var i = 0; i < keys.length; i++) {
                            if (obj[keys[i]] == item.field) {
                                this.setHtml(format(message.msg('hdfs.audit.group.grid'), keys[i], storeItem.get(item.field)));
                                break;
                            }
                        }
                    }
                }
            });
        }

        chart.setSeries(series);
    },

    /**
     * Trend Chart's Series Dynamic Creation Event
     */
    /*    onAuditTrendLoad: function (store, records, successful, eOpts) {
     var auditTrend = query('auditTrend series');
     var obj = new Object(eOpts.getResultSet().message);
     var keys = Object.keys(obj);

     for (var i = 0; i < 10; i++) {
     if (i < keys.length) {
     auditTrend[i].setHidden(false);
     } else {
     auditTrend[i].setHidden(true);
     }
     }
     },*/

    /**
     * Audit Log 목록을 화면에 출력한다.
     * 기본 페이지에 출력되는 범위는 모든 로그 정보가 페이지 단위로 출력됨.
     */
    onAuditListAfterRender: function (grid) {
        setTimeout(function () {
            grid.getStore().load({
                params: {
                    clusterName: ENGINE.id
                }
            });
        }, 10);
    },

    /**
     * HDFS Audit Log의 통계 조건에서 조회조건 콤보 박스에서 선택한 조건의 결과값을 가져온다.
     */
    onAuditChartSelect: function (combo) {
        var dateFields = query('auditChart');
        var startDate = dateFields.down('#startDateAuditChart').getValue();
        var endDate = dateFields.down('#endDateAuditChart').getValue();
        var searchType = combo.value;
        var auditTop10 = query('auditTop10');
        var auditNowStatus = query('auditNowStatus');
        var auditTrend = query('auditTrend');

        auditTop10.getStore().load({
            params: {
                clusterName: ENGINE.id,
                startDate: startDate,
                endDate: endDate,
                searchType: searchType
            }
        });

        auditNowStatus.getStore().load({
            params: {
                clusterName: ENGINE.id,
                startDate: startDate,
                endDate: endDate,
                searchType: searchType
            }
        });

        auditTrend.getStore().load({
            params: {
                clusterName: ENGINE.id,
                startDate: startDate,
                endDate: endDate,
                searchType: searchType
            }
        });
    },

    /**
     * HDFS Audit Log의 시작일 및 종료일 Date Fields에서 선택한 날짜의 결과값을 가져온다.
     */
    onAuditChartFindClick: function () {
        var dateFields = query('auditChart');
        var startDate = dateFields.down('#startDateAuditChart').getValue();
        var endDate = dateFields.down('#endDateAuditChart').getValue();
        var searchType = dateFields.down('#type').getValue();
        var convertedStartDate = '';
        var convertedEndDate = '';

        /**
         * 1. 시작일 미입력
         * 2. 종료일 미입력
         * 3. 시작일 and 종료일 미입력 -> ex. 1970-01-01 09-00-00 ~ Current Time
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

        var auditTop10 = query('auditTop10');
        auditTop10.getStore().reload({
            params: {
                clusterName: ENGINE.id,
                startDate: convertedStartDate,
                endDate: convertedEndDate,
                searchType: searchType
            }
        });

        var auditNowStatus = query('auditNowStatus');
        auditNowStatus.getStore().reload({
            params: {
                clusterName: ENGINE.id,
                startDate: convertedStartDate,
                endDate: convertedEndDate,
                searchType: searchType
            }
        });

        var auditTrend = query('auditTrend');
        auditTrend.setTheme('yellow-gradients');

        auditTrend.getStore().reload({
            params: {
                clusterName: ENGINE.id,
                startDate: convertedStartDate,
                endDate: convertedEndDate,
                searchType: searchType
            }
        });
    },

    /**
     * Audit Chart's Reset Button Click Event
     */
    onAuditChartResetClick: function () {
        var dateFields = query('auditChart #datefield');
        for (var i = 0; i < dateFields.length; i++) {
            dateFields[i].setValue(null);
            dateFields[i].setMaxValue(new Date());
            dateFields[i].setMinValue(null);
        }
    },

    /**
     * Audit Chart's Find Button Click Event
     */
    onAuditFindClick: function () {
        var me = this;
        var refs = me.getReferences();
        var startDate = refs.startDate.getValue();
        var endDate = refs.endDate.getValue();
        var type = refs.type.getValue();
        var path = refs.path.getValue();
        var auditGridToolbar = query('auditGrid #auditGridToolbar');
        var auditGrid = query('auditGrid');
        var convertedStartDate = '';
        var convertedEndDate = '';

        /**
         * 1. 시작일 미입력
         * 2. 종료일 미입력
         * 3. 시작일 and 종료일 미입력 -> ex. 1970-01-01 09-00-00 ~ Current Time
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

        auditGridToolbar.moveFirst();
        setTimeout(function () {
            auditGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    startDate: convertedStartDate,
                    endDate: convertedEndDate,
                    auditType: type,
                    path: path,
                    nextPage: 0
                }
            })
        }, 10);
    },

    /**
     * Audit Chart's Reset Button Click Event
     */
    onAuditResetClick: function () {
        var startDateFields = query('auditGrid #gridStartDate');
        var endDateFields = query('auditGrid #gridEndDate');
        var auditGrid = query('auditGrid');

        startDateFields.reset();
        endDateFields.reset();
        query("auditGrid #type").reset();
        query("auditGrid #path").setValue('');

        setTimeout(function () {
            auditGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id
                }
            })
        }, 10);
    }
});