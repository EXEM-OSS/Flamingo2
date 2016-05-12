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
 * Java Property View
 *
 * @class
 * @extends Flamingo2.view.designer.property._NODE_HADOOP
 * @author <a href="mailto:fharenheit@gmail.com">Byoung Gon, Kim</a>
 */
Ext.define('Flamingo2.view.designer.property.HADOOP_JAVA', {
    extend: 'Flamingo2.view.designer.property._NODE_HADOOP',
    alias: 'widget.HADOOP_JAVA',

    requires: [
        'Flamingo2.view.designer.property._JarBrowserField',
        'Flamingo2.view.designer.property._ValueGrid',
        'Flamingo2.view.designer.property._JarGrid'
    ],

    width: 500,
    height: 300,

    items: [
        {
            title: message.msg('workflow.common.java'),
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
                    xtype: '_jarBrowserField',
                    fieldLabel: message.msg('workflow.common.mapreduce.jar'),
                    emptyText: message.msg('workflow.common.mapreduce.jar.title'),
                    allowBlank: false
                },
                {
                    xtype: 'textfield',
                    name: 'driver',
                    fieldLabel: message.msg('workflow.common.mapreduce.driver'),
                    emptyText: message.msg('workflow.common.mapreduce.driver.empty.text'),
                    allowBlank: false
                },
                {
                    xtype: 'textfield',
                    name: 'javaOpts',
                    fieldLabel: message.msg('workflow.he.java.java.jvm'),
                    emptyText: message.msg('workflow.he.java.java.jvmempty'),
                    allowBlank: true
                }
            ]
        },
        {
            title: message.msg('workflow.common.classpath'),
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
                    xtype: '_jarGrid',
                    title: message.msg('workflow.common.dependency.jar'),
                    flex: 1
                }
            ],
            listeners: {
                afterrender: function (form, eOpts) {
                }
            }
        },
        {
            title: message.msg('workflow.common.command'),
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
                    height: 50,
                    value: message.msg('workflow.common.command.guide')
                },
                {
                    xtype: '_valueGrid',
                    flex: 1
                }
            ]
        }
    ]
});