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
Ext.define('Flamingo2.view.monitoring.historyserver.Tasks', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.tasks',

    bind: {
        store: '{tasksStore}'
    },

    viewConfig: {
        emptyText: 'MapReduce Job ' + message.msg('monitoring.yarn.tip.na'),
        deferEmptyText: false,
        enableTextSelection: true,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    },

    columns: [
        {
            text: message.msg('common.id'), dataIndex: 'id', align: 'center', flex: 1
        },
        {
            text: message.msg('common.type'), dataIndex: 'type', width: 65, align: 'center'
        },
        {
            text: message.msg('common.status'), dataIndex: 'state', width: 85, align: 'center'
        },
        {
            text: message.msg('common.progress'), dataIndex: 'progress', width: 110, align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return Ext.String.format('<div class="x-progress x-progress-default x-border-box">' +
                    '<div class="x-progress-text x-progress-text-back" style="width: 100px;">{0}%</div>' +
                    '<div class="x-progress-bar x-progress-bar-default" role="presentation" style="width:{0}%">' +
                    '<div class="x-progress-text" style="width: 100px;"><div>{0}%</div></div></div></div>', value);
            }
        },
        {
            text: message.msg('dashboard.jobdetail.job.elapsed'), dataIndex: 'elapsedTime', width: 60, align: 'center'
        },
        {
            text: message.msg('common.start'), dataIndex: 'startTime', width: 140, align: 'center'
        },
        {
            text: message.msg('common.finish'), dataIndex: 'finishTime', width: 140, align: 'center'
        }
    ]
});