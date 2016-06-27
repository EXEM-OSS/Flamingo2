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
    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('common.refresh'),
            handler: 'onRMSummaryRefreshClick'
        }
    ],
    listeners: {
        afterrender: 'onRMSummaryAfterRender'
    }
});
