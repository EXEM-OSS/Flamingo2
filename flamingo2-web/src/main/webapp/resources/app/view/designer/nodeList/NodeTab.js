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
Ext.define('Flamingo2.view.designer.nodeList.NodeTab', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.nodeTab',

    requires: [
        'Flamingo2.view.designer.nodeList.NodeList'
    ],

    minTabWidth: 120,

    items: [
        {
            title: message.msg('workflow.common.all'),
            xtype: 'nodeList'
        },
        {
            title: message.msg('workflow.common.hadoop.eco'),
            xtype: 'nodeList',
            type: 'HADOOP'
        },
        {
            title: message.msg('workflow.common.stat'),
            xtype: 'nodeList',
            type: 'STATISTICS'
        },
        {
            title: message.msg('workflow.common.data.process'),
            xtype: 'nodeList',
            type: 'ETL'
        },
        {
            title: message.msg('workflow.common.data.mining'),
            xtype: 'nodeList',
            type: 'MINING'
        },
        {
            title: message.msg('workflow.common.mahaut'),
            xtype: 'nodeList',
            type: 'MAHOUT'
        },
        {
            title: message.msg('workflow.common.inmemory'),
            xtype: 'nodeList',
            type: 'INMEMORY'
        },
        {
            title: message.msg('workflow.common.integration'),
            xtype: 'nodeList',
            type: 'INT'
        },
        {
            title: message.msg('workflow.common.etl'),
            xtype: 'nodeList',
            type: 'OTHERS'
        }
    ]
});