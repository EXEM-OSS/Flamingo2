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
Ext.define('Flamingo2.view.pig.PigController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.pigController',

    listen: {
        controller: {
            websocketController: {
                pigLogMessage: 'onPigLogMessage'
            },
            pigEditorController: {
                editorDestroyed: 'onEditorDestroyed'
            }
        }
    },

    onAfterrender: function () {
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

        var editor = Ext.create('Flamingo2.view.pig.editor.Editor', {
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
        var me = this;

        if (newCard.isRunning) {
            me.setButtonDisabled(true);
        }
        else {
            me.setButtonDisabled(false);
        }
    },

    /**
     * Excute 클릭 이벤트
     * */
    onExecuteClick: function () {
        var me = this;
        var refs = me.getReferences();
        var tab = refs.editorTab.getActiveTab();
        var tabRefs = tab.getReferences();
        var query = escape(tabRefs.queryEditor.editor.getSession().getValue());

        if (Ext.isEmpty(query)) {
            error(message.msg('common.error'), 'Pig Script를 입력하시오.');
            return;
        }

        invokePostByMap(CONSTANTS.PIG.EXECUTE, {
                query: query,
                clusterName: ENGINE.id,
                uuid: tab.uuid
            },
            function (response) {
                me.setButtonDisabled(true);
                tab.isRunning = true;
            },
            function (response) {
                me.setButtonDisabled(false);
                tab.isRunning = false;

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
     * 서버에서 수신받은 WebSoket 메시지 처리
     * Tab에서 UUID에 맞는 에디터에 로그메시지를 표시한다.
     * */
    onPigLogMessage: function (msg) {
        var me = this;
        var refs = me.getReferences();
        var body = Ext.decode(msg.body);
        var uuid = body.uuid;
        var tab, tabs = refs.editorTab.items.items;

        for (var i = 0; i < tabs.length; i++) {
            tab = tabs[i];
            if (tab.uuid == uuid)
                break;
        }
        var tabRefs = tab.getReferences();

        if (body.isEnd) {
            me.setButtonDisabled(false);
            tab.isRunning = false;
        }

        if (body.isError) {
            error(message.msg('common.error'), message.msg('pig.msg.script_run_fail'));
        }

        if (!Ext.isEmpty(body.message)) {
            tabRefs.logViewer.insertLast(body.message);
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
     * 쿼리창이 종료되었으면 부모창에서 탭의 갯수를 판단하여 탭이 없으면 새로운 창을 생성함.
     * */
    onEditorDestroyed: function () {
        var me = this;
        var refs = me.getReferences();

        // Pig Editor 자체를 닫았을때 문제가 발생함
        try {
            if (refs.editorTab.items.items.length == 0) {
                me.onNewClick();
            }
        }
        catch (exception) {
            return;
        }
    },

    /**
     * 다른 화면으로 이동 시 Websocket연결을 끊어준다.
     * */
    onBeforedestroy: function () {
        var me = this;

    },

    onHdfsBrowserClick: function () {
        Ext.create('Flamingo2.view.hive.editor.HdfsBrowserWindow').center().show();
    },

    /**
     * 버튼 Disabled 처리 함수
     * */
    setButtonDisabled: function (disabled) {
        var refs = this.getReferences();
        refs.btnExecute.setDisabled(disabled);
        refs.btnUndo.setDisabled(disabled);
        refs.btnRedo.setDisabled(disabled);
    }
});