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

Ext.define('Flamingo2.view.batch.JobList', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.jobListGrid',

    bind: {
        store: '{jobListStore}'
    },

    border: true,

    features: [
        Ext.create('Ext.grid.feature.Grouping', {
            groupHeaderTpl: '{name} (' + message.msg('common.total') + ': {rows.length})'
        })
    ],

    columns: [
        {text: message.msg('batch.identifier'), width: 220, dataIndex: 'jobId', align: 'center', sortable: false},
        {text: message.msg('batch.job_name'), flex: 1, dataIndex: 'name', align: 'center', sortable: false},
        {text: message.msg('batch.schedule'), width: 160, dataIndex: 'cron', align: 'center'},
        {
            text: message.msg('batch.start_time'), width: 150, dataIndex: 'start', align: 'center',
            renderer: function (value) {
                return App.Util.Date.format(new Date(value), 'yyyy-MM-dd HH:mm:ss');
            }
        },
        {
            text: message.msg('batch.next_time'), width: 150, dataIndex: 'next', align: 'center',
            renderer: function (value) {
                return App.Util.Date.format(new Date(value), 'yyyy-MM-dd HH:mm:ss');
            }
        },
        {
            text: message.msg('common.status'), width: 90, dataIndex: 'status', align: 'center'
        },
        {text: message.msg('batch.username'), width: 70, dataIndex: 'username', align: 'center', sortable: false}
    ],
    dockedItems: [
        {
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            pageSize: CONSTANTS.GRID_SIZE_PER_PAGE,
            displayInfo: true
        }
    ],
    viewConfig: {
        stripeRows: true,
        columnLines: true,
        getRowClass: function () {
            return 'cell-height-30';
        }
    },
    tbar: [
        {
            xtype: 'button',
            text: message.msg('common.new'),
            iconCls: 'common-new',
            handler: 'onBtnRegistClick'
        },
        '-',
        {
            xtype: 'button',
            reference: 'suspendJobButton',
            text: message.msg('common.pause'),
            iconCls: 'common-pause',
            disabled: true,
            handler: 'onBtnSuspendClick'
        },
        '-',
        {
            xtype: 'button',
            reference: 'resumeJobButton',
            text: message.msg('common.resume'),
            iconCls: 'common-replay',
            disabled: true,
            handler: 'onBtnResumeClick'
        },
        '-',
        {
            xtype: 'button',
            reference: 'stopJobButton',
            text: message.msg('common.stop'),
            iconCls: 'common-stop',
            disabled: true,
            handler: 'onBtnStopClick'
        },
        '-',
        {
            xtype: 'button',
            reference: 'modifyJobButton',
            text: message.msg('common.change'),
            iconCls: 'common-rename',
            disabled: true,
            handler: 'onBtnModifyClick'
        },
        '->',
        {
            xtype: 'button',
            reference: 'refreshButton',
            formBind: true,
            text: message.msg('common.refresh'),
            iconCls: 'common-refresh',
            labelWidth: 50,
            handler: 'onBtnRefershClick'
        }
    ],
    listeners: {
        afterrender: 'onJobListAfterRender',
        select: 'onJobListSelect'
    }
});