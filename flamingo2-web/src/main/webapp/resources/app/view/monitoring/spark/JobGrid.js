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
 * Created by Park on 15. 8. 14..
 */
Ext.define('Flamingo2.view.monitoring.spark.JobGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.sparkJobGrid',
    columnLines: true,

    bind: {
        store: '{jobs}'
    },

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
        width: 80,
        align: 'center'
    },{
        text: 'Job Name',
        dataIndex: 'jobname',
        style: 'text-align:center',
        flex: 1
    },{
        text: 'Submitted',
        dataIndex: 'submitted',
        width: 130,
        align: 'center'
    },{
        text: 'Completed',
        dataIndex: 'completed',
        width: 130,
        align: 'center'
    },{
        text: 'Duration',
        dataIndex: 'duration',
        align: 'center'
    },{
        text: 'Stages',
        dataIndex: 'stage_rate',
        width: 200,
        style: 'text-align:center',
        renderer: function (value, metaData, record, row, col, store, gridView) {
            value = value * 100;
            var text;

            if (record.get('stage_failed') > 0) {
                text = record.get('stages') + ' / ' + record.get('stage_completed') + ' (' + record.get('stage_failed') + ' failed)';
            } else if (record.get('stage_skipped') > 0) {
                text = record.get('stages') + ' / ' + record.get('stage_completed') + ' (' + record.get('stage_skipped') + ' skipped)';
            } else {
                text = record.get('stages') + ' / ' + record.get('stage_completed');
            }

            return Ext.String.format('<div class="x-progress x-progress-default x-border-box">' +
                '<div class="x-progress-text x-progress-text-back" style="width: 190px;">{1}</div>' +
                '<div class="x-progress-bar x-progress-bar-default" role="presentation" style="width:{0}%">' +
                '<div class="x-progress-text" style="width: 190px;"><div>{1}</div></div></div></div>', value, text);
        }
    },{
        text: 'Tasks',
        dataIndex: 'task_rate',
        width: 200,
        style: 'text-align:center',
        renderer: function (value, metaData, record, row, col, store, gridView) {
            value = value * 100;
            var text = record.get('tasks') + ' / ' + record.get('task_completed');
            return Ext.String.format('<div class="x-progress x-progress-default x-border-box">' +
                '<div class="x-progress-text x-progress-text-back" style="width: 190px;">{1}</div>' +
                '<div class="x-progress-bar x-progress-bar-default" role="presentation" style="width:{0}%">' +
                '<div class="x-progress-text" style="width: 190px;"><div>{1}</div></div></div></div>', value, text);
        }
    },{
        text: 'Status',
        dataIndex: 'status',
        align: 'center'
    }],

    tbar: ['->',{
        xtype: 'button',
        text: 'Event Timeline',
        handler: 'onJobEventTimelineClick',
        iconCls: 'fa fa-line-chart',
        disabled: true,
        reference: 'btnJobsTimeline'
    }],

    listeners: {
        dataLoad: 'onGridDataLoad',
        select: 'onJobGridSelect'
    }
});