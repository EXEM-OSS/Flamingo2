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
Ext.define('Flamingo2.view.component._HelpPopup', {
    extend: 'Ext.window.Window',

    title: message.msg('common.help'),
    modal: false,
    maximizable: true,
    resizable: true,
    width: 900,
    height: 650,
    iconCls: 'fa fa-book',
    layout: 'fit',
    items: [
        {
            xtype: 'panel',
            flex: 1,
            closable: false,
            showCloseOthers: false,
            showCloseAll: false,
            type: 'help',
            forceFit: true,
            printMargin: true,
            html: '<iframe style="overflow:auto;width:100%;height:100%;" frameborder="0"  src="' + config['manual.page'] + '"></iframe>',
            border: false,
            autoScroll: true
        }
    ]
});