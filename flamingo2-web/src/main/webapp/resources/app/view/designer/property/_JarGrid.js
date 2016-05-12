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

/**
 * Inner Grid : InputPath Grid
 *
 * @class
 * @extends Flamingo2.view.designer.property._Grid
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.designer.property._JarGrid', {
    extend: 'Flamingo2.view.designer.property._Grid',
    alias: 'widget._jarGrid',

    store: Ext.create('Ext.data.Store', {
        fields: [
            {name: 'path'}
        ]
    }),

    minRecordLength: 0,
    selType: 'rowmodel',
    forceFit: true,
    columnLines: true,
    hasDefaults: false,
    fileBrowser: true,
    columns: [
        {
            text: message.msg('common.path'),
            dataIndex: 'path',
            editor: {
                xtype: 'textfield',
                allowBlank: false
            }
        }
    ]
});