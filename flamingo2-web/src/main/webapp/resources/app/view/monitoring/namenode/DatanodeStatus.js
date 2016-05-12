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
Ext.define('Flamingo2.view.monitoring.namenode.DatanodeStatus', {
    extend: 'Ext.Panel',
    alias: 'widget.datanodeStatus',

    border: false,

    initComponent: function () {
        var me = this;
        var store = Ext.create('Ext.data.Store', {
            fields: ['num', 'nodeAll', 'nodeDead', 'nodeLive', 'reg_dt'],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.NAMENODE.DFS_USAGE,
                extraParams: {
                    clusterName: ENGINE.id
                },
                remoteSort: true,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        });

        me.items = {
            xtype: 'cartesian',
            border: false,
            store: store,
            insetPadding: 20,
            interactions: 'itemhighlight',
            legend: {
                docked: 'bottom',
                border: false,
                style: {borderColor: 'red'}
            },
            axes: [
                {
                    type: 'numeric',
                    fields: ['nodeAll', 'nodeDead', 'nodeLive'],
                    position: 'left',
                    grid: true,
                    titleMargin: 20,
                    renderer: function (value) {
                        return toCommaNumber(value);
                    },
                    label: {
                        fontFamily: 'Nanum Gothic',
                        fontSize: '12px'
                    }
                }
            ],
            animation: Ext.isIE8 ? false : {
                easing: 'bounceOut',
                duration: 500
            },
            series: [
                {
                    type: 'line',
                    axis: 'left',
                    title: 'All',
                    xField: 'num',
                    yField: 'nodeAll',
                    style: {
                        opacity: 0.50,
                        minGapWidth: 20,
                        fill: '#94AE0A', // background color
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
                            this.setHtml(dateFormat2(storeItem.get('reg_dt')) + ' : <font color="#CC2900"><b>' + toCommaNumber(storeItem.get('nodeAll')) + '</b></font>');
                        }
                    }
                },
                {
                    type: 'line',
                    axis: 'left',
                    title: 'Dead',
                    xField: 'num',
                    yField: 'nodeDead',
                    style: {
                        opacity: 0.50,
                        minGapWidth: 20,
                        fill: '#115FA6', // background color
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
                            this.setHtml(dateFormat2(storeItem.get('reg_dt')) + ' : <font color="#CC2900"><b>' + toCommaNumber(storeItem.get('nodeDead')) + '</b></font>');
                        }
                    }
                },
                {
                    type: 'line',
                    axis: 'left',
                    title: 'Live',
                    xField: 'num',
                    showInLegend: true,
                    yField: 'nodeLive',
                    style: {
                        opacity: 0.50,
                        minGapWidth: 20,
                        fill: '#A61120', // background color
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
                            this.setHtml(dateFormat2(storeItem.get('reg_dt')) + ' : <font color="#CC2900"><b>' + toCommaNumber(storeItem.get('nodeLive')) + '</b></font>');
                        }
                    }
                }
            ]
        };

        me.callParent(arguments);
    }
});