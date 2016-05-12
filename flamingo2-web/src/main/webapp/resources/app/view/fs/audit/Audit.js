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
Ext.define('Flamingo2.view.fs.audit.Audit', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.audit',

    requires: [
        'Flamingo2.view.fs.audit.AuditController',
        'Flamingo2.view.fs.audit.AuditModel',
        'Flamingo2.view.fs.audit.AuditChart',
        'Flamingo2.view.fs.audit.AuditGrid'
    ],

    controller: 'auditController',

    viewModel: {
        type: 'auditModel'
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    bodyPadding: 5,
    flex: 1,
    bodyStyle: {
        background: '#ffffff'
    },
    items: [
        {
            xtype: 'auditChart',
            title: message.msg('hdfs.audit.title.stat'),
            iconCls: 'fa fa-server fa-fw',
            region: 'center',
            height: 250
        },
        {
            xtype: 'auditGrid',
            title: message.msg('hdfs.audit.title.list'),
            iconCls: 'fa fa-server fa-fw',
            region: 'south',
            margin: '5 0',
            minHeight: 420,
            flex: 1,
            border: true
        }
    ]
});