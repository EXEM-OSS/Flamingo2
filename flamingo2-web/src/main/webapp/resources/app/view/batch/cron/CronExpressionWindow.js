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

Ext.define('Flamingo2.view.batch.cron.CronExpressionWindow', {
    extend: 'Ext.window.Window',

    requires: [
        'Flamingo2.view.batch.cron.CronExpressionWindowController',
        'Flamingo2.view.batch.cron.CronTrigger'
    ],

    controller: 'cronExpressionWindowController',

    height: 500,
    width: 550,
    closable: true,
    resizable: false,
    hideCollapseTool: false,
    title: 'Cron Expression',
    titleCollapse: false,
    modal: true,
    closeAction: 'close',
    layout: 'fit',

    items: [
        {
            xtype: 'cronTrigger'
        }
    ],
    buttonAlign: 'center',
    buttons: [
        {
            text: message.msg('common.confirm'),
            handler: 'onCronClickOK'
        },
        {
            text: message.msg('common.cancel'),
            handler: 'onCronClickCancel'
        }
    ],
    listeners: {
        afterrender: 'onAfterRender'
    }
});