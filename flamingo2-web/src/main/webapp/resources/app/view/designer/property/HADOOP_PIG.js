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
Ext.define('Flamingo2.view.designer.property.HADOOP_PIG', {
    extend: 'Flamingo2.view.designer.property._NODE_HADOOP',
    alias: 'widget.HADOOP_PIG',

    requires: [
        'Flamingo2.view.designer.property._BrowserField',
        'Flamingo2.view.designer.property._NameValueGrid',
        'Flamingo2.view.designer.property._KeyValueGrid'
    ],

    width: 600,
    height: 400,

    items: [
        {
            title: message.msg('workflow.common.pig'),
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
                    xtype: 'textareafield',
                    name: 'script',
                    flex: 1,
                    padding: '5 5 5 5',
                    layout: 'fit',
                    allowBlank: false,
                    style: {
                        'font-size': '12px'
                    },
                    listeners: {
                        boxready: function (editor, width, height) {
                            var codeMirror = CodeMirror.fromTextArea(editor.getEl().query('textarea')[0], {
                                mode: 'text/x-pig',
                                layout: 'fit',
                                theme: 'mdn-like',
                                indentUnit: 2,
                                lineWrapping: true,
                                lineNumbers: true,
                                matchBrackets: true,
                                smartIndent: true,
                                showModes: false,
                                autofocus: true
                            });

                            codeMirror.on('blur', function () {
                                editor.setValue(codeMirror.getValue());
                            });
                        },
                        resize: function (editor, width, height) {
                            this.getEl().query('.CodeMirror')[0].CodeMirror.setSize('100%', height);
                        }
                    }
                }
            ]
        },
        {
            title: message.msg('workflow.common.script.variable'),
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
                    height: 40,
                    value: message.msg('workflow.common.script.variable.example')
                },
                {
                    xtype: '_nameValueGrid',
                    flex: 1
                }
            ]
        },
        {
            title: message.msg('workflow.common.hadoop.conf'),
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
                    value: message.msg('workflow.common.hadoop.conf.guide')
                },
                {
                    xtype: '_keyValueGrid',
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
                    value: '<a href="http://pig.apache.org/docs/r0.15.0/" target="_blank">Apache Pig</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://pig.apache.org/docs/r0.15.0/start.html" target="_blank">Getting Started</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://pig.apache.org/docs/r0.15.0/basic.html" target="_blank">Pig Latin Basics</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://pig.apache.org/docs/r0.15.0/func.html" target="_blank">Built In Functions</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://pig.apache.org/docs/r0.15.0/udf.html" target="_blank">User Defined Functions</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://hortonworks.com/hadoop-tutorial/using-hive-data-analysis/" target="_blank">Using Hive for Data Analysis</a>'
                }
            ]
        }
    ]
});