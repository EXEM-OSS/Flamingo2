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
Ext.define('Flamingo2.view.pig.editor.EditorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.pigEditorController',

    onAfterrender: function (panel) {
        var me = this;
        var refs = this.getReferences();
    },

    onQueryResultsMetachange: function (store, meta) {
        var refs = this.getReferences();
        meta.columns.splice(0, 0, {
            xtype: 'rownumberer',
            text: message.msg('common.number'),
            width: 50,
            sortable: false
        });

        // if init, resize colums
        Ext.each(meta.columns, function (c) {
            c.flex = undefined;
            c.maxWidth = 10000;
            c.sortable = true;
        });

        refs.resultGrid.reconfigure(store, meta.columns);
    },

    /**
     * 쿼리창 종료하기 전 쿼리 실행여부 판단
     * */
    onBeforeclose: function (panel) {
        if (panel.isRunning) {
            Ext.MessageBox.show({
                title: message.msg('pig.kill_query'),
                message: message.msg('pig.msg.kill_query'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (btn) {
                    if (btn === 'yes') {
                        //TODO: 하이브 쿼리 강제 종료
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
    }
});
