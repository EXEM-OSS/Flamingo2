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
 * ViewController for Flamingo2.view.system.hawq.queue.HawqRQueue
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.queue.HawqRQueue
 * @see Flamingo2.view.system.hawq.queue._QueueRegistController
 */
Ext.define('Flamingo2.view.system.hawq.queue.HawqRQueueController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hawqRQueueController',

    requires: [
        'Flamingo2.view.system.hawq.queue._QueueRegist'
    ],

    listen: {
        controller: {
            hawqRQueueController: {
                hawqDropResourceQueueClick: 'hawqDropResourceQueueClick'
            },
            queueRegist: {
                queueRefresh: 'onBtnRefreshClick'
            }
        }
    },

    /**
     * 신규등록 버튼 클릭 이벤트 처리
     * */
    onBtnNewClick: function () {
        Ext.create('Flamingo2.view.system.hawq.queue._QueueRegist', {
            modal: true,
            width: 300,
            height: 270,
            resizable: false
        }).show();
    },

    hawqRQueueGridItemclick: function (grid, record, item, index, e, eOpts) {
        var me = this,
            refs = me.getReferences(),
            formPanel = refs.hawqRQueueViewForm;

        formPanel.setLoading(true);

        if (record.get('queueid')) {
            invokeGet(
                CONSTANTS.HAWQ.AUTH.RESOURCE_QUEUE,
                {
                    clusterName: ENGINE.id,
                    oid: record.get('queueid')
                },
                function (response) {
                    var result = Ext.decode(response.responseText);
                    if (result.success) {
                        formPanel.setLoading(false);
                        formPanel.getForm().setValues(result.object);
                    } else {
                        Ext.MessageBox.show({
                            title: message.msg('hawq.title.fail'),
                            message: message.msg('hawq.msg.fail.getrqueue') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING,
                            fn: function () {
                                formPanel.setLoading(false);
                            }
                        });
                    }
                },
                function (response) {
                    Ext.MessageBox.show({
                        title: message.msg('hawq.title.fail'),
                        message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.ERROR,
                        fn: function () {
                            formPanel.setLoading(false);
                        }
                    });
                }
            );
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidrqueue'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqRQueueGridRowcontextmenu: function (grid, record, tr, rowIndex, e, eOpts) {
        e.stopEvent();
        var me = this,
            refs = me.getReferences(),
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.rqueue.drop'),
                        iconCls: 'common-delete',
                        handler: function (item, e) {
                            me.fireEvent('hawqDropResourceQueueClick', grid, refs);
                        }
                    }
                ]
            });
        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    hawqDropResourceQueueClick: function (grid, refs) {
        var record = grid.getSelectionModel().getSelection()[0];
        if (record) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.drop'),
                message: format(message.msg('hawq.msg.question.droprqueue'), record.get('rsqname')),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        invokePostByMap(
                            CONSTANTS.HAWQ.AUTH.DROP_RESOURCE_QUEUE,
                            {
                                clusterName: ENGINE.id,
                                queueName: record.get('rsqname')
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.droprqueue'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            grid.getStore().reload();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.droprqueue') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });
                                }
                            },
                            function (response) {
                                Ext.MessageBox.show({
                                    title: message.msg('hawq.title.fail'),
                                    message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.ERROR
                                });
                            }
                        );
                    }
                }
            });
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.droprqueue'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    onBtnRefreshClick: function () {
        this.getReferences().hawqRQueueGrid.getStore().reload();
    }
});