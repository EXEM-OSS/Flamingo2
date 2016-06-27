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
Ext.define('Flamingo2.view.uima.grid.UimaManagement', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.uimaManagementGrid',

    bind: {
        store: '{uimaGridStore}'
    },

    columns: [
        {
            xtype: 'rownumberer',
            width: 40,
            sortable: false
        },
        {
            text: '프로세스 ID',
            dataIndex: 'processId',
            width: 80,
            align: 'center'
        },
        {
            text: '프로세스 유형',
            dataIndex: 'processType',
            width: 90,
            align: 'center'
        },
        {
            text: '로깅레벨', dataIndex: 'loggingLevel', width: 80, align: 'center', sortable: true
        },
        {
            text: '콜렉션 리더', dataIndex: 'collectionReader', flex: 0.5, align: 'center'
        },
        {
            text: 'IP', dataIndex: 'ip', flex: 0.5, align: 'center', sortable: true
        },
        {
            text: 'Annotator 유형', dataIndex: 'annotatorType', width: 120, align: 'center', sortable: true
        },
        {
            text: '데이터', dataIndex: 'data', flex: 1.5, align: 'center', sortable: true
        },
        {
            text: '날짜', dataIndex: 'logDate', width: 140, align: 'center', sortable: true/*,
            renderer: function (value, metaData, record, row, col, store, gridView) {
                return dateFormat2(value);
            }*/
        }
    ],
    viewConfig: {
        emptyText: 'Uima Log 정보 없음',
        enableTextSelection: true,
        deferEmptyText: false,
        columnLines: true,
        stripeRows: true,
        getRowClass: function (b, e, d, c) {
            return 'cell-height-30';
        }
    },
/*    tbar: [
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.regist'),
            iconCls: 'common-add',
            labelWidth: 50,
            listeners: {
                click: 'onApplicationRegisterClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.start'),
            iconCls: 'common-execute',
            labelWidth: 50,
            listeners: {
                click: 'onApplicationStartClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.stop'),
            iconCls: 'common-pause',
            labelWidth: 50,
            listeners: {
                click: 'onApplicationStopClick'
            }
        },
        {
            xtype: 'button',
            formBind: true,
            text: message.msg('common.abort'),
            iconCls: 'common-stop',
            labelWidth: 50,
            listeners: {
                click: 'onApplicationKillClick'
            }
        }
    ],*/
    dockedItems: [
        {
            xtype: 'pagingtoolbar',
            itemId: 'uimaManagementToolbar',
            dock: 'bottom',
            bind: {
                store: '{uimaGridStore}'
            },
            displayInfo: true,
            doRefresh: function () {
                var uimaToolbar = query('uimaManagement #uimaManagementToolbar');
                var uimaGrid = query('uimaManagement');

                uimaToolbar.moveFirst();
                uimaGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            },
            listeners: {
                beforechange: function (toolbar, page, eOpts) {
                    toolbar.getStore().getProxy().extraParams.clusterName = ENGINE.id;
                }
            }
        }
    ],
    tools: [
        {
            type: 'refresh',
            tooltip: message.msg('common.refresh'),
            handler: 'onUimaGridRefreshClick'
        }
    ],
    listeners: {
        //itemclick: 'onUimaGridItemClick',
        afterrender: 'onUimaGridAfterRender'
    }
});