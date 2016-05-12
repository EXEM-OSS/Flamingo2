/*
 * Copyright (C) 2015 Bahamas Project (http://www.cloudine.io).
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
Ext.define('Flamingo2.view.admin.user.UserEvent', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.userevent',

    requires: [
        'Flamingo2.view.admin.user.UserEventController',
        'Flamingo2.view.admin.share.Model'
    ],
    viewModel: {
        type: 'admin.shareModel'
    },

    controller: 'userEventController',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [
        {
            xtype: 'grid',
            itemId: 'usereventGrid',
            reference: 'usereventGrid',
            border: 0,
            viewConfig: {
                stripeRows: true,
                columnLines: true,
                enableTextSelection: true,
                getRowClass: function (b, e, d, c) {
                    return 'cell-height-30';
                }
            },
            bind: {
                store: '{userEvent}'
            },
            columns: [
                {text: message.msg('batch.job_name'), dataIndex: 'name', flex: 1},
                {text: message.msg('common.status'), dataIndex: 'status', width: 120, align: 'center'},
                {
                    text: message.msg('common.detail'), width: 100, align: 'center',
                    renderer: function (val, meta, rec) {
                        var id = Ext.id();
                        Ext.defer(function () {
                            Ext.create('Ext.button.Button', {
                                renderTo: id,
                                text: message.msg('common.view'),
                                iconCls: 'common-view',
                                usage: 'vmproperty-apply',
                                scale: 'small',
                                handler: function () {
                                    var popup = Ext.create('Ext.Window', {
                                        title: message.msg('common.history'),
                                        width: 850,
                                        height: 500,
                                        layout: 'fit',
                                        maximizable: true,
                                        items: {
                                            xtype: 'workflowDetail',
                                            identifier: rec.data.identifier
                                        }
                                    }).center().show();
                                }
                            });
                        }, 50);
                        return Ext.String.format('<div id="{0}"></div>', id);
                    }
                },
                {
                    text: message.msg('common.select'),
                    width: 50,
                    align: 'center',
                    xtype: 'checkcolumn',
                    dataIndex: 'isSee'
                }

            ],
            dockedItems: [
                {
                    xtype: 'pagingtoolbar',
                    bind: {
                        store: '{userEvent}'
                    },
                    dock: 'bottom'
                }
            ],
            listeners: {
                itemclick: function (view, record, item, index, e) {
                }
            }
        }
    ],
    tbar: [
        {
            xtype: 'tbtext',
            text: message.msg('hdfs.audit.tbar.chart.condition')
        },
        {
            xtype: 'combo',
            width: 130,
            itemId: 'conditionKey',
            name: 'conditionKey',
            displayField: 'name',
            valueField: 'value',
            emptyText: '- ' + message.msg('common.select') + ' -',
            editable: true,
            selectOnFocus: true,
            collapsible: false,
            store: Ext.create('Ext.data.Store', {
                fields: ['name', 'value'],
                data: [
                    {name: message.msg('batch.job_name'), value: 'NAME'}
                ]
            }),
            listeners: {
                change: function (combo, newValue, oldValue, eOpts) {
                    var store = query('userevent #usereventGrid').getStore();
                    store.getProxy().extraParams.conditionKey = newValue;
                }
            }
        },
        {
            xtype: 'textfield',
            width: 130,
            emptyText: '',
            name: 'conditionValue',
            itemId: 'conditionValue'
        },
        {
            xtype: 'button',
            itemId: 'findButton',
            formBind: true,
            text: message.msg('common.retrieve'),
            iconCls: 'common-find',
            labelWidth: 50,
            handler: function (button) {
                var store = query('userevent #usereventGrid').getStore();
                var conditionKey = store.getProxy().extraParams.conditionKey;
                var conditionValue = query('userevent #conditionValue').getValue().trim();
                store.getProxy().extraParams.conditionValue = conditionValue;

                if (conditionKey && conditionValue) {
                    store.getProxy().extraParams = {
                        conditionKey: conditionKey,
                        conditionValue: conditionValue
                    };
                    store.loadPage(1);
                } else if (!conditionKey || !conditionValue) {
                    store.getProxy().extraParams = {};
                    store.loadPage(1);
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.error'),
                        message: message.msg('common.msg.enter_condition'),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING,
                        fn: function (e) {
                            return false;
                        }
                    });
                }
            }
        },
        {
            xtype: 'button',
            itemId: 'clearButton',
            formBind: true,
            text: message.msg('system.user.common.clear'),
            iconCls: 'common-find-clear',
            labelWidth: 50,
            handler: function () {
                var store = query('userevent #usereventGrid').getStore();
                store.getProxy().extraParams = {};
                query('userevent #conditionKey').clearValue();
                query('userevent #conditionValue').setValue('');
            }
        },
        {
            xtype: 'button',
            itemId: 'deleteButton',
            formBind: true,
            text: message.msg('common.delete'),
            iconCls: 'common-delete',
            labelWidth: 50,
            handler: function () {
                var identifiers = [];
                var store = query('userevent #usereventGrid').getStore();
                Ext.each(store.data.items, function (item) {
                    if (item.data.isSee) {
                        identifiers.push(item.data.identifier);
                    }
                });
                if (identifiers.length > 0) {
                    invokePostByMap(CONSTANTS.USER.PREFERENCE.EVENT.SAW,
                        {identifiers: identifiers},
                        function (response) {
                            store.load();
                        },
                        function (response) {
                            store.load();
                        }
                    );
                }
            }
        }
    ],

    listeners: {
        afterrender: function () {
            var store = this.getViewModel().getStore('userEvent');
            store.getProxy().extraParams.conditionKey = '';
            store.getProxy().extraParams.conditionValue = '';
            store.load();
        }
    }

});