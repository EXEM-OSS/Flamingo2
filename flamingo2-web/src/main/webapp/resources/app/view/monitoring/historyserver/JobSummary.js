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
Ext.define('Flamingo2.view.monitoring.historyserver.JobSummary', {
    extend: 'Ext.form.Panel',
    alias: 'widget.jobSummary',

    listeners: {
        afterrender: 'onJobSummaryAfterrender'
    },

    layout: {
        type: 'table',
        columns: 2,
        tableAttrs: {
            style: {
                width: '100%'
            }
        }
    },

    defaults: {
        labelAlign: 'right',
        anchor: '100%',
        labelWidth: 150,
        margins: '10 10 10 10'
    },

    defaultType: 'textfield',

    bodyPadding: '10',

    items: [
        {
            colspan: 2,
            fieldLabel: message.msg('dashboard.wh.column.text'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'name'
        },
        {
            fieldLabel: message.msg('common.workflowName'),
            xtype: 'displayfield',
            labelAlign: 'right',
            hidden: true,
            name: 'flamingo_workflow_name'
        },
        {
            fieldLabel: message.msg('common.actionName'),
            xtype: 'displayfield',
            labelAlign: 'right',
            hidden: true,
            name: 'flamingo_action_name'
        },
        {
            fieldLabel: message.msg('dashboard.jobdetail.job.jobid'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'id'
        },
        {
            fieldLabel: message.msg('dashboard.common.workflowId'),
            xtype: 'displayfield',
            labelAlign: 'right',
            hidden: true,
            name: 'flamingo_job_id'
        },
        {
            fieldLabel: message.msg('common.user'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'user'
        },
        {
            fieldLabel: message.msg('common.status'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'state'
        },
        {
            fieldLabel: message.msg('common.queue'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'queue'
        },
        {
            fieldLabel: message.msg('dashboard.wdetail.column.duration'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'durationStr'
        },
        {
            fieldLabel: message.msg('monitoring.map_task_count'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'mapsStr'
        },
        {
            fieldLabel: message.msg('monitoring.reduce_task_count'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'reducesStr'
        },
        {
            fieldLabel: message.msg('monitoring.history.fail_map_task_count'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'failedMapAttemptStr'
        },
        {
            fieldLabel: message.msg('monitoring.history.fail_reduce_task_count'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'failedReduceAttemptStr'
        },
        {
            fieldLabel: message.msg('common.start'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'startTimeStr'
        },
        {
            fieldLabel: message.msg('common.finish'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'finishTimeStr'
        },
        {
            colspan: 2,
            fieldLabel: message.msg('monitoring.history.avg_time'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'averageTime'
        },
        {
            colspan: 2,
            fieldLabel: message.msg('monitoring.history.diagnostics'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'diagnostics'
        }
    ]
});