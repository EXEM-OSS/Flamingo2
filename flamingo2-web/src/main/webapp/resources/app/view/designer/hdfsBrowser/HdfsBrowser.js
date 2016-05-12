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
Ext.define('Flamingo2.view.designer.HdfsBrowser', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.hdfsBrowser',

    layout: 'border',

    stores: ['HdfsBrowserTree', 'HdfsBrowserList'],

    autoCreate: true,

    initComponent: function () {
        this.items = [
            {
                title: message.msg('common.directory'),
                region: 'west',
                layout: 'fit',
                width: 200,
                collapsible: true,
                split: true,
                items: [
                    {
                        itemId: 'hdfsBrowserTree',
                        xtype: 'treepanel',
                        store: 'HdfsBrowserTree',
                        rootVisible: true,
                        dockedItems: [
                            {
                                xtype: 'toolbar',
                                items: [
                                    '->',
                                    {
                                        itemId: 'refreshButton',
                                        text: msg.button_refresh,
                                        iconCls: 'common_refresh',
                                        tooltip: message.msg('fs.hdfs.tip_file_refresh')
                                    }
                                ]
                            }
                        ],
                        bbar: [
                            {
                                itemId: 'usageButton',
                                text: msg.hdfs_tree_dfs_usage,
                                iconCls: 'hdfs_used'
                            },
                            Ext.create('Ext.ProgressBar', { // HDFS 사용량을 보여주는 Progress Bar
                                itemId: 'hdfsSize',
                                width: 130,
                                text: '',
                                value: 0
                            })
                        ],
                        listeners: {
                            itemclick: function (view, node, item, index, event, opts) {
                                var listStore = Ext.ComponentQuery.query('#hdfsBrowserListGrid')[0].getStore();
                                listStore.load({scope: this, params: {'node': node.data.id}});
                            }
                        }
                    }
                ]
            },
            {
                title: msg.hdfs_title_file,
                region: 'center',
                layout: 'fit',
                items: [
                    {
                        itemId: 'hdfsBrowserListGrid',
                        xtype: 'grid',
                        store: 'HdfsBrowserList',
                        stripeRows: true,
                        columnLines: true,
                        columns: [
                            {text: msg.hdfs_grid_filename, flex: 1, dataIndex: 'name'},
                            {
                                text: msg.hdfs_grid_filesize,
                                width: 100,
                                sortable: true,
                                dataIndex: 'length',
                                align: 'right'
                            },
                            {text: msg.hdfs_grid_timestamp, width: 150, dataIndex: 'modificationTime', align: 'center'},
                            {
                                text: msg.hdfs_grid_permission,
                                width: 80,
                                dataIndex: 'permission',
                                align: 'center',
                                hidden: true
                            },
                            {text: msg.hdfs_grid_group, width: 60, dataIndex: 'group', align: 'center', hidden: true},
                            {text: msg.hdfs_grid_owner, width: 60, dataIndex: 'owner', align: 'center', hidden: true},
                            {
                                text: msg.hdfs_grid_replica,
                                width: 40,
                                dataIndex: 'replication',
                                align: 'center',
                                hidden: true
                            },
                            {
                                text: msg.hdfs_grid_block_size,
                                width: 80,
                                dataIndex: 'blocksize',
                                align: 'center',
                                hidden: true
                            }
                        ]
                    }
                ],
                dockedItems: [
                    {
                        xtype: 'toolbar',
                        items: [
                            '->',
                            {
                                itemId: 'refreshButton',
                                iconCls: 'common_refresh',
                                tooltip: message.msg('fs.hdfs.tip_file_refresh'),
                                text: msg.button_refresh
                            }
                        ]
                    }
                ]
            }
        ];
        this.callParent(arguments);

        // HDFS Progress Bar를 초기화 한다.
        var hdfsSizeProgressBar = Ext.ComponentQuery.query('#hdfsSize')[0];
        invokeGet(CONSTANTS.REST_HDFS_FS_STATUS, {},
            function (response) {
                if (config.hdfs_browser_show_wait != 'false') Ext.MessageBox.hide();
                var obj = Ext.decode(response.responseText);
                if (obj.success) {
                    hdfsSizeProgressBar.updateProgress(obj.map['humanProgress'], obj.map['humanProgressPercent'], true);
                } else {
                    hdfsSizeProgressBar.updateProgress(0, message.msg('fs.hdfs.unknown'), true);
                }
            },
            function (response) {
                hdfsSizeProgressBar.updateProgress(0, message.msg('fs.hdfs.unknown'), true);
            }
        );
    }
});