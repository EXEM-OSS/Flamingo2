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
Ext.define('Flamingo2.view.fs.audit.AuditNowStatus', {
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.auditNowStatus',

    insetPadding: '15 15 5 15',

    interactions: 'itemhighlight',

    axes: [
        {
            type: 'numeric',
            fields: 'cnt',
            position: 'left',
            grid: true
        },
        {
            type: 'category',
            fields: 'name',
            position: 'bottom',
            label: {
                rotate: {
                    degrees: -35
                }
            },
            grid: true
        }
    ],

    animation: Ext.isIE8 ? false : {
        easing: 'backOut',
        duration: 500
    },

    series: {
        type: 'bar',
        xField: 'name',
        yField: 'cnt',
        highlight: {
            strokeStyle: 'black',
            fillStyle: 'gold',
            lineDash: [5, 3]
        },
        label: {
            field: 'cnt',
            display: 'insideEnd',
            orientation: 'horizontal'
        },
        tooltip: {
            trackMouse: true,
            style: 'background: #fff',
            renderer: function (storeItem) {
                this.setHtml(format(message.msg('hdfs.audit.group.grid'), storeItem.get('name'), storeItem.get('cnt')));
            }
        }
    },

    listeners: {
        afterrender: 'onAuditNowStatusAfterRender'
    }
});