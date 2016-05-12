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
Ext.define('Flamingo2.view.monitoring.resourcemanager.ApplicationStatus', {
    extend: 'Ext.Panel',
    alias: 'widget.applicationStatus',

    border: false,

    items: [
        {
            xtype: 'cartesian',
            height: 300,
            insetPadding: 40,
            interactions: 'itemhighlight',
            //theme: 'yellow-gradients',
            listeners: {
                afterrender: 'onStoreAfterrender'
            },
            bind: {
                store: '{applicationStatusStore}'
            },
            axes: [
                {
                    type: 'numeric',
                    fields: 'value',
                    position: 'left',
                    titleMargin: 20,
                    title: {
                        text: message.msg('monitoring.rm.app_count'),
                        fontFamily: 'Nanum Gothic',
                        fontSize: '12px'
                    },
                    label: {
                        fontFamily: 'Nanum Gothic',
                        fontSize: '12px'
                    },
                    grid: true
                },
                {
                    type: 'category',
                    fields: 'name',
                    position: 'bottom',
                    grid: true,
                    label: {
                        rotate: {
                            degrees: -20
                        },
                        fontFamily: 'Nanum Gothic',
                        fontSize: '12px'
                    }
                }
            ],
            animation: Ext.isIE8 ? false : {
                easing: 'backOut',
                duration: 500
            },
            series: {
                type: 'bar',
                xField: 'name',
                yField: 'value',
                legend: {
                    docked: 'bottom'
                },
                style: {
                    minGapWidth: 20
                },
                highlight: {
                    strokeStyle: 'black',
                    fillStyle: 'gold',
                    lineDash: [5, 3]
                },
                label: {
                    field: 'value',
                    display: 'insideEnd',
                    renderer: function (value) {
                        return value;
                    }
                }
            }
        }
    ]
});