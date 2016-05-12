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
Ext.define('Flamingo2.view.monitoring.namenode.CldbUsage', {
    extend: 'Ext.Panel',
    alias: 'widget.hdfsUsage',

    border: false,

    initComponent: function () {
        var me = this;
        var store = Ext.create('Ext.data.Store', {
            fields: ['num', 'total', 'used', 'free', 'reg_dt'],
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
                    fields: ['total', 'used', 'free'],
                    position: 'left',
                    grid: true,
                    titleMargin: 20,
                    renderer: function (value) {
                        return fileSize(value);
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
                /*
                 {
                 type: 'area',
                 axis: 'left',
                 title: '총 크기',
                 xField: 'num',
                 yField: 'total',
                 style: {
                 opacity: 0.50,
                 minGapWidth: 20,
                 fill: '#DB4D4D', // background color
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
                 this.setHtml(dateFormat2(storeItem.get('reg_dt')) + ' : <font color="#CC2900"><b>' + fileSize(storeItem.get('total')) + '</b></font>');
                 }
                 }
                 },
                 */
                {
                    type: 'area',
                    axis: 'left',
                    xField: 'num',
                    title: message.msg('monitoring.namenode.used'),
                    yField: 'used',
                    style: {
                        opacity: 0.50,
                        minGapWidth: 20,
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
                            this.setHtml(dateFormat2(storeItem.get('reg_dt')) + ' : <font color="#CC2900"><b>' + fileSize(storeItem.get('used')) + ' / ' + fileSize(storeItem.get('total')) + '</b></font>');
                        }
                    }
                }
            ]
        };

        me.callParent(arguments);
    }
});