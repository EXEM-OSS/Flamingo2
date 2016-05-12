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
Ext.define('Flamingo2.view.designer.property.bpmn.SUBPROCESS', {
    extend: 'Flamingo2.view.designer.property._NODE',
    alias: 'widget.SUBPROCESS',

    requires: ['Flamingo2.view.designer.property._KeyValueProtectGrid'],

    width: 600,
    height: 350,

    items: [
        {
            title: message.msg('workflow.etc.subflow.workflow.title'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.etc.subflow.workflow.label'),
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'hidden',
                            itemId: 'treeId',
                            name: 'treeId',
                            allowBlank: true
                        },
                        {
                            xtype: 'textfield',
                            flex: 1,
                            name: 'name',
                            itemId: 'name',
                            allowBlank: false,
                            value: '',
                            emptyText: message.msg('workflow.etc.subflow.workflow.nameempty')
                        },
                        {
                            xtype: 'button',
                            itemId: 'selectBtn',
                            margin: '0 0 0 5',
                            text: message.msg('workflow.etc.subflow.workflow.selectbtn'),
                            width: 120,
                            handler: function () {
                                var popWindow = Ext.create('Ext.Window', {
                                    title: message.msg('common.workflow'),
                                    width: 400,
                                    height: 400,
                                    modal: true,
                                    resizable: true,
                                    constrain: true,
                                    layout: 'fit',
                                    items: {
                                        xtype: 'folderTree'
                                    },
                                    buttonAlign: 'center',
                                    buttons: [
                                        {
                                            text: message.msg('common.confirm'),
                                            handler: function () {
                                                var tree = popWindow.query('folderTree #folderTreeTreePanel')[0];
                                                var node = tree.getSelectionModel().getSelection()[0].data;
                                                if (node.cls == 'folder') {
                                                    // 에러 표시
                                                } else {
                                                    // AJAX 호출후 값 설정
                                                    invokeGet(CONSTANTS.DESIGNER.GET, {
                                                            clusterName: ENGINE.id,
                                                            treeId: node.id
                                                        },
                                                        function (response) {
                                                            var wf = Ext.decode(response.responseText);
                                                            query('SUBPROCESS #treeId').setValue(node.id);
                                                            query('SUBPROCESS #name').setValue(wf.map.workflowName);
                                                            var store = query('SUBPROCESS #keyValueProtectGrid').getStore();
                                                            var vars = Ext.decode(unescape(wf.map.variable));
                                                            var global = vars.global;
                                                            store.removeAll();
                                                            for (var k in global) {
                                                                store.add({
                                                                    keys: k,
                                                                    values: global[k],
                                                                    protected: false
                                                                });
                                                            }
                                                            popWindow.close();
                                                        },
                                                        function (response) {

                                                        }
                                                    );
                                                }
                                            }
                                        },
                                        {
                                            text: message.msg('common.cancel'),
                                            handler: function () {
                                                popWindow.close();
                                            }
                                        }
                                    ]
                                }).show();
                            }
                        }
                    ]
                },
                {
                    itemId: 'keyValueProtectGrid',
                    xtype: '_keyValueProtectGrid',
                    title: message.msg('workflow.common.workflow.variable'),
                    margin: '0 0 0 5',
                    border: 1,
                    flex: 1
                }
            ]
        }
    ]
});