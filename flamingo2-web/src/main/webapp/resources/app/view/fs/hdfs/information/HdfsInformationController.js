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
Ext.define('Flamingo2.view.fs.hdfs.information.HdfsInformationController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hdfsInformationViewController',

    /**
     * HDFS 브라우저의 정보를 가져온다.
     */
    onAfterRender: function () {
        var me = this;

        me.getHdfsInformation();
        me.getHdfsUsage();
        me.getHdfsTop5();
    },

    /**
     * HDFS Usage (DFS, Non-DFS) 정보를 가져온다.
     */
    getHdfsUsage: function () {
        var url = CONSTANTS.MONITORING.NAMENODE.INFO;
        var param = {
            clusterName: ENGINE.id
        };

        invokeGet(url, param,
            function (response) {
                var res = Ext.decode(response.responseText);

                if (res.success) {
                    var hdfsUsagePolar = query('hdfsUsagePolarPanel #hdfsUsagePolar');

                    hdfsUsagePolar.getStore().proxy.data = [
                        {
                            name: message.msg('fs.hdfs.chart.pie.capacityUsedNonDFS'),
                            value: res.map.capacityUsedNonDFS
                        },
                        {
                            name: message.msg('fs.hdfs.chart.pie.used'),
                            value: res.map.used
                        },
                        {
                            name: message.msg('fs.hdfs.chart.pie.free'),
                            value: res.map.free
                        }
                    ];

                    hdfsUsagePolar.getStore().load();
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: res.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function () {
                Ext.MessageBox.show({
                    title: message.msg('common.warning'),
                    message: format(message.msg('common.failure'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    },

    /**
     * HDFS 상위 5 디렉토리 정보를 업데이트한다.
     */
    getHdfsTop5: function () {
        var hdfsTop5Grid = query('hdfsTop5DirectoryPanel #hdfsTop5Grid');

        setTimeout(function () {
            hdfsTop5Grid.getStore().proxy.extraParams.clusterName = ENGINE.id;
            hdfsTop5Grid.getStore().load();
        }, 10);
    },

    /**
     * HDFS 정보를 가져온다.
     */
    getHdfsInformation: function () {
        var hdfsSummaryForm = query('hdfsSummaryPanel #hdfsInformationForm');
        var url = CONSTANTS.MONITORING.NAMENODE.INFO;
        var param = {
            clusterName: ENGINE.id
        };

        invokeGet(url, param,
            function (response) {
                var res = Ext.decode(response.responseText);

                if (res.success) {
                    hdfsSummaryForm.getForm().reset();
                    hdfsSummaryForm.getForm().setValues(res.map);
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.notice'),
                        message: res.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function () {
                Ext.MessageBox.show({
                    title: message.msg('common.warning'),
                    message: format(message.msg('common.failure'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    },

    /**
     * HDFS Summary 정보를 업데이트한다.
     */
    onHdfsSummaryRefreshClick: function () {
        var me = this;

        me.getHdfsInformation();
    },

    /**
     * HDFS Usage (DFS, Non-DFS) 사용량 정보를 업데이트한다.
     */
    onHdfsUsagePolarRefreshClick: function () {
        var me = this;

        me.getHdfsUsage();
    },

    /**
     * HDFS Top 5 디렉토리 목록을 업데이트한다.
     */
    onHdfsTop5DirectoryRefreshClick: function () {
        var me = this;

        me.getHdfsTop5();
    }
});