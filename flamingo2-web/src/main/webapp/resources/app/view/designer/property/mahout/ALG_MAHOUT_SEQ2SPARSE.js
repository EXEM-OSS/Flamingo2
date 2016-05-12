/**
 * Created by cloudine on 15. 1. 20..
 */
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

Ext.ns('Flamingo2.view.designer.property.mahout');
Ext.define('Flamingo2.view.designer.property.mahout.ALG_MAHOUT_SEQ2SPARSE', {
    extend: 'Flamingo2.view.designer.property._NODE_ALG',
    alias: 'widget.ALG_MAHOUT_SEQ2SPARSE',

    requires: [
        'Flamingo2.view.designer.property._ConfigurationBrowserField',
        'Flamingo2.view.designer.property._BrowserField',
        'Flamingo2.view.designer.property._ColumnGrid',
        'Flamingo2.view.designer.property._DependencyGrid',
        'Flamingo2.view.designer.property._NameValueGrid',
        'Flamingo2.view.designer.property._KeyValueGrid',
        'Flamingo2.view.designer.property._EnvironmentGrid'
    ],

    width: 450,
    height: 320,

    items: [
        {
            title: message.msg('workflow.common.parameter'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                labelWidth: 150
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'minSupport',
                    fieldLabel: message.msg('workflow.am.seq2sparse.min.support'),
                    value: '2',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'analyzerName',
                    fieldLabel: message.msg('workflow.am.seq2sparse.min.support'),
                    value: '2',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'chunkSize',
                    fieldLabel: message.msg('workflow.am.seq2sparse.chunk.size'),
                    value: '100',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'minDF',
                    fieldLabel: message.msg('workflow.am.seq2sparse.min.document.frequency'),
                    value: '1',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'maxDFSigma',
                    fieldLabel: message.msg('workflow.am.seq2sparse.max.document.frequency.sigma'),
                    value: '-1.0',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'maxDFPercent',
                    fieldLabel: message.msg('workflow.am.seq2sparse.max.document.frequency.percent'),
                    value: '99',
                    allowBlank: true
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.seq2sparse.weight'),
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'combo',
                            name: 'weight',
                            value: 'TFIDF',
                            flex: 1,
                            forceSelection: true,
                            multiSelect: false,
                            disabled: false,
                            editable: false,
                            displayField: 'name',
                            valueField: 'value',
                            mode: 'local',
                            queryMode: 'local',
                            triggerAction: 'all',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['name', 'value', 'description'],
                                data: [
                                    {name: 'TF', value: 'TF', description: 'tf'},
                                    {name: 'TFIDF', value: 'TFIDF', description: 'tfidf'}
                                ]
                            })
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'norm',
                    itemId: 'normVal',
                    fieldLabel: message.msg('workflow.am.seq2sparse.normalization')
                },
                {
                    xtype: 'textfield',
                    name: 'minLLR',
                    fieldLabel: message.msg('workflow.am.seq2sparse.min.log.ratio'),
                    value: '1.0',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'numReducers',
                    fieldLabel: message.msg('workflow.am.seq2sparse.num_reducer'),
                    value: '1',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'maxNGramSize',
                    fieldLabel: message.msg('workflow.am.seq2sparse.max.ngrams.size'),
                    tooltip: '2=bigrams, 3=trigrams, etc.',
                    value: '1',
                    allowBlank: true
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.testnb.overwrite'),
                    defaultType: 'checkboxfield',
                    items: [
                        {
                            name: 'overwrite'
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.seq2sparse.sequential.access.vector'),
                    defaultType: 'checkboxfield',
                    items: [
                        {
                            name: 'sequentialAccessVector'
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.seq2sparse.named.vector'),
                    defaultType: 'checkboxfield',
                    items: [
                        {
                            name: 'namedVector'
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.seq2sparse.log_normal'),
                    defaultType: 'checkboxfield',
                    items: [
                        {
                            name: 'logNormalize'
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
                    fieldLabel: message.msg('workflow.common.mahaut.jar'),
                    value: MAHOUT.JAR,
                    disabledCls: 'disabled-plain',
                    readOnly: true
                },
                {
                    xtype: 'textfield',
                    name: 'driver',
                    fieldLabel: message.msg('workflow.common.mapreduce.driver'),
                    value: 'org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles',
                    disabledCls: 'disabled-plain',
                    readOnly: true
                },
                {
                    xtype: '_dependencyGrid',
                    title: message.msg('workflow.common.mapreduce.jar.title'),
                    flex: 1
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
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.common.input.path'),
                    defaults: {
                        hideLabel: true
                    },
                    layout: 'hbox',
                    items: [
                        {
                            xtype: '_browserField',
                            name: 'input',
                            allowBlank: false,
                            readOnly: false,
                            flex: 1
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.common.output.path'),
                    defaults: {
                        hideLabel: true
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
            ],
            listeners: {
                afterrender: function (form, eOpts) {
                    var canvas = Ext.ComponentQuery.query('canvas')[0];
                    var canvasForm = canvas.getForm();
                }
            }
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
                    value: '<a href="https://mahout.apache.org/users/classification/bayesian.html" target="_blank">Naive Bayes</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://www.slideshare.net/bigdatasyd/machine-learning-withmahout" target="_blank">Machine Learning with Mahout</a>'
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
            "driver": props.driver ? props.driver : '',
            "jar": props.jar ? props.jar : '',
            "confKey": props.hadoopKeys ? props.hadoopKeys : '',
            "confValue": props.hadoopValues ? props.hadoopValues : '',
            params: []
        };

//        props.mapreduce.params.push("seq2sparse");

        if (props.input) {
            props.mapreduce.params.push("--input", props.input);
        }

        if (props.output) {
            props.mapreduce.params.push("--output", props.output);
        }

        if (props.minSupport) {
            props.mapreduce.params.push("--minSupport", props.minSupport);
        }

        if (props.analyzerName) {
            props.mapreduce.params.push("--analyzerName", props.analyzerName);
        }

        if (props.chunkSize) {
            props.mapreduce.params.push("--chunkSize", props.chunkSize);
        }

        if (props.minDF) {
            props.mapreduce.params.push("--minDF", props.minDF);
        }

        if (props.maxDFSigma) {
            props.mapreduce.params.push("--maxDFSigma", props.maxDFSigma);
        }

        if (props.maxDFPercent) {
            props.mapreduce.params.push("--maxDFPercent", props.maxDFPercent);
        }

        if (props.weight) {
            props.mapreduce.params.push("--weight", props.weight);
        }

        if (props.norm) {
            props.mapreduce.params.push("--norm", props.norm);
        }

        if (props.minLLR) {
            props.mapreduce.params.push("--minLLR", props.minLLR);
        }

        if (props.numReducers) {
            props.mapreduce.params.push("--numReducers", props.numReducers);
        }

        if (props.maxNGramSize) {
            props.mapreduce.params.push("--maxNGramSize", props.maxNGramSize);
        }

        if (props.overwrite) {
            props.mapreduce.params.push("--overwrite");
        }

        if (props.sequentialAccessVector) {
            props.mapreduce.params.push("--sequentialAccessVector", props.sequentialAccessVector);
        }

        if (props.namedVector) {
            props.mapreduce.params.push("--namedVector", props.namedVector);
        }

        if (props.logNormalize) {
            props.mapreduce.params.push("--logNormalize", props.logNormalize);
        }

        this.callParent(arguments);
    }
});