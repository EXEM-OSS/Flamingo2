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
Ext.define('Flamingo2.view.visualization.ggplot2.DataPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.ggplot2DataPanel',
    title: message.msg('common.data'),
    layout: 'border',
    margins: '5 5 5 5',
    width: 200,
    items: [
        {
            xtype: 'grid',
            region: 'center',
            columnLines: true,
            reference: 'dataList',
            bind: {
                store: '{dataList}'
            },
            columns: [
                {
                    text: message.msg('common.name'),
                    dataIndex: 'text',
                    flex: 1
                },
                {
                    text: message.msg('common.type'),
                    dataIndex: 'type',
                    flex: 1
                }
            ]
        },
        {
            xtype: 'grid',
            title: message.msg('visual.general_option'),
            region: 'south',
            reference: 'grdGeneralOptions',
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
                store: '{generalOptions}'
            }
        }
    ]
});