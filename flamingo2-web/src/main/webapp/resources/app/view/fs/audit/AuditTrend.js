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
Ext.define('Flamingo2.view.fs.audit.AuditTrend', {
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.auditTrend',

    insetPadding: '15 15 5 15',
    height: 517,
    width: 852,
    colors: [
        '#115fa6',
        '#94ae0a',
        '#a61120',
        '#ff8809',
        '#ffd13e',
        '#a61187',
        '#24ad9a',
        '#7c7474',
        '#a66111'
    ],
    axes: [
        {
            type: 'category',
            fields: [
                'time'
            ],
            label: {
                rotate: {
                    degrees: -35
                }
            },
            grid: true,
            position: 'bottom',
            renderer: function (value) {
                return dateFormat(new Date(value), 'MM-dd');
            }
        },
        {
            type: 'numeric',
            fields: [
                'data1', 'data2', 'data3', 'data4', 'data5', 'data6', 'data7', 'data8', 'data9', 'data10', 'data11'
            ],
            grid: true,
            position: 'left'
        }
    ],
    animation: Ext.isIE8 ? false : {
        easing: 'bounceOut',
        duration: 500
    },
    listeners: {
        afterrender: 'onAuditTrendAfterRender'
    }
});