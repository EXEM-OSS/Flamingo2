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
Ext.define('Flamingo2.view.designer.property.HADOOP_MR', {
    extend: 'Flamingo2.view.designer.property._NODE_HADOOP',
    alias: 'widget.HADOOP_MR',

    requires: [
        'Flamingo2.view.designer.property.browser.hdfs.BrowserPanel',
        'Flamingo2.view.designer.property._JarBrowserField',
        'Flamingo2.view.designer.property._JarGrid',
        'Flamingo2.view.designer.property._InputGrid',
        'Flamingo2.view.designer.property._BrowserField',
        'Flamingo2.view.designer.property._KeyValueGrid',
        'Flamingo2.view.designer.property._CommandlineGrid'
    ],

    width: 600,
    height: 300,

    items: [
        {
            xtype: 'form',
            title: message.msg('workflow.common.mapreduce'),
            defaults: {
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'displayfield',
                    value: message.msg('workflow.he.mr.mr.display')
                },
                {
                    xtype: '_jarBrowserField'
                },
                {
                    xtype: 'textfield',
                    name: 'driver',
                    fieldLabel: message.msg('workflow.common.mapreduce.driver'),
                    emptyText: message.msg('workflow.common.mapreduce.driver.empty.text'),
                    allowBlank: false
                },
                {
                    xtype: '_jarGrid',
                    title: message.msg('workflow.common.mapreduce.jar.title'),
                    flex: 1
                }
            ]
        },
        {
            title: message.msg('workflow.common.inout.path'),
            xtype: 'form',
            defaults: {
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: '_inputGrid',
                    title: message.msg('workflow.common.input.path'),
                    flex: 1
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.common.output.path'),
                    defaults: {
                        hideLabel: true,
                        margin: "5 0 0 0"  // Same as CSS ordering (top, right, bottom, left)
                    },
                    layout: 'hbox',
                    items: [
                        {
                            xtype: '_browserField',
                            name: 'output',
                            allowBlank: false,
                            readOnly: false,
                            flex: 1
                        }
                    ]
                }
            ]
        },
        {
            title: message.msg('workflow.common.hadoop.conf'),
            xtype: 'form',
            defaults: {
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: message.msg('workflow.common.hadoop.conf.guide')
                },
                {
                    xtype: '_keyValueGrid',
                    flex: 1
                }
            ]
        },
        {
            title: message.msg('workflow.common.command'),
            xtype: 'form',
            defaults: {
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'displayfield',
                    height: 50,
                    value: message.msg('workflow.common.command.guide')
                },
                {
                    xtype: '_commandlineGrid',
                    flex: 1
                }
            ]
        },
        {
            title: message.msg('common.references'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="https://developer.yahoo.com/hadoop/tutorial/" target="_blank">Yahoo! Hadoop Tutorial</a>'
                }
            ]
        }
    ]
});