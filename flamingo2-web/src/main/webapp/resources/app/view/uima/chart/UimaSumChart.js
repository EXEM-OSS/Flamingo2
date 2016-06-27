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
Ext.define('Flamingo2.view.uima.chart.UimaSumChart', {
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.uimaSumChart',

    itemId: 'uimaSumChart',
    bind: {
        store: '{uimaSumChartStore}'
    },
    interactions: 'itemhighlight',
    axes: [
        {
            type: 'numeric',
            fields: 'sum',
            position: 'left',
            grid: true,
            titleMargin: 20,
            label: {
                fontFamily: 'Nanum Gothic',
                fontSize: '12px'
            }
        },
        {
            type: 'category',
            grid: true,
            fields: 'time',
            position: 'bottom',
            label: {
                fontFamily: 'Nanum Gothic',
                fontSize: '12px'
            }/*,
         renderer: function (value) {
         return dateFormat(new Date(value), 'MM-dd HH:mm:ss');
         }*/
        }
    ],
    animation: Ext.isIE8 ? false : {
        easing: 'bounceOut',
        duration: 500
    },
    series: [
        {
            type: 'area',
            axis: 'left',
            xField: 'time',
            yField: 'sum',
            style: {
                opacity: 0.50,
                minGapWidth: 20,
                fill: '#E68A00', // backgroud color : E68A00,476C91
                stroke: 'black' // line color
            },
            marker: {
                opacity: 0,
                scaling: 0.01,
                fx: {
                    duration: 200,
                    easing: 'easeOut'
                }
            },
            highlightCfg: {
                opacity: 1,
                scaling: 1.5
            },
            tooltip: {
                trackMouse: true,
                style: 'background: #fff',
                renderer: function (storeItem, item) {
                    this.setHtml(storeItem.get('time') + ' : <span style="color: #CC2900; "><b>'
                        + storeItem.get('sum') + '%' + '</b></font>');
                }
            },
            label: {
                field: 'sum',
                display: 'insideEnd',
                renderer: function (value) {
                    return value.toFixed(1);
                }
            }
        }
    ],
    listeners: {
        afterrender: 'onUimaSumChartAfterRender'
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
            itemId: 'uimaStartDate',
            editable: false,
            value: '',
            maxValue: new Date(),
            vtype: 'dateRange',
            width: 100,
            listeners: {
                select: function (field, value, eOpts) {
                    var start = dateFormat(value, 'yyyy-MM-dd');
                    var endDateFields = query('uimaSumChart #uimaEndDate');
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
            itemId: 'uimaEndDate',
            editable: false,
            value: '',
            maxValue: new Date(),
            vtype: 'dateRange',
            width: 100,
            listeners: {
                select: function (field, value, eOpts) {
                    var startDateFields = query('uimaSumChart #uimaStartDate');
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
                //click: 'onUimaSumChartFindClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.reset'),
            iconCls: 'common-search-clear',
            labelWidth: 50,
            listeners: {
                //click: 'onUimaSumChartResetClick'
            }
        }
    ],
    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('common.refresh'),
            handler: 'onUimaSumChartRefreshClick'
        }
    ]
});