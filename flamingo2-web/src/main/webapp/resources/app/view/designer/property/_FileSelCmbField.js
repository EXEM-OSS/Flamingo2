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
Ext.define('Flamingo2.view.designer.property._FileSelCmbField', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget._fileSelCmbField',

    fieldLabel: message.msg('common.browse'),
    defaults: {
        hideLabel: true
    },
    layout: 'hbox',
    items: [
        {
            xtype: 'combo',
            name: 'selectionType',
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
                    {name: message.msg('workflow.type_same_path'), value: 'NO', description: ''},
                    {name: message.msg('workflow.type_latest_file'), value: 'RECENT', description: ''},
                    {name: message.msg('workflow.type_string_pattern'), value: 'PATTERN', description: ''},
                    {name: message.msg('workflow.type_a_few_days_ago'), value: 'DAY', description: ''}
                ]
            }),
            listeners: {
                change: function (combo, newValue, oldValue, eOpts) {
                    // 콤보 값에 따라 관련 textfield 를 enable | disable 처리한다.
                    var customValueField = combo.nextSibling('textfield');
                    if (newValue === 'PATTERN' || newValue === 'DAY') {
                        customValueField.enable();
                        customValueField.isValid();
                    } else {
                        customValueField.disable();
                        customValueField.setValue('');
                    }
                }
            }
        },
        {
            xtype: 'textfield',
            name: 'selectionValue',
            flex: 1,
            value: '',
            disabled: true,
            allowBlank: false
        }
    ]
});