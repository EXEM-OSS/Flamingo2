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

Ext.define('Flamingo2.view.fs.hdfs.simple.SimpleHdfsFileBrowser', {
    extend: 'Ext.window.Window',

    requires: [
        'Flamingo2.view.fs.hdfs.simple.SimpleHdfsFileBrowserController',
        'Flamingo2.view.fs.hdfs.simple.SimpleHdfsBrowserModel',
        'Flamingo2.view.fs.hdfs.Directory',
        'Flamingo2.view.fs.hdfs.File'
    ],

    controller: 'simpleHdfsFileBrowserController',

    viewModel: {
        type: 'simpleHdfsBrowserModel'
    },

    title: message.msg('fs.hdfs.common.browser'),
    layout: 'border',
    width: 800,
    height: 400,
    modal: true,

    items: [
        {
            xtype: 'treepanel',
            reference: 'trpDirectory',
            bind: {
                store: '{directoryStore}'
            },
            region: 'west',
            collapsible: true,
            title: message.msg('fs.hdfs.browser.directory'),
            width: 220,
            layout: 'fit',
            split: true,
            border: true,
            listeners: {
                itemcontextmenu: 'onTrpDirectoryItemcontextmenu',
                itemclick: 'onClickDirectoryItem'
            }
        },
        {
            xtype: 'grid',
            region: 'center',
            title: message.msg('fs.hdfs.browser.file'),
            split: true,
            border: true,
            reference: 'hdfsFileGrid',
            bind: {
                store: '{fileStore}'
            },
            plugins: [
                {
                    ptype: 'bufferedrenderer',
                    leadingBufferZone: 50,
                    trailingBufferZone: 20
                }
            ],
            viewConfig: {
                columnLines: true,
                stripeRows: true,
                getRowClass: function () {
                    return 'cell-height-30';
                }
            },
            columns: [
                {
                    xtype: 'rownumberer',
                    width: 30,
                    sortable: false
                },
                {
                    text: message.msg('fs.hdfs.common.filename'),
                    width: 225,
                    dataIndex: 'filename',
                    align: 'center',
                    tdCls: 'monospace-column',
                    renderer: function (value, metaData, record) {
                        metaData.tdAttr = 'data-qtip="'
                            + message.msg('fs.hdfs.common.filename') + ' : ' + record.get('filename')
                            + '<br/>'
                            + message.msg('fs.hdfs.common.file.length') + ' : ' + fileSize(record.get('length')) + ' (' + toCommaNumber(record.get('length')) + ')'
                            + '<br/>'
                            + message.msg('fs.hdfs.common.replication') + ' : ' + record.get('replication')
                            + '<br/>'
                            + message.msg('fs.hdfs.common.spaceConsumed') + ' : ' + fileSize(record.get('spaceConsumed'))
                            + '"';
                        return value;
                    }
                },
                {
                    text: message.msg('fs.hdfs.common.file.length'),
                    width: 80,
                    sortable: true,
                    dataIndex: 'length',
                    align: 'center',
                    renderer: function (value) {
                        return fileSize(value);
                    }
                },
                {
                    text: message.msg('fs.hdfs.common.modification'),
                    width: 140,
                    dataIndex: 'modificationTime',
                    align: 'center'
                },
                {
                    text: message.msg('fs.hdfs.common.owner'), width: 80, dataIndex: 'owner', align: 'center'
                },
                {
                    text: message.msg('fs.hdfs.common.group'), width: 80, dataIndex: 'group', align: 'center'
                },
                {
                    text: message.msg('fs.hdfs.common.permission'), width: 80, dataIndex: 'permission', align: 'center'
                },
                {
                    text: message.msg('fs.hdfs.common.replication'),
                    width: 60,
                    dataIndex: 'replication',
                    align: 'center'
                },
                {
                    text: message.msg('fs.hdfs.common.spaceConsumed'),
                    width: 80,
                    dataIndex: 'spaceConsumed',
                    align: 'center',
                    renderer: function (value) {
                        return fileSize(value);
                    }
                }
            ]
        }
    ],
    buttonAlign: 'right',
    buttons: [
        {
            text: message.msg('common.ok'),
            iconCls: 'common-ok',
            handler: 'onBtnOkClick'
        },
        {
            text: message.msg('common.cancel'),
            iconCls: 'common-cancel',
            handler: 'onBtnCancelClick'
        }
    ],
    listeners: {
        afterrender: 'onAfterRender',
        containercontextmenu: function (tree, event) {
            event.stopEvent();
        }
    }
});