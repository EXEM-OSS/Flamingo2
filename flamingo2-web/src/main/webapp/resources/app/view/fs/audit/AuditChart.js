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
Ext.define('Flamingo2.view.fs.audit.AuditChart', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.auditChart',

    requires: [
        'Flamingo2.view.fs.audit.AuditTop10',
        'Flamingo2.view.fs.audit.AuditNowStatus',
        'Flamingo2.view.fs.audit.AuditTrend'
    ],

    layout: {
        type: 'hbox',
        align: 'stretch'
    },

    items: [
        {
            xtype: 'auditTop10',
            bind: {
                store: '{auditTop10Store}'
            },
            title: message.msg('hdfs.audit.title.chart.typeList'),
            iconCls: 'fa fa-server fa-fw',
            margin: '5 0 0 0',
            autoScroll: true,
            width: 220,
            border: true
        },
        {
            xtype: 'auditNowStatus',
            itemId: 'auditNowStatus',
            bind: {
                store: '{auditNowStatusStore}'
            },
            title: message.msg('hdfs.audit.title.chart.typeStat'),
            iconCls: 'fa fa-server fa-fw',
            margin: '5 0 0 5',
            flex: 2,
            border: true
        },
        {
            xtype: 'auditTrend',
            itemId: 'auditTrendChart',
            bind: {
                store: '{auditTrendStore}'
            },
            title: message.msg('hdfs.audit.title.chart.dateTypeStat'),
            iconCls: 'fa fa-server fa-fw',
            margin: '5 0 0 5',
            flex: 2,
            border: true
        }
    ],
    tbar: [
        {
            xtype: 'tbtext',
            text: message.msg('common.searchCondition')
        },
        '|',
        {
            xtype: 'tbtext',
            text: message.msg('common.searchType')
        },
        {
            xtype: 'combo',
            name: 'type',
            itemId: 'type',
            editable: false,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            width: 90,
            value: 'ACT',
            bind: {
                store: '{keyword}'
            }
        },
        {
            xtype: 'tbspacer',
            width: 10
        },
        {
            xtype: 'tbtext',
            text: message.msg('common.startDate')
        },
        {
            xtype: 'datefield',
            itemId: 'startDateAuditChart',
            width: 100,
            editable: false,
            vtype: 'dateRange',
            format: 'Y-m-d',
            value: '',
            maxValue: new Date(),
            listeners: {
                afterrender: function (item) {
                    if (item.rawValue == '') {
                        var today = new Date();
                        var firstDay = new Date(today.getFullYear(), today.getMonth(), 1);

                        item.setRawValue(dateFormat(new Date(firstDay), 'yyyy-MM-dd'));
                    }
                },
                select: function (field, value, eOpts) {
                    var start = dateFormat(value, 'yyyy-MM-dd');
                    var endDateFields = query('auditChart #endDateAuditChart');
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
            itemId: 'endDateAuditChart',
            width: 100,
            format: 'Y-m-d',
            vtype: 'dateRange',
            editable: false,
            value: '',
            maxValue: new Date(),
            listeners: {
                afterrender: function (item) {
                    if (item.rawValue == '') {
                        item.setRawValue(dateFormat(new Date(), 'yyyy-MM-dd'));
                    }
                },
                select: function (field, value, eOpts) {
                    var startDateFields = query('auditChart #startDateAuditChart');
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
            xtype: 'button',
            formBind: true,
            text: message.msg('common.retrieve'),
            iconCls: 'common-search',
            labelWidth: 50,
            listeners: {
                click: 'onAuditChartFindClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.reset'),
            iconCls: 'common-search-clear',
            labelWidth: 50,
            listeners: {
                click: 'onAuditChartResetClick'
            }
        }
    ],
    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('common.refresh'),
            handler: 'onAuditChartRefreshClick'
        }
    ]
});