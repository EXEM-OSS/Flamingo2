/*
 * Copyright (C) 2011  Flamingo Project (http://www.cloudine.io).
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
Ext.define('Flamingo2.view.batch.chart.Jobs', {
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.jobs',
    requires: [
        'Flamingo2.view.batch.chart.EngineSummaryChartModel'
    ],
    viewModel: {
        type: 'enginesummarychart'
    },
    bind: {
        store: '{jobs}'
    },
    border: false,
    insetPadding: 20,
    interactions: 'itemhighlight',
    /*
     legend: {
     docked: 'bottom',
     border: false,
     style: {borderColor: 'red'}
     },
     */
    axes: [
        {
            type: 'numeric',
            fields: ['running', 'total'],
            position: 'left',
            grid: true,
            titleMargin: 20,
            minimum: 0,
            majorTickSteps: 1,
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
        /*
         {
         type: 'area',
         axis: 'left',
         title: message.msg('batch.total_count'),
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
         this.setHtml(storeItem.get('time') + ' : <font color="#CC2900"><b>' + message.msg('batch.total_count') + ' ' + toCommaNumber(storeItem.get('total')) + '</b></font>');
         }
         }
         },
         */
        {
            type: 'area',
            axis: 'left',
            xField: 'num',
            title: message.msg('batch.running_count'),
            yField: 'running',
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
                    this.setHtml(storeItem.get('time') + ' : <font color="#CC2900"><b>' + message.msg('batch.running_count') + ' ' + toCommaNumber(storeItem.get('running')) + '</b></font>');
                }
            }
        }
    ]
});