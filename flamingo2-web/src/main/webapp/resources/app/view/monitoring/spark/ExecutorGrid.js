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
/**
 * Created by Park on 15. 8. 17..
 */
Ext.define('Flamingo2.view.monitoring.spark.ExecutorGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.sparkExecutorGrid',

    bind: {
        store: '{executors}'
    },

    columnLines: true,

    columns: [{
        text: 'Executor ID',
        style: 'text-align:center',
        dataIndex: 'executorid'
    },{
        text: 'Address',
        style: 'text-align:center',
        dataIndex: 'address'
    },{
        text: 'RDD Blocks',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'rdd_blocks'
    },{
        text: 'Storage',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'disk_used'
    },{
        text: 'Memory',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'max_memory'
    },{
        text: 'Cores',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'total_cores'
    },{
        text: 'Failed Tasks',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'failed_tasks'
    },{
        text: 'Complete Tasks',
        width: 120,
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'completed_tasks'
    },{
        text: 'Total Tasks',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'total_tasks'
    },{
        text: 'Task Time',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'total_duration'
    },{
        text: 'Input',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'total_input_bytes'
    },{
        text: 'Shuffle Read',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'total_shuffle_read'
    },{
        text: 'Shuffle Write',
        style: 'text-align:center',
        align: 'right',
        dataIndex: 'total_shuffle_write'
    },{
        text: 'stdout',
        style: 'text-align:center',
        dataIndex: 'stdout_url'
    },{
        text: 'stderr',
        style: 'text-align:center',
        dataIndex: 'stderr_url'
    }],

    listeners: {
        dataLoad: 'onGridDataLoad'
    }
});