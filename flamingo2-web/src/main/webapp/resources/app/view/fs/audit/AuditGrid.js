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
Ext.define('Flamingo2.view.fs.audit.AuditGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.auditGrid',

    bind: {
        store: '{auditGridStore}'
    },
    columns: [
        {text: message.msg('hdfs.audit.column.grid.id'), width: 60, dataIndex: 'id', align: 'center', hidden: true},
        {
            text: message.msg('hdfs.audit.column.grid.clusterId'),
            width: 60,
            dataIndex: 'clusterId',
            align: 'center',
            hidden: true
        },
        {
            text: message.msg('hdfs.audit.column.grid.clusterName'),
            width: 120,
            dataIndex: 'clusterName',
            align: 'center',
            hidden: true
        },
        {text: message.msg('hdfs.audit.column.grid.username'), width: 90, dataIndex: 'username', align: 'center'},
        {
            text: message.msg('hdfs.audit.column.grid.fileSystemType'),
            width: 100,
            dataIndex: 'fileSystemType',
            align: 'center',
            sortable: false,
            hidden: true
        },
        {
            text: message.msg('hdfs.audit.column.grid.fileType'),
            width: 80,
            dataIndex: 'fileType',
            align: 'center',
            sortable: false
        },
        {
            text: message.msg('hdfs.audit.column.grid.auditType'),
            width: 120,
            dataIndex: 'auditType',
            align: 'center',
            sortable: false
        },
        {
            text: message.msg('hdfs.audit.column.grid.requestType'),
            width: 100,
            dataIndex: 'requestType',
            align: 'center',
            sortable: false,
            hidden: true
        },
        {text: message.msg('common.path'), flex: 1, dataIndex: 'from', style: 'text-align: center', sortable: false},
        {
            text: message.msg('hdfs.audit.column.grid.length'),
            width: 100,
            dataIndex: 'length',
            align: 'center',
            sortable: true
        },
        {text: message.msg('hdfs.audit.column.grid.workDate'), width: 150, dataIndex: 'workDate', align: 'center'}
    ],
    dockedItems: [
        {
            xtype: 'pagingtoolbar',
            itemId: 'auditGridToolbar',
            dock: 'bottom',
            bind: {
                store: '{auditGridStore}'
            },
            displayInfo: true,
            doRefresh: function () {
                var auditGridToolbar = query('auditGrid #auditGridToolbar');
                var auditGrid = query('auditGrid');
                var startDateFields = query('auditGrid #gridStartDate');
                var endDateFields = query('auditGrid #gridEndDate');
                var auditType = query('auditGrid #type');
                var path = query('auditGrid #path');

                auditGridToolbar.moveFirst();
                auditGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                auditGrid.getStore().getProxy().extraParams.startDate = startDateFields.getValue();
                auditGrid.getStore().getProxy().extraParams.endDate = endDateFields.getValue();
                auditGrid.getStore().getProxy().extraParams.auditType = auditType.getValue();
                auditGrid.getStore().getProxy().extraParams.path = path.getValue();
                auditGrid.getStore().load();
            },
            listeners: {
                beforechange: function (toolbar, nextPage, eOpts) {
                    var auditGrid = query('auditGrid');
                    var startDateFields = query('auditGrid #gridStartDate');
                    var endDateFields = query('auditGrid #gridEndDate');
                    var auditType = query('auditGrid #type');
                    var path = query('auditGrid #path');

                    auditGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                    auditGrid.getStore().getProxy().extraParams.nextPage = nextPage;
                    auditGrid.getStore().getProxy().extraParams.startDate = startDateFields.getValue();
                    auditGrid.getStore().getProxy().extraParams.endDate = endDateFields.getValue();
                    auditGrid.getStore().getProxy().extraParams.auditType = auditType.getValue();
                    auditGrid.getStore().getProxy().extraParams.path = path.getValue();
                }
            }
        }
    ],
    viewConfig: {
        enableTextSelection: true,
        stripeRows: true,
        columnLines: true,
        getRowClass: function () {
            return 'cell-height-30';
        }
    },
    tbar: [
        {
            xtype: 'tbtext',
            text: message.msg('hdfs.audit.tbar.grid.condition')
        },
        '|',
        {
            xtype: 'tbtext',
            text: message.msg('hdfs.audit.tbar.grid.start')
        },
        {
            xtype: 'datefield',
            format: 'Y-m-d',
            reference: 'startDate',
            itemId: 'gridStartDate',
            value: '',
            maxValue: new Date(),
            vtype: 'dateRange',
            width: 100,
            listeners: {
                select: function (field, value, eOpts) {
                    var start = dateFormat(value, 'yyyy-MM-dd');
                    var endDateFields = query('auditGrid #gridEndDate');
                    var end = dateFormat(endDateFields.getValue(), 'yyyy-MM-dd');

                    if (start > end && endDateFields.getValue()) {
                        return field.setActiveError(message.msg('hdfs.audit.date.start.msg'));
                    }
                }
            }
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'tbtext',
            text: message.msg('hdfs.audit.tbar.grid.end')
        },
        {
            xtype: 'datefield',
            format: 'Y-m-d',
            reference: 'endDate',
            itemId: 'gridEndDate',
            value: '',
            maxValue: new Date(),
            vtype: 'dateRange',
            width: 100,
            listeners: {
                select: function (field, value, eOpts) {
                    var startDateFields = query('auditGrid #gridStartDate');
                    var start = dateFormat(startDateFields.getValue(), 'yyyy-MM-dd');
                    var end = dateFormat(value, 'yyyy-MM-dd');

                    if (start > end) {
                        return field.setActiveError(message.msg('hdfs.audit.date.end.msg'));
                    }
                }
            }
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'tbtext',
            text: message.msg('hdfs.audit.tbar.grid.condition_type')
        },
        {
            xtype: 'combo',
            name: 'type',
            reference: 'type',
            itemId: 'type',
            editable: false,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            width: 125,
            value: 'ALL',
            bind: {
                store: '{auditListKeyword}'
            }
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'tbtext',
            text: message.msg('common.path')
        },
        {
            xtype: 'textfield',
            reference: 'path',
            itemId: 'path'
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('hdfs.audit.button.grid.search'),
            iconCls: 'common-search',
            labelWidth: 50,
            listeners: {
                click: 'onAuditFindClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('hdfs.audit.button.grid.search_clear'),
            iconCls: 'common-search-clear',
            labelWidth: 50,
            listeners: {
                click: 'onAuditResetClick'
            }
        }
    ],
    listeners: {
        afterrender: 'onAuditListAfterRender'
    }
});