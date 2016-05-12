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
Ext.define('Flamingo2.view.terminal.Terminals', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.terminals',

    requires: [
        'Flamingo2.view.terminal.TerminalController',
        'Flamingo2.view.terminal.TerminalModel',
        'Flamingo2.view.terminal.TerminalLogin'
    ],

    viewModel: {
        type: 'terminalModel'
    },

    controller: 'terminalController',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    height: 605,

    items: [
        {
            xtype: 'tabpanel',
            layout: 'fit',
            margin: '5 0 0 0',

            cols: 150,

            rows: 26,

            language: 'kor',

            plugins: [
                Ext.create('Flamingo2.view.terminal.TabCloseMenu')
            ],
            flex: 1,
            listeners: {
                resize: 'onCenterResize',
                tabchange: 'onCenterTabchange'
            },
            tabBar: {
                items: [
                    {
                        xtype: 'tbspacer',
                        width: 20
                    },
                    {
                        xtype: 'button',
                        iconCls: 'common-view',
                        text: message.msg('terminal.tbar.button.new_terminal'),
                        handler: 'onAddTerminalBtnClick'
                    }
                ]
            },
            items: [
                {
                    title: message.msg('terminal.tbar.label.usage'),
                    layout: 'fit',
                    closable: false,
                    region: 'center',
                    html: '<div class="start-div"><div style="float:left; margin: 0 0 0 0; padding 0 0 0 0;" ><!--<img src="images/layout-icon.gif" />--></div><div style="display: block; margin-left:40px; margin-top:20px;"> <!--<h2>Remote Terminal</h2>--><div><p>' + message.msg('terminal.tbar.label.usage') + '</p><p><b>' + message.msg('terminal.tbar.label.usage.1') + '</p><p><b>' + message.msg('terminal.tbar.label.usage.2') + '</b></p> <p><p>' + message.msg('terminal.tbar.label.usage.3') + '</p></p></div>',
                    border: true,
                    margins: '5 0 5 0'
                }
            ]
        }
    ]
});
