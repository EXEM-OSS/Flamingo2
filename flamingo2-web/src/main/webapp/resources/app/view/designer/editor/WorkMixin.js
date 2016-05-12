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
Ext.define('Flamingo2.view.designer.editor.WorkMixin', {
    extend: 'Ext.util.Observable',
    path: '',
    sourceCode: '',
    autofocus: true,
    fontSize: '12px',
    theme: 'clouds',
    printMargin: false,
    printMarginColumn: 80,
    highlightActiveLine: true,
    highlightGutterLine: true,
    highlightSelectedWord: true,
    showGutter: true,
    fullLineSelection: true,
    tabSize: 4,
    useSoftTabs: false,
    showInvisible: false,
    useWrapMode: false,
    codeFolding: true,
    value: '',

    constructor: function (owner, config) {
        var me = this;
        me.owner = owner;

        me.addEvents({'editorcreated': true}, 'change');

        me.callParent();
    },

    initEditor: function () {
        var me = this;
        ace.require("ace/ext/language_tools");

        me.editor = ace.edit(me.editorId);
        me.editor.setOptions({
            enableBasicAutocompletion: true
        });
        me.editor.ownerCt = me;
        me.setMode(me.parser);
        me.setTheme(me.theme);
        me.editor.getSession().setUseWrapMode(me.useWrapMode);
        me.editor.setShowFoldWidgets(me.codeFolding);
        me.editor.setShowInvisibles(me.showInvisible);
        me.editor.setHighlightGutterLine(me.highlightGutterLine);
        me.editor.setHighlightSelectedWord(me.highlightSelectedWord);
        me.editor.renderer.setShowGutter(me.showGutter);
        me.setFontSize(me.fontSize);
        me.editor.setShowPrintMargin(me.printMargin);
        me.editor.setPrintMarginColumn(me.printMarginColumn);
        me.editor.setHighlightActiveLine(me.highlightActiveLine);
        me.editor.getSession().setTabSize(me.tabSize);
        me.editor.getSession().setUseSoftTabs(me.useSoftTabs);
        me.setValue(me.sourceCode);

        var saveButton = me.down('#saveButton');
        me.editor.getSession().on('change', function () {
            me.fireEvent('change', me);
        }, me);

        if (me.autofocus)
            me.editor.focus();
        else {
            me.editor.renderer.hideCursor();
            me.editor.blur();
        }

        me.editor.setOptions({
            fontFamily: "Monaco,Gulimche,'Courier New',NanumGhothicCoding"
        });

        me.editor.initialized = true;
        me.fireEvent('editorcreated', me);
    },

    resize: function () {
        this.editor.resize();
    },

    insertValue: function (value) {
        var position = this.editor.getCursorPosition();
        this.editor.getSession().insert(position, value);
    },

    getEditor: function () {
        return this.editor;
    },

    getSession: function () {
        return this.editor.getSession();
    },

    getTheme: function () {
        return this.editor.getTheme();
    },

    setTheme: function (name) {
        this.editor.setTheme("ace/theme/" + name);
    },

    setMode: function (mode) {
        this.editor.getSession().setMode("ace/mode/" + mode);
    },

    getValue: function () {
        return this.editor.getSession().getValue();
    },

    setValue: function (value) {
        this.editor.getSession().setValue(value);
    },

    setFontSize: function (value) {
        this.editor.setFontSize(value);
    },

    undo: function () {
        this.editor.undo();
    },

    redo: function () {
        this.editor.redo();
    },

    save: function () {
        var me = this;
        var script = this.editor.getSession().getValue();
        var saveField = me.saveField;
        var saveId = me.saveId;
        var conditions = saveField.getValue();
        conditions = JSON.parse(conditions);
        if (conditions[saveId]) {
            conditions[saveId] = script;
        }
        saveField.setValue(JSON.stringify(conditions));

        Ext.MessageBox.show({
            title: 'Status',
            message: format(message.msg('workflow.msg_editorwork'), saveId),
            buttons: Ext.MessageBox.OK,
            icon: Ext.MessageBox.INFO
        });
    }
});
