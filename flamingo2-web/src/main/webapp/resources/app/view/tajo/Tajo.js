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
Ext.define('Flamingo2.view.tajo.Tajo', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.tajoViewport',

    requires: [
        'Flamingo2.view.tajo.TajoController',
        'Flamingo2.view.tajo.TajoModel'
    ],

    controller: 'tajoController',

    viewModel: {
        type: 'tajoModel'
    },

    layout: 'border',
    border: false,
    flex: 1,
    items: [
        {
            region: 'west',
            xtype: 'tajoMetastoreViewport',
            reference: 'metastore',
            border: false,
            collapsible: true,
            split: true,
            title: message.msg('tajo.metastore'),
            width: 300
        },
        {
            region: 'center',
            xtype: 'panel',
            layout: 'fit',
            tbar: [
                {
                    text: message.msg('common.new'),
                    iconCls: 'common-new',
                    handler: 'onNewClick'
                },
                '-',
                {
                    text: message.msg('hive.execute'),
                    reference: 'btnExecute',
                    itemId: 'executeButton',
                    handler: 'onExecuteClick',
                    iconCls: 'common-execute'
                },
                {
                    text: message.msg('hive.cancel'),
                    reference: 'btnCancel',
                    handler: 'onCancelClick',
                    iconCls: 'common-stop',
                    disabled: true
                },
                '-',
                {
                    text: message.msg('common.undo'),
                    reference: 'btnUndo',
                    itemId: 'undoButton',
                    iconCls: 'common-undo',
                    handler: 'onUndoClick'
                },
                {
                    text: message.msg('common.redo'),
                    reference: 'btnRedo',
                    itemId: 'redoButton',
                    iconCls: 'common-redo',
                    handler: 'onRedoClick'
                },
                '->',
                {
                    xtype: 'numberfield',
                    fieldLabel: message.msg('hawq.button.maxrows'),
                    itemId: 'maxRows',
                    labelAlign: 'right',
                    labelWidth: 60,
                    reference: 'maxRows',
                    minValue: -1,
                    value: 5000,
                    maxValue: 2147483647,
                    width: 180,
                    inputAttrTpl: message.msg('hawq.button.maxrows.tooltip')
                },
                {
                    text: message.msg('fs.hdfs.common.browser'),
                    iconCls: 'designer-filesystem',
                    listeners: {
                        click: 'onHdfsBrowserClick'
                    }
                }
            ],
            items: [
                {
                    xtype: 'tabpanel',
                    reference: 'editorTab',
                    listeners: {
                        tabchange: 'onEditorTabChange'
                    }
                }
            ]
        }
    ],
    listeners: {
        afterrender: 'onAfterrender'
    }
});
