Ext.define('Ext.ux.data.WebSocket', {
    alias: 'websocket.websocket',

    mixins: [
        'Ext.mixin.Inheritable',
        'Ext.mixin.Bindable',
        'Ext.mixin.Observable'
    ],

    config: {
        /**
         * @cfg {SockJS} SockJS websocket client
         */
        socket: null,

        /**
         * @cfg {Stomp} Stomp text-orientated messaging protocol
         */
        protocol: null,
        /**
         * @cfg {String} Websocket endpoint URL
         */
        url: null,

        /**
         * @cfg {String} simple-broker prefix
         */
        brokerPrefix: null,

        /**
         * @cfg {String} application-destination-prefix
         */
        destinationPrefix: null,

        /**
         * @cfg {String} User destination prefix
         */
        userPrefix: '/user',

        /**
         * @cfg {Boolean} Message boradcast
         */
        broadCast: true,

        /**
         * @cfg {Boolean} Receive messages in the browser
         */
        subscribes: null
    },

    constructor: function (config) {
        var me = this;

        me.isInitializing = true;
        me.mixins.observable.constructor.call(me, config);
        me.isInitializing = false;
    },

    connect: function() {
        var me = this;

        if (Ext.isEmpty(me.getSocket())) {
            me.setSocket(new SockJS(me.getUrl()));
            me.setProtocol(Stomp.over(me.getSocket()));

            me.getProtocol().connect({}, function(frame) {
                if (me.getSubscribes() != null) {
                    var i, subscribes = me.getSubscribes();
                    for (i=0; i<subscribes.length; i++) {
                        me.getProtocol().subscribe(subscribes[i], function(message) {
                            me.fireEvent('subscribe', me, message);
                        });
                    }
                }
            }, function() {
                me.fireEvent('connectionLost', me);
            });
        }

        me.fireEvent('connected', me);

        return me;
    },

    reconnect: function() {
        var me = this;
        me.getProtocol().connect({}, function(frame) {
            if (me.getSubscribes() != null) {
                var i, subscribes = me.getSubscribes();
                for (i=0; i<subscribes.length; i++) {
                    me.getProtocol().subscribe(subscribes[i]);
                }
            }
            me.fireEvent('reconnected', me);
            console.debug(2);
        }, function() {
            me.fireEvent('connectionLost', me);
        });
    },

    getConnected: function() {
        return this.getProtocol().connected;
    },

    disconnect: function() {
        var me = this;

        me.getProtocol().disconnect();

        me.fireEvent('disconnected', me);
    },

    send: function(url, message) {
        var me = this;

        me.getProtocol().send(url, {}, message);
    },

    /**
     * StompJS Subscribe 추가 함수.
     *
     * Server와 Websocket으로 통신할 때 서버쪽에서 수신되는 데이터는 subscribe를 기준으로 데이터를 수신할 수 있음.
     * subscribe를 활용하면 1개의 Connection으로 여러개의 통신을 처리할 수 있으므로 매우 유용함.
     *
     * @param {String} url Subscribe 적용할 url.
     * @param {Object} headers (Optional) subscribe의 ID를 지정할 수 있음. Ex(headers: {id: subId}).
     *                            만약 지정하지 않으면 StompJS에서 임의로 설정됨.
     */
    addSubscribe: function(url, headers) {
        var me = this;

        me.getProtocol().subscribe(url, function(message) {
            me.fireEvent('subscribe', me, message);
        }, headers);
    },

    /**
     * StompJS Subscribe 제거.
     *
     * 추가한 Subscribe를 해제하는 함수
     *
     * @param {String} id 제거할 subscribe의 id.
     */
    removeSubscribe: function(id) {
        var me = this;
        me.getProtocol().unsubscribe(id)
    }
});