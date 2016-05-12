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
Ext.define('Flamingo2.view.designer.property._ParallelGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget._parallelGrid',

    requires: [
        'Flamingo2.view.designer.property._ParallelGridController'
    ],

    controller: 'designer._parallelGridController',

    stripeRows: true,

    margins: '0 0 0 0',

    selectableShapes: [],

    shapeElement: null,

    store: Ext.create('Ext.data.Store', {
        fields: [
            {name: 'node'},
            {name: 'id'},
            {name: 'sequence'},
            {name: 'parallel'}
        ],
        data: []
    }),

    columns: [
        {
            text: message.msg('workflow.etc.flows.column.node'),
            flex: 1,
            sortable: true,
            dataIndex: 'node',
            align: 'center'
        },
        {
            text: message.msg('workflow.etc.flows.column.id'),
            flex: 1,
            sortable: true,
            dataIndex: 'id',
            align: 'center'
        },
        {
            text: "Provider",
            hidden: true,
            flex: 1,
            sortable: true,
            dataIndex: 'provider',
            align: 'center'
        },
        {
            text: message.msg('workflow.etc.flows.column.sequence'),
            flex: 1,
            sortable: true,
            dataIndex: 'sequence',
            align: 'center',
            field: {
                xtype: 'numberfield',
                allowBlank: false,
                minValue: 0,
                maxValue: 100000
            }
        },
        {
            text: message.msg('workflow.etc.flows.column.parallel'),
            flex: 1,
            sortable: true,
            dataIndex: 'parallel',
            align: 'center',
            renderer: function (value) {
                if (value) {
                    return message.msg('workflow.etc.flows.column.parallel.sync');
                }
                return message.msg('workflow.etc.flows.column.parallel.async');
            }
        }
    ],
    plugins: {
        ptype: 'cellediting',
        clicksToEdit: 1
    },

    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    reference: 'flowUp',
                    text: message.msg('workflow.etc.flows.dock.flowUp'),
                    disabled: true,
                    listeners: {
                        click: 'onFlowUpClick'
                    }
                },
                {
                    reference: 'flowDown',
                    text: message.msg('workflow.etc.flows.dock.flowDown'),
                    disabled: true,
                    listeners: {
                        click: 'onFlowDownClick'
                    }
                }
            ]
        }
    ],

    tipUse: 'on',

    listeners: {
        drawGrid: 'drawGrid',
        edit: 'onCellEdit',
        select: 'onItemSelect',
        formfieldForSave: 'formfieldForSave'
    }
});