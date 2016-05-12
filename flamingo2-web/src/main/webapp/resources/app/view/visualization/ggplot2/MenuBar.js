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
Ext.define('Flamingo2.view.visualization.ggplot2.MenuBar', {
    extend: 'Ext.toolbar.Toolbar',
    alias: 'widget.ggplot2MenuBar',

    defaults: {
        scale: 'small'
    },

    items: [
        {
            xtype: 'button',
            reference: 'btnDataLoad',
            iconCls: 'common-directory-open',
            text: message.msg('visual.data_load'),
            handler: 'onLoadBtnClick'
        },
        {
            xtype: 'button',
            reference: 'btnDataClose',
            iconCls: 'common-directory-close',
            text: message.msg('visual.data_close'),
            handler: 'onBtnCloseClick',
            disabled: true
        }, '-',
        {
            xtype: 'button',
            reference: 'btnDrawPlot',
            iconCls: 'common-execute',
            text: message.msg('visual.draw'),
            handler: 'onDrawPlotClick',
            disabled: true
        },
        {
            xtype: 'button',
            reference: 'btnResetPlot',
            iconCls: 'common-refresh',
            text: message.msg('common.reset'),
            handler: 'onResetClick',
            disabled: true
        }
    ]
});