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
 * ViewController for Flamingo2.view.hawq.Hawq
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.Hawq
 */
Ext.define('Flamingo2.view.hawq.HawqController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hawqController',

    listen: {
        controller: {
            hawqEditorController: {
                editorDestroyed: 'onEditorDestroyed',
                executeQuery: 'onExecuteQuery'
            },
            websocketController: {
                hawqPidMessage: 'onHawqPidMessage',
                hawqQueryMessage: 'onHawqQueryMessage',
                hawqMessage: 'onHawqMessage'
            }
        }
    },

    onAfterrender: function () {
        var me = this;

        if (me.getViewModel().getData().panelCount <= 1) {
            me.onAddHawqEditorTab();
        }

        Ext.get('hawqDownloadForm').child('input#clusterName').set({value: ENGINE.id});
    },

    /**
     * HAWQ Editor 신규탭을 생성한다.
     */
    onAddHawqEditorTab: function () {
        var me = this,
            refs = me.getReferences(),
            uuid = UUIDGenerate().replace(/-/g, ''),
            panelCount = me.getViewModel().getData().panelCount,
            editor = Ext.create('Flamingo2.view.hawq.editor.HawqEditor', {
                title: message.msg('hawq.title.editor.query') + ' ' + panelCount,
                uuid: uuid,
                closable: true,
                isRunning: false, //쿼리 실행상태 체크값
                printMargin: false
            });

        refs.editorTab.add(editor);
        refs.editorTab.setActiveTab(editor);
        me.getViewModel().setData({panelCount: ++panelCount});
    },

    /**
     * 에디터 탭 Change Event
     * 탭이 이동할때 현재 탭의 상태를 확인하여 버튼 처리
     */
    onEditorTabChange: function (tabPanel, newCard, oldCard) {
        this.setDisabledButtons(newCard.isRunning);

        Ext.each(newCard.getReferences().hawqQueryResultTabpanel.items.items, function (panel, index, allPanels) {
            if (panel.editor) {
                panel.editor.resize();
            }
        });
    },

    /**
     * 쿼리창이 종료되었으면 부모창에서 탭의 갯수를 판단하여 탭이 없으면 새로운 창을 생성함.
     */
    onEditorDestroyed: function () {
        var me = this,
            refs = me.getReferences();

        // Editor 자체를 닫았을때 문제가 발생함
        try {
            if (refs.editorTab.items.items.length == 0) {
                me.onAddHawqEditorTab();
            }
        } catch (exception) {
            return;
        }
    },

    /**
     * 쿼리실행
     */
    onExecuteQuery: function () {
        var me = this,
            refs = me.getReferences(),
            tab = refs.editorTab.getActiveTab(),
            tabRefs = tab.getReferences(),
            queryString = trim(tabRefs.queryEditor.editor.getSession().getValue()),
            range = tabRefs.queryEditor.editor.getSelection().getRange(),
            rangeQueryString = tabRefs.queryEditor.editor.getSession().getTextRange(range),
            hawqMaxRows = refs.hawqMaxRows;

        if (hawqMaxRows.isValid()) {
            if (!isBlank(queryString) || !isBlank(rangeQueryString)) {
                if (hawqMaxRows.getValue() !== 0) {
                    var runningQuery = !isBlank(rangeQueryString) ? rangeQueryString : queryString;

                    tabRefs.queryEditor.setLoading(true);
                    tab.setTitle(tab.title + ' ' + message.msg('hawq.title.editor.running'));
                    tabRefs.hawqQueryResultTabpanel.setActiveTab(2);// message panel
                    tabRefs.hawqResultMessagePanel.setValue('');

                    invokePostByMap(
                        CONSTANTS.HAWQ.EDITOR.EXECUTE,
                        {
                            clusterName: ENGINE.id,
                            databaseName: query('hawqBrowser #hawqDatabaseCombobox').getValue(),
                            query: runningQuery,
                            uuid: tab.uuid,
                            maxRows: hawqMaxRows.getValue()
                        },
                        function (response) {
                            var result = Ext.decode(response.responseText);
                            if (result.success) {
                                tab.isRunning = true;
                                me.setDisabledButtons(true);
                                tab.getViewModel().getStore('hawqResultSetData').removeAll();
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('hawq.title.fail'),
                                    message: format(message.msg('hawq.msg.fail.execquery') + '<br/>' + message.msg('hawq.msg.cause'), result.error.message),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING,
                                    fn: function () {
                                        tab.isRunning = false;
                                        me.setDisabledButtons(false);
                                        tabRefs.queryEditor.setLoading(false);
                                        tab.setTitle(tab.title.replace(' ' + message.msg('hawq.title.editor.running'), ''));
                                        tabRefs.queryEditor.editor.focus();
                                    }
                                });
                            }
                        },
                        function (response) {
                            Ext.MessageBox.show({
                                title: message.msg('hawq.title.fail'),
                                message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.ERROR,
                                fn: function () {
                                    tab.isRunning = false;
                                    me.setDisabledButtons(false);
                                    tabRefs.queryEditor.setLoading(false);
                                    tab.setTitle(tab.title.replace(' ' + message.msg('hawq.title.editor.running'), ''));
                                    tabRefs.queryEditor.editor.focus();
                                }
                            });
                        }
                    );
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('hawq.title.warning'),
                        message: message.msg('hawq.msg.warning.zeromaxrows'),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING,
                        fn: function () {
                            hawqMaxRows.focus(true);
                        }
                    });
                }
            }
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidmaxrows'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    hawqMaxRows.focus(true);
                }
            });
        }
    },

    onHawqPidMessage: function (wsMessage) {
        var me = this,
            refs = me.getReferences(),
            result = Ext.decode(wsMessage.body);

        if (result.success) {
            var response = result.map,
                uuid = response.uuid,
                pid = response.pid,
                tabItems = refs.editorTab.items.items,
                runningTab = me.getRunningTabByUuid(tabItems, uuid);

            runningTab.pid = pid;
        } else {
            refs.hawqResultLogPanel.setValue(
                message.msg('hawq.msg.fail.getpid') + '\n' +
                format(message.msg('hawq.msg.cause'), result.error.message) + '\n'
            );
            refs.hawqResultLogPanel.resize();
        }
    },

    onHawqQueryMessage: function (wsMessage) {
        var me = this,
            result = Ext.decode(wsMessage.body),
            queryResult = result.map,
            runningTab = me.getRunningTabByUuid(me.getReferences().editorTab.items.items, queryResult.uuid);

        if (result.success) {
            if (queryResult.isEnd) {
                me.endGetQueryResult(queryResult);
            } else {
                var results = runningTab.getViewModel().getData().queryResults,
                    added = false,
                    record = {};

                for (var ky in queryResult.record) {// column 명이 id 일 경우 grid 에서 중복데이터 안나오는 현상 수정
                    record[ky === 'id' ? '_id' : ky] = queryResult.record[ky] ? queryResult.record[ky].replace(/ /g, '&nbsp;') : '';
                }

                for (var i = 0, length = results.length; i < length; i++) {
                    var runningResults = results[i];
                    if (runningResults && runningResults.uuid === queryResult.uuid) {
                        runningResults.records.push(record);
                        added = true;
                    }
                }

                if (!added) {
                    var fields = [];
                    for (var key in record) {
                        var text = key === '_id' ? 'id' : key;
                        if (text.indexOf('_pivotal_hawq_management_editor_executed_query_by_hawq_query_editor_') > -1) {// 포함됨
                            text = text.replace(/_pivotal_hawq_management_editor_executed_query_by_hawq_query_editor_.*/, '');
                        }
                        fields.push({
                            text: text,
                            dataIndex: key
                        });
                    }
                    results.push({
                        uuid: queryResult.uuid,
                        records: [record],
                        fields: fields
                    });
                }
            }
        } else {
            if (!Ext.Object.isEmpty(runningTab)) {
                runningTab.getReferences().hawqQueryResultTabpanel.setActiveTab(0);
                var logPanel = runningTab.getReferences().hawqResultLogPanel;

                runningTab.isRunning = false;
                runningTab.getReferences().queryEditor.setLoading(false);
                runningTab.setTitle(runningTab.title.replace(' ' + message.msg('hawq.title.editor.running'), ''));
                me.setDisabledButtons(false);

                logPanel.insertLast(
                    message.msg('hawq.msg.fail.execquery') + '\n' +
                    format(message.msg('hawq.msg.cause'), result.error.message) + '\n'
                );
                logPanel.scrollToLine(logPanel.getLength(), false, false, null);
                logPanel.resize();
                me.getReferences().editorTab.getActiveTab().getReferences().queryEditor.editor.focus();
            }
        }
    },

    endGetQueryResult: function (queryResult) {
        var me = this,
            refs = me.getReferences(),
            runningTab = me.getRunningTabByUuid(refs.editorTab.items.items, queryResult.uuid),
            tabRefs = runningTab.getReferences(),
            queryResultTab = tabRefs.hawqQueryResultTabpanel,
            resultDataPanel = tabRefs.hawqResultDataPanel,
            logPanel = tabRefs.hawqResultLogPanel,
            donutPanel = tabRefs.hawqDonutChartPanel,
            barPanel = tabRefs.hawqBarChartPanel,
            areaPanel = tabRefs.hawqAreaChartPanel,
            linePanel = tabRefs.hawqLineChartPanel;

        runningTab.isRunning = false;
        tabRefs.queryEditor.setLoading(false);
        runningTab.setTitle(runningTab.title.replace(' ' + message.msg('hawq.title.editor.running'), ''));
        me.setDisabledButtons(false);

        if (queryResult.queryType === 'SELECT_QUERY') {
            queryResultTab.setActiveTab(1);

            var resultStore = runningTab.getViewModel().getStore('hawqResultSetData'),
                results = runningTab.getViewModel().getData().queryResults,
                runningResults = {},
                i = 0;

            for (var length = results.length; i < length; i++) {
                runningResults = results[i];
                if (runningResults && runningResults.uuid === queryResult.uuid) {
                    break;
                }
            }
            resultStore.loadData(runningResults ? runningResults.records || [] : []);

            resultDataPanel.removeAll();
            var grid = Ext.create('Flamingo2.view.hawq.editor.HawqResultSearchGridPanel', {
                reference: 'hawqResultSearchGridPanel',
                store: resultStore,
                columns: runningResults ? runningResults.fields || [] : []
            });
            resultDataPanel.add(grid);
            grid.getDockedItems('toolbar[dock="bottom"]')[0].setText(
                format(message.msg('hawq.msg.success.runningtime'), queryResult.runningTime) + ', ' +
                format(message.msg('hawq.msg.success.result'), queryResult.recordsSize)
            );

            donutPanel.removeAll(true);
            donutPanel.chartFields = runningResults ? runningResults.fields || [] : [];
            barPanel.removeAll(true);
            barPanel.chartFields = runningResults ? runningResults.fields || [] : [];
            areaPanel.removeAll(true);
            areaPanel.chartFields = runningResults ? runningResults.fields || [] : [];
            linePanel.removeAll(true);
            linePanel.chartFields = runningResults ? runningResults.fields || [] : [];

            delete results[i];
        } else {// queryResult.queryType === 'NOT_SELECT_QUERY'
            queryResultTab.setActiveTab(0);
            logPanel.insertLast(
                message.msg('hawq.msg.success.execquery') + '\n' +
                format(message.msg('hawq.msg.success.runningtime'), queryResult.runningTime) + '\n'
            );
            logPanel.scrollToLine(logPanel.getLength(), false, false, null);
            logPanel.resize();
            me.getReferences().editorTab.getActiveTab().getReferences().queryEditor.editor.focus();
        }
        // focus 가 풀려서 focus 맞춰줌
        refs.editorTab.getActiveTab().getReferences().queryEditor.editor.focus();
    },

    onHawqMessage: function (wsMessage) {
        var me = this,
            response = Ext.decode(wsMessage.body),
            log = response.object,
            runningTab = me.getRunningTabByUuid(me.getReferences().editorTab.items.items, response.map.uuid),
            messagePanel = runningTab.getReferences().hawqResultMessagePanel;

        if (!isBlank(trim(log)) && !Ext.Object.isEmpty(runningTab)) {
            messagePanel.insertLast(log);
            messagePanel.scrollToLine(messagePanel.getLength(), false, false, null);
        }
        if (response.map.isLogEnd) {
            messagePanel.resize();
            messagePanel.scrollToLine(messagePanel.getLength(), false, false, null);
        }
    },

    getRunningTabByUuid: function (tabItems, uuid) {
        var runningTab = {};
        for (var i = 0, length = tabItems.length, tab = {}; i < length; i++) {
            tab = tabItems[i];
            if (tab.uuid === uuid) {
                runningTab = tab;
                break;
            }
        }
        return runningTab;
    },

    setDisabledButtons: function (isRunning) {
        var refs = this.getReferences();
        refs.hawqExecuteButton.setDisabled(isRunning);
        refs.hawqStopButton.setDisabled(!isRunning);
        refs.hawqUndoButton.setDisabled(isRunning);
        refs.hawqRedoButton.setDisabled(isRunning);
        refs.hawqMaxRows.setDisabled(isRunning);
        refs.hawqViewPlanButton.setDisabled(isRunning);
        refs.hawqTemplateButton.setDisabled(isRunning);
    },

    undoEditHawqQuery: function () {
        this.getReferences().editorTab.getActiveTab().getReferences().queryEditor.editor.undo();
    },

    redoEditHawqQuery: function () {
        this.getReferences().editorTab.getActiveTab().getReferences().queryEditor.editor.redo();
    },

    stopHawqQuery: function (button) {
        var me = this,
            tab = me.getReferences().editorTab.getActiveTab(),
            pid = tab.pid;

        if (pid) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.stop'),
                message: message.msg('hawq.msg.question.stopquery'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        me.hawqQuerySessionKill(tab.uuid, pid);
                    }
                }
            });
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.fail'),
                message: message.msg('hawq.msg.fail.getpid'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.ERROR
            });
        }
    },

    viewPlan: function () {
        var me = this,
            tab = me.getReferences().editorTab.getActiveTab(),
            tabRefs = tab.getReferences(),
            queryString = trim(tabRefs.queryEditor.editor.getSession().getValue());

        if (!isBlank(queryString)) {
            var params = {
                clusterName: ENGINE.id,
                databaseName: query('hawqBrowser #hawqDatabaseCombobox').getValue(),
                query: queryString
            };
            invokePostByMap(
                CONSTANTS.HAWQ.EDITOR.VIEW_PLAN,
                params,
                function (response) {
                    var result = Ext.decode(response.responseText);
                    if (result.success) {
                        tabRefs.hawqQueryResultTabpanel.setActiveTab(1);
                        var resultStore = tab.getViewModel().getStore('hawqResultSetData'),
                            fields = [{
                                text: 'QUERY_PLAN',
                                dataIndex: 'QUERY PLAN',
                                flex: 1,
                                producesHTML: false,
                                renderer: function (value) {
                                    return value.replace(/ /g, '&nbsp;')
                                }
                            }];
                        resultStore.removeAll();
                        resultStore.loadData(result.list);
                        resultStore.setFields(fields);
                        tabRefs.hawqResultSearchGridPanel.reconfigure(resultStore, fields);
                    } else {
                        Ext.MessageBox.show({
                            title: message.msg('hawq.title.fail'),
                            message: message.msg('hawq.msg.fail.viewplan') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    }
                },
                function (response) {
                    Ext.MessageBox.show({
                        title: message.msg('hawq.title.fail'),
                        message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.ERROR
                    });
                }
            );
        }
    },

    hawqQuerySessionKill: function (uuid, pid) {
        var me = this,
            tab = me.getRunningTabByUuid(me.getReferences().editorTab.items.items, uuid),
            tabRefs = tab.getReferences();

        invokePostByMap(
            CONSTANTS.HAWQ.EDITOR.KILL_SESSION,
            {
                clusterName: ENGINE.id,
                pid: pid
            },
            function (response) {
                var result = Ext.decode(response.responseText);
                if (result.success) {
                    var killSuccess = result.object;
                    if (killSuccess) {
                        App.UI.infomsg(message.msg('hawq.title.success'), message.msg('hawq.msg.success.stopquery'));
                        me.setDisabledButtons(false);
                        tab.isRunning = false;
                        tabRefs.queryEditor.setLoading(false);
                        tab.setTitle(tab.title.replace(' ' + message.msg('hawq.title.editor.running'), ''));
                    } else {
                        App.UI.errormsg(message.msg('hawq.title.fail'), message.msg('hawq.msg.fail.notrunning'));
                        me.setDisabledButtons(false);
                        tab.isRunning = false;
                        tabRefs.queryEditor.setLoading(false);
                        tab.setTitle(tab.title.replace(' ' + message.msg('hawq.title.editor.running'), ''));
                    }
                } else {
                    App.UI.errormsg(message.msg('hawq.title.fail'), message.msg('hawq.msg.fail.stopquery') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message));
                    me.setDisabledButtons(false);
                    tab.isRunning = false;
                    tabRefs.queryEditor.setLoading(false);
                    tab.setTitle(tab.title.replace(' ' + message.msg('hawq.title.editor.running'), ''));
                }
            },
            function (response) {
                Ext.MessageBox.show({
                    title: message.msg('hawq.title.fail'),
                    message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.ERROR
                });
            }
        );
    },

    hawqTemplateComboboxChange: function (field, value) {
        var editor = this.getReferences().editorTab.getActiveTab().getReferences().queryEditor;
        editor.insertFirst(unescape(value));
        editor.editor.focus();
    }
});