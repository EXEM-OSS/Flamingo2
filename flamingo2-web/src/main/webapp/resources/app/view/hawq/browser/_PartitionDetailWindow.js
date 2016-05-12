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
/**
 * HAWQ > Browser > Table Informations > Partition > item double click
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser.HawqBrowser > hawqPartitionTree
 */
Ext.define('Flamingo2.view.hawq.browser._PartitionDetailWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget._hawqPartitionDetailWindow',

    controller: 'hawqBrowserController',

    height: 570,
    width: 350,
    closable: true,
    hideCollapseTool: false,
    title: message.msg('hawq.title.detail.partition'),
    titleCollapse: false,
    modal: true,
    closeAction: 'destroy',
    layout: 'fit',
    autoScroll: true,
    border: false,
    bodyPadding: 10,
    bodyStyle: {
        background: '#fff'
    },

    listeners: {
        afterrender: 'hawqPartitionDetailWindowAfterrender'
    },

    items: [
        {
            xtype: 'form',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            defaults: {
                xtype: 'textfield',
                labelAlign: 'right',
                labelWidth: 130,
                readOnly: true
            },
            items: [
                {
                    fieldLabel: message.msg('hawq.partition.table'),
                    name: 'partitiontablename'
                },
                {
                    fieldLabel: message.msg('hawq.partition.name'),
                    name: 'partitionname'
                },
                {
                    fieldLabel: message.msg('hawq.partition.parenttable'),
                    name: 'parentpartitiontablename'
                },
                {
                    fieldLabel: message.msg('hawq.partition.parentpartition'),
                    name: 'parentpartitionname'
                },
                {
                    fieldLabel: message.msg('hawq.partition.type'),
                    name: 'partitiontype'
                },
                {
                    fieldLabel: message.msg('hawq.partition.level'),
                    name: 'partitionlevel'
                },
                {
                    fieldLabel: message.msg('hawq.partition.rank'),
                    name: 'partitionrank'
                },
                {
                    fieldLabel: message.msg('hawq.partition.position'),
                    name: 'partitionposition'
                },
                {
                    fieldLabel: message.msg('hawq.partition.listvalue'),
                    name: 'partitionlistvalues'
                },
                {
                    fieldLabel: message.msg('hawq.partition.start'),
                    name: 'partitionrangestart'
                },
                {
                    fieldLabel: message.msg('hawq.partition.startinclusive'),
                    name: 'partitionstartinclusive'
                },
                {
                    fieldLabel: message.msg('hawq.partition.end'),
                    name: 'partitionrangeend'
                },
                {
                    fieldLabel: message.msg('hawq.partition.endinclusive'),
                    name: 'partitionendinclusive'
                },
                {
                    fieldLabel: message.msg('hawq.partition.every'),
                    name: 'partitioneveryclause'
                },
                {
                    fieldLabel: message.msg('hawq.partition.default'),
                    name: 'partitionisdefault'
                },
                {
                    fieldLabel: message.msg('hawq.partition.boundary'),
                    name: 'partitionboundary'
                },
                {
                    fieldLabel: message.msg('hawq.partition.parenttablespace'),
                    name: 'parenttablespace'
                },
                {
                    fieldLabel: message.msg('hawq.partition.tablespace'),
                    name: 'partitiontablespace'
                }
            ]
        }
    ],

    buttons: [
        {
            text: message.msg('hawq.button.cancel'),
            iconCls: 'common-cancel',
            handler: 'cancelButtonHandler'
        }
    ]
});