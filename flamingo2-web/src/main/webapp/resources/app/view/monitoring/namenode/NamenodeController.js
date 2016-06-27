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
Ext.define('Flamingo2.view.monitoring.namenode.NamenodeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.namenodeController',

    /**
     * Engine Combobox Changed Event
     */
    onEngineChanged: function () {
        var me = this;
        me.getNamenodeSummary();
    },

    /**
     * 네임노드 요약 정보를 가져온다.
     *
     * @param view
     */
    onNamenodeSummaryAfterRender: function (view) {
        var me = this;
        // Table Layout의 colspan 적용시 cell간 간격 조정이되지 않는 문제를 해결하기 위해서 적용함
        setTableLayoutFixed(view);
        me.getNamenodeSummary();
    },

    /**
     * HDFS 사용량 JVM Heap 사용량 블록 상태, 총 파일의 개수, 총 블록의 개수, 데이터 노드의 정보를 가져온다.
     */
    onNamenodeChartAfterRender: function() {
        var me = this;

        me.onHdfsUsageRefreshClick();
        me.onJVMHeapUsageRefreshClick();
        me.onBlockStatusRefreshClick();
        me.onFileCountRefreshClick();
        me.onBlockCountRefreshClick();
        me.onDatanodeStatusRefreshClick();
    },

    /**
     * 네임노드 요약 정보를 업데이트한다.
     */
    onNamenodeSummaryRefreshClick: function () {
        var me = this;
        me.getNamenodeSummary();
    },

    /**
     * 네임노드 요약 정보를 가져온다.
     */
    getNamenodeSummary: function () {
        var url = CONSTANTS.MONITORING.NAMENODE.INFO;
        var param = {
            clusterName: ENGINE.id
        };

        invokeGet(url, param,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    var namenodeSummaryForm = query('namenodeSummary');

                    namenodeSummaryForm.getForm().reset();
                    obj.map.datanode = message.msg('common.total') + ' ' + obj.map.all + message.msg('monitoring.namenode.count') + ' / ' + message.msg('monitoring.namenode.live') + ' ' + obj.map.live + message.msg('monitoring.namenode.count') + ' / ' + message.msg('monitoring.namenode.dead') + ' ' + obj.map.dead + message.msg('monitoring.namenode.count');
                    namenodeSummaryForm.getForm().setValues(obj.map);
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
    },

    /**
     * 네임노드의 HDFS 사용량 Chart 정보를 업데이트한다.
     */
    onHdfsUsageRefreshClick: function () {
        var hdfsUsageChart = query('hdfsUsage #hdfsUsageChart');

        setTimeout(function () {
            hdfsUsageChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            hdfsUsageChart.getStore().load();
        }, 10);
    },

    /**
     * JVM Heap Usage Chart 정보를 업데이트한다.
     */
    onJVMHeapUsageRefreshClick: function () {
        var jvmHeapUsageChart = query('jvmHeapUsage #jvmHeapUsageChart');

        setTimeout(function () {
            jvmHeapUsageChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            jvmHeapUsageChart.getStore().load();
        }, 10);
    },

    /**
     * Block Status Chart 정보를 업데이트한다.
     */
    onBlockStatusRefreshClick: function () {
        var blockStatusChart = query('blockStatus #blockStatusChart');

        setTimeout(function () {
            blockStatusChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            blockStatusChart.getStore().load();
        }, 10);
    },

    /**
     * File Count Chart 정보를 업데이트한다.
     */
    onFileCountRefreshClick: function () {
        var fileCountChart = query('fileCount #fileCountChart');

        setTimeout(function () {
            fileCountChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            fileCountChart.getStore().load();
        }, 10);
    },

    /**
     * Block Count Chart 정보를 업데이트한다.
     */
    onBlockCountRefreshClick: function () {
        var blockCountChart = query('blockCount #blockCountChart');

        setTimeout(function () {
            blockCountChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            blockCountChart.getStore().load();
        }, 10);
    },

    /**
     * Datanode Status Chart 정보를 업데이트한다.
     */
    onDatanodeStatusRefreshClick: function () {
        var datanodeStatusChart = query('datanodeStatus #datanodeStatusChart');

        setTimeout(function () {
            datanodeStatusChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            datanodeStatusChart.getStore().load();
        }, 10);
    }
});