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
Ext.define('Flamingo2.view.component.WebSocketProxyController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.websocketController',

    onConnected: function (socket) {

    },

    onSubscribe: function (socket, message) {
        var me = this;

        switch (message.headers.destination) {
            case '/user/topic/hive':
                me.fireEvent('hiveLogMessage', message);
                break;
            case '/user/topic/pig':
                me.fireEvent('pigLogMessage', message);
                break;
            case '/user/topic/hawqPid':
                me.fireEvent('hawqPidMessage', message);
                break;
            case '/user/topic/hawqQuery':
                me.fireEvent('hawqQueryMessage', message);
                break;
            case '/user/topic/hawqMessage':
                me.fireEvent('hawqMessage', message);
                break;
            case '/user/topic/workflow':
                me.fireEvent('workflowMessage', message);
                break;
            case '/topic/alarm':
                me.fireEvent('alarmMessage', message);
                break;
            default :
                Ext.MessageBox.show({
                    title: message.msg('common.confirm'),
                    message: message.msg('component.msg.not_defined_destination') + '<br>' + message.headers.destination,
                    icon: Ext.MessageBox.INFO,
                    buttons: Ext.MessageBox.OK
                });
                break;
        }
    },

    onConnectionLost: function (socket) {
        var me = this;
        error(message.msg('main.connection_lost'), message.msg('main.msg.connection_lost'));

        me.reconnect(0);
    },

    reconnect: function (cnt) {
        var me = this;
        if (me.getView().getConnected()) {
            return;
        }

        if (cnt >= 3) {
            error(message.msg('main.connection_lost'), message.msg('main.msg.cannot_connect'));
            return;
        }

        Ext.defer(function () {
            me.getView().reconnect();
            me.reconnect(++cnt);
        }, 3000);
    }
});