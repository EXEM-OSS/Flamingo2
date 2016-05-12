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
Ext.define('Flamingo2.view.designer.property._NodeValueGrid', {
    extend: 'Flamingo2.view.component.LiveSearchGridPanel',
    alias: 'widget._nodeValueGrid',

    stripeRows: true,

    margins: '0 0 0 0',

    selectableShapes: [],

    search: 'server',

    store: null,

    columns: [],

    plugins: {
        ptype: 'cellediting',
        clicksToEdit: 1
    },

    initComponent: function () {
        var me = this;

        if (me.search == 'variable') {
            me.store = Ext.create('Ext.data.Store', {
                fields: [
                    {name: 'name'},
                    {name: 'expression'}
                ],
                data: []
            });

            me.columns = [
                {text: message.msg('common.node'), flex: 1, sortable: true, dataIndex: 'name'},
                {
                    text: message.msg('common.value'),
                    flex: 1,
                    dataIndex: 'expression',
                    renderer: function (val, meta, rec) {
                        return me.getExpressionValue(rec);
                    }
                },
                {
                    text: message.msg('common.use'),
                    width: 80,
                    align: 'center',
                    renderer: function (val, meta, rec) {
                        var id = Ext.id();
                        Ext.defer(function () {
                            Ext.create('Ext.button.Button', {
                                itemId: rec.data.recid + 'button',
                                renderTo: id,
                                text: message.msg('common.use'),
                                iconCls: 'common-view',
                                usage: 'vmproperty-apply',
                                scale: 'small',
                                handler: function () {
                                    me.applyValueInEditor(rec);
                                }
                            });
                        }, 50);
                        return Ext.String.format('<div id="{0}"></div>', id);
                    }
                }
            ];
        }
        this.callParent(arguments);
    },

    listeners: {
        afterrender: function () {
            this.reload();
        }
    },

    reload: function () {
        var me = this;
        var mydata = [];
        if (me.search == 'variable') {
            var variableGrid = Ext.ComponentQuery.query('variableGrid')[0];
            var items = variableGrid.getStore().data.items;
            for (var i = 0; i < items.length; i++) {
                mydata.push({
                    name: items[i].data.name,
                    recid: Ext.id(),
                    expression: ''
                });
            }
            me.getStore().loadData(mydata);
        }
    },

    getExpressionValue: function (rec) {
        var me = this;
        var expressionValue;
        if (me.search == 'variable') {
            var nodefrom;
            nodefrom = 'WORKFLOW';
            var value = rec.data.name;
            expressionValue = '#{' + nodefrom + '::' + value + '}';
        }
        rec.data.expression = expressionValue;
        return expressionValue;
    },

    applyValueInEditor: function (rec) {
        var me = this;
        if (me.editor) {
            var expressionValue = rec.data.expression;
            query(me.editor).insertValue(expressionValue);
        }
    }
});