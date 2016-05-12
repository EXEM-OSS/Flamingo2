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
Ext.define("Flamingo2.view.component.ProgressBar", {
    extend: 'Ext.ProgressBar',
    alias: 'widget.progressBarCustom',

    status: '',

    initComponent: function () {
        var me = this;
        me.width = 300;
        me.callParent(arguments);
    },

    listeners: {
        update: function (obj, val) {
            // http://www.w3schools.com/tags/ref_colorpicker.asp
            if (obj.getEl()) {
                if (this.status == 'FINISHED') {
                    obj.getEl().child(".x-progress-bar", true).style.backgroundColor = "#008F00";
                    obj.getEl().child(".x-progress-bar", true).style.borderRightColor = "#008F00";
                    obj.getEl().child(".x-progress-bar", true).style.backgroundImage = "url('')";
                } else if (this.status == 'WAITING') {
                    obj.getEl().child(".x-progress-bar", true).style.backgroundColor = "#757547";
                    obj.getEl().child(".x-progress-bar", true).style.borderRightColor = "#757547";
                    obj.getEl().child(".x-progress-bar", true).style.backgroundImage = "url('')";
                } else if (this.status == 'FAILED') {
                    obj.getEl().child(".x-progress-bar", true).style.backgroundColor = "#A30000";
                    obj.getEl().child(".x-progress-bar", true).style.borderRightColor = "#A30000";
                    obj.getEl().child(".x-progress-bar", true).style.backgroundImage = "url('')";
                } else {
                    obj.getEl().child(".x-progress-bar", true).style.backgroundColor = "#5C85D6";
                    obj.getEl().child(".x-progress-bar", true).style.borderRightColor = "#5C85D6";
                    obj.getEl().child(".x-progress-bar", true).style.backgroundImage = "url('')";
                }
            }
        }
    }
});