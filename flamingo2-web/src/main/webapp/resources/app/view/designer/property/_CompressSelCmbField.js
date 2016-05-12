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
 * Inner FieldContainer : CompressSelCmbField
 *
 * @class
 * @extends Ext.form.FieldContainer
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.designer.property._CompressSelCmbField', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget._compressSelCmbField',

    fieldLabel: message.msg('workflow.label_compression_type'),

    defaults: {
        hideLabel: true
    },
    layout: 'hbox',
    items: [
        {
            xtype: 'combo',
            name: 'compressionType',
            value: 'NO',
            flex: 1,
            forceSelection: true,
            editable: false,
            displayField: 'name',
            valueField: 'value',
            mode: 'local',
            queryMode: 'local',
            disabled: true,
            triggerAction: 'all',
            tpl: '<tpl for="."><div class="x-boundlist-item" data-qtip="{description}">{name}</div></tpl>',
            store: Ext.create('Ext.data.Store', {
                fields: ['name', 'value', 'description'],
                data: [
                    {
                        name: message.msg('workflow.common.compress.no.name'),
                        value: 'NO',
                        description: message.msg('workflow.common.compress.no.desc')
                    },
                    {
                        name: message.msg('workflow.common.compress.snappy.name'),
                        value: 'SNAPPY',
                        description: message.msg('workflow.common.compress.snappy.desc')
                    },
                    {
                        name: message.msg('workflow.common.compress.lzo.name'),
                        value: 'LZO',
                        description: message.msg('workflow.common.compress.lzo.desc')
                    },
                    {
                        name: message.msg('workflow.common.compress.bzip.name'),
                        value: 'BZIP',
                        description: message.msg('workflow.common.compress.bzip.desc')
                    }
                ]
            })
        },
        {
            xtype: 'checkboxfield',
            name: 'isGetMerge',
            boxLabel: 'Get Merge',
            flex: 1
        }
    ]
});