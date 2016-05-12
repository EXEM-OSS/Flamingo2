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

Ext.define('Flamingo2.view.designer.property._HiveColumnGrid', {
    extend: 'Flamingo2.view.designer.property._Grid',
    alias: 'widget._hiveColumnGrid',

    store: Ext.create('Ext.data.Store', {
        fields: [
            {name: 'name'},
            {name: 'type'},
            {name: 'collection'},
            {name: 'comment'}
        ],
        data: []
    }),
    selType: 'rowmodel',
    forceFit: true,
    columnLines: true,
    columns: [
        {
            text: message.msg('common.name'),
            dataIndex: 'name',
            width: 100,
            sortable: false,
            editor: {
                vtype: 'alphanum',
                allowBlank: false,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        },
        {
            text: message.msg('hive.column.type'),
            dataIndex: 'type',
            width: 80,
            sortable: false,
            editor: {
                xtype: 'combo',
                allowBlank: false,
                editable: false,
                triggerAction: 'all',
                queryMode: 'local',
                mode: 'local',
                store: new Ext.data.ArrayStore(
                    {
                        id: 0,
                        fields: ['typeId', 'typeString'],
                        data: [
                            ['tinyint', 'tinyint'],
                            ['smallint', 'smallint'],
                            ['int', 'int'],
                            ['bigint', 'bigint'],
                            ['boolean', 'boolean'],
                            ['float', 'float'],
                            ['double', 'double'],
                            ['string', 'string'],
                            ['timestamp', 'timestamp'],
                            ['binary', 'binary'],
                            ['struct', 'struct'],
                            ['map', 'map'],
                            ['array', 'array']
                        ]
                    }
                ),
                valueField: 'typeString',
                displayField: 'typeString',
                listClass: 'x-combo-list-small'
            }
        },
        {
            xtype: 'actioncolumn',
            itemId: 'collection',
            width: 20,
            items: [
                {
                    getClass: function (v, meta, record) {
                        var type = record.get('type');

                        if (type == 'struct' || type == 'map' || type == 'array') {
                            return 'common-find';
                        }
                    }
                }
            ]
        },
        {
            text: message.msg('hawq.label.exttable.comment'),
            dataIndex: 'comment',
            width: 150,
            sortable: false,
            editor: {
                allowBlank: true
            }
        }
    ],

    /**
     * Minimum Record
     */
    minRecordLength: 1
});