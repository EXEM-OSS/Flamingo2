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
Ext.define('Flamingo2.view.monitoring.spark.ApplicationGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.sparkAppGrid',

    columnLines: true,

    bind: {
        store: '{application}'
    },

    columns: [{
        text: 'App ID',
        dataIndex: 'appid',
        style: 'text-align:center',
        width: 190
    },{
        text: 'App Name',
        dataIndex: 'appname',
        style: 'text-align:center',
        flex: 1
    },{
        text: 'Started',
        dataIndex: 'started',
        align: 'center',
        width: 130
    },{
        text: 'Completed',
        dataIndex: 'completed',
        align: 'center',
        width: 130
    },{
        text: 'Duration',
        dataIndex: 'duration',
        align: 'center',
        width: 80
    },{
        text: 'User',
        dataIndex: 'user',
        align: 'center'
    }],
    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('common.refresh'),
            handler: 'appGridLoad'
        }
    ],
    tbar: ['->',{
        xtype: 'button',
        text: 'Event Timeline',
        handler: 'onAppEventTimelineClick',
        iconCls: 'fa fa-line-chart',
        disabled: true,
        reference: 'btnTimeline'
    }],

    listeners: {
        select: 'onAppGridSelect'
    }
});