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
Ext.define('Flamingo2.view.fs.hdfs.File', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.hdfsFilePanel',

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
    selModel: {
        selType: 'checkboxmodel'
    },
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
            width: 50,
            sortable: false
        },
        {
            text: message.msg('fs.hdfs.common.filename'),
            flex: 1,
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
            align: 'center',
            hidden: true
        },
        {
            text: message.msg('fs.hdfs.common.spaceConsumed'),
            width: 80,
            dataIndex: 'spaceConsumed',
            hidden: true,
            align: 'center',
            renderer: function (value) {
                return fileSize(value);
            }
        }
    ],
    tbar: [
        {
            text: message.msg('fs.hdfs.common.copy'),
            iconCls: 'common-file-copy',
            itemId: 'copyButton',
            tooltip: message.msg('fs.hdfs.file.panel.tip.copy'),
            handler: 'onClickCopyFile'
        },
        {
            text: message.msg('fs.hdfs.common.move'),
            iconCls: 'common-file-move',
            itemId: 'moveButton',
            tooltip: message.msg('fs.hdfs.file.panel.tip.move'),
            handler: 'onClickMoveFile'
        },
        {
            text: message.msg('fs.hdfs.common.rename'),
            iconCls: 'common-file-rename',
            itemId: 'renameButton',
            tooltip: message.msg('fs.hdfs.file.panel.tip.rename'),
            handler: 'onClickRenameFile'
        },
        {
            text: message.msg('fs.hdfs.common.delete'),
            iconCls: 'common-delete',
            itemId: 'deleteButton',
            tooltip: message.msg('fs.hdfs.file.panel.tip.delete'),
            handler: 'onClickDeleteFile'
        },
        '-',
        {
            text: message.msg('fs.hdfs.common.upload'),
            iconCls: 'common-upload',
            itemId: 'uploadButton',
            tooltip: message.msg('fs.hdfs.file.panel.tip.upload'),
            handler: 'onClickUploadFile'
        },
        {
            text: message.msg('fs.hdfs.common.download'),
            iconCls: 'common-download',
            itemId: 'downloadButton',
            tooltip: message.msg('fs.hdfs.file.panel.tip.download'),
            handler: 'onClickDownloadFile'
        },
        {
            text: message.msg('fs.hdfs.common.viewFile'),
            iconCls: 'common-file-view',
            itemId: 'viewFileContentsButton',
            tooltip: message.msg('fs.hdfs.file.panel.tip.viewFile'),
            handler: 'onClickViewFile'
        },
        {
            text: message.msg('fs.hdfs.common.setPermission'),
            iconCls: 'common-user-auth',
            itemId: 'setPermission',
            tooltip: message.msg('fs.hdfs.file.panel.tip.permission'),
            handler: 'onClickFilePermission'
        },
        '->',
        {
            text: message.msg('common.refresh'),
            iconCls: 'common-refresh',
            itemId: 'refreshButton',
            tooltip: message.msg('fs.hdfs.file.tip.refresh'),
            handler: 'onFileRefreshBtn'
        }
    ],

    listeners: {
        itemclick: 'onFileItemClick',
        itemdblclick: 'onClickFileInfo',
        afterrender: 'onFileAfterRender',
        itemcontextmenu: 'onFileItemContextMenu',
        simpleHdfsBeforeOk: 'onFileSimpleHdfsBeforeOk',
        containercontextmenu: function (grid, event) {
            event.stopEvent();
        }
    }
});