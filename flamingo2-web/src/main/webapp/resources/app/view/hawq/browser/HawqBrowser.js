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
/**
 * HAWQ > Browser
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.Hawq > hawqBrowser
 * @see Flamingo2.view.hawq.browser.HawqBrowserController
 * @see Flamingo2.view.hawq.browser.HawqBrowserModel
 */
Ext.define('Flamingo2.view.hawq.browser.HawqBrowser', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.hawqBrowser',

    controller: 'hawqBrowserController',
    viewModel: {
        type: 'hawqBrowserModel'
    },

    border: true,
    split: true,
    layout: 'border',

    listeners: {
        afterrender: 'hawqBrowserAfterrender'
    },

    items: [
        {
            xtype: 'panel',
            region: 'north',
            title: message.msg('hawq.title.connection'),
            layout: 'anchor',
            tools: [
                {
                    type: 'refresh',
                    callback: 'hawqConnectionRefresh'
                }
            ],
            items: [
                {
                    xtype: 'fieldcontainer',
                    padding: '5 0 0 5',
                    anchor: '100%',
                    layout: 'anchor',
                    items: [
                        {
                            xtype: 'fieldcontainer',
                            layout: {
                                type: 'hbox',
                                pack: 'center'
                            },
                            items: [
                                {
                                    xtype: 'combobox',
                                    fieldLabel: message.msg('hawq.database'),
                                    labelAlign: 'right',
                                    labelWidth: 90,
                                    itemId: 'hawqDatabaseCombobox',
                                    reference: 'hawqDatabaseCombobox',
                                    bind: {
                                        store: '{hawqDatabase}'
                                    },
                                    displayField: 'databaseName',
                                    valueField: 'databaseName',
                                    editable: false,
                                    listeners: {
                                        select: 'hawqDatabaseComboboxSelect'
                                    }
                                },
                                {
                                    xtype: 'splitbutton',
                                    text: message.msg('hawq.menu'),
                                    margin: '0 0 0 5',
                                    handler: 'hawqConnectionManagerConfigShowMenu',
                                    menu: {
                                        items: [
                                            {
                                                text: message.msg('hawq.button.database.refresh'),
                                                handler: 'hawqConnectionManagerDatabaseRefresh',
                                                iconCls: 'common-refresh'
                                            },
                                            '-',
                                            {
                                                text: message.msg('hawq.button.database.create'),
                                                handler: 'hawqConnectionManagerDatabaseCreate',
                                                iconCls: 'common-database-add'
                                            },
                                            {
                                                text: message.msg('hawq.button.database.drop'),
                                                handler: 'hawqConnectionManagerDatabaseDrop',
                                                iconCls: 'common-database-remove'
                                            }
                                        ]
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            layout: {
                                type: 'hbox',
                                pack: 'center'
                            },
                            items: [
                                {
                                    xtype: 'combobox',
                                    fieldLabel: message.msg('hawq.schema'),
                                    labelAlign: 'right',
                                    labelWidth: 90,
                                    itemId: 'hawqSchemaCombobox',
                                    reference: 'hawqSchemaCombobox',
                                    bind: {
                                        store: '{hawqSchema}'
                                    },
                                    displayField: 'schemaName',
                                    valueField: 'schemaName',
                                    editable: false,
                                    listeners: {
                                        select: 'hawqSchemaComboboxSelect'
                                    }
                                },
                                {
                                    xtype: 'splitbutton',
                                    text: message.msg('hawq.menu'),
                                    margin: '0 0 0 5',
                                    handler: 'hawqConnectionManagerConfigShowMenu',
                                    menu: {
                                        items: [
                                            {
                                                text: message.msg('hawq.button.schema.refresh'),
                                                handler: 'hawqConnectionManagerSchemaRefresh',
                                                iconCls: 'common-refresh'
                                            },
                                            '-',
                                            {
                                                text: message.msg('hawq.button.schema.create'),
                                                handler: 'hawqConnectionManagerSchemaCreate',
                                                iconCls: 'common-schema-add'
                                            },
                                            {
                                                text: message.msg('hawq.button.schema.drop'),
                                                handler: 'hawqConnectionManagerSchemaDrop',
                                                iconCls: 'common-schema-remove'
                                            }
                                        ]
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'tabpanel',
            title: message.msg('hawq.title.explorer'),
            reference: 'objectExplorerTabpanel',
            region: 'center',
            split: true,
            flex: 1,
            plain: false,
            activeTab: 0,
            tools: [
                {
                    type: 'refresh',
                    callback: 'refreshObjectExplorer'
                }
            ],
            items: [
                {
                    xtype: 'grid',
                    title: message.msg('hawq.table'),
                    reference: 'hawqTablesGrid',
                    bind: {
                        store: '{hawqTable}'
                    },
                    viewConfig: {
                        stripeRows: true,
                        columnLines: true,
                        enableTextSelection: true
                    },
                    hideHeaders: true,
                    columns: [
                        {dataIndex: 'table_name', flex: 1},
                        {
                            dataIndex: 'table_size', width: 80, align: 'right',
                            renderer: 'hawqTableSizeRenderer'
                        }
                    ],
                    listeners: {
                        itemclick: 'hawqObjectExplorerGridItemclick',
                        containercontextmenu: 'onTableContainercontextmenu',
                        rowcontextmenu: 'onTableRowcontextmenu'
                    }
                },
                {
                    xtype: 'grid',
                    title: message.msg('hawq.view'),
                    reference: 'hawqViewsGrid',
                    bind: {
                        store: '{hawqView}'
                    },
                    viewConfig: {
                        stripeRows: true,
                        columnLines: true,
                        enableTextSelection: true
                    },
                    hideHeaders: true,
                    columns: [
                        {dataIndex: 'table_name', flex: 1}
                    ],
                    listeners: {
                        itemclick: 'hawqObjectExplorerGridItemclick',
                        containercontextmenu: 'onViewContainercontextmenu',
                        rowcontextmenu: 'onViewRowcontextmenu'
                    }
                },
                {
                    xtype: 'grid',
                    title: message.msg('hawq.externaltable'),
                    reference: 'hawqExternalTablesGrid',
                    bind: {
                        store: '{hawqExternalTable}'
                    },
                    viewConfig: {
                        stripeRows: true,
                        columnLines: true,
                        enableTextSelection: true
                    },
                    hideHeaders: true,
                    columns: [
                        {dataIndex: 'table_name', flex: 1}
                    ],
                    listeners: {
                        itemclick: 'hawqObjectExplorerGridItemclick',
                        containercontextmenu: 'onExternalContainercontextmenu',
                        rowcontextmenu: 'onExternalRowcontextmenu'
                    }
                },
                {
                    xtype: 'grid',
                    title: message.msg('hawq.function'),
                    reference: 'hawqFunctionsGrid',
                    bind: {
                        store: '{hawqFunction}'
                    },
                    viewConfig: {
                        stripeRows: true,
                        columnLines: true,
                        enableTextSelection: true
                    },
                    hideHeaders: true,
                    columns: [
                        {dataIndex: 'procdesc', flex: 1}
                    ],
                    listeners: {
                        itemclick: 'hawqObjectExplorerGridItemclick',
                        containercontextmenu: 'onFunctionContainercontextmenu',
                        rowcontextmenu: 'onFunctionRowcontextmenu'
                    }
                }
            ]
        },
        {
            xtype: 'tabpanel',
            title: message.msg('hawq.title.tableinfo'),
            region: 'south',
            split: true,
            flex: 1,
            plain: true,
            activeTab: 0,
            items: [
                {
                    xtype: 'grid',
                    title: message.msg('hawq.column'),
                    reference: 'hawqColumnsGrid',
                    bind: {
                        store: '{hawqColumn}'
                    },
                    viewConfig: {
                        stripeRows: true,
                        columnLines: true,
                        enableTextSelection: true
                    },
                    columns: [
                        {text: message.msg('hawq.column.name'), dataIndex: 'column_name', flex: 3},
                        {text: message.msg('hawq.datatype'), dataIndex: 'data_type', flex: 1},
                        {text: message.msg('hawq.null'), dataIndex: 'is_nullable', flex: .5}
                    ],
                    listeners: {
                        itemdblclick: 'hawqTableInformationsGridItemdblclick'
                    }
                },
                {
                    xtype: 'grid',
                    title: message.msg('hawq.title.metadata'),
                    reference: 'hawqObjectMetadatasGrid',
                    hideHeaders: true,
                    bind: {
                        store: '{hawqObjectMetadata}'
                    },
                    viewConfig: {
                        stripeRows: true,
                        columnLines: true,
                        enableTextSelection: true
                    },
                    columns: [
                        {dataIndex: 'key', flex: 1},
                        {dataIndex: 'value', flex: 1}
                    ]
                },
                {
                    xtype: 'treepanel',
                    title: message.msg('hawq.partition'),
                    reference: 'hawqPartitionTree',
                    useArrows: true,
                    store: {
                        type: 'tree',
                        autoLoad: false,
                        nodeParam: 'oid',
                        rootVisible: true,
                        root: {
                            text: 'Partitions',
                            id: 'root',
                            oid: 0,
                            expanded: false
                        },

                        fields: [
                            'id', 'oid', 'nspname', 'relname', 'table_name', 'text',
                            'relhassubclass', 'leaf', 'parname', 'table_type', 'object_type', 'relstorage'
                        ],
                        proxy: {
                            type: 'ajax',
                            url: CONSTANTS.HAWQ.BROWSER.PARTITIONS_TREE,
                            method: 'GET',
                            extraParams: {
                                clusterName: ENGINE.id
                            },
                            headers: {
                                'Accept': 'application/json'
                            },
                            reader: {
                                type: 'json',
                                rootProperty: 'list'
                            }
                        }
                    },
                    columns: [
                        {
                            text: message.msg('hawq.table.name'),
                            dataIndex: 'text',
                            width: 240,
                            sortable: false,
                            xtype: 'treecolumn'
                        },
                        {text: message.msg('hawq.partition.name'), dataIndex: 'parname', flex: 1, align: 'center'}
                    ],
                    listeners: {
                        beforeload: 'hawqPartitionTreeBeforeload',
                        itemclick: 'hawqObjectExplorerGridItemclick',
                        itemcontextmenu: 'hawqPartitionTreeItemcontextmenu',
                        containercontextmenu: 'hawqPartitionTreeContainercontextmenu'
                    }
                },
                {
                    xtype: 'form',
                    title: message.msg('hawq.title.showcreate'),
                    reference: 'hawqTableDefForm',
                    layout: 'fit',
                    items: [
                        {
                            xtype: 'textareafield',
                            name: 'def',
                            editable: false,
                            readOnly: true
                        }
                    ]
                }
            ]
        }
    ]
});