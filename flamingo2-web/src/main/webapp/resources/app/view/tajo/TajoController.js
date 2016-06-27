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
Ext.define('Flamingo2.view.tajo.TajoController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.tajoController',

    listen: {
        controller: {
            websocketController: {
                tajoMessage: 'onTajoMessage'
            },
            tajoEditorController: {
                setButton: 'onSetButton'
            }
        }
    },

    onAfterrender: function (view) {
        var me = this;

        me.onNewClick();
    },

    /**
     * 신규탭을 생성한다.
     * */
    onNewClick: function () {
        var me = this;
        var refs = me.getReferences();
        var uuid = UUIDGenerate().replace(/-/g, '');
        var panelCount = me.getViewModel().getData().panelCount;

        var editor = Ext.create('Flamingo2.view.tajo.editor.Editor', {
            title: message.msg('common.query') + ' ' + panelCount,
            uuid: uuid,
            closable: true,
            isRunning: false //쿼리 실행상태 체크값
        });

        refs.editorTab.add(editor);
        refs.editorTab.setActiveTab(editor);
        me.getViewModel().setData({panelCount: ++panelCount});
    },

    /**
     * 에디터 탭 Change Event
     * 탭이 이동할때 현재 탭의 상태를 확인하여 버튼 처리
     * */
    onEditorTabChange: function (panel, newCard, oldCard) {
        var refs = this.getReferences();
        var tab = refs.editorTab.getActiveTab();
        var tabRefs = tab.getReferences();

        if (newCard.isRunning) {
            refs.btnExecute.setDisabled(true);
            refs.btnUndo.setDisabled(true);
            refs.btnRedo.setDisabled(true);
            refs.btnCancel.setDisabled(false);
        }
        else {
            refs.btnExecute.setDisabled(false);
            refs.btnUndo.setDisabled(false);
            refs.btnRedo.setDisabled(false);
            refs.btnCancel.setDisabled(true);
        }

        if (tabRefs.queryEditor.editor) {
            tabRefs.queryEditor.editor.resize();
        }
    },

    /**
     * Undo 클릭 이벤트
     * */
    onUndoClick: function () {
        var me = this;
        var refs = me.getReferences();
        var tab = refs.editorTab.getActiveTab();
        var tabRefs = tab.getReferences();

        tabRefs.queryEditor.editor.undo();
    },

    /**
     * Redo 클릭 이벤트
     * */
    onRedoClick: function () {
        var me = this;
        var refs = me.getReferences();
        var tab = refs.editorTab.getActiveTab();
        var tabRefs = tab.getReferences();

        tabRefs.queryEditor.editor.redo();
    },

    /**
     * Excute 클릭 이벤트
     * */
    onExecuteClick: function () {
        var me = this;
        var refs = me.getReferences();
        var tab = refs.editorTab.getActiveTab();
        var metaRefs = refs.metastore.getReferences();
        var database = metaRefs.cbxDatabase.getValue();

        if (Ext.isEmpty(database)) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('tajo.msg.select_database'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        refs.btnExecute.setDisabled(true);
        refs.btnUndo.setDisabled(true);
        refs.btnRedo.setDisabled(true);
        refs.btnCancel.setDisabled(false);

        tab.fireEvent('queryExecute', database);
    },

    /**
     * 서버에서 수신받은 WebSoket 메시지 처리
     * Tab에서 UUID에 맞는 에디터에 로그메시지를 표시한다.
     * */
    onTajoMessage: function (msg) {
        var refs = this.getReferences();
        var body = Ext.decode(msg.body);
        var uuid = body.uuid;
        var tab, tabs = refs.editorTab.items.items;

        for (var i = 0; i < tabs.length; i++) {
            tab = tabs[i];
            if (tab.uuid == uuid)
                break;
        }
        var tabRefs = tab.getReferences();
        var title = tab.title;

        var progressText = body.progress * 100;
        tabRefs.progress.updateProgress(body.progress, progressText.toFixed(0) + '%');

        if (body.progress == 1) {
            tabRefs.logViewer.remove(tabRefs.progress);
            tabRefs.logViewer.insertLast('Finish :' + new Date(body.finish));
            tabRefs.logViewer.insertLast('Time :' + body.runningTime / 1000 );
        }

        if (body.isEnd) {
            tabRefs.queryEditor.setLoading(false);

            //현재 선택되어 있는 탭의 쿼리가 종료되면 버튼 활성화
            if (uuid == refs.editorTab.getActiveTab().uuid) {
                tab.isRunning = false;
                refs.btnExecute.setDisabled(false);
                refs.btnUndo.setDisabled(false);
                refs.btnRedo.setDisabled(false);
                refs.btnCancel.setDisabled(true);
            }

            tab.setTitle(title.replace('(' + message.msg('hive.running') + '..)', ''));

            switch (body.status) {
                case 'QUERY_SUCCEEDED':
                    tab.isRunning = false;
                    tabRefs.queryEditor.setLoading(false);

                    Ext.defer(function () {
                        tabRefs.tabpanel.setActiveTab(1);

                        Ext.defer(function () {
                            tabRefs.resultGrid.getView().refresh();

                            var params= {
                                uuid: body.uuid,
                                clusterName: ENGINE.id,
                                maxRows: refs.maxRows.value
                            };

                            invokeGet(CONSTANTS.TAJO.RESULTS, params,
                                function (response) {
                                    var obj = Ext.decode(response.responseText);

                                    if (obj.success) {

                                        var store = Ext.create('Ext.data.Store', {
                                            fields: obj.map.columns,
                                            data: obj.map.result
                                        });

                                        var columns = [];

                                        for (var i = 0; i < obj.map.columns.length; i++) {
                                            var column = {
                                                text: obj.map.columns[i],
                                                dataIndex: obj.map.columns[i]
                                            };
                                            columns.push(column);
                                        }
                                        tabRefs.resultGrid.reconfigure(store, columns);

                                    } else {
                                        Ext.MessageBox.show({
                                            title: message.msg('common.fail'),
                                            message: message.msg('batch.msg.workflow_get_fail'),
                                            buttons: Ext.MessageBox.OK,
                                            icon: Ext.MessageBox.WARNING
                                        });
                                    }
                                },
                                function (response) {
                                    Ext.MessageBox.show({
                                        title: message.msg('common.warning'),
                                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });

                                }
                            );
                        }, 300);

                    }, 300);
                    break;
                case 'QUERY_KILLED':
                    tabRefs.logViewer.remove(tabRefs.progress);
                    error(message.msg('tajo.error'), message.msg('tajo.msg.query_killed'));
                    tabRefs.logViewer.insertLast('Finish :' + new Date(body.finish));
                    tabRefs.logViewer.insertLast(message.msg('common.query_kill'));
                    tabRefs.queryEditor.setLoading(false);
                    tab.isRunning = false;
                    refs.btnExecute.setDisabled(false);
                    refs.btnUndo.setDisabled(false);
                    refs.btnRedo.setDisabled(false);
                    refs.btnCancel.setDisabled(true);
                    break;
                default:
                    tabRefs.logViewer.remove(tabRefs.progress);
                    error(message.msg('tajo.error'), message.msg('hive.msg.abnormal_terminate'));
                    //FIXME: Hive쿼리가 Kill, Cancel 등 비정상적으로 종료 되었을때는 Log값이 끝까지 가져오질 못함.
                    tabRefs.queryEditor.setLoading(false);
                    tab.isRunning = false;
                    refs.btnExecute.setDisabled(false);
                    refs.btnUndo.setDisabled(false);
                    refs.btnRedo.setDisabled(false);
                    refs.btnCancel.setDisabled(true);
                    break;
            }
        }

        if (body.isError) {
            if (tab.status == 'CANCEL') {
                error(message.msg('tajo.error'), message.msg('tajo.msg.query_cancel'));
            } else {
                error(message.msg('tajo.error'), message.msg('hive.msg.abnormal_terminate'));
            }
            tab.setTitle(title.replace('(' + message.msg('hive.running') + '..)', ''));

            tabRefs.queryEditor.setLoading(false);
            tab.isRunning = false;
            refs.btnExecute.setDisabled(false);
            refs.btnUndo.setDisabled(false);
            refs.btnRedo.setDisabled(false);
            refs.btnCancel.setDisabled(true);
        }
    },

    /**
     * 쿼리 강제 종료
     * */
    onCancelClick: function () {
        var me = this;
        var refs = me.getReferences();
        var tab = refs.editorTab.getActiveTab();
        var tabRefs = tab.getReferences();

        Ext.MessageBox.show({
            title: message.msg('hive.query_cancel'),
            message: message.msg('hive.msg.cancel_query'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn === 'yes') {
                    tab.status = 'CANCEL';
                    invokePostByMap(CONSTANTS.TAJO.CANCEL, {
                            clusterName: ENGINE.id,
                            uuid: tab.uuid
                        },
                        function (response) {
                            var result = Ext.decode(response.responseText);
                            if (!result.success) {
                                // 실행시 로그 창을 먼저 표시한다.
                                tabRefs.tabpanel.setActiveTab(0);
                            } else {
                                // 실행시 로그 창을 먼저 표시한다.
                                tabRefs.tabpanel.setActiveTab(0);
                            }
                        },
                        function (response) {
                            tabRefs.queryEditor.setLoading(false);
                            Ext.MessageBox.show({
                                title: message.msg('common.warn'),
                                message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
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
     * 버튼 Disabled 설정 함수
     * */
    onSetButton: function () {
        var refs = this.getReferences();
        var tab = refs.editorTab.getActiveTab();

        if (tab.isRunning) {
            refs.btnExecute.setDisabled(true);
            refs.btnUndo.setDisabled(true);
            refs.btnRedo.setDisabled(true);
            refs.btnCancel.setDisabled(false);
        }
        else {
            refs.btnExecute.setDisabled(false);
            refs.btnUndo.setDisabled(false);
            refs.btnRedo.setDisabled(false);
            refs.btnCancel.setDisabled(true);
        }
    },

    onHdfsBrowserClick: function() {
        Ext.create('Flamingo2.view.fs.hdfs.simple.SimpleHdfsFileBrowser').center().show();
    }

});