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

Ext.define('Flamingo2.view.fs.hdfs.summary.HdfsSumChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hdfsSumChartViewController',

    /**
     * HDFS 브라우저 상단의 Summary Chart 정보를 표시한다.
     */
    onAfterRender: function () {
        var me = this;
        var refs = me.getReferences();
        var url = CONSTANTS.MONITORING.NAMENODE.INFO;
        var param = {
            clusterName: ENGINE.id
        };

        invokeGet(url, param,
            function (response) {
                var res = Ext.decode(response.responseText);
                if (res.success) {
                    refs.hdfsSumChartForm.getForm().setValues(res.map);

                    // PIE 차트를 업데이트 한다.
                    var pie = query('hdfsSumChartPanel #usage');
                    pie.getStore().proxy.data = [
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
                    pie.getStore().load();
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

        var hdfsTop5Grid = query('hdfsSumChartPanel #hdfsTop5Grid');
        setTimeout(function () {
            hdfsTop5Grid.getStore().proxy.extraParams.clusterName = ENGINE.id;
            hdfsTop5Grid.getStore().load();
        }, 10);
    }
});