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
Ext.define('Flamingo2.view.monitoring.namenode.JvmHeapUsage', {
    extend: 'Ext.Panel',
    alias: 'widget.jvmHeapUsage',

    border: false,

    initComponent: function () {
        var me = this;
        var store = Ext.create('Ext.data.Store', {
            fields: ['num', 'jvmMaxMemory', 'jvmTotalMemory', 'jvmUsedMemory', 'jvmFreeMemory', 'reg_dt'],
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
                border: false
            },
            axes: [
                {
                    type: 'numeric',
                    fields: ['jvmMaxMemory', 'jvmTotalMemory', 'jvmUsedMemory', 'jvmFreeMemory'],
                    position: 'left',
                    grid: true,
                    titleMargin: 20,
                    renderer: function (value) {
                        return value + 'M';
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
                    type: 'area',
                    axis: 'left',
                    title: message.msg('monitoring.namenode.max_jvm_mem'),
                    xField: 'num',
                    yField: 'jvmMaxMemory',
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
                    highlight: {
                        opacity: 1,
                        scaling: 1.5
                    },
                    tooltip: {
                        trackMouse: true,
                        style: 'background: #fff',
                        renderer: function (storeItem, item) {
                            this.setHtml(dateFormat2(storeItem.get('reg_dt')) + ' : <font color="#CC2900"><b>' + storeItem.get('jvmMaxMemory') + 'M</b></font>');
                        }
                    }
                },
                {
                    type: 'area',
                    axis: 'left',
                    xField: 'num',
                    yField: 'jvmUsedMemory',
                    title: message.msg('monitoring.namenode.used_jvm_mem'),
                    style: {
                        opacity: 0.50,
                        minGapWidth: 20,
                        stroke: 'black' // line color
                    },
                    marker: {
                        opacity: 0.50,
                        scaling: 0.01,
                        fx: {
                            duration: 200,
                            easing: 'easeOut'
                        }
                    },
                    highlight: {
                        opacity: 1,
                        scaling: 1.5
                    },
                    tooltip: {
                        trackMouse: true,
                        style: 'background: #fff',
                        renderer: function (storeItem, item) {
                            this.setHtml(dateFormat2(storeItem.get('reg_dt')) + ' : <font color="#CC2900"><b>' + storeItem.get('jvmUsedMemory') + 'M</b></font>');
                        }
                    }
                }
            ]
        };

        me.callParent(arguments);
    }
});