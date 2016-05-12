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
Ext.define('Flamingo2.view.fs.hdfs.Browser', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.browser',

    requires: [
        'Flamingo2.view.fs.hdfs.BrowserController',
        'Flamingo2.view.fs.hdfs.BrowserModel',
        'Flamingo2.view.fs.hdfs.summary.HdfsSumChart',
        'Flamingo2.view.fs.hdfs.Directory',
        'Flamingo2.view.fs.hdfs.File',
        'Flamingo2.view.component._StatusBar'
    ],

    controller: 'browserViewController',

    viewModel: {
        type: 'browserModel'
    },

    layout: {
        type: 'border'
    },
    margin: '0 5',
    bodyStyle: {
        background: '#ffffff'
    },
    flex: 1,
    items: [
        {
            xtype: 'container',
            itemId: 'title',
            region: 'north',
            height: 45,
            margin: '0 0 10 0',
            cls: 'page-header',
            bind: {
                html: '<h1>{title}</h1>'
            }
        },
        {
            xtype: 'hdfsSumChartPanel',
            itemId: 'sumChart',
            region: 'north',
            height: 190
        },
        {
            xtype: 'hdfsDirectoryPanel',
            region: 'west',
            collapsible: true,
            collapsed: false,
            border: true,
            split: true,
            title: message.msg('fs.hdfs.browser.directory'),
            width: 260,
            layout: 'fit'
        },
        {
            xtype: 'hdfsFilePanel',
            region: 'center',
            title: message.msg('fs.hdfs.browser.file'),
            border: true
        },
        {
            xtype: '_statusBar',
            region: 'south',
            defaultText: '',
            cls: 'monospace-text',
            text: message.msg('common.path'),
            border: true
        }
    ],
    listeners: {
        engineChanged: 'onEngineChanged'
    }
});