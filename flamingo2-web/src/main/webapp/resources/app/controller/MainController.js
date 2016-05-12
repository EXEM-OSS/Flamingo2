Ext.define('Flamingo2.controller.MainController', {
    extend: 'Ext.app.Controller',

    requires: [
        'Flamingo2.view.component.WebSocketPorxy'
    ],

    init: function () {
        this.control({
            'app-main': {
                afterrender: this.onAfterrender,
                menutoggle: this.onMenutoggle,
                menuItemClick: this.onMenuItemClick,
                helpClick: this.onHelpClick,
                logoutClick: this.onLogoutClick,
                settingClick: this.onSettingClick,
                aboutClick: this.onAboutClick,
                externalClick: this.onExternalClick
            },
            'app-main #pnlWest': {
                viewready: this.onMenuViewready
            }
        });
    },

    refs: [
        {ref: 'appMain', selector: 'app-main'},
        {ref: 'pnlCenter', selector: 'app-main #pnlCenter'},
        {ref: 'pnlWest', selector: 'app-main #pnlWest'}
    ],

    onAfterrender: function (panel) {
        var me = this;

        WEBSOCKET = Ext.create('Flamingo2.view.component.WebSocketPorxy').connect();

        var timer = setInterval(function () {
            var selector = $('.selector');
            if (selector.length > 0) {
                clearInterval(timer);

                // Sidebar 토글버튼 이벤트 처리
                Ext.get('sidebar-collapse').on('click', function () {
                    panel.fireEvent('menutoggle', this);
                });

                // 도움말
                Ext.get('help').on('click', function () {
                    panel.fireEvent('helpClick', this);
                });

                // 설정
                Ext.get('setting').on('click', function () {
                    panel.fireEvent('settingClick', this);
                });

                // 로그아웃
                Ext.get('logout').on('click', function () {
                    panel.fireEvent('logoutClick', this);
                });

                // 로그아웃
                Ext.get('about').on('click', function () {
                    panel.fireEvent('aboutClick', this);
                });

                // External
                Ext.get('external').on('click', function () {
                    panel.fireEvent('externalClick', this);
                });

            }
        }, 300);

        $('.chosen-select').chosen({disable_search_threshold: 10, width: '100%'});

        $('.chosen-select').on('change', function (evt, params) {
            me.onEngineComboChange(evt, params);
        });

        Ext.StoreManager.lookup('mainEngine').load();
    },

    /**
     * EngineCombo Change 이벤트 처리
     *
     * 엔진이 변경 되면 화면을 리소스관리자로 변경한다.
     */
    onEngineComboChange: function (evt, params) {
        var me = this;
        var store = Ext.StoreManager.lookup('mainEngine');
        var row = store.find('id', params.selected);

        Ext.MessageBox.show({
            title: message.msg('main.title.engine'),
            message: message.msg('main.msg.engine'),
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.QUESTION,
            fn: function (btn) {
                if (btn === 'yes') {
                    ENGINE = store.getAt(row).data;

                    me.getPnlCenter().removeAll();
                    me.getPnlCenter().add(Ext.create(config['start.page']));
                } else if (btn === 'no') {
                    return;
                }
            }
        });
    },

    /**
     * 메뉴 토글버튼 이벤트 처리
     */
    onMenutoggle: function () {
        var me = this;

        if ($('#sidebar').hasClass('menu-min')) {
            me.getPnlWest().setWidth(42);
        } else {
            me.getPnlWest().setWidth(220);
        }
    },

    /**
     * 메뉴 클릭이벤트 처리
     */
    onMenuItemClick: function (item) {
        var me = this;
        $('.selector').removeClass('active');
        $('#' + item.id).addClass('active');

        var namespace = 'Flamingo2.view.' + $('#' + item.id).attr('alt');
        var center = me.getPnlCenter();
        if (center.currentPage == namespace) {
            return;
        }
        var panel = Ext.create(namespace);
        center.currentPage = namespace;
        center.removeAll();
        center.add(panel);
    },

    /**
     * Notification 이벤트 처리
     */
    onNotiClick: function (item) {

    },

    /**
     * 사용자 설정 이벤트 처리
     */
    onSettingClick: function () {
        Ext.create('Flamingo2.view.setting.PreferencesWindow').center().show();
    },

    /**
     * Help 이벤트 처리
     */
    onHelpClick: function () {
        Ext.create('Flamingo2.view.component._HelpPopup').center().show();
    },

    /**
     * About 이벤트 처리
     */
    onAboutClick: function () {
        Ext.create('Flamingo2.view.main.About').center().show();
    },

    /**
     * External 이벤트 처리
     */
    onExternalClick: function () {
        if (toBoolean(config['external.enabled'])) {
            var url = config['external.url'];
            var win = window.open(url, '_blank');
            win.focus();
        } else {
            Ext.MessageBox.show({
                title: message.msg('common.warning'),
                message: message.msg('main.msg.external'),
                icon: Ext.MessageBox.INFO,
                width: 300,
                buttons: Ext.MessageBox.OK,
                scope: this
            });
        }
    },

    /**
     * Logout 이벤트 처리
     */
    onLogoutClick: function (item) {
        Ext.MessageBox.show({
            title: message.msg('main.title.logout'),
            message: message.msg('main.msg.logout'),
            icon: Ext.MessageBox.INFO,
            width: 250,
            buttons: Ext.MessageBox.YESNO,
            fn: function (buttonId) {
                switch (buttonId) {
                    case 'no':
                        break;
                    case 'yes':
                        window.location.href = CONSTANTS.USER.LOGOUT;
                        break;
                }
            },
            scope: this
        });
    },

    /**
     * Fires when the View's item elements representing Store items has been rendered. No items will be available for selection until this event fires.
     * Dataview에서 Store의 데이터가 렌더링이 완료되면 발생함.
     * **/
    onMenuViewready: function (view) {
        var me = this;

        var timer = setInterval(function () {
            var selector = $('.selector');
            if (selector.length > 0) {
                clearInterval(timer);

                $('#sidebar').ace_sidebar();

                var menuItem = $('.selector');

                for (var i = 0; i < menuItem.length; i++) {
                    var item = Ext.get(menuItem[i].id);
                    item.on('click', function () {
                        me.getAppMain().fireEvent('menuItemClick', this);
                    });
                }
            }
        }, 100);
    }
});