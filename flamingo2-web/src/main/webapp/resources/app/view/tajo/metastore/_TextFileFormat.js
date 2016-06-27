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
Ext.define('Flamingo2.view.tajo.metastore._TextFileFormat', {
    extend: 'Ext.container.Container',
    alias: 'widget.tajoMetaTextFileFormat',

    layout: {
        type: 'table',
        columns: 2,
        tableAttrs: {
            style: {
                width: '100%'
            }
        }
    },

    defaults: {
        labelAlign: 'right'
    },
    items: [{
        xtype: 'fieldcontainer',
        layout: 'hbox',
        items: [
            {
                xtype: 'textfield',
                fieldLabel: 'Delimiter',
                reference: 'delimiter',
                name: 'delimiter',
                readOnly: true,
                value: 'Vertivalbar',
                delimiter: '\\174'
            },{
                xtype: 'button',
                iconCls: 'common-search',
                margin: '0 4 0 4',
                handler: 'onBtnDelimiterClick',
                target: 'delimiter'
            }
        ]
    },{
        xtype: 'combobox',
        fieldLabel: 'Null Text',
        name: 'nulltext',
        editable: false,
        displayField: 'key',
        valueField: 'value',
        value: 'empty',
        bind: {
            store: '{nullText}'
        }
    }/*,{
        xtype: 'combobox',
        fieldLabel: 'Timezone',
        name: 'timezone',
        editable: false
    }*/,{
        xtype: 'numberfield',
        fieldLabel: 'Error Tolerance',
        name: 'errorTolerance',
        step: 1,
        colspan: 2,
        minValue: 0,
        value: 0
    },{
        xtype: 'textfield',
        fieldLabel: 'SerDe',
        width: '100%',
        name: 'serde',
        colspan: 2
    },{
        xtype: 'textfield',
        fieldLabel: 'Compress',
        width: '100%',
        name: 'compress',
        colspan: 2
    }]
});