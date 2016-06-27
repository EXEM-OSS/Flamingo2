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
Ext.define('Flamingo2.view.dashboard.WorkflowHistory', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.workflowHistory',

    viewConfig: {
        stripeRows: true,
        columnLines: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    },

    bind: {
        store: '{workflowHistoryStore}'
    },

    columns: [
        {
            text: message.msg('dashboard.wh.column.text'),
            dataIndex: 'text',
            width: 220,
            align: 'center',
            sortable: false
        },
        {
            dataIndex: 'id',
            hidden: true
        },
        {
            dataIndex: 'rowid',
            hidden: true
        },
        {
            dataIndex: 'jobStringId',
            hidden: true,
            sortable: false,
            renderer: 'onRenderer'
        },
        {
            dataIndex: 'workflowId',
            hidden: true,
            renderer: 'onRenderer'
        },
        {
            text: message.msg('dashboard.wh.column.currentaction'),
            flex: 1,
            dataIndex: 'currentAction',
            align: 'center',
            sortable: false,
            renderer: 'onRenderer'
        },
        {
            text: message.msg('dashboard.wh.column.username'),
            width: 80,
            dataIndex: 'username',
            align: 'center',
            renderer: 'onRenderer'
        },
        {
            text: message.msg('dashboard.wh.column.elapsed'),
            width: 60,
            dataIndex: 'elapsed',
            align: 'center',
            renderer: function (value, metaData, record) {
                var diff;

                if (record.get('type') != "workflow")
                    return "";

                if (record.get('status') == 'RUNNING') {
                    var start = new Date(record.get('startDate'));
                    var now = new Date();
                    diff = (now.getTime() - start.getTime()) / 1000;
                } else {
                    diff = value / 1000;
                }

                return App.Util.Date.toHumanReadableTime(Math.floor(diff));
            }
        },
        /*
         {
         text: message.msg('dashboard.wh.column.progress'),
         width: 90,
         dataIndex: 'progress',
         align: 'center',
         sortable: false,
         renderer: function (value, metaData, record) {
         if (record.data.type != "workflow")
         return "";

         var percent = Math.floor(100 * (record.data.currentStep / record.data.totalStep));
         return Ext.String.format('<div class="x-progress x-progress-default x-border-box">' +
         '<div class="x-progress-text x-progress-text-back" style="width: 76px;">{0}%</div>' +
         '<div class="x-progress-bar x-progress-bar-default" role="presentation" style="width:{0}%">' +
         '<div class="x-progress-text" style="width: 76px;"><div>{0}%</div></div></div></div>', percent);
         }
         },
         */
        {
            text: message.msg('dashboard.wh.column.status'),
            width: 90,
            dataIndex: 'status',
            align: 'center'
        },
        {
            text: message.msg('dashboard.wh.column.startdate'),
            width: 140,
            dataIndex: 'startDate',
            align: 'center',
            renderer: 'onRenderer'
        },
        {
            text: message.msg('dashboard.wh.column.enddate'),
            width: 140,
            dataIndex: 'endDate',
            align: 'center',
            renderer: function (value, metaData, record) {
                return (record.get('type') != 'workflow' || record.get('status') == 'RUNNING') ? '' : value;
            }
        }
    ],
    dockedItems: [
        {
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            bind: {
                store: '{workflowHistoryStore}'
            },
            displayInfo: true,
            listeners: {
                beforechange: 'onBeforechange'
            }
        }
    ],
    tbar: [
        {
            xtype: 'tbtext',
            text: message.msg('dashboard.wh.tbar.startdate')
        },
        {
            xtype: 'datefield',
            itemId: 'startDate',
            reference: 'startDate',
            vtype: 'dateRange',
            width: 100,
            endDateField: 'endDate',
            editable: false,
            format: 'Y-m-d'
        },
        {
            xtype: 'tbtext',
            text: message.msg('dashboard.wh.tbar.enddate')
        },
        {
            xtype: 'datefield',
            itemId: 'endDate',
            reference: 'endDate',
            vtype: 'dateRange',
            width: 100,
            startDateField: 'startDate',
            editable: false,
            format: 'Y-m-d'
        },
        {
            xtype: 'tbtext',
            text: message.msg('dashboard.wh.tbar.status')
        },
        {
            xtype: 'combo',
            itemId: 'status',
            reference: 'status',
            width: 80,
            name: 'status',
            editable: false,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            value: 'ALL',
            store: Ext.create('Ext.data.Store', {
                fields: ['name', 'value', 'description'],
                data: [
                    {name: message.msg('dashboard.wh.tbar.status.all'), value: 'ALL'},
                    {name: message.msg('dashboard.wh.tbar.status.running'), value: 'RUNNING'},
                    {name: message.msg('dashboard.wh.tbar.status.success'), value: 'SUCCEEDED'},
                    {name: message.msg('dashboard.wh.tbar.status.fail'), value: 'FAILED'},
                    {name: message.msg('dashboard.wh.tbar.status.kill'), value: 'KILLED'}
                ]
            })
        },
        {
            xtype: 'tbtext',
            text: message.msg('common.workflowName')
        },
        {
            xtype: 'textfield',
            itemId: 'workflowName',
            reference: 'workflowName'
        },
        '->',
        {
            xtype: 'button',
            itemId: 'findWorkflowButton',
            formBind: true,
            text: message.msg('dashboard.wh.tbar.find'),
            iconCls: 'common-search',
            labelWidth: 50
        },
        {
            xtype: 'button',
            itemId: 'clearWorkflowButton',
            text: message.msg('dashboard.wh.tbar.clear'),
            iconCls: 'common-search-clear',
            labelWidth: 50
        },
        '|',
        {
            xtype: 'button',
            itemId: 'refreshWorkflowButton',
            text: message.msg('common.refresh'),
            iconCls: 'common-refresh',
            labelWidth: 50
        },
        '|',
        {
            xtype: 'button',
            itemId: 'killWorkflowButton',
            text: message.msg('dashboard.wh.tbar.status.kill'),
            iconCls: 'workflow-kill',
            labelWidth: 50
        }
    ]
});