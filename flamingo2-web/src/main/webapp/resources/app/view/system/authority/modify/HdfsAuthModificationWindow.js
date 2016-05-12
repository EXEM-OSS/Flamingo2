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

Ext.define('Flamingo2.view.system.authority.HdfsAuthModificationWindow', {
    extend: 'Ext.window.Window',

    requires: [
        'Flamingo2.view.system.authority.HdfsAuthModificationController',
        'Flamingo2.view.system.authority.HdfsBrowserAuthModel',
        'Flamingo2.view.system.authority.register.HdfsAuthModificationForm'
    ],

    controller: 'hdfsAuthModificationViewController',

    viewModel: {
        type: 'hdfsBrowserAuthModel'
    },

    title: message.msg('system.authority.modify'),
    width: 430,
    resizable: false,
    closable: true,
    hideCollapseTool: false,
    titleCollapse: false,
    modal: true,
    closeAction: 'destroy',
    layout: 'fit',
    items: {
        xtype: 'hdfsAuthModificationForm'
    },
    buttonAlign: 'center',
    listeners: {
        afterrender: 'onComboAfterRender'
    }
});