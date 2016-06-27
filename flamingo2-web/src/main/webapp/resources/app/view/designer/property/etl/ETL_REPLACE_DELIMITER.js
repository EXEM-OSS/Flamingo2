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
 * ETL Replace Delimiter
 *
 * @cli hadoop jar flamingo2-mapreduce-hadoop2-2.0.5-job.jar replace_delimiter --input <INPUT> --output <OUTPUT> --from ',' --to '|'
 * @extend Flamingo2.view.designer.property._NODE_ETL
 * @author <a href="mailto:haneul.kim@cloudine.co.kr">Haneul, Kim</a>
 * @see <a href="http://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html" target="_blank">Apache Hadoop MapReduce Tutorial</a>
 */
Ext.ns('Flamingo2.view.designer.property.etl');
Ext.define('Flamingo2.view.designer.property.etl.ETL_REPLACE_DELIMITER', {
    extend: 'Flamingo2.view.designer.property._NODE_ETL',
    alias: 'widget.ETL_REPLACE_DELIMITER',

    width: 450,
    height: 320,

    items: [
        {
            title: message.msg('workflow.common.parameter'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                labelWidth: 170
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.etl.delimiter.from'),
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'combo',
                            name: 'from',
                            value: ',',
                            editable: false,
                            displayField: 'name',
                            valueField: 'value',
                            queryMode: 'local',
                            tpl: '<tpl for="."><div class="x-boundlist-item" data-qtip="{description}">{name}</div></tpl>',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['name', 'value', 'description'],
                                data: [
                                    {
                                        name: message.msg('workflow.common.delimiter.double.colon'),
                                        value: '::',
                                        description: '::'
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.comma'),
                                        value: ',',
                                        description: ','
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.pipe'),
                                        value: '|',
                                        description: '|'
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.tab'),
                                        value: '\u0009',
                                        description: '\\t'
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.blank'),
                                        value: '\u0020',
                                        description: ' '
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.user.def'),
                                        value: 'CUSTOM',
                                        description: message.msg('workflow.common.delimiter.user.def')
                                    }
                                ]
                            }),
                            listeners: {
                                change: function (combo, newValue, oldValue, eOpts) {
                                    // 콤보 값에 따라 관련 textfield 를 enable | disable 처리한다.
                                    var customValueField = combo.next('textfield');
                                    if (newValue === 'CUSTOM') {
                                        customValueField.enable();
                                        customValueField.isValid();
                                    } else {
                                        customValueField.disable();
                                        if (newValue) {
                                            customValueField.setValue(newValue);
                                        } else {
                                            customValueField.setValue(',');
                                        }
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'textfield',
                            name: 'fromValue',
                            flex: 1,
                            vtype: 'exceptcommaspace',
                            disabled: true,
                            allowBlank: false,
                            value: ','
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.etl.delimiter.to'),
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'combo',
                            name: 'to',
                            value: ',',
                            editable: false,
                            displayField: 'name',
                            valueField: 'value',
                            queryMode: 'local',
                            tpl: '<tpl for="."><div class="x-boundlist-item" data-qtip="{description}">{name}</div></tpl>',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['name', 'value', 'description'],
                                data: [
                                    {
                                        name: message.msg('workflow.common.delimiter.double.colon'),
                                        value: '::',
                                        description: '::'
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.comma'),
                                        value: ',',
                                        description: ','
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.pipe'),
                                        value: '|',
                                        description: '|'
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.tab'),
                                        value: '\u0009',
                                        description: '\\t'
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.blank'),
                                        value: '\u0020',
                                        description: ' '
                                    },
                                    {
                                        name: message.msg('workflow.common.delimiter.user.def'),
                                        value: 'CUSTOM',
                                        description: message.msg('workflow.common.delimiter.user.def')
                                    }
                                ]
                            }),
                            listeners: {
                                change: function (combo, newValue, oldValue, eOpts) {
                                    // 콤보 값에 따라 관련 textfield 를 enable | disable 처리한다.
                                    var customValueField = combo.next('textfield');
                                    if (newValue === 'CUSTOM') {
                                        customValueField.enable();
                                        customValueField.isValid();
                                    } else {
                                        customValueField.disable();
                                        if (newValue) {
                                            customValueField.setValue(newValue);
                                        } else {
                                            customValueField.setValue(',');
                                        }
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'textfield',
                            name: 'toValue',
                            flex: 1,
                            vtype: 'exceptcommaspace',
                            disabled: true,
                            allowBlank: false,
                            value: ','
                        }
                    ]
                }
            ]
        },
        {
            title: message.msg('workflow.common.mapreduce'),
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
                    name: 'jar',
                    fieldLabel: message.msg('workflow.common.mapreduce.jar'),
                    value: ETL.JAR,
                    disabledCls: 'disabled-plain',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    name: 'driver',
                    fieldLabel: message.msg('workflow.common.mapreduce.driver'),
                    value: 'replace_delimiter',
                    disabledCls: 'disabled-plain',
                    readOnly: true
                }
            ]
        },
        {
            title: message.msg('workflow.common.inout.path'),
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
                // MapReduce가 동작하는데 필요한 입력 경로를 지정한다.  이 경로는 N개 지정가능하다.
                {
                    xtype: '_inputGrid',
                    title: message.msg('workflow.common.input.path'),
                    flex: 1
                },
                // MapReduce가 동작하는데 필요한 출력 경로를 지정한다. 이 경로는 오직 1개만 지정가능하다.
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
                    value: '<a href="http://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html" target="_blank">Apache Hadoop MapReduce Tutorial</a>'
                }
            ]
        }
    ],

    /**
     * UI 컴포넌트의 Key를 필터링한다.
     *
     * ex) 다음과 같이 필터를 설정할 수 있다.
     * propFilters: {
     *     // 추가할 프라퍼티
     *     add   : [
     *         {'test1': '1'},
     *         {'test2': '2'}
     *     ],
     *
     *     // 변경할 프라퍼티
     *     modify: [
     *         {'delimiterType': 'delimiterType2'},
     *         {'config': 'config2'}
     *     ],
     *
     *     // 삭제할 프라퍼티
     *     remove: ['script', 'metadata']
     * }
     */
    propFilters: {
        add: [],
        modify: [],
        remove: ['config']
    },

    /**
     * MapReduce의 커맨드 라인 파라미터를 수동으로 설정한다.
     * 커맨드 라인 파라미터는 Flamingo2 Workflow Engine에서 props.mapreduce를 Key로 꺼내어 구성한다.
     *
     * @param props UI 상에서 구성한 컴포넌트의 Key Value값
     */
    afterPropertySet: function (props) {
        props.mapreduce = {
            driver: props.driver || '',
            jar: props.jar || '',
            confKey: props.hadoopKeys || '',
            confValue: props.hadoopValues || '',
            params: []
        };

        if (props.input) {
            props.mapreduce.params.push("--input", props.input);
        }
        if (props.output) {
            props.mapreduce.params.push("--output", props.output);
        }
        props.mapreduce.params.push('--from', '\'' + (props.from === 'CUSTOM' ? props.fromValue : props.from) + '\'');
        props.mapreduce.params.push('--to', '\'' + (props.to === 'CUSTOM' ? props.toValue : props.to) + '\'');

        this.callParent(arguments);
    }
});