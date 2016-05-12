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
Ext.define('Flamingo2.view.component.WebSocketPorxy', {
    extend: 'Ext.ux.data.WebSocket',
    alias: 'widget.alertSocket',
    requires: [
        'Flamingo2.view.component.WebSocketProxyController'
    ],
    controller: 'websocketController',

    url: CONSTANTS.SYSTEM.WEBSOCKET,
    userPrefix: '/user',
    brokerPrefix: '/topic',
    destinationPrefix: '/socketapp',
    subscribes: [
        '/user/topic/hive',
        '/user/topic/pig',
        '/user/topic/hawqPid',
        '/user/topic/hawqQuery',
        '/user/topic/hawqMessage',
        '/user/topic/workflow',
        '/topic/alarm'
        //Broadcast로 메시지를 받을때는 UserPrefix를 사용하지 않는다.
    ],
    listeners: {
        connected: 'onConnected',
        connectionLost: 'onConnectionLost',
        subscribe: 'onSubscribe'
    }
});