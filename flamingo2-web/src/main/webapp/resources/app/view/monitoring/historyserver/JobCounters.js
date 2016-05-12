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
Ext.define('Flamingo2.view.monitoring.historyserver.JobCounters', {
    extend: 'Ext.tree.Panel',
    alias: 'widget.jobCounters',

    requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.tree.*'
    ],

    useArrows: true,
    rootVisible: false,
    multiSelect: false,
    singleExpand: false,
    expanded: true,

    bind: {
        store: '{jobCounterStore}'
    },

    viewConfig: {
        enableTextSelection: true,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-20';
        }
    },

    columns: [
        {
            xtype: 'treecolumn', //this is so we know which column will show the tree
            text: message.msg('monitoring.history.counter_group'),
            flex: 4,
            sortable: true,
            dataIndex: 'name',
            style: 'text-align:center'
        },
        {
            text: 'Map',
            flex: 1,
            dataIndex: 'mapCounterValue',
            align: 'center',
            style: 'text-align:center',
            renderer: function (value) {
                return Ext.util.Format.number(value, '0,000');
            }
        },
        {
            text: 'Reduce',
            flex: 1,
            dataIndex: 'reduceCounterValue',
            align: 'center',
            style: 'text-align:center',
            renderer: function (value) {
                return Ext.util.Format.number(value, '0,000');
            }
        },
        {
            text: message.msg('common.sum'),
            flex: 1,
            dataIndex: 'totalCounterValue',
            align: 'center',
            style: 'text-align:center',
            renderer: function (value) {
                return Ext.util.Format.number(value, '0,000');
            }
        }
    ]
});