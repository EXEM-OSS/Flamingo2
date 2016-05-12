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

    onAfterrender: function () {
        Ext.defer(function () {
            invokeGet('/system/license/valid', {
                    clusterName: ENGINE.id
                },
                function (response) {
                    var res = Ext.decode(response.responseText);
                    if (res.success) {
                        if (!res.map.isValid) {
                            Ext.Msg.alert(message.msg('common.warn'), '하둡 노드가 라이센스 승인된 노드 수 보다 많습니다.<br>라이센스를 갱신해야 합니다.<br>라이센스 노드 수: ' + res.map.maxNode, function () {
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
                function (response) {
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

    onNotiAfterrender: function () {
        var me = this;
        Ext.defer(function () {
            invokeGet('/monitoring/alarm/getAlarm', {
                    clusterName: ENGINE.id
                },
                function (response) {
                    var res = Ext.decode(response.responseText);
                    if (res.success) {
                        me.makeAlarm(res.map);
                    } else {

                    }
                },
                function (response) {
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

    send: function (command, param) {
        param.command = command;
        param.username = SESSION.USERNAME;
        this.ws.send(JSON.stringify(param));
    },

    onMenuViewready: function (view) {
        var me = this;

        Ext.create('Flamingo2.view.component.Notification', {
            renderTo: 'notification-grid',
            width: 270,
            listeners: {
                afterrender: 'onNotiAfterrender'
            }
        });

        /*if (toBoolean(config['notification.autoupadate'])) {
         setInterval(function () {
         Flamingo2.progress.update();
         }, parseInt(config['notification.update']));
         }*/
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
     * **/
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
        me.mainPageLoad();
    },

    /**
     * MenuStore Load 이벤트 처리
     * */
    onMenuStoreLoad: function (store, records, success, eOpts) {
        var me = this;
        me.getViewModel().setData({menuLoaded: true});
        me.mainPageLoad();
    },

    /**
     * 메인 페이지 로드
     * */
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
                        title: 'Flamingo License',
                        html: format('Flamingo License 기간이 {0}일 남았습니다.', LICENSE.DAYS),
                        autoClose: false,
                        align: 't',
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
        var refs = this.getReferences();
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
        DATANODE: '데이터노드 장애 발생',
        NODEMANAGER: '노드메니저 장애 발생'
    }
});
