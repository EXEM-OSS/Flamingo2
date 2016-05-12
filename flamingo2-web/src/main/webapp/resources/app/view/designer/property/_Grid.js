/*
 * Copyright (C) 2015 Flamingo2 Project (http://www.cloudine.io).
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
Ext.define('Flamingo2.view.designer.property._Grid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget._grid',

    /**
     * 최소 레코드 갯수 : 최소 레코드 갯수를 설정하여 isFormValid 에서 유효성을 체크한다.
     */
    minRecordLength: 0,

    /**
     * 읽기 전용 여부 : true 이면 +, -, x 툴바를 제거하여 grid 를 변경 불가 하도록 한다.
     */
    readOnly: false,

    /**
     * 디폴트값 적용 여부 : true 이면 setParameters(parameters) 시 parameters 가 없는 경우 store 에 지정된 default 값을 적용한다.
     */
    hasDefaults: false,

    plugins: [],

    initComponent: function () {
        if (!this.readOnly) {
            this.plugins = [
                Ext.create('Ext.grid.plugin.RowEditing', {
                    clicksToEdit: 2,
                    pluginId: 'rowEditorPlugin',
                    listeners: {
                        canceledit: function (editor, e, eOpts) {
                            // Cancel Edit 시 유효하지 않으면 추가된 레코드를 삭제한다.
                            if (e.store.getAt(e.rowIdx) != undefined && !e.store.getAt(e.rowIdx).isValid()) {
                                e.store.removeAt(e.rowIdx);
                            }
                        },
                        edit: function (editor, e, eOpts) {
                            if (!e.store.getAt(e.rowIdx).isValid()) {
                                editor.startEdit(e.rowIdx, 0);
                            }
                        }
                    }
                })
            ];

            if (this.fileBrowser) {
                var plus = {
                    type: 'plus',
                    tooltip: message.msg('workflow.common.add'),
                    handler: function (event, toolEl, panel) {
                        var popWindow = Ext.create('Ext.Window', {
                            title: message.msg('workflow.common.hdfsbrowser'),
                            width: 800,
                            height: 400,
                            modal: true,
                            resizable: true,
                            constrain: true,
                            layout: 'fit',
                            items: [
                                {
                                    xtype: 'hdfsBrowserPanelForDesigner'
                                }
                            ],
                            buttonAlign: 'center',
                            buttons: [
                                {
                                    text: message.msg('workflow.common.confirm'),
                                    handler: function () {
                                        var selection = Ext.ComponentQuery.query('hdfsFilePanelForDesigner')[0].getSelectionModel().getSelection();
                                        if (selection.length > 0) {
                                            var path = selection[0].get('path') + '/' + selection[0].get('filename');
                                            var grid = panel.up('_grid'),
                                                store = grid.getStore(),
                                                rowEditor = grid.getPlugin('rowEditorPlugin');
                                            rowEditor.cancelEdit();
                                            store.insert(store.getCount(), eval("[{" + store.getModel().getFields()[0].getName() + ": '" + path + "'}]"));
                                            rowEditor.startEdit(store.getCount() - 1, 0);
                                            popWindow.close();
                                        }
                                    }
                                },
                                {
                                    text: message.msg('workflow.common.cancel'),
                                    handler: function () {
                                        popWindow.close();
                                    }
                                }
                            ]
                        }).center().show();
                    }
                };
            } else {
                plus = {
                    type: 'plus',
                    tooltip: message.msg('workflow.common.add'),
                    handler: function (event, toolEl, panel) {
                        // 그리드의 row 를 추가한다.
                        var grid = panel.up('_grid'),
                            store = grid.getStore(),
                            rowEditor = grid.getPlugin('rowEditorPlugin');
                        rowEditor.cancelEdit();
                        store.insert(store.getCount(), {});
                        rowEditor.startEdit(store.getCount() - 1, 0);
                    }
                };
            }

            this.tools = [
                plus,
                {
                    type: 'minus',
                    tooltip: message.msg('workflow.common.remove'),
                    handler: function (event, toolEl, panel) {
                        // 그리드의 row 를 삭제한다.
                        var grid = panel.up('_grid'),
                            store = grid.getStore(),
                            selectionModel = grid.getSelectionModel();
                        store.remove(selectionModel.getSelection());
                    }
                },
                {
                    type: 'close',
                    tooltip: message.msg('workflow.common.remove.all'),
                    handler: function (event, toolEl, panel) {
                        // 그리드의 모든 row 를 삭제한다.
                        var grid = panel.up('_grid'),
                            store = grid.getStore();
                        store.removeAll();
                    }
                }
            ];
        }

        this.callParent(arguments);
    },

    /**
     * 그리드 레코드를 파리미터형태로 변환하여 반환한다.
     * <pre>
     *     example)
     *     {
	 *             field1 : "value1,value2",
	 *             field2 : "value1,value2"
	 *     }
     * </pre>
     *
     * @return {Object} Property(name:value) Object
     */
    getParameters: function () {
        var me = this;
        var hashMap = new Ext.util.HashMap(), parameters = {};
        this.store.each(function (record, idx) {
            var data = record.data;
            for (var key in data) {
                var xtype = me.getColumnXtype(me, key);
                if (xtype == 'checkcolumn') {
                    var dataKey = false;
                    if (typeof data[key] == 'boolean' && data[key]) {
                        dataKey = true;
                    }
                    if (hashMap.containsKey(key)) {
                        hashMap.get(key).push(dataKey);
                    } else {
                        hashMap.add(key, [dataKey]);
                    }
                } else {
                    if (hashMap.containsKey(key)) {
                        hashMap.get(key).push(escape(data[key]));
                    } else {
                        hashMap.add(key, [escape(data[key]) || '']);
                    }
                }
            }
        });

        hashMap.each(function (key, value, length) {
            parameters[key] = value.toString();
        });
        return parameters;
    },

    /**
     * 주어진 파라미터정보로 그리드의 레코드를 설정한다.
     * <pre>
     *     parameters example)
     *     {
	 *             field1 : "value1,value2",
	 *             field2 : "value1,value2"
	 *     }
     * </pre>
     *
     * @param {Object} parameters Property(name:value) Object
     */
    setParameters: function (parameters) {
        var me = this;
        var hashMap = new Ext.util.HashMap(), maxLength = 0, dataArray = [], data, values;

        Ext.each(this.store.model.getFields(), function (field, idx) {
            if (Ext.isDefined(parameters[field.name]) && !Ext.isEmpty(parameters[field.name]) && field.name != 'id') {
                hashMap.add(field.name, parameters[field.name].split(','));
                if (maxLength < parameters[field.name].split(',').length) {
                    maxLength = parameters[field.name].split(',').length;
                }
            }
        });

        for (var i = 0; i < maxLength; i++) {
            data = {};
            Ext.each(hashMap.getKeys(), function (key, idx) {
                values = hashMap.get(key);
                var xtype = me.getColumnXtype(me, key);
                if (xtype == 'checkcolumn') {
                    if (values[i] == 'true')
                        data[key] = true;
                    else if (values[i] == 'false')
                        data[key] = false;
                    else
                        data[key] = false;
                }
                else
                    data[key] = unescape(values[i]) || '';
            });
            dataArray.push(data);
        }

        if (dataArray.length > 0) {
            this.store.loadData(dataArray);
        } else if (!this.hasDefaults) {
            this.store.removeAll();
        }
    },

    /**
     * 그리드 레코드의 form 유효성을 체크한다.
     *
     * @return {Boolean}
     */
    isFormValid: function () {
        var isValid = true;
        if (this.readOnly === true) {
            return true;
        }
        if (this.store.getCount() >= this.minRecordLength) {
            this.store.each(function (record, idx) {
                if (!record.isValid()) {
                    var rowEditor = this.getPlugin('rowEditorPlugin');
                    if (rowEditor) {
                        rowEditor.cancelEdit();
                        rowEditor.startEdit(idx, 0);
                    }
                    isValid = false;

                    return false;
                }
            }, this);
        } else {
            // 그리드의 row 를 추가한다.
            var rowEditor = this.getPlugin('rowEditorPlugin');
            if (rowEditor) {
                rowEditor.cancelEdit();
                this.store.insert(this.store.getCount(), {});
                rowEditor.startEdit(this.store.getCount() - 1, 0);
            }

            isValid = false;
        }

        return isValid;
    },
    getColumnXtype: function (grid, key) {
        var gridColumns = grid.headerCt.getGridColumns();
        for (var i = 0; i < gridColumns.length; i++) {
            if (gridColumns[i].dataIndex == key) {
                return gridColumns[i].xtype;
            }
        }
        return null;
    }
});