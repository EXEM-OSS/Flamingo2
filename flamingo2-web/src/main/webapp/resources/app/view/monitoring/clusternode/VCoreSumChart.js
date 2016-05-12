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
Ext.define('Flamingo2.view.monitoring.clusternode.VCoreSumChart', {
    extend: 'Ext.Panel',
    alias: 'widget.vCoreSumChart',

    initComponent: function () {
        var me = this;
        var store = Ext.create('Ext.data.Store', {
            fields: ['vcoreSum', 'memorySum', 'time'],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.CLUSTERNODES.TIMESERIES,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        });

        me.items = {
            xtype: 'cartesian',
            height: 200,
            store: store,
            interactions: 'itemhighlight',
            axes: [
                {
                    type: 'numeric',
                    fields: 'vcoreSum',
                    position: 'left',
                    grid: true,
                    titleMargin: 20,
                    renderer: function (value) {
                        if (value >= 0) {
                            return toCommaNumber(value);
                        } else {
                            return 0;
                        }
                    }
                },
                {
                    type: 'category',
                    grid: true,
                    fields: 'time',
                    position: 'bottom'
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
                    yField: 'vcoreSum',
                    style: {
                        opacity: 0.50,
                        minGapWidth: 20,
                        fill: '#DB4D4D', // backgroud color
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
                            this.setHtml(storeItem.get('time') + ' : <font color="#CC2900"><b>' + toCommaNumber(storeItem.get('vcoreSum')) + message.msg('monitoring.clusternode.count') + '</b></font>');
                        }
                    },
                    label: {
                        field: 'vcoreSum',
                        display: 'insideEnd',
                        renderer: function (value) {
                            return value.toFixed(1);
                        }
                    }
                }
            ]
        };

        me.callParent(arguments);
    }
});