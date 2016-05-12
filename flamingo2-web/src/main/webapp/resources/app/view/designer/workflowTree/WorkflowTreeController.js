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
Ext.define('Flamingo2.view.designer.workflowTree.WorkflowTreeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.workflowTreeController',

    onWorkflowTreeExpand: function () {
        var panel = this.lookupReference('treepanel');
        panel.expandAll();
    },

    onWorkflowTreeCollapse: function () {
        var panel = this.lookupReference('treepanel');
        panel.collapseAll();
    },

    onWorkflowTreeRefresh: function () {
        var panel = this.lookupReference('treepanel');
        var refreshButton = this.lookupReference('refreshButton');
        refreshButton.setDisabled(true);
        panel.getStore().load({
            callback: function () {
                refreshButton.setDisabled(false);
            }
        });
    },

    onWorkflowTreeLoad: function (node, records) {
        var refreshButton = this.lookupReference('refreshButton');
        if (records.length > 0) {
            refreshButton.setDisabled(false);
        }
    },

    onWorkflowTreeRender: function () {
        // 브라우저 자체 Right Button을 막고자 한다면 uncomment한다.
        Ext.getBody().on("contextmenu", Ext.emptyFn, null, {preventDefault: true});

        // If the root node has any child nodes, enable the refresh button.
        var panel = this.lookupReference('treepanel');
        if (panel.getRootNode().childNodes.length > 0) {
            var refreshButton = this.lookupReference('refreshButton');
            refreshButton.setDisabled(false);
        }
    },

    onWorkflowTreeItemappend: function () {
        var refreshButton = this.lookupReference('refreshButton');
        refreshButton.setDisabled(false);
    },

    /**
     * 워크플로우 Tree 목록에 저장된 워크플로우 정보를 불러온다.
     * @param view
     * @param record
     * @param item
     * @param index
     * @param e
     */
    onWorkflowTreeItemdblclick: function (view, record, item, index, e) {
        var me = this;

        if (record.data.iconCls == 'designer_not_load') {
            Ext.MessageBox.show({
                title: message.msg('designer.title.wf.loading'),
                message: message.msg('designer.msg.wf.loading.warn'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return;
        }

        if (record.data.cls != 'folder' && record.data.id != '/') {
            var canvas = query('canvas');
            Ext.MessageBox.show({
                title: message.msg('designer.title.wf.loading'),
                message: Ext.String.format(message.msg('designer.msg.wf.loading.yesno'), record.data.text),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.INFO,
                fn: function handler(btn) {
                    if (btn == 'yes') {
                        var treePanel = me.lookupReference('treepanel');
                        var node = treePanel.getSelectionModel().getSelection()[0].raw;

                        // 폴더인 경우에는 경로 메시지를 띄우고 노드의 경우에는 정상 처리한다.
                        if (node.leaf) {
                            var mask = new Ext.LoadMask(query('canvas'), {
                                msg: message.msg('fs.hdfs.msg.please.wait')
                            });
                            mask.show();

                            invokeGetWithHeader(CONSTANTS.DESIGNER.LOAD,
                                {
                                    'Accept': 'text/plain'
                                },
                                {
                                    clusterName: ENGINE.id,
                                    treeId: node.id
                                },
                                function (response) {
                                    var res = Ext.decode(response.responseText);

                                    mask.hide();

                                    var canvas = Ext.ComponentQuery.query('canvas')[0];
                                    var variableGrid = Ext.ComponentQuery.query('variableGrid')[0];
                                    var graphXML, graphJSON, workflowData;
                                    var form = canvas.getForm();

                                    // graph xml example
                                    graphXML = res.object;

                                    // XML 스트링을 JSON Object 로 변환하여 정보 획득
                                    graphJSON = OG.Util.xmlToJson(OG.Util.parseXML(graphXML));

                                    workflowData = OG.JSON.decode(unescape(graphJSON.opengraph['@data']));

                                    // 워크플로우 정보 로딩(클러스터, 워크플로우명, 설명, 워크플로우 식별자, 트리 식별자)
                                    form.reset();
                                    form.setValues(workflowData.workflow);

                                    form.findField('name').setValue(node.text);

                                    // 워크플로우 변수 정보 로딩
                                    variableGrid.getStore().loadData(workflowData.globalVariables);

                                    // 워크플로우 그래프 Shape 로딩
                                    canvas.graph.loadJSON(graphJSON);
                                    canvas.setwireEventAll();

                                    query('canvas #wd_btn_run').setDisabled(false);
                                    query('canvas #wd_btn_copy').setDisabled(false);
                                    query('canvas #wd_btn_xml').setDisabled(false);
                                },
                                function (response) {
                                    mask.hide();

                                    Ext.MessageBox.show({
                                        title: message.msg('designer.title.wf.loading'),
                                        message: message.msg('designer.msg.wf.loading.cause') + response.responseText,
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });
                                }
                            );
                        } else {
                            mask.hide();

                            Ext.MessageBox.show({
                                title: message.msg('designer.title.wf.loading'),
                                message: message.msg('designer.msg.wf.loading.choice'),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    }
                }
            });
        }
    },

    /**
     * 디렉토리에서 마우스 오른쪽 버튼을 누르는 경우 Context Menu를 표시한다.
     */
    onWorkflowTreeItemcontextmenu: function (view, record, item, index, e) {
        var contextMenu = new Ext.menu.Menu({
            items: [
                {
                    text: message.msg('designer.title.create.folder'),
                    iconCls: 'common-directory-add',
                    tooltip: message.msg('designer.tooltip.create'),
                    itemId: 'createFolderMenu',
                    handler: this.onCreateFolderMenuClick
                }, '-',
                {
                    text: message.msg('designer.label.wf.and.folder.delete'),
                    iconCls: 'common-directory-remove',
                    tooltip: message.msg('designer.tooltip.delete'),
                    itemId: 'deleteFolderMenu',
                    handler: this.onDeleteWorkflowMenuClick
                }, '-',
                {
                    text: message.msg('designer.label.wf.and.folder.rename'),
                    iconCls: 'common-directory-rename',
                    itemId: 'renameMenu',
                    tooltip: message.msg('designer.tooltip.msg.rename'),
                    handler: this.onRenameFolderMenuClick
                }
            ]
        });

        if (record.data.id == '/' || record.data.id == CONSTANTS.ROOT) {
            contextMenu.query('#createFolderMenu')[0].disabled = false;
            contextMenu.query('#renameMenu')[0].disabled = true;
            contextMenu.query('#deleteFolderMenu')[0].disabled = true;
        } else {
            contextMenu.query('#createFolderMenu')[0].disabled = false;
            contextMenu.query('#renameMenu')[0].disabled = false;
            contextMenu.query('#deleteFolderMenu')[0].disabled = false;
        }
        e.stopEvent();
        contextMenu.showAt([e.pageX - 5, e.pageY - 5]);
    },

    /**
     * 워크플로우 트리에서 폴더를 생성한다.
     */
    onCreateFolderMenuClick: function (widget, event) {
        var treepanel = query('workflowTree #workflowTreePanel');
        var selected = treepanel.getSelectionModel().getLastSelected();
        var isLeaf = selected.isLeaf();

        if (isLeaf) {
            Ext.MessageBox.show({
                title: message.msg('designer.title.create.folder'),
                message: message.msg('designer.msg.select.folder'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.MessageBox.show({
            title: message.msg('designer.title.create.folder'),
            message: message.msg('designer.msg.enter.to.create.folder.name'),
            width: 300,
            prompt: true,
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.INFO,
            multiline: false,
            value: 'folder',
            fn: function (btn, text) {
                if (btn === 'yes') {
                    if (isBlank(text)) {
                        return;
                    }

                    var param = {
                        clusterName: ENGINE.id,
                        id: selected.data.id,
                        parent: selected.data.id,
                        name: text,
                        nodeType: 'folder',
                        treeType: 'WORKFLOW'
                    };

                    invokePostByMap(CONSTANTS.DESIGNER.NEW, param,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
//                                var controller = Flamingo2.app.getController('designer.DesignerController');
//                                controller._info(format(message.msg('designer.msg.created.folder'), text));
                                updateNode(query('#workflowTreePanel'));
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('designer.title.create.folder'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function (response) {
                            Ext.MessageBox.show({
                                title: message.msg('designer.title.create.folder'),
                                message: format(message.msg('designer.msg.cannot.create.folder'), response.statusText, response.status),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 선택한 워크플로우를 삭제한다.
     */
    onDeleteWorkflowMenuClick: function (widget, event) {
        var treepanel = query('workflowTree #workflowTreePanel');
        var node = treepanel.getSelectionModel().getLastSelected();

        if (!node) {
            Ext.MessageBox.show({
                title: message.msg('designer.label.wf.and.folder.delete'),
                message: message.msg('designer.label.select.folder.or.workflow.for.delete'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        if (node.get('id') == '/') {
            Ext.MessageBox.show({
                title: message.msg('designer.label.wf.and.folder.delete'),
                message: message.msg('designer.label.cant.delete.root.folder'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var selectedNode = node;
        Ext.MessageBox.show({
            title: message.msg('designer.label.wf.and.folder.delete'),
            message: Ext.String.format(message.msg('designer.msg.delete.yn'), node.get('text')),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            fn: function handler(btn) {
                if (btn == 'yes') {
                    var param = {
                        clusterName: ENGINE.id,
                        id: '' + selectedNode.data.id,
                        text: selectedNode.data.text,
                        nodeType: selectedNode.data.cls == 'file' ? 'ITEM' : 'FOLDER',
                        leaf: selectedNode.data.cls == 'file' ? 'true' : 'false',
                        treeType: 'WORKFLOW'
                    };

                    invokePostByMap(CONSTANTS.DESIGNER.DELETE, param,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                updateParentNode(treepanel);

                                /////////////////////////////////////////////////////////
                                // 현재 로딩한 화면과 삭제할 트리 노드가 동일하다면 캔버스도 초기화한다.
                                /////////////////////////////////////////////////////////

                                var canvas = query('canvas');
                                var variableGrid = Ext.ComponentQuery.query('variableGrid')[0];
                                var form = canvas.getForm();
                                var treeId = form.getValues()['tree_id'];
                                var startNode, endNode;

                                if (param.id == treeId) {
                                    // 워크플로우 기본 정보 초기화
                                    form.reset();

                                    // 워크플로우 변수 정보 로딩
                                    variableGrid.getStore().removeAll();

                                    // 워크플로우 그래프 Clear
                                    canvas.graph.clear();

                                    if (canvas.graph) {
                                        startNode = canvas.graph.drawShape([100, 100], new OG.E_Start(message.msg('common.start')), [30, 30]);
                                        endNode = canvas.graph.drawShape([700, 100], new OG.E_End(message.msg('common.end')), [30, 30]);

                                        canvas.graph.setCustomData(startNode, {
                                            metadata: {
                                                "type": "START",
                                                "identifier": "START",
                                                "name": message.msg('common.start'),
                                                "minPrevNodeCounts": "0",
                                                "maxPrevNodeCounts": "0",
                                                "minNextNodeCounts": "1",
                                                "maxNextNodeCounts": "N",
                                                "notAllowedPrevTypes": "",
                                                "notAllowedNextTypes": "END,IN,OUT",
                                                "notAllowedPrevNodes": "",
                                                "notAllowedNextNodes": "END"
                                            }
                                        });
                                        canvas.graph.setCustomData(endNode, {
                                            metadata: {
                                                "type": "END",
                                                "identifier": "END",
                                                "name": message.msg('common.end'),
                                                "minPrevNodeCounts": "1",
                                                "maxPrevNodeCounts": "N",
                                                "minNextNodeCounts": "0",
                                                "maxNextNodeCounts": "0",
                                                "notAllowedPrevTypes": "START,IN,OUT",
                                                "notAllowedNextTypes": "",
                                                "notAllowedPrevNodes": "START",
                                                "notAllowedNextNodes": ""
                                            }
                                        });
                                    }
                                }
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('designer.label.wf.and.folder.delete'),
                                    message: message.msg('designer.cannot.delete.selection') + obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function (response) {
                            Ext.MessageBox.show({
                                title: message.msg('designer.label.wf.and.folder.delete'),
                                message: message.msg('designer.cannot.delete.selection2'),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 워크플로우 트리에서 지정한 폴더 및 워크플로우의 이름을 변경한다.
     */
    onRenameFolderMenuClick: function (widget, event) {
        var treepanel = query('workflowTree #workflowTreePanel');
        var selected = treepanel.getSelectionModel().getLastSelected();

        Ext.MessageBox.show({
            title: message.msg('designer.title.change.folder.or.workflow.name'),
            message: message.msg('designer.msg.enter.folder.or.workflow.name'),
            width: 300,
            prompt: true,
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.WARNING,
            multiline: false,
            value: selected.get('text'),
            fn: function (btn, text) {
                if (btn === 'yes') {
                    if (App.Util.String.isBlank(text)) {
                        return;
                    }

                    var param = {
                        clusterName: ENGINE.id,
                        id: selected.data.id,
                        name: text,
                        leaf: selected.data.leaf,
                        workflowId: selected.data.workflowId
                    };

                    invokePostByMap(CONSTANTS.DESIGNER.RENAME, param,
                        function (response) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
//                                var controller = Flamingo2.app.getController('designer.DesignerController');
//                                controller._info(format(message.msg('designer.msg.changed.name'), text));
                                updateParentNode(treepanel);
//                                query('#workflowTreePanel').getStore().load();
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('designer.title.change.folder.or.workflow.name'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function (response) {
                            Ext.MessageBox.show({
                                title: message.msg('designer.title.change.folder.or.workflow.name'),
                                message: format(message.msg('designer.msg.cannot.change.name'), response.statusText, response.status),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    }
});