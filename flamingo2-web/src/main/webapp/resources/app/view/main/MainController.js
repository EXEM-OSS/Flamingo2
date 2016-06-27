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
Ext.define('Flamingo2.view.main.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.main',

    requires: [
        'Ext.window.MessageBox',
        'Flamingo2.view.component._HelpPopup'
    ],

    listen: {
        controller: {
            websocketController: {
                alarmMessage: 'onAlarmMessage'
            }
        }
    },

    onAfterRender: function () {
        Ext.defer(function () {
            var url = CONSTANTS.SYSTEM.LICENSE.VALID;
            var param = {
                clusterName: ENGINE.id
            };

            invokeGet(url, param,
                function (response) {
                    var res = Ext.decode(response.responseText);
                    if (res.success) {
                        if (!res.map.isValid) {
                            Ext.Msg.alert(message.msg('common.warn'), message.msg('license.msg.node_warn') + res.map.maxNode, function () {
                                window.location.href = CONSTANTS.USER.LOGOUT;
                            });
                        }
                    } else {
                        Ext.MessageBox.show({
                            title: message.msg('common.warn'),
                            message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    }
                },
                function () {
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            );
        }, 5000);
    },

    onNotiAfterRender: function () {
        var me = this;
        var url = CONSTANTS.MAIN.NOTIFICATION.GET_ALARM;
        var param = {
            clusterName: ENGINE.id
        };

        Ext.defer(function () {
            invokeGet(url, param,
                function (response) {
                    var res = Ext.decode(response.responseText);
                    if (res.success) {
                        me.makeAlarm(res.map);
                    } else {

                    }
                },
                function () {
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            );
        }, 500);
    },

    onHeaderAfterRender: function () {
        Ext.create('Flamingo2.view.component.Notification', {
            renderTo: 'notification-grid',
            width: 270,
            listeners: {
                afterrender: 'onNotiAfterRender'
            }
        });

        /*if (toBoolean(config['notification.autoupadate'])) {
         setInterval(function () {
         Flamingo2.progress.update();
         }, parseInt(config['notification.update']));
         }*/
    },


    send: function (command, param) {
        param.command = command;
        param.username = SESSION.USERNAME;
        this.ws.send(JSON.stringify(param));
    },

    /**
     * EngineCombo Change 이벤트 처리
     **/
    onEngineComboChange: function (evt, params) {
        var refs = this.getReferences();
        var store = Ext.StoreManager.lookup('mainEngine');
        var row = store.find('id', params.selected);

        ENGINE = store.getAt(row).data;

        var panelItems = refs.pnlCenter.items.items;

        if (panelItems.length > 0) {
            panelItems[0].fireEvent('engineChanged', ENGINE);
        }
    },

    /**
     * Engine Combo Store Load 이벤트 처리
     */
    onEngineStoreLoad: function (store, records, success, eOpts) {
        var me = this;
        var i, element = '';

        if (!success) {
            Ext.MessageBox.show({
                title: message.msg('main.msg_cluster_error'),
                message: message.msg('main.msg.can_not_get_cluster'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return;
        }

        for (i = 0; i < records.length; i++) {
            var record = records[i];
            element += '<option value="' + record.get('id') + '">' + record.get('name') + '</option>';
        }

        $('#engineSelector').remove('option');
        $('#engineSelector').append(element);
        $('.chosen-select').trigger('chosen:updated');
        me.onEngineComboChange(null, {selected: $('.chosen-select').val()});
        me.getViewModel().setData({engineLoaded: true});
        //me.mainPageLoad();
    },

    /**
     * MenuStore Load 이벤트 처리
     */
    onMenuStoreLoad: function (store, records, success, eOpts) {
        var me = this;
        me.getViewModel().setData({menuLoaded: true});
        me.mainPageLoad();
    },

    /**
     * 메인 페이지 로드
     */
    mainPageLoad: function () {
        var me = this;
        var data = me.getViewModel().getData();
        var refs = me.getReferences();

        if (data.engineLoaded && data.menuLoaded) {
            Ext.defer(function () {
                refs.pnlCenter.currentPage = config['start.page'];
                refs.pnlCenter.add(Ext.create(config['start.page']));
                if (LICENSE.DAYS < 15) {
                    Ext.toast({
                        title: 'License',
                        html: format(message.msg('license.msg.remain_license'), LICENSE.DAYS),
                        autoClose: false,
                        iconCls: 'fa fa-info-circle fa-lg',
                        minWidth: 150,
                        align: 't'
                    });
                }
            }, 300);
        }
    },

    onAlarmMessage: function (msg) {
        var me = this;
        var body = Ext.decode(msg.body);

        me.makeAlarm(body);
    },

    makeAlarm: function (msg) {
        var me = this;
        var i, noti = msg[ENGINE.id];

        for (i = 0; i < noti.length; i++) {
            if (noti[i].isAlarm) {
                Flamingo2.notification.merge(noti[i].type, me.alarmMessage[noti[i].type], noti[i].type, noti[i].cnt);
            }
            else {
                Flamingo2.notification.remove(noti[i].type);
            }
        }
    },

    alarmMessage: {
        DATANODE: message.msg('alarm.datanode'),
        NODEMANAGER: message.msg('alarm.nodemanager')
    }
});
