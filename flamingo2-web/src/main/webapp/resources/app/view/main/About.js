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

Ext.define('Flamingo2.view.main.About', {
    extend: 'Ext.window.Window',
    alias: 'widget.about',

    width: 450,
    resizable: true,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('common.about'),
    titleCollapse: false,
    modal: true,
    iconCls: 'fa fa-exclamation-circle',
    closeAction: 'destroy',
    layout: 'fit',

    items: [
        {
            xtype: 'form',
            width: '100%',
            bodyPadding: 10,
            border: false,
            style: {
                background: '#ffffff'
            },
            defaults: {
                xtype: 'displayfield',
                labelWidth: 120,
                labelAlign: 'right'
            },
            items: [
                {
                    fieldLabel: message.msg('about.product'),
                    value: config['application.title']
                },
                {
                    fieldLabel: message.msg('about.version'),
                    value: config['version']
                },
                {
                    fieldLabel: message.msg('about.build.date'),
                    value: config['build.timestamp']
                },
                {
                    fieldLabel: message.msg('about.build.number'),
                    value: config['build.number']
                },
                {
                    fieldLabel: message.msg('about.revision.number'),
                    value: config['revision.number']
                },
                {
                    fieldLabel: message.msg('about.organization'),
                    value: config['organization']
                },
                {
                    fieldLabel: message.msg('about.homepage'),
                    value: '<a href="' + config['homepage'] + '" target="_blank">' + config['homepage'] + '</a>'
                }
            ]
        }
    ]
});