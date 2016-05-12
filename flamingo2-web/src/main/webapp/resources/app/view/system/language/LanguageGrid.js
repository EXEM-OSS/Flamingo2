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
Ext.define('Flamingo2.view.system.language.LanguageGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.languageGridPanel',

    requires: [
        'Ext.grid.plugin.RowEditing'
    ],

    title: message.msg('system.language.list.title'),
    reference: 'languageGridPanel',
    border: true,
    bind: {
        store: '{languageStore}'
    },
    columns: [
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text: message.msg('system.language.common.key'),
            dataIndex: 'key',
            align: 'left',
            flex: 1,
            editor: {
                allowBlank: false,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        },
        {
            text: message.msg('system.language.common.ko_KR'),
            dataIndex: 'ko_KR',
            align: 'left',
            flex: 1,
            editor: {
                allowBlank: true,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        },
        {
            text: message.msg('system.language.common.en_US'),
            dataIndex: 'en_US',
            align: 'left',
            flex: 1,
            editor: {
                allowBlank: true,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        },
        {
            text: message.msg('system.language.common.ja_JP'),
            dataIndex: 'ja_JP',
            align: 'left',
            flex: 1,
            editor: {
                allowBlank: true,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        },
        {
            text: message.msg('system.language.common.zh_CN'),
            dataIndex: 'zh_CN',
            align: 'center',
            flex: 1,
            editor: {
                allowBlank: true,
                listeners: {
                    errorchange: function (comp, error, eopts) {
                        comp.focus(false, 50);
                    }
                }
            }
        }
    ],
    plugins: [
        Ext.create('Ext.grid.plugin.RowEditing', {
            clicksToEdit: 2,
            pluginId: 'rowEditorPlugin',
            listeners: {
                canceledit: function (editor, e, eOpts) {
                    // Cancel Edit 시 유효하지 않으면 추가된 레코드를 삭제한다.
                    if (e.store.getAt(e.rowIdx)) {
                        if (!e.store.getAt(e.rowIdx).isValid()) {
                            e.store.removeAt(e.rowIdx);
                        }
                    }
                },
                edit: function (editor, e, eOpts) {
                    if (!e.store.getAt(e.rowIdx).isValid()) {
                        editor.startEdit(e.rowIdx, 0);
                    }
                }
            }
        })
    ],
    viewConfig: {
        enableTextSelection: true,
        stripeRows: true,
        columnLines: true,
        getRowClass: function (record, index) {
            var complated = true;
            if (Ext.isEmpty(record.data.ko_KR))complated = false;
            if (Ext.isEmpty(record.data.en_US))complated = false;
            if (Ext.isEmpty(record.data.ja_JP))complated = false;
            if (Ext.isEmpty(record.data.zh_CN))complated = false;
            if (!complated)
                return 'cell-height-30 cell-highlight';
            else
                return 'cell-height-30';
        }
    },
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    xtype: 'tbtext',
                    text: message.msg('system.user.common.searchCondition')
                },
                {
                    xtype: 'combo',
                    name: 'condition_key',
                    reference: 'conditionKey',
                    itemId: 'conditionKey',
                    editable: true,
                    queryMode: 'local',
                    typeAhead: true,
                    selectOnFocus: true,
                    displayField: 'name',
                    valueField: 'value',
                    width: 80,
                    value: 'ALL',
                    store: Ext.create('Ext.data.Store', {
                        fields: ['name', 'value'],
                        data: [
                            {name: message.msg('system.language.common.all'), value: 'ALL'},
                            {name: message.msg('system.language.common.key'), value: 'key'},
                            {name: message.msg('system.language.common.ko_KR'), value: 'ko_KR'},
                            {name: message.msg('system.language.common.en_US'), value: 'en_US'},
                            {name: message.msg('system.language.common.ja_JP'), value: 'ja_JP'},
                            {name: message.msg('system.language.common.zh_CN'), value: 'zh_CN'}
                        ]
                    })
                },
                {
                    xtype: 'textfield',
                    reference: 'condition',
                    itemId: 'condition',
                    emptyText: message.msg('system.language.common.search'),
                    width: 150
                },
                {
                    xtype: 'button',
                    itemId: 'findLanguagButton',
                    formBind: true,
                    text: message.msg('system.user.common.search'),
                    iconCls: 'common-search',
                    labelWidth: 50,
                    handler: 'onFindLanguagButton'
                },
                {
                    xtype: 'button',
                    itemId: 'clearLanguagButton',
                    formBind: true,
                    text: message.msg('system.user.common.clear'),
                    iconCls: 'common-search-clear',
                    labelWidth: 50,
                    handler: 'onClearLanguageButton'
                },
                {
                    xtype: 'button',
                    itemId: 'rowplusButton',
                    formBind: true,
                    text: message.msg('common.add'),
                    iconCls: 'common-add',
                    labelWidth: 50,
                    handler: 'rowplus'
                },
                {
                    xtype: 'button',
                    itemId: 'rowminusButton',
                    formBind: true,
                    text: message.msg('common.remove'),
                    iconCls: 'common-delete',
                    labelWidth: 50,
                    handler: 'rowminus'
                },
                {
                    xtype: 'button',
                    itemId: 'saveButton',
                    formBind: true,
                    text: message.msg('common.save'),
                    iconCls: 'common-save',
                    labelWidth: 50,
                    handler: 'onSaveClick'
                },
                '->',
                {
                    xtype: 'splitbutton',
                    text: message.msg('system.language.export'),
                    margin: '0 0 0 5',
                    menu: {
                        items: [
                            {
                                text: message.msg('system.language.export.xlsx'),
                                iconCls: '',
                                handler: 'onExportXlsxClick'
                            },
                            {
                                text: message.msg('system.language.export.properties'),
                                iconCls: '',
                                handler: 'onExportTextClick'
                            }
                        ]
                    }
                },
                {
                    xtype: 'splitbutton',
                    text: message.msg('system.language.import'),
                    margin: '0 0 0 5',
                    menu: {
                        items: [
                            {
                                text: message.msg('system.language.import.xlsx'),
                                iconCls: '',
                                handler: 'onImportXlsxClick'
                            },
                            {
                                text: message.msg('system.language.import.zip'),
                                iconCls: '',
                                handler: 'onImportTextClick'
                            }
                        ]
                    }
                }
            ]
        }
    ],
    listeners: {
        itemcontextmenu: function (grid, record, item, index, event) {
            event.stopEvent();
        },
        containercontextmenu: function (grid, event) {
            event.stopEvent();
        }
    }
});