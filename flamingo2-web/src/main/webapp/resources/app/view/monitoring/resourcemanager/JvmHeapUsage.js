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
Ext.define('Flamingo2.view.monitoring.resourcemanager.JvmHeapUsage', {
    extend: 'Ext.Panel',
    alias: 'widget.rmJvmHeapUsage',

    border: false,

    items: [
        {
            xtype: 'polar',
            height: 300,
            listeners: {
                afterrender: 'onStoreAfterrender'
            },
            bind: {
                store: '{jvmHeapStore}'
            },
            legend: {
                docked: 'bottom'
            },
            insetPadding: 10,
            innerPadding: 10,
            // theme = ['Sky', 'Red', 'Yellow', 'Blue', 'Base', 'blue-gradients', 'Midnight', 'Purple'];
            series: [
                {
                    type: 'pie',
                    angleField: 'value',
                    highlight: true,
                    label: {
                        field: 'name',
                        font: '12px NanumGothic',
                        fontFamily: 'NanumGothic',
                        calloutLine: {
                            length: 1,
                            width: 3
                            // specifying 'color' is also possible here
                        }
                    },
                    style: {
                        opacity: 0.80,
                        stroke: 'white' // line color
                    },
                    tooltip: {
                        trackMouse: true,
                        renderer: function (storeItem, item) {
                            this.setHtml(storeItem.get('name') + ': ' + storeItem.get('value') + ' M');
                        }
                    }
                }
            ]
        }
    ]
});