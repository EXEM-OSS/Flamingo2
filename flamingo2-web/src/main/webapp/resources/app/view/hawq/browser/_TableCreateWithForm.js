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
 * Flamingo2.view.hawq.browser._TableCreate > Option
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._TableCreate
 */
Ext.define('Flamingo2.view.hawq.browser._TableCreateWithForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget._hawqTableCreateWithForm',

    controller: '_hawqTableCreateController',
    viewModel: {
        type: '_hawqTableCreateModel'
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [
        {
            xtype: 'combobox',
            fieldLabel: message.msg('hawq.label.table.appendonly'),
            labelAlign: 'right',
            name: 'appendOnly',
            editable: false,
            bind: {
                store: '{hawqAppendOnly}'
            },
            displayField: 'displ',
            valueField: 'value'
        },
        {
            xtype: 'numberfield',
            fieldLabel: message.msg('hawq.label.table.blocksize'),
            labelAlign: 'right',
            name: 'blockSize',
            minValue: 8192,
            step: 8192,
            maxValue: 2097152,
            listeners: {
                blur: 'blockSizeNumberfieldBlur'
            }
        },
        {
            xtype: 'combobox',
            fieldLabel: message.msg('hawq.label.table.orientation'),
            labelAlign: 'right',
            name: 'orientation',
            bind: {
                store: '{hawqOrientation}'
            },
            displayField: 'displ',
            valueField: 'value'
        },
        {
            xtype: 'combobox',
            fieldLabel: message.msg('hawq.label.table.compresstype'),
            labelAlign: 'right',
            name: 'compressType',
            bind: {
                store: '{hawqCompressType}'
            },
            displayField: 'displ',
            valueField: 'value'
        },
        {
            xtype: 'numberfield',
            fieldLabel: message.msg('hawq.label.table.compresslevel'),
            labelAlign: 'right',
            name: 'compressLevel',
            minValue: 0,
            maxValue: 9
        }
    ],

    listeners: {
        afterrender: 'hawqTableCreateWithFormAfterrender'
    }
});