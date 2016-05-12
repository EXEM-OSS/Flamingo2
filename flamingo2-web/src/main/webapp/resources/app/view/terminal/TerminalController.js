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
Ext.define('Flamingo2.view.terminal.TerminalController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.terminalController',

    onPasswordFieldAfterrender: function (field) {
        field.focus();
    },

    onEnterPasswordField: function (f, e) {
        if (e.getKey() == e.ENTER || e.getKey() == e.RETURN) {
            this.onLoginSubmit();
        }
    },
    onAddTerminalBtnClick: function () {
        var panel = query('terminals tabpanel');

        var maxSession = 4;
        try {
            maxSession = parseInt(config['terminal.max.session']);
            if (isNaN(maxSession)) {
                maxSession = 4;
            }
        } catch (e) {
            maxSession = 4;
        }

        if (panel.items.length > maxSession) {
            Ext.MessageBox.show({
                title: message.msg('terminal.title.warning'),
                message: message.msg('terminal.msg.exceeded_win'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        } else {
            Ext.create('Ext.window.Window', {
                layout: 'fit',
                title: message.msg('terminal.title.password'),
                modal: true,
                width: 200,
                items: [
                    {
                        xtype: 'terminalLogin'
                    }
                ]
            }).center().show();
        }
    },

    onCenterResize: function (panel) {
        //term cols 와 width 의 비율은 3 : 20 이다.
        //term row 와 height 의 비율은 3 : 50 이다.
        //if(panel.getActiveTab() && panel.getActiveTab().term){
        //    var termid = panel.getActiveTab().termid;
        //    var width = $('#'+termid).width();
        //    var height = $('#'+termid).height();
        //    var cols = Math.round((width*3)/20);
        //    var rows = Math.floor((height*3)/50);
        //    panel.getActiveTab().term.resize(cols , rows);
        //    var termid = panel.getActiveTab().termid;
        //    this.requestToServerReStyle(termid , cols , rows );
        //}
    },
    onCenterTabchange: function (panel) {

    },
    requestToServerReStyle: function (termid, cols, rows, terminalsocket) {
        terminalsocket.emit('restyle', {
            clientname: SESSION.USERNAME,
            termid: termid,
            cols: cols,
            rows: rows
        });
    },
    onIdentifyChange: function (radio) {
        var me = this;
        if (radio.getValue()) {
            if (radio.inputValue == 'password') {
                me.lookupReference('password').show();
                me.lookupReference('keyfile').hide();
            } else {
                me.lookupReference('password').hide();
                me.lookupReference('keyfile').show();
            }
        }
    },
    onKeyfileChange: function (field, value, eOpts) {
        var file = field.fileInputEl.dom.files[0];
        if (file && file.size > 100000) {
            field.reset();
            Ext.MessageBox.show({
                title: message.msg('terminal.title.warning'),
                message: message.msg('terminal.msg.exceeded'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },
    statusInterval: null,
    statusAnimation: function (command) {
        var me = this;
        if (me.statusInterval)
            clearInterval(me.statusInterval);
        var statusPanel = me.lookupReference('status');
        var contents;
        if (command == 'connecting') {
            contents = message.msg('terminal.status.connecting');
            animate();
        }
        if (command == 'connect_timeout') {
            statusPanel.update(message.msg('terminal.status.timeout'));
            statusPanel.doLayout();
        }
        if (command == 'login') {
            contents = message.msg('terminal.status.login');
            animate();
        }
        if (command == 'login_failed') {
            statusPanel.update(message.msg('terminal.status.login_failed'));
            statusPanel.doLayout();
        }
        if (command == 'login_success') {
            statusPanel.update(message.msg('terminal.status.login_success'));
            statusPanel.doLayout();
        }
        var count = 0;
        var dots = 0;

        function animate() {
            me.statusInterval = setInterval(function () {
                dots++;
                var inner = contents;
                if (dots == 4)
                    dots = 0;
                for (var i = 0; i < dots; i++) {
                    inner = inner + ".";
                }
                statusPanel.update(inner);
                statusPanel.doLayout();
            }, 300);
        }
    },
    onLoginSubmit: function () {
        var me = this;
        var reset = me.lookupReference('reset');
        var submit = me.lookupReference('submit');
        var form = me.getView().getForm();
        if (form.isValid()) {
            reset.disable();
            submit.disable();
            me.statusAnimation('connecting');

            var terminalsocket = io.connect('http://' + config['terminal.server.ip'] + ':' + config['terminal.server.port'],
                {
                    'force new connection': true,
                    'reconnection': false,
                    'timeout': 5000
                }
            );

            terminalsocket.on('connect', function () {
                me.statusAnimation('login');
            });

            terminalsocket.on('login', function (data) {
                if (data == "success") {
                    me.statusAnimation('login_success');
                    me.destroyLoginSocket(terminalsocket);
                    reset.disable();
                    submit.disable();
                    me.startTerminal();
                } else if (data == "failed") {
                    me.statusAnimation('login_failed');
                    me.destroyLoginSocket(terminalsocket);
                }
            });

            terminalsocket.on('disconnect', function () {
                me.statusAnimation('connect_timeout');
                me.destroyLoginSocket(terminalsocket);
            });

            terminalsocket.on('connect_error', function () {
                me.statusAnimation('connect_timeout');
                me.destroyLoginSocket(terminalsocket);
            });

            terminalsocket.on('connect_timeout', function () {
                me.statusAnimation('connect_timeout');
                me.destroyLoginSocket(terminalsocket);
            });

            terminalsocket.emit('login', {
                clientname: SESSION.USERNAME,
                password: form.getValues().password,
                username: SESSION.USERNAME
            });
        }
    },
    destroyLoginSocket: function (terminalsocket) {
        var me = this;
        var reset = me.lookupReference('reset');
        var submit = me.lookupReference('submit');
        reset.enable();
        submit.enable();
        terminalsocket.destroy();
    },
    startTerminal: function () {
        var me = this;
        var panel = query('terminals tabpanel');

        var terminalsocket = io.connect('http://' + config['terminal.server.ip'] + ':' + config['terminal.server.port'],
            {
                'force new connection': true,
                'reconnection': false,
                'timeout': 5000
            }
        );

        terminalsocket.on('create', function (data) {
            if (data.termid) {
                var tabcount = panel.value;
                tabcount = (typeof tabcount == 'undefined' || tabcount == 'undefined') ? 1 : tabcount;
                panel.value = tabcount + 1;

                console.log('tabcount : ' + tabcount);
                var newTab = {
                    title: message.msg('terminal.title.terminal') + ' ' + tabcount,
                    xtype: 'container',
                    border: true,
                    layout: 'fit',
                    height: window.innerHeight - 165 || document.body.clientHeight - 165,
                    forceFit: true,
                    region: 'center',
                    theme: 'eclipse',
                    iconCls: 'common-view',
                    flex: 1,
                    printMargin: true,
                    tabConfig: {
                        tooltip: data.termid
                    },
                    termid: data.termid,
                    html: '<div id="' + data.termid +
                    '" style="width:100%;height:100%;position:relative;overflow-y:auto;overflow-x:auto;z-index:1;background:black"></div>' +
                    '<div id="' + data.termid + 'mini" style="position:absolute;height:20px;right:20px;top:5px;z-index:2;background:white"></div>>',
                    closable: true,
                    term: null,
                    listeners: {
                        afterrender: function (termPanel) {
                            var term = new Terminal({
                                cols: panel.cols,
                                rows: panel.rows,
                                useStyle: true,
                                screenKeys: true,
                                cursorBlink: false
                            });

                            term.on('data', function (termdata) {
                                // 타이핑 입력일 경우 자음 , 모음의 입력이 중단 될 때까지 기다린 후 송출한다.
                                if (panel.language == 'kor') {
                                    if (!termPanel.Hangullist)
                                        termPanel.Hangullist = [];
                                    if (termdata == "\x7f") {
                                        if (termPanel.Hangullist.length > 0) {
                                            termPanel.Hangullist.pop();
                                            if (termPanel.Hangullist.length > 0) {
                                                var assemble = Hangul.assemble(termPanel.Hangullist);
                                                $("#" + data.termid + "mini").html(assemble);
                                            } else {
                                                $("#" + data.termid + "mini").html("");
                                                $("#" + data.termid + "mini").hide();
                                            }
                                        } else {
                                            emitData();
                                        }
                                    }
                                    else if (termdata.length == 1) {
                                        if (Hangul.isConsonant(termdata) || Hangul.isVowel(termdata)) {
                                            termPanel.Hangullist.push(termdata);
                                            var assemble = Hangul.assemble(termPanel.Hangullist);
                                            var isHangul = Hangul.isHangul(assemble);

                                            $("#" + data.termid + "mini").show();
                                            $("#" + data.termid + "mini").html(assemble);
                                        } else {
                                            if (termPanel.Hangullist.length > 0) {
                                                var assemble = Hangul.assemble(termPanel.Hangullist);
                                                //term.write(assemble);
                                                emitData(assemble + termdata);
                                                termPanel.Hangullist = [];
                                                $("#" + data.termid + "mini").html("");
                                                $("#" + data.termid + "mini").hide();
                                            } else {
                                                emitData();
                                            }
                                        }
                                    } else {
                                        emitData();
                                    }
                                } else {
                                    emitData();
                                }
                                function emitData(value) {
                                    terminalsocket.emit('data', {
                                        clientname: SESSION.USERNAME,
                                        termdata: value ? value : termdata,
                                        termid: data.termid
                                    });
                                }
                            });

                            term.open(document.getElementById(data.termid));

                            termPanel.term = term;

                            terminalsocket.emit('data', {
                                clientname: SESSION.USERNAME,
                                termdata: '\n',
                                termid: data.termid
                            });

                            if (panel.items.items.length == 1) {
                                panel.setActiveTab(termPanel);
                            }

                            setTimeout(function () {
                                Ext.ComponentQuery.query('terminals')[0].getController().requestToServerReStyle(data.termid, panel.cols, panel.rows, terminalsocket);
                            }, 300);
                        },
                        beforedestroy: function (termPanel) {
                            if (termPanel.term) {
                                terminalsocket.emit('delete', {
                                    termid: data.termid
                                });
                                termPanel.term.destroy();
                            }
                        }
                    }
                };

                panel.add(newTab).show();

                query('terminalLogin').up('window').close();
            }
            if (data == "failed") {
                Ext.MessageBox.show({
                    title: message.msg('terminal.title.dont_create'),
                    message: message.msg('terminal.msg.dont_create'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
                var me = this;
                var reset = me.lookupReference('reset');
                var submit = me.lookupReference('submit');
                reset.enable();
                submit.enable();
            }
        });

        terminalsocket.on('data', function (data) {
            var termid = data.termid;
            Ext.each(panel.items.items, function (container) {
                if (container.termid && container.termid == termid) {
                    if (container.term) {
                        container.term.write(data.termdata);
                    }
                }
            });
        });

        terminalsocket.on('disconnect', function () {
            Ext.MessageBox.show({
                title: message.msg('terminal.title.disconnected'),
                message: message.msg('terminal.msg.disconnected'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            this.destroyTerminalSocket(terminalsocket);
        });

        terminalsocket.on('connect_error', function () {
            Ext.MessageBox.show({
                title: message.msg('terminal.title.connect_failed'),
                message: message.msg('terminal.msg.connect_failed'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        });

        terminalsocket.on('connect_timeout', function () {
            Ext.MessageBox.show({
                title: message.msg('terminal.title.timeout'),
                message: message.msg('terminal.msg.timeout'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        });

        terminalsocket.emit('create', {
            clientname: SESSION.USERNAME,
            username: SESSION.USERNAME
        });
    },
    onBeforedestroy: function (tabpanel, eOpts) {
        //FIXME 패널이 죽으면 disconnect가 되므로 자동으로 소켓이 죽을 것이다.
    },

    destroyTerminalSocket: function (terminalsocket) {
        terminalsocket.destroy();
    }
});