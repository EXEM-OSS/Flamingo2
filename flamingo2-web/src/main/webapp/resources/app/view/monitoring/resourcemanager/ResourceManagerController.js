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
Ext.define('Flamingo2.view.monitoring.resourcemanager.ResourceManagerController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.resourcemanagerController',

    /**
     * Engine Combobox Changed Event
     */
    onEngineChanged: function () {
        var grid = query('runningApplications');

        grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
        grid.getStore().load({
            callback: function (records, operation, success) {
                grid.setTitle(format(message.msg('monitoring.rm.total_run_app'), this.getCount()));
            }
        });
    },

    onAfterrender: function (grid, opts) {
        setTimeout(function () {
            grid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            grid.getStore().load({
                callback: function (records, operation, success) {
                    grid.setTitle(format(message.msg('monitoring.rm.total_run_app'), this.getCount()));
                }
            });
        }, 10);
    },

    /**
     * Resource Manager Summary 정보를 가져온다.
     */
    getRMSummary: function () {
        var url = CONSTANTS.MONITORING.GET_RESOURCEMANAGER_INFO;
        var param = {
            clusterName: ENGINE.id
        };

        invokeGet(url, param,
            function (response) {
                var res = Ext.decode(response.responseText);
                if (res) {
                    try {
                        query('resourceManagerSummary #ip').setValue(res.metrics['ip']);
                    } catch (err) {
                    }

                    query('resourceManagerSummary #runningStatus').setValue(message.msg('common.running'));
                    query('resourceManagerSummary #version').setValue(res.version);
                    query('resourceManagerSummary #runningTime').setValue(dateFormat2(res.startTime));
                    query('resourceManagerSummary #monitoringInterval').setValue(res.nmHeartbeatInterval / 1000 + ' ' + message.msg('common.sec'));
                    query('resourceManagerSummary #queue').setValue(Ext.String.format('{0}', res.queue));
                    query('resourceManagerSummary #jvmMemory').setValue(
                        Ext.String.format(message.msg('monitoring.rm.total_free'), res.metrics['heap-total'], res.metrics['heap-free'])
                    );
                    query('resourceManagerSummary #nodeStatus').setValue(
                        Ext.String.format(message.msg('monitoring.rm.active_lost_unhealthy_decomm'), res.cluster['activeNodes'], res.cluster['lostNodes'], res.cluster['unhealthyNodes'], res.cluster['decommissionedNodes'], res.cluster['rebootedNodes'])
                    );
                    query('resourceManagerSummary #clusterMemory').setValue(
                        Ext.String.format(message.msg('monitoring.rm.total_allocate_reserve'), res.cluster['totalMB'], res.cluster['allocatedMB'], res.cluster['reservedMB'])
                    );
                    query('resourceManagerSummary #containerStatus').setValue(
                        Ext.String.format(message.msg('monitoring.rm.allocate_reserve_wait'), res.cluster['containersAllocated'], res.cluster['containersReserved'], res.cluster['containersPending'])
                    );
                    query('resourceManagerSummary #appsStatus').setValue(
                        Ext.String.format(message.msg('monitoring.rm.run_submit_complete_kill_fail'), res.cluster['appsRunning'], res.cluster['appsSubmitted'],
                            res.cluster['appsCompleted'], res.cluster['appsKilled'], res.cluster['appsFailed'])
                    );
                    query('resourceManagerSummary #hostname').setValue(res.metrics['hostname']);
                } else {
                    query('resourceManagerSummary #summaryForm1').getForm().reset();
                    query('resourceManagerSummary #summaryForm2').getForm().reset();
                    query('resourceManagerSummary #runningStatus').setValue(message.msg('monitoring.rm.can_not_get_status'));

                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: message.msg('monitoring.rm.can_not_get_rm_info'),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function () {
                query('resourceManagerSummary #summaryForm1').getForm().reset();
                query('resourceManagerSummary #summaryForm2').getForm().reset();
                query('resourceManagerSummary #runningStatus').setValue(message.msg('monitoring.rm.can_not_get_status'));

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
     * Resource Manager의 Summary 패널 정보를 가져온다.
     */
    onRMSummaryAfterRender: function (comp, opts) {
        var me = this;

        setTableLayoutFixed(comp);
        me.getRMSummary();
    },

    /**
     * Resource Manager의 정보를 가져온다.
     *
     * @param comp
     * @param opts
     */
    onStoreAfterrender: function (comp, opts) {
        setTimeout(function () {
            comp.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            comp.getStore().load();
        }, 10);
    },

    /**
     * Resource Manager의 Configuration 정보를 가져온다.
     *
     * @param grid
     * @param opts
     */
    onRMConfigurationAfterRender: function (grid, opts) {
        var me = this;

        setTimeout(function () {
            grid.getStore().load();
        }, 10);

        me.updateSortTitle(grid);
    },

    updateSortTitle: function (grid) {
        var sortDetail = [];

        grid.store.getSorters().each(function (sorter) {
            sortDetail.push(sorter.getProperty() + ' ' + sorter.getDirection());
        });
//        this.down('#order').update('Sorted By: ' + sortDetail.join(', '));
    },

    /**
     * Resource Manager의 Summary 정보를 업데이트한다.
     */
    onRMSummaryRefreshClick: function () {
        var me = this;
        me.getRMSummary();
    },

    /**
     * Resource Manager의 Running Yarn Application Grid 정보를 업데이트한다.
     */
    onRunningYarnAppRefreshClick: function () {
        var grid = query('runningApplications');

        grid.getStore().load({
            callback: function () {
                grid.setTitle(format(message.msg('monitoring.rm.total_run_app'), this.getCount()))
            }
        });
    },

    /**
     * Resource Manager의 Node Status Chart 정보를 업데이트한다.
     */
    onRMNodeStatusRefreshClick: function () {
        var nodeStatusChart = query('nodeStatus #nodeStatusChart');

        setTimeout(function () {
            nodeStatusChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            nodeStatusChart.getStore().load();
        }, 10);
    },

    /**
     * Resource Manager의 Application Status Chart 정보를 업데이트한다.
     */
    onRMApplicationStatusRefreshClick: function () {
        var applicationStatusChart = query('applicationStatus #applicationStatusChart');

        setTimeout(function () {
            applicationStatusChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            applicationStatusChart.getStore().load();
        }, 10);
    },

    /**
     * Resource Manager의 Container Status Chart 정보를 업데이트한다.
     */
    onRMContainerStatusRefreshClick: function () {
        var containerStatusChart = query('containerStatus #containerStatusChart');

        setTimeout(function () {
            containerStatusChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            containerStatusChart.getStore().load();
        }, 10);
    },

    /**
     * Resource Manager의 JVM Heap Usage Chart 정보를 업데이트한다.
     */
    onRMJVMHeapUsageRefreshClick: function () {
        var jvmHeapUsageChart = query('rmJvmHeapUsage #jvmHeapUsageChart');

        setTimeout(function () {
            jvmHeapUsageChart.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            jvmHeapUsageChart.getStore().load();
        }, 10);
    },

    /**
     * Resource Manager의 Configuration 정보를 업데이트한다.
     */
    onRMConfigurationRefreshClick: function () {
        var me = this;
        var rmConfigurationGrid = query('resourceManagerConfiguration');

        setTimeout(function () {
            rmConfigurationGrid.getStore().load();
        }, 10);

        me.updateSortTitle(grid);
    }
});
