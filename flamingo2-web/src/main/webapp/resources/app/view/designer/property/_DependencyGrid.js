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
 * Inner Grid : Dependency
 *
 * @class
 * @extends Flamingo2.view.designer.property._Grid
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.designer.property._DependencyGrid', {
    extend: 'Flamingo2.view.designer.property._Grid',
    alias: 'widget._dependencyGrid',

    store: Ext.create('Ext.data.Store', {
        fields: [
            {name: 'groupId'},
            {name: 'artifactId'},
            {name: 'version'}
        ],
        data: []
    }),
    selType: 'rowmodel',
    forceFit: true,
    columnLines: true,
    columns: [
        {
            text: message.msg('workflow.common.group.id'),
            dataIndex: 'groupId',
            width: 150,
            editor: {
                vtype: 'exceptcomma',
                allowBlank: false,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        },
        {
            text: message.msg('workflow.common.artifact.id'),
            dataIndex: 'artifactId',
            width: 150,
            editor: {
                vtype: 'exceptcomma',
                allowBlank: true
            }
        },
        {
            text: message.msg('workflow.common.version'),
            dataIndex: 'version',
            width: 50,
            editor: {
                vtype: 'exceptcomma',
                allowBlank: true
            }
        }
    ],

    /**
     * 최소 레코드 갯수
     */
    minRecordLength: 0
});