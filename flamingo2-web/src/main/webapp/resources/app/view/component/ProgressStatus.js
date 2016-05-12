/*
 * Copyright (C) 2011  Flamingo Project (http://www.cloudine.io).
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
Ext.define('Flamingo2.view.component.ProgressStatus', {
    extend: 'Ext.Window',
    alias: 'widget.progress',

    title: message.msg('component.process_progressing'),

    width: 200,

    autoHeight: true,

    modal: false,

    x: 700,

    y: 120,

    initComponent: function () {
        var me = this;

        var offset = $('#user-menu').offset();
        me.x = offset.left - 100;
        me.y = offset.top + 120;

        this.runner = new Ext.util.TaskRunner();

        this.task = {
            run: function () {
                var pbar = me.down('#progressbar');
                if (pbar.progress >= 1) {
                    pbar.progress = 0.1;
                    pbar.updateProgress(0.1, message.msg('component.progressing'));
                } else {
                    pbar.progress += 0.1;
                    pbar.updateProgress(pbar.progress, message.msg('component.progressing'));
                }
            },
            interval: 500
        };

        Ext.apply(me, {
            items: [
                {
                    xtype: 'progressbar',
                    border: 0,
                    progress: 0,
                    itemId: 'progressbar'
                }
            ]
        });

        me.callParent(arguments);
    },

    listeners: {
        render: function (comp) {
            comp.runner.start(comp.task);
        },
        beforeclose: function (win) {
            // user has already answered yes
            if (win.closeMe) {
                win.closeMe = false;
                return true;
            }

            // ask user if really close
            Ext.MessageBox.show({
                title: message.msg('common.confirm'),
                message: message.msg('component.msg.exit'),
                buttons: Ext.MessageBox.YESNO,
                callback: function (btn) {
                    if ('yes' === btn) {
                        // save the user answer
                        win.closeMe = true;

                        // call close once again
                        win.close();
                    }
                }
            });

            // Always cancel closing if we get here
            return false;
        },
        destroy: function (comp) {
            comp.runner.stop(comp.task);
        }
    }

});