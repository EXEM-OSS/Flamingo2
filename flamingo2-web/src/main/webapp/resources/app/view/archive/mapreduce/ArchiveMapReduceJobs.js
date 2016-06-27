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
Ext.define('Flamingo2.view.archive.mapreduce.ArchiveMapReduceJobs', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.archiveMapReduceJobs',

    bind: {
        store: '{archiveMRJobsStore}'
    },

    title: message.msg('monitoring.history.title.finished'),
    height: 310,
    columns: [
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false,
            locked: true
        },
        {
            text: message.msg('dashboard.jobdetail.job.jobid'),
            dataIndex: 'jobId',
            width: 170,
            align: 'center',
            locked: true
        },
        {
            text: message.msg('dashboard.wh.column.text'),
            dataIndex: 'name',
            minWidth: 200,
            flex: 1,
            style: 'text-align:center',
            renderer: function (value, metaData, record, rowIdx, colIdx, store) {
                metaData.tdAttr = 'data-qtip="'
                    + message.msg('dashboard.jobdetail.job.jobid') + ' : ' + record.get('id')
                    + '<br/>'
                    + message.msg('dashboard.wh.column.text') + ' : ' + (record.get('name') ? record.get('name') : message.msg('monitoring.yarn.tip.na')) + '"';
                return value;
            }
        },
        {
            text: message.msg('common.status'), dataIndex: 'state', width: 100, align: 'center', sortable: true
        },
        {
            text: message.msg('common.user'), dataIndex: 'user', width: 100, align: 'center'
        },
        {
            text: message.msg('common.queue'), dataIndex: 'queue', width: 100, align: 'center'
        },
        {
            text: 'Map', dataIndex: 'mapsTotal', width: 60, align: 'center', sortable: true
        },
        {
            text: 'Reduce', dataIndex: 'reducesTotal', width: 60, align: 'center', sortable: true
        },
        {
            text: message.msg('dashboard.jobdetail.job.elapsed'), dataIndex: 'elapsedTime', width: 80, align: 'center',
            renderer: function (value, metaData, record, row, col, store, gridView) {
                var end, start = new Date(record.data.startTime);
                if (record.data.state == 'RUNNING') {
                    end = new Date();
                } else {
                    end = new Date(record.data.finishTime);
                }
                var diff = (end.getTime() - start.getTime()) / 1000;
                return toHumanReadableTime(Math.floor(diff));
            }
        },
        {
            text: message.msg('common.start'), dataIndex: 'startTime', width: 140, align: 'center', sortable: true
        },
        {
            text: message.msg('common.finish'), dataIndex: 'finishTime', width: 140, align: 'center'
        }
    ],
    viewConfig: {
        emptyText: message.msg('monitoring.history.msg.no_mapreduce.jog'),
        deferEmptyText: false,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    },
    tbar: [
        {
            xtype: 'tbtext',
            text: message.msg('common.searchCondition')
        },
        '|',
        {
            xtype: 'tbtext',
            text: message.msg('common.startDate')
        },
        {
            xtype: 'datefield',
            format: 'Y-m-d',
            itemId: 'startDate',
            editable: false,
            value: '',
            maxValue: new Date(),
            vtype: 'dateRange',
            width: 100,
            listeners: {
                select: function (field, value, eOpts) {
                    var start = dateFormat(value, 'yyyy-MM-dd');
                    var endDateFields = query('archiveMapReduceJobs #endDate');
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
            text: message.msg('common.endDate')
        },
        {
            xtype: 'datefield',
            format: 'Y-m-d',
            itemId: 'endDate',
            editable: false,
            value: '',
            maxValue: new Date(),
            vtype: 'dateRange',
            width: 100,
            listeners: {
                select: function (field, value, eOpts) {
                    var startDateFields = query('archiveMapReduceJobs #startDate');
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
            text: message.msg('common.searchType')
        },
        {
            xtype: 'combo',
            name: 'filter',
            itemId: 'filter',
            editable: false,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            width: 125,
            value: 'ALL',
            bind: {
                store: '{archiveMRSearchKeyword}'
            }
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'tbtext',
            text: message.msg('common.workflowName')
        },
        {
            xtype: 'textfield',
            itemId: 'mrJobName'
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.retrieve'),
            iconCls: 'common-search',
            labelWidth: 50,
            listeners: {
                click: 'onArchiveMRJobsFindClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.reset'),
            iconCls: 'common-search-clear',
            labelWidth: 50,
            listeners: {
                click: 'onArchiveMRJobsResetClick'
            }
        }
    ],
    dockedItems: [
        {
            xtype: 'pagingtoolbar',
            itemId: 'archiveMRJobsToolbar',
            dock: 'bottom',
            bind: {
                store: '{archiveMRJobsStore}'
            },
            displayInfo: true,
            doRefresh: function () {
                var mrJobsToolbar = query('archiveMapReduceJobs #archiveMRJobsToolbar');
                var mrJobsGrid = query('archiveMapReduceJobs');
                var startDateFields = query('archiveMapReduceJobs #startDate');
                var endDateFields = query('archiveMapReduceJobs #endDate');
                var filter = query('archiveMapReduceJobs #filter');
                var mrJobName = query('archiveMapReduceJobs #mrJobName');

                mrJobsToolbar.moveFirst();
                mrJobsGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                mrJobsGrid.getStore().getProxy().extraParams.startDate = startDateFields.getValue();
                mrJobsGrid.getStore().getProxy().extraParams.endDate = endDateFields.getValue();
                mrJobsGrid.getStore().getProxy().extraParams.filter = filter.getValue();
                mrJobsGrid.getStore().getProxy().extraParams.mrJobName = mrJobName.getValue();
            },
            listeners: {
                beforechange: function (toolbar, page, eOpts) {
                    var startDateFields = query('archiveMapReduceJobs #startDate');
                    var endDateFields = query('archiveMapReduceJobs #endDate');
                    var filter = query('archiveMapReduceJobs #filter');
                    var mrJobName = query('archiveMapReduceJobs #mrJobName');

                    toolbar.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                    toolbar.getStore().getProxy().extraParams.startDate = startDateFields.getValue();
                    toolbar.getStore().getProxy().extraParams.endDate = endDateFields.getValue();
                    toolbar.getStore().getProxy().extraParams.filter = filter.getValue();
                    toolbar.getStore().getProxy().extraParams.mrJobName = mrJobName.getValue();
                }
            }
        }
    ],
    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('common.refresh'),
            handler: 'onArchiveMapReduceJobsRefreshClick'
        }
    ],
    listeners: {
        itemclick: 'onArchiveMRJobGridItemClick',
        afterrender: 'onArchiveMapReduceJobsAfterRender'
    }
});