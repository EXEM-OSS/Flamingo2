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
Ext.define('Flamingo2.view.designer.property.browser.hdfs.FilePanel', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.hdfsFilePanelForDesigner',

    store: Ext.create('Flamingo2.store.designer.property.browser.hdfs.FileStore'),

    stripeRows: true,

    columnLines: true,

    itemId: 'fileGrid',

    viewConfig: {
        enableTextSelection: true
    },

    columns: [
        {
            text: message.msg('workflow.common.hdfsbrowser.file.name'), flex: 2, dataIndex: 'filename', align: 'center',
            renderer: function (value, metadata, record, rowIndex, colIndex, store) {
                metadata.style = '!important;cursor: pointer;';
                metadata.tdAttr = 'data-qtip="' + value + '"';
                return value;
            }
        },
        {
            text: message.msg('workflow.common.hdfsbrowser.file.size'),
            flex: 1,
            sortable: true,
            dataIndex: 'length',
            align: 'center',
            renderer: function (value, metadata, record, rowIndex, colIndex, store) {
                metadata.style = '!important;cursor: pointer;';
                metadata.tdAttr = 'data-qtip="' + App.Util.String.toCommaNumber(value) + '"';
                return Ext.util.Format.fileSize(value);
            }
        },
        {
            text: message.msg('workflow.common.hdfsbrowser.file.date'),
            flex: 1.5,
            dataIndex: 'modificationTime',
            align: 'center',
            renderer: function (value, metadata, record, rowIndex, colIndex, store) {
                var date = new Date(value);
                var formatted = Ext.Date.format(date, 'Y-m-d H:i:s');

                metadata.style = '!important;cursor: pointer;';
                metadata.tdAttr = 'data-qtip="' + value + '"';
                return formatted;
            }
        },
        {
            text: message.msg('workflow.common.hdfsbrowser.file.auth'),
            flex: 1,
            dataIndex: 'permission',
            align: 'center',
            renderer: function (value, metadata, record, rowIndex, colIndex, store) {
                metadata.style = '!important;cursor: pointer;';
                metadata.tdAttr = 'data-qtip="' + value + '"';
                return value;
            }
        },
        {
            text: message.msg('workflow.common.hdfsbrowser.file.group'),
            flex: 1,
            dataIndex: 'group',
            align: 'center',
            hidden: true,
            renderer: function (value, metadata, record, rowIndex, colIndex, store) {
                metadata.style = '!important;cursor: pointer;';
                metadata.tdAttr = 'data-qtip="' + value + '"';
                return value;
            }
        },
        {
            text: message.msg('workflow.common.hdfsbrowser.file.owner'),
            flex: 1,
            dataIndex: 'owner',
            align: 'center',
            hidden: true,
            renderer: function (value, metadata, record, rowIndex, colIndex, store) {
                metadata.style = '!important;cursor: pointer;';
                metadata.tdAttr = 'data-qtip="' + value + '"';
                return value;
            }
        },
        {
            text: message.msg('workflow.common.hdfsbrowser.file.replication'),
            flex: 1,
            dataIndex: 'replication',
            align: 'center',
            renderer: function (value, metadata, record, rowIndex, colIndex, store) {
                metadata.style = '!important;cursor: pointer;';
                metadata.tdAttr = 'data-qtip="' + value + '"';
                return value;
            }
        },
        {
            text: message.msg('workflow.common.hdfsbrowser.file.block.size'),
            flex: 1,
            dataIndex: 'blockSize',
            align: 'center',
            hidden: true,
            renderer: function (value, metadata, record, rowIndex, colIndex, store) {
                metadata.style = '!important;cursor: pointer;';
                metadata.tdAttr = 'data-qtip="' + App.Util.String.toCommaNumber(value) + '"';
                return Ext.util.Format.fileSize(value);
            }
        }
    ],

    tbar: [
        '->',
        {
            text: message.msg('common.refresh'),
            iconCls: 'common-refresh',
            itemId: 'refreshButton',
            tooltip: message.msg('workflow.common.hdfsbrowser.refresh.tooltip')
        }
    ]
});
