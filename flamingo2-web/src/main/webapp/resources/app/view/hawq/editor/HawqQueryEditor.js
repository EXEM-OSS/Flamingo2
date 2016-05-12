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
Ext.define('Flamingo2.view.hawq.editor.HawqQueryEditor', {
    extend: 'Flamingo2.view.hawq.editor.EditorPanel',
    alias: 'widget.hawqQueryEditor',

    initComponent: function () {
        var me = this;

        var wordCount = Ext.create('Ext.toolbar.TextItem', {text: message.msg('hawq.label.resultgrid.column') + ': 0'}),
            lineCount = Ext.create('Ext.toolbar.TextItem', {text: message.msg('hawq.label.resultgrid.line') + ': 0'});

        Ext.apply(me, {
            bbar: Ext.create('Flamingo2.view.component.StatusBar', {
                itemId: 'statusBar',
                items: [lineCount, wordCount]
            })
        });

        // 생성자를 통해서 값이 넘어오면 설정한다.
        var content = this.value;

        me.on('editorcreated', function () {
            // 생성자를 통해서 내용이 넘어오면 생성된 이후에 내용을 채운다
            if (content) {
                me.editor.getSession().setValue(content);
            }

            me.editor.selection.on("changeCursor", function (e) {
                var c = me.editor.selection.getCursor(),
                    l = c.row + 1;

                wordCount.update(message.msg('hawq.label.resultgrid.column') + ': ' + c.column);
                lineCount.update(message.msg('hawq.label.resultgrid.line') + ': ' + l);

            }, me);

            var panel = me;
            me.editor.commands.addCommand({
                name: "execute",
                bindKey: {win: "Ctrl-Enter", mac: "Command-Enter"},
                exec: function (editor) {
                    me.fireEvent('extcuteQuery');
                }
            });
        });

        me.callParent(arguments);
    },

    listeners: {
        extcuteQuery: 'onExecuteQuery'
    }
});