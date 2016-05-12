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
Ext.define('Flamingo2.view.monitoring.resourcemanager.ResourceManagerSummary', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.resourceManagerSummary',

    title: message.msg('monitoring.rm.title'), // FIXME

    width: '100%',

    layout: {
        type: 'table',
        columns: 2,
        tableAttrs: {
            style: {
                width: '100%'
            }
        }
    },

    items: [
        {
            xtype: 'form',
            itemId: 'summaryForm1',
            items: [
                {
                    xtype: 'displayfield',
                    itemId: 'hostname',
                    fieldLabel: message.msg('monitoring.namenode.hostname'),
                    labelAlign: 'right',
                    labelWidth: 150
                },
                {
                    xtype: 'displayfield',
                    itemId: 'runningStatus',
                    fieldLabel: message.msg('common.status'),
                    labelAlign: 'right',
                    labelWidth: 150
                },
                {
                    xtype: 'displayfield',
                    itemId: 'version',
                    fieldLabel: message.msg('monitoring.rm.version'),
                    labelAlign: 'right',
                    labelWidth: 150
                },
                {
                    xtype: 'displayfield',
                    itemId: 'clusterMemory',
                    fieldLabel: message.msg('monitoring.rm.cluster_mem'),
                    labelAlign: 'right',
                    labelWidth: 150,
                    value: ''
                },
                {
                    xtype: 'displayfield',
                    itemId: 'queue',
                    fieldLabel: message.msg('common.queue'),
                    labelAlign: 'right',
                    labelWidth: 150,
                    value: ''
                },
                {
                    xtype: 'displayfield',
                    itemId: 'containerStatus',
                    fieldLabel: message.msg('monitoring.rm.container_status'),
                    labelAlign: 'right',
                    labelWidth: 150,
                    value: ''
                }
            ]
        },
        {
            xtype: 'form',
            itemId: 'summaryForm2',
            items: [
                {
                    xtype: 'displayfield',
                    itemId: 'ip',
                    fieldLabel: message.msg('monitoring.datanode.ip_addr'),
                    labelAlign: 'right',
                    labelWidth: 150
                },
                {
                    xtype: 'displayfield',
                    itemId: 'runningTime',
                    fieldLabel: message.msg('batch.start_time'),
                    labelAlign: 'right',
                    labelWidth: 150
                },
                {
                    xtype: 'displayfield',
                    itemId: 'monitoringInterval',
                    fieldLabel: message.msg('monitoring.rm.cycle'),
                    labelAlign: 'right',
                    labelWidth: 150
                },
                {
                    xtype: 'displayfield',
                    itemId: 'jvmMemory',
                    fieldLabel: message.msg('monitoring.rm.jvm_mem'),
                    labelAlign: 'right',
                    labelWidth: 150,
                    value: ''
                },
                {
                    xtype: 'displayfield',
                    itemId: 'nodeStatus',
                    fieldLabel: message.msg('monitoring.clusternode.nodes.node_status'),
                    labelAlign: 'right',
                    labelWidth: 150,
                    value: ''
                },
                {
                    xtype: 'displayfield',
                    itemId: 'appsStatus',
                    fieldLabel: message.msg('monitoring.rm.app_status'),
                    labelAlign: 'right',
                    labelWidth: 150,
                    value: ''
                }
            ]
        }
    ],
    listeners: {
        afterrender: function (comp, opts) {
            setTableLayoutFixed(comp);

            invokeGet(CONSTANTS.MONITORING.GET_RESOURCEMANAGER_INFO, {clusterName: ENGINE.id},
                function (response) {
                    var res = Ext.decode(response.responseText);
                    if (res) {
                        try {
                            query('resourceManagerSummary #ip').setValue(res.metrics['ip']);
                        } catch (err) {
                        }
                        ;
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
                function (response) {
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
        }
    }
});
