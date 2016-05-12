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
Ext.define('Flamingo2.view.system.language.LanguageController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.languageViewController',

    rowplus: function (event, toolEl, panel) {
        var grid = this.lookupReference('languageGridPanel'),
            store = grid.getStore(),
            rowEditor = grid.getPlugin('rowEditorPlugin');
        rowEditor.cancelEdit();
        store.add({key: '', ko_KR: '', en_US: '', ja_JP: '', zh_CN: ''});
        rowEditor.startEdit(store.getCount() - 1, 0);
    },

    rowminus: function (event, toolEl, panel) {
        var grid = this.lookupReference('languageGridPanel'),
            store = grid.getStore(),
            selectionModel = grid.getSelectionModel();
        store.remove(selectionModel.getSelection());
    },

    /**
     * 메시지 번들을 모두 불러온다.
     */
    onAfterRender: function () {
        var languageGrid = query('languageGridPanel');
        setTimeout(function () {
            languageGrid.getStore().load({
                params: {
                    clusterName: ENGINE.id
                }
            });
        }, 10);
    },

    /**
     * 입력한 모든 값이 초기화되고 메시지 번들을 모두 불러온다.
     */
    onClearLanguageButton: function () {
        var me = this;
        var refs = me.getReferences();
        refs.conditionKey.setValue('ALL');
        refs.condition.setValue('');
        me.onFindLanguagButton();
    },

    /**
     * 입력한 조건값에 해당하는 메시지를 불러온다.
     */
    onFindLanguagButton: function () {
        var me = this;
        var refs = me.getReferences();
        var languageGrid = query('languageGridPanel');
        var conditionKey = refs.conditionKey.getValue();
        var condition = refs.condition.getValue();

        languageGrid.getStore().clearFilter(true);
        if (conditionKey == 'ALL') {
            languageGrid.getStore().filterBy(function (record) {
                var hasValue = false;
                if (record.data.key && record.data.key.indexOf(condition) != -1)hasValue = true;
                if (record.data.ko_KR && record.data.ko_KR.indexOf(condition) != -1)hasValue = true;
                if (record.data.en_US && record.data.en_US.indexOf(condition) != -1)hasValue = true;
                if (record.data.ja_JP && record.data.ja_JP.indexOf(condition) != -1)hasValue = true;
                if (record.data.zh_CN && record.data.zh_CN.indexOf(condition) != -1)hasValue = true;
                return hasValue;
            });
        } else {
            languageGrid.getStore().filterBy(function (record) {
                var hasValue = false;
                if (record.data[conditionKey] && record.data[conditionKey].indexOf(condition) != -1)hasValue = true;
                return hasValue;
            });
        }
    },
    onExportXlsxClick: function () {
        var languageGrid = query('languageGridPanel');
        languageGrid.getStore().clearFilter(true);
        var jsonData = Ext.encode(Ext.pluck(languageGrid.getStore().data.items, 'data'));

        /**
         * 그리드의 수정데이터를 post 로 넘기고, 리다이렉트를 파일로 다운받기위해 폼의 타겟을 iframe으로 변경.
         * 기존의 폼과 iframe 은 삭제시켜준다.
         */
        $('#exportLanguageForm').remove();
        $('#exportLanguageFrame').remove();
        var formstr = '<form id="exportLanguageForm" action="' + CONSTANTS.SYSTEM.LANGUAGE.EXPORT_XLSX + '" method=post target="exportLanguageFrame">' +
            '<input type=hidden name="grid" />' +
            '<input type=hidden name="type" />' +
            '</form>';
        var iframe = '<iframe id="exportLanguageFrame" name="exportLanguageFrame"></iframe>';

        $('body').append(formstr);
        $('body').append(iframe);

        var form = document.getElementById('exportLanguageForm');
        form.grid.value = jsonData;
        form.type.value = 'xlsx';
        form.submit();
    },
    onExportTextClick: function () {
        var languageGrid = query('languageGridPanel');
        languageGrid.getStore().clearFilter(true);
        var jsonData = Ext.encode(Ext.pluck(languageGrid.getStore().data.items, 'data'));

        /**
         * 그리드의 수정데이터를 post 로 넘기고, 리다이렉트를 파일로 다운받기위해 폼의 타겟을 iframe으로 변경.
         * 기존의 폼과 iframe 은 삭제시켜준다.
         */
        $('#exportLanguageForm').remove();
        $('#exportLanguageFrame').remove();
        var formstr = '<form id="exportLanguageForm" action="' + CONSTANTS.SYSTEM.LANGUAGE.EXPORT_ZIP + '" method=post target="exportLanguageFrame">' +
            '<input type=hidden name="grid" />' +
            '<input type=hidden name="type" />' +
            '</form>';
        var iframe = '<iframe id="exportLanguageFrame" name="exportLanguageFrame"></iframe>';

        $('body').append(formstr);
        $('body').append(iframe);

        var form = document.getElementById('exportLanguageForm');
        form.grid.value = jsonData;
        form.type.value = 'zip';
        form.submit();
    },
    onImportXlsxClick: function () {

        $('#importLanguageForm').remove();
        $('#importLanguageFrame').remove();
        var formstr = '<form id="importLanguageForm" action="' + CONSTANTS.SYSTEM.LANGUAGE.IMPORT_XLSX + '" method=post target="importLanguageFrame" enctype="multipart/form-data">' +
            '<label for="importLanguageFile"><input type="file" id="importLanguageFile" name="file"/></label>' +
            '</form>';
        var iframe = '<iframe id="importLanguageFrame" name="importLanguageFrame"></iframe>';
        $('body').append(formstr);
        $('body').append(iframe);

        $('#importLanguageFrame').on("load", function () {
            console.log('loaded');
            var res = JSON.parse($(this).contents().text());
            if (res.success) {
                var languageGrid = query('languageGridPanel');
                for (var i = 0; i < res.list.length; i++) {
                    res.list[i].key = res.list[i].id;
                }
                languageGrid.getStore().loadData(res.list);
            } else {
                Ext.Msg.alert('Status', 'Invalid file type.');
            }
        });
        $("#importLanguageFile").change(function () {
            var form = document.getElementById('importLanguageForm');
            form.submit();
        });
        $('#importLanguageFile').click();
    },
    onImportTextClick: function () {

        $('#importLanguageForm').remove();
        $('#importLanguageFrame').remove();
        var formstr = '<form id="importLanguageForm" action="' + CONSTANTS.SYSTEM.LANGUAGE.IMPORT_ZIP + '" method=post target="importLanguageFrame" enctype="multipart/form-data">' +
            '<label for="importLanguageFile"><input type="file" id="importLanguageFile" name="file"/></label>' +
            '</form>';
        var iframe = '<iframe id="importLanguageFrame" name="importLanguageFrame"></iframe>';
        $('body').append(formstr);
        $('body').append(iframe);

        $('#importLanguageFrame').on("load", function () {
            var res = JSON.parse($(this).contents().text());
            if (res.success) {
                var languageGrid = query('languageGridPanel');
                for (var i = 0; i < res.list.length; i++) {
                    res.list[i].key = res.list[i].id;
                }
                languageGrid.getStore().loadData(res.list);
            } else {
                Ext.Msg.alert('Status', 'Invalid file type.');
            }
        });
        $("#importLanguageFile").change(function () {
            var form = document.getElementById('importLanguageForm');
            form.submit();
        });
        $('#importLanguageFile').click();
    },
    onSaveClick: function () {
        var languageGrid = query('languageGridPanel');
        languageGrid.getStore().clearFilter(true);
        var jsonData = Ext.encode(Ext.pluck(languageGrid.getStore().data.items, 'data'));
        var params = {
            clusterName: ENGINE.id,
            grid: jsonData
        };

        invokePostByMap(CONSTANTS.SYSTEM.LANGUAGE.SAVE, params,
            function (response) {
                var res = Ext.decode(response.responseText);
                if (res.success) {
                    info(message.msg('system.language.save.succes'), 'Stored to ' + res.map.dir + '.');
                } else {
                    error(message.msg('workflow.msg.loading_fail'), '<font color="red">' + message.msg('system.language.save.fail') + ' : ' + res.error.message + '</font>');
                }
            },
            function (response) {
                var res = Ext.decode(response.responseText);
                error(message.msg('workflow.msg.loading_fail'), '<font color="red">' + message.msg('system.language.save.fail') + ' : ' + res.error.message + '</font>');
            }
        );
    }
});