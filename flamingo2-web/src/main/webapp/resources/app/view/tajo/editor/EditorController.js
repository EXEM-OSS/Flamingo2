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
Ext.define('Flamingo2.view.tajo.editor.EditorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.tajoEditorController',

    onAfterrender: function (panel) {
        var me = this;
        var refs = this.getReferences();
    },

    onResize: function () {
        var me = this;
        var refs = this.getReferences();
        if (refs.queryEditor.editor) {
            refs.queryEditor.editor.resize();
        }
    },

    onQueryResultsMetachange: function (store, meta) {
        var me = this;
        var refs = this.getReferences();

        // if init, resize colums
        Ext.each(meta.columns, function (c) {
            c.flex = undefined;
            c.maxWidth = 10000;
            c.sortable = true;
        });

        refs.resultPanel.removeAll();
        var grid = Ext.create('Flamingo2.view.tajo.editor.ResultSearchGridPanel', {
            reference: 'resultGrid',
            store: store,
            columns: meta.columns
        });

        refs.resultPanel.add(grid);
    },

    onQueryResultsLoad: function (store, records, successful) {
        var me = this;
        var refs = me.getReferences();

        var i, fields = [];
        for (i = 0; i < refs.resultGrid.columns.length; i++) {
            fields.push(refs.resultGrid.columns[i].dataIndex)
        }

        var pageStore = Ext.create('Ext.data.Store', {
            fields: fields
        });

        for (i = 0; i < records.length; i++) {
            pageStore.insert(i, records[i]);
        }

        me.getViewModel().setData({pageStore: pageStore});
    },

    /**
     * 쿼리창 종료하기 전 쿼리 실행여부 판단
     * */
    onBeforeclose: function (panel) {
        if (panel.isRunning) {
            Ext.MessageBox.show({
                title: message.msg('hive.query_kill'),
                message: message.msg('hive.msg.query_kill'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn === 'yes') {
                        //FIXME: 하이브 쿼리 강제 종료
                        panel.doClose();
                    } else if (btn === 'no') {
                        return false;
                    }
                }
            });
        }
        else {
            panel.doClose();
        }
        return false;
    },

    /**
     * 쿼리창이 종료되었으면 부모창에서 탭의 갯수를 판단하여 탭이 없으면 새로운 창을 생성함.
     * */
    onDestroy: function () {
        this.fireEvent('editorDestroyed');
    },

    /**
     * 쿼리결과 CSV 다운로드
     * */
    btnDownloadClick: function () {
        var me = this;
        var refs = me.getReferences();


        Ext.core.DomHelper.append(document.body, {
            tag: 'iframe',
            id: 'testIframe' + new Date().getTime(),
            css: 'display:none;visibility:hidden;height:0px;',
            src: "/hive/query/downloadResult.json?clusterName=" + ENGINE.id + "&uuid=" + me.getView().uuid,
            frameBorder: 0,
            width: 0,
            height: 0
        });
    },

    /**
     * 쿼리 실행 이벤트
     * */
    onQueryExecute: function (database) {
        var me = this;
        var refs = me.getReferences();
        var tab = me.getView();

        if (!trim(refs.queryEditor.editor.getSession().getValue()) != '')
            return;

        refs.queryEditor.setLoading(true);
        refs.resultGrid.getStore().removeAll();
        tab.setTitle(me.getView().title + '(' + message.msg('hive.running') + '..)');
        tab.isRunning = true;
        tab.status = 'RUNNING';

        invokePostByMap(CONSTANTS.TAJO.EXECUTE, {
                query: escape(refs.queryEditor.editor.getSession().getValue()),
                clusterName: ENGINE.id,
                uuid: tab.uuid,
                database: database,
                maxRows: tab.up('panel').up('panel').query('numberfield')[0].value
            },
            function (response) {
                var result = Ext.decode(response.responseText);

                // 실행시 로그 창을 먼저 표시한다.
                refs.tabpanel.setActiveTab(0);

                if (result.success) {
                    refs.logViewer.insertLast('QueryId: ' + result.map.queryId);
                    refs.logViewer.insertLast('Start: ' + new Date(result.map.startTime));

                    if (result.map.isResult && result.map.isFinish) {
                        refs.logViewer.insertLast('Finish: ' + new Date(result.map.finish));

                        refs.tabpanel.setActiveTab(1);

                        Ext.defer(function() {
                            var store = Ext.create('Ext.data.Store', {
                                fields: result.map.columns,
                                data: result.map.result
                            });

                            var columns = [];

                            for (var i = 0; i < result.map.columns.length; i++) {
                                var column = {
                                    text: result.map.columns[i],
                                    dataIndex: result.map.columns[i]
                                };
                                columns.push(column);
                            }
                            refs.resultGrid.reconfigure(store, columns);

                            tab.isRunning = false;
                            refs.queryEditor.setLoading(false);
                            tab.setTitle(tab.title.replace('(' + message.msg('hive.running') + '..)', ''));
                            me.fireEvent('setButton');
                        }, 300);
                    } else if (result.map.isFinish) {
                        refs.logViewer.insertLast('QUERY_SUCCEEDED');

                        tab.isRunning = false;
                        refs.queryEditor.setLoading(false);
                        tab.setTitle(tab.title.replace('(' + message.msg('hive.running') + '..)', ''));
                        me.fireEvent('setButton');
                    } else {
                        var p = Ext.create('Ext.ProgressBar', {
                            width: 300,
                            autoRender: true,
                            floating: true,
                            reference: 'progress'
                        });

                        refs.logViewer.add(p);

                        p.show();
                    }

                } else {
                    tab.isRunning = false;
                    refs.queryEditor.setLoading(false);
                    tab.setTitle(tab.title.replace('(' + message.msg('hive.running') + '..)', ''));
                    refs.logViewer.insertLast(result.error.cause);
                    me.fireEvent('setButton');
                }
            },
            function (response) {
                refs.queryEditor.setLoading(false);
                tab.status = '';
                Ext.MessageBox.show({
                    title: message.msg('common.warn'),
                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    }
});
