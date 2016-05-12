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
Ext.define('Flamingo2.view.designer.property.browser.hdfs.BrowserPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.hdfsBrowserPanelForDesigner',

    layout: 'border',

    requires: [
        'Flamingo2.view.designer.HdfsBrowserController',
        'Flamingo2.view.designer.property.browser.hdfs.DirectoryPanel',
        'Flamingo2.view.designer.property.browser.hdfs.FilePanel'
    ],

    controller: 'designerHdfsBrowserController',

    items: [
        {
            layout: 'fit',
            region: 'west',
            title: message.msg('common.directory'),
            collapsible: true,
            split: true,
            width: 240,
            items: [
                {
                    split: true,
                    autoScroll: true,
                    xtype: 'hdfsDirectoryPanelForDesigner'
                }
            ]
        },
        {
            layout: 'fit',
            region: 'center',
            title: message.msg('common.file'),
            items: [
                {
                    split: true,
                    xtype: 'hdfsFilePanelForDesigner'
                }
            ]
        }
    ]
});