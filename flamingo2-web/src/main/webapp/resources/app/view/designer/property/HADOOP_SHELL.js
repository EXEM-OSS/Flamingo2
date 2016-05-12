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
 * Shell Script Property View
 *
 * @class
 * @extends Flamingo2.view.designer.property.HADOOP_SHELL
 * @author <a href="mailto:fharenheit@gmail.com">Byoung Gon, Kim</a>
 */
Ext.define('Flamingo2.view.designer.property.HADOOP_SHELL', {
    extend: 'Flamingo2.view.designer.property._NODE_HADOOP',
    alias: 'widget.HADOOP_SHELL',

    requires: [
        'Flamingo2.view.designer.property._CommandlineGrid',
        'Flamingo2.view.designer.property._NameValueGrid',
        'Flamingo2.view.designer.property._ValueGrid',
        'Flamingo2.view.designer.property._EnvironmentGrid'
    ],

    width: 600,
    height: 350,

    items: [
        {
            title: message.msg('workflow.common.shell'),
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
                    xtype: 'textfield',
                    fieldLabel: message.msg('workflow.common.shell.path'),
                    name: 'path',
                    value: '/bin/bash',
                    editable: false,
                    padding: '2 5 0 5',   // Same as CSS ordering (top, right, bottom, left)
                    allowBlank: false,
                    listeners: {
                        render: function (p) {
                            var theElem = p.getEl();
                            var theTip = Ext.create('Ext.tip.Tip', {
                                html: message.msg('workflow.common.shell.message'),
                                margin: '25 0 0 150',
                                shadow: false
                            });
                            p.getEl().on('mouseover', function () {
                                theTip.showAt(theElem.getX(), theElem.getY());
                            });
                            p.getEl().on('mouseleave', function () {
                                theTip.hide();
                            });
                        }
                    }
                },
                {
                    xtype: 'textfield',
                    fieldLabel: message.msg('workflow.common.shell.workingpath'),
                    name: 'working',
                    value: '',
                    padding: '2 5 0 5',   // Same as CSS ordering (top, right, bottom, left)
                    allowBlank: true,
                    listeners: {
                        render: function (p) {
                            var theElem = p.getEl();
                            var theTip = Ext.create('Ext.tip.Tip', {
                                html: message.msg('workflow.common.shell.workingpath'),
                                margin: '25 0 0 150',
                                shadow: false
                            });
                            p.getEl().on('mouseover', function () {
                                theTip.showAt(theElem.getX(), theElem.getY());
                            });
                            p.getEl().on('mouseleave', function () {
                                theTip.hide();
                            });
                        }
                    }
                },
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
                                mode: 'text/x-sh',
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
                    value: '<a href="https://www.gnu.org/software/bash/manual/bash.html" target="_blank">Bash Reference Manual</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://www.gnu.org/software/bash/manual/bash.pdf" target="_blank">Bash Reference Manual (PDF)</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://tldp.org/HOWTO/Bash-Prog-Intro-HOWTO.html" target="_blank">BASH Programming - Introduction HOW-TO</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://www.tldp.org/LDP/abs/html/" target="_blank">Advanced Bash-Scripting Guide</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://www.tldp.org/LDP/abs/abs-guide.pdf" target="_blank">Advanced Bash-Scripting Guide (PDF)</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://www.tldp.org/LDP/Bash-Beginners-Guide/html/" target="_blank">Bash Guide for Beginners</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://www.tldp.org/LDP/Bash-Beginners-Guide/Bash-Beginners-Guide.pdf" target="_blank">Bash Guide for Beginners (PDF)</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://cli.learncodethehardway.org/bash_cheat_sheet.pdf" target="_blank">Linux Bash Shell Cheat Sheet</a>'
                }
            ]
        }
    ]
});