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
Ext.define('Flamingo2.view.visualization.ggplot2.LayerPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.ggplot2LayerPanel',

    requires: [
        'Flamingo2.view.visualization.ggplot2._geomMenu'
    ],

    layout: 'border',
    margins: '5 5 5 5',
    width: 200,
    border: false,
    title: message.msg('visual.layer'),

    tbar: [
        '->',
        {
            xtype: 'button',
            text: message.msg('common.add'),
            iconCls: 'common-add',
            reference: 'btnLayerAdd',
            disabled: true,
            menu: {
                xtype: 'ggplot2GeomMenu'
            }
        },
        {
            xtype: 'button',
            text: message.msg('common.delete'),
            iconCls: 'common-remove',
            reference: 'btnLayerRemove',
            disabled: true,
            handler: 'onBtnLayerRemoveClick'
        }
    ],
    items: [
        {
            xtype: 'grid',
            region: 'center',
            reference: 'layerList',
            border: false,
            store: new Ext.data.ArrayStore({
                fields: ['id', 'Geom'],
                idIndex: 0//, // id for each record will be the first element
            }),
            columns: [
                {
                    xtype: 'rownumberer'
                },
                {
                    text: "Geom", flex: 1, dataIndex: 'Geom', sortable: true
                }
            ],
            viewConfig: {
                forceFit: true
            },
            margins: '3 3 0 3',
            hideHeaders: true,
            autoScroll: true,
            listeners: {
                itemclick: 'onLayerItemclick',
                rowcontextmenu: 'onLayerRowcontextmenu'
            }

        },
        ,
        {
            xtype: 'grid',
            title: message.msg('visual.layer_option'),
            region: 'south',
            reference: 'grdLayerOptions',
            margins: '3 3 3 3',
            columnLines: true,
            split: true,
            height: 250,
            columns: [
                {
                    text: message.msg('common.name'),
                    dataIndex: 'name',
                    flex: 1
                },
                {
                    text: message.msg('common.value'),
                    dataIndex: 'value',
                    flex: 1
                }
            ],
            bind: {
                store: '{layerOptions}'
            }
        }
    ]
});