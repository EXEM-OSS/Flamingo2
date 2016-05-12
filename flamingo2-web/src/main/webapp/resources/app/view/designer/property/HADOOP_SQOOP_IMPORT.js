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
 * Sqoop Import View
 *
 * @class
 * @extends Flamingo2.view.designer.property.HADOOP_SQOOP_IMPORT
 * @author <a href="mailto:chiwanpark@icloud.com">Chiwan Park</a>
 */
Ext.define('Flamingo2.view.designer.property.HADOOP_SQOOP_IMPORT', {
    extend: 'Flamingo2.view.designer.property._NODE_HADOOP',
    alias: 'widget.HADOOP_SQOOP_IMPORT',

    requires: [
        'Flamingo2.view.designer.property._CommandlineGrid',
        'Flamingo2.view.designer.property._NameValueGrid',
        'Flamingo2.view.designer.property._ValueGrid',
        'Flamingo2.view.designer.property._BrowserField',
        'Flamingo2.view.designer.property._DelimiterSelCmbField',
        'Flamingo2.view.designer.property._EnvironmentGrid'
    ],

    width: 500,
    height: 300,

    items: [
        {
            title: message.msg('workflow.he.sqoop.source.database'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                anchor: '100%',
                labelWidth: 100
            },
            defaultType: 'textfield',
            items: [
                {
                    name: 'jdbcUrl',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.uri')
                },
                {
                    name: 'jdbcDriver',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.driver')
                },
                {
                    name: 'sqoopUsername',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.username')
                },
                {
                    name: 'sqoopPassword',
                    inputType: 'password',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.password')
                },
                {
                    name: 'sqoopTable',
                    fieldLabel: message.msg('workflow.he.sqoop.jdbc.table')
                }
            ]
        },
        {
            xtype: 'form',
            title: message.msg('workflow.he.sqoop.destination.hdfs'),
            border: false,
            autoScroll: true,
            defaults: {
                anchor: '100%',
                labelWidth: 100
            },
            defaultType: 'textfield',
            items: [
                {
                    name: 'output',
                    fieldLabel: message.msg('workflow.he.sqoop.hdfs.path'),
                    xtype: '_browserField'
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
                    value: '<a href="http://sqoop.apache.org/docs/1.4.6/SqoopUserGuide.html" target="_blank">Sqoop User Guide</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://sqoop.apache.org/docs/1.4.6/SqoopDevGuide.html" target="_blank">Sqoop Developer Guide</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://sqoop.apache.org/docs/1.4.6/api/index.html" target="_blank">Sqoop API</a>'
                }
            ]
        }
    ]
});