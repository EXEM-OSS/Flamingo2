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
Ext.define('Flamingo2.view.hive.editor.EditorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hiveEditorController',

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
        var grid = Ext.create('Flamingo2.view.hive.editor.ResultSearchGridPanel', {
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
     * 쿼리 결과 '다음' 클릭 이벤트
     * 임의의 Store에 데이터를 저장하고 결과창 Grid의 스토어는 현재 Page에 맞게 표시한다.
     * */
    onNextClick: function (button) {
        var me = this;
        var refs = me.getReferences();
        var store = refs.resultGrid.getStore();
        var pageSize = store.pageSize;
        var pageStore = me.getViewModel().getData().pageStore;
        var page = ++me.getViewModel().getData().resultPage;

        //스토어에 설정된 페이지 사이즈와 현재 스토어의 카운트를 비교하여 카운트가 적으면 마지막 페이지로 인식한다.
        if (pageSize > store.getCount()) {
            Ext.MessageBox.alert(message.msg('common.check'), message.msg('hive.msg.last_page'));
            return;
        }

        var end = pageSize * page - 1;
        var start = pageSize * page - pageSize;
        var datas = pageStore.getRange(start, end);

        if (me.getViewModel().getData().resultPage == 2) {
            refs.btnPrev.setDisabled(false);
        }

        if (datas.length > 0) {
            store.removeAll();
            store.setData(datas);
            me.getViewModel().setData({page: page});
            refs.dfPage.setValue(message.msg('common.page') + ' ' + page);
        }
        else {
            var params = {
                clusterName: ENGINE.id,
                uuid: me.getView().uuid,
                limit: store.pageSize,
                page: me.getViewModel().getData().resultPage
            };

            invokeGet(CONSTANTS.HIVE.GET_PAGE, params,
                function (response) {
                    var r = Ext.decode(response.responseText);

                    if (r.success) {
                        //refs.dfPage.setValue(me.getViewModel().getData().resultPage);
                        if (r.list.length == 0) {
                            button.setDisabled(true);
                            return;
                        } else if (r.list.length < page) {
                            button.setDisabled(true);
                        }
                        store.removeAll();
                        store.loadData(r.list);
                        pageStore.insert(pageStore.data.items.length, r.list);
                        me.getViewModel().setData({page: page});
                        me.getViewModel().setData({pageStore: pageStore});
                        refs.dfPage.setValue(message.msg('common.page') + ' ' + page);
                    } else {
                        // FIXME
                    }
                },
                function (response) {
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            );
        }

    },

    /**
     * 쿼리 결과 '이전' 클릭 이벤트
     * 임의의 Store에 데이터를 저장하고 결과창 Grid의 스토어는 현재 Page에 맞게 표시한다.
     * */
    onPrevClick: function (button) {
        var me = this;
        var refs = me.getReferences();
        var store = refs.resultGrid.getStore();
        var pageSize = store.pageSize;
        var pageStore = me.getViewModel().getData().pageStore;
        var page = --me.getViewModel().getData().resultPage;

        if (page == 1) {
            button.setDisabled(true);
        }

        var end = pageSize * page - 1;
        var start = (pageSize * page) - pageSize;
        var datas = pageStore.getRange(start, end);

        store.removeAll();
        store.loadData(datas);

        me.getViewModel().setData({page: page});
        refs.dfPage.setValue(message.msg('common.page') + ' ' + page);
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
    onQueryExecute: function () {
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

        invokePostByMap(CONSTANTS.HIVE.EXECUTE, {
                query: escape(refs.queryEditor.editor.getSession().getValue()),
                clusterName: ENGINE.id,
                uuid: tab.uuid,
                database: query('#cbxDatabase').getValue()
            },
            function (response) {
                var result = Ext.decode(response.responseText);

                if (!result.success) {
                    // 실행시 로그 창을 먼저 표시한다.
                    refs.tabpanel.setActiveTab(0);

                    tab.isRunning = false;
                    refs.queryEditor.setLoading(false);
                    tab.setTitle(tab.title.replace('(' + message.msg('hive.running') + '..)', ''));
                    me.fireEvent('setButton');

                    // 로그창의 내용을 모두 지운다.
                    if (tab.status == 'CANCEL') {
                        error(message.msg('hive.msg.query_running_error'), '쿼리가 취소되었습니다.');
                    }
                    else {
                        refs.logViewer.setValue(result.error.exception);
                        error(message.msg('hive.msg.query_running_error'), result.error.message);
                    }

                    tab.status = '';
                } else {
                    // 실행시 로그 창을 먼저 표시한다.
                    refs.tabpanel.setActiveTab(0);

                    // 로그창의 내용을 모두 지운다.
                    refs.logViewer.setValue('');

                    setTimeout(function () {
                        me.getLog(1, tab.uuid);
                    }, 1000);
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
    },

    /**
     * Ajax때문에 만드는 재귀함수
     * 재귀를 이런데 써먹을 줄이야.........
     * */
    getLog: function (count, uuid) {
        var me = this;
        var refs = me.getReferences();
        var tab = me.getView();

        invokeGet(CONSTANTS.HIVE.GET_LOG, {uuid: tab.uuid},
            function (response) {
                var r = Ext.decode(response.responseText);

                if (r.success) {
                    refs.logViewer.insertLast(unescape(r.map.log));
                    if (r.map.end) {
                        tab.isRunning = false;
                        refs.queryEditor.setLoading(false);
                        tab.setTitle(tab.title.replace('(' + message.msg('hive.running') + '..)', ''));

                        // MR이 실행안되고 바로 결과가 나오는 경우.
                        refs.queryEditor.setLoading(false);

                        //refs.tabpanel.setActiveTab(1);
                        invokeGet(CONSTANTS.HIVE.RESULTS, {uuid: tab.uuid},
                            function (response) {
                                var r = Ext.decode(response.responseText);

                                if (r.success) {
                                    refs.tabpanel.setActiveTab(1);

                                    if (r.list.length > 0) {
                                        var keys = Object.keys(r.list[0]);
                                        var meta = {};

                                        meta.columns = [];
                                        meta.fields = [];

                                        for (var idx in keys) {
                                            meta.columns.push({dataIndex: keys[idx], text: keys[idx]});
                                            meta.fields.push({name: keys[idx], type: 'string'});
                                        }

                                        Ext.defer(function () {
                                            refs.resultGrid.getStore().loadData(r.list);
                                            me.onQueryResultsMetachange(refs.resultGrid.getStore(), meta);

                                            me.onQueryResultsLoad(refs.resultGrid.getStore(), r.list, true);
                                            refs.resultGrid.getView().refresh();
                                        }, 300);
                                    } else {
                                        info(message.msg('common.warn'), '조회된 데이터가 없습니다.');
                                    }
                                    ;

                                }
                                me.fireEvent('setButton');
                            },
                            function (response) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.warn'),
                                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        );
                    }
                    else {
                        // MR이 실행된 경우. WebSocket으로 로그를 가져온다.
                        invokePostByMap(CONSTANTS.HIVE.GET_LOG_ASYNC, {uuid: tab.uuid, clusterName: ENGINE.id},
                            function (response) {
                                // 성공 시 Websocket으로 로그메시지가 오기 때문에 실패여부만 확인
                            },
                            function (response) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.warn'),
                                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                                tab.isRunning = false;
                                tab.status = '';
                                refs.queryEditor.setLoading(false);
                                tab.setTitle(tab.title.replace('(' + message.msg('hive.running') + '..)', ''));
                                me.fireEvent('setButton');
                            }
                        );
                    }
                }
                else {
                    // 로그 가져오기를 3회 시도를 해도 로그값이 안넘어오면 오류로 간주하여 쿼리를 취소한다.
                    if (count >= 3) {
                        tab.isRunning = false;
                        tab.status = '';
                        refs.queryEditor.setLoading(false);
                        tab.setTitle(tab.title.replace('(' + message.msg('hive.running') + '..)', ''));
                        error(message.msg('hive.query_execution_error'), message.msg('hive.msg.cannot_get_log'));
                        me.fireEvent('setButton');
                        return;
                    }
                    else {
                        me.getLog(++count, uuid);
                    }
                }
            },
            function (response) {
                tab.isRunning = false;
                tab.status = '';
                refs.queryEditor.setLoading(false);
                tab.setTitle(tab.title.replace('(' + message.msg('hive.running') + '..)', ''));
                error(message.msg('hive.query_execution_error'), message.msg('hive.msg.cannot_get_log'));
                me.fireEvent('setButton');
                return;
            }
        );
        return;
    }
});
