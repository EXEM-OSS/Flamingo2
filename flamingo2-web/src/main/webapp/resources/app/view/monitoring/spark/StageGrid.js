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
 * Created by Park on 15. 8. 16..
 */
Ext.define('Flamingo2.view.monitoring.spark.StageGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.sparkStageGrid',

    bind: {
        store: '{stages}'
    },

    columnLines: true,

    features: [{
        ftype: 'grouping',
        groupHeaderTpl: ['Status: {[this.getValue(values)]} ({rows.length} Item{[values.rows.length > 1 ? "s" : ""]})',{
            getValue: function(values) {
                return values.rows[0].get('status');
            }
        }],
        hideGroupedHeader: true,
        startCollapsed: false
    }],

    columns: [{
        text: 'Job ID',
        dataIndex: 'jobid',
        align: 'center'
    },{
        text: 'Stage ID',
        dataIndex: 'stageid',
        align: 'center'
    },{
        text: 'Attempt ID',
        dataIndex: 'attemptid',
        align: 'center'
    },{
        text: 'Submitted',
        dataIndex: 'submitted',
        width: 125,
        align: 'center'
    },{
        text: 'Completed',
        dataIndex: 'completed',
        width: 125,
        align: 'center'
    },{
        text: 'Name',
        dataIndex: 'name',
        style: 'text-align:center',
        width: 270
    },{
        text: 'Tasks',
        dataIndex: 'tasks',
        style: 'text-align:center',
        align: 'right'
    },{
        text: 'Input bytes',
        dataIndex: 'input_bytes',
        style: 'text-align:center',
        width: 130,
        align: 'right'
    },{
        text: 'Input records',
        dataIndex: 'input_records',
        style: 'text-align:center',
        width: 130,
        align: 'right'
    },{
        text: 'Output bytes',
        dataIndex: 'output_bytes',
        style: 'text-align:center',
        width: 130,
        align: 'right'
    },{
        text: 'Output records',
        dataIndex: 'output_records',
        style: 'text-align:center',
        width: 130,
        align: 'right'
    },{
        text: 'Shuffle read bytes',
        dataIndex: 'shuffle_read_bytes',
        style: 'text-align:center',
        width: 130,
        align: 'right'
    },{
        text: 'Shuffle read records',
        dataIndex: 'shuffle_read_records',
        style: 'text-align:center',
        width: 130,
        align: 'right'
    },{
        text: 'Shuffle write bytes',
        dataIndex: 'shuffle_write_bytes',
        style: 'text-align:center',
        width: 130,
        align: 'right'
    },{
        text: 'Shuffle write recrods',
        dataIndex: 'shuffle_write_recrods',
        style: 'text-align:center',
        width: 130,
        align: 'right'
    }],
    tbar: ['->',{
        xtype: 'button',
        text: 'Details',
        iconCls: 'fa fa-bar-chart',
        disabled: true,
        reference: 'btnStagesDetail',
        handler: 'onStageDetailClick'
    },{
        xtype: 'button',
        text: 'Event Timeline',
        iconCls: 'fa fa-line-chart',
        disabled: true,
        reference: 'btnStagesTimeline',
        handler: 'onStageTimelineClick'
    }],
    listeners: {
        dataLoad: 'onGridDataLoad',
        select: 'onStageGridSelect'
    }
});