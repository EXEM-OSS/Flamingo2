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
 * Apache Mahout의 Item based CF
 *
 * @command hadoop jar mahout-core-0.5-job.jar org.apache.mahout.cf.taste.hadoop.item.RecommenderJob --input input --output output --similarityClassname SIMILARITY_PEARSON_CORRELATION
 * @data 5,3600222,5.000000 (User,Item,Rating)
 * @see https://cwiki.apache.org/confluence/display/MAHOUT/Itembased+Collaborative+Filtering
 * @author Byoung Gon, Kim
 */

Ext.ns('Flamingo2.view.designer.property.mahout');
Ext.define('Flamingo2.view.designer.property.mahout.ALG_MAHOUT_CF_ITEM', {
    extend: 'Flamingo2.view.designer.property._NODE_ALG',
    alias: 'widget.ALG_MAHOUT_CF_ITEM',

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
                    name: 'numrecommendation',
                    fieldLabel: message.msg('workflow.am.num.recommend'),
                    allowBlank: true
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.users.file'),
                    defaults: {
                        hideLabel: true,
                        margin: "5 0 0 0"
                    },
                    layout: 'hbox',
                    items: [
                        {
                            xtype: '_browserField',
                            name: 'usersFile',
                            allowBlank: true,
                            readOnly: false,
                            flex: 1
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.items.file'),
                    defaults: {
                        hideLabel: true,
                        margin: "5 0 0 0"
                    },
                    layout: 'hbox',
                    items: [
                        {
                            xtype: '_browserField',
                            name: 'itemsFile',
                            allowBlank: true,
                            readOnly: false,
                            flex: 1
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.filter.file'),
                    defaults: {
                        hideLabel: true,
                        margin: "5 0 0 0"
                    },
                    layout: 'hbox',
                    items: [
                        {
                            xtype: '_browserField',
                            name: 'filterFile',
                            allowBlank: true,
                            readOnly: false,
                            flex: 1
                        }
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: message.msg('workflow.am.bool.data'),
                    columns: 2,
                    itemId: 'itemSimBooleanData',
                    items: [
                        {
                            xtype: 'radiofield',
                            boxLabel: 'True',
                            name: 'booleanData',
                            inputValue: 'true'
                        },
                        {
                            xtype: 'radiofield',
                            boxLabel: 'False',
                            name: 'booleanData',
                            inputValue: 'false'
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'maxPrefsPerUser',
                    fieldLabel: message.msg('workflow.am.max.prefer.value'),
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'minPrefsPerUser',
                    fieldLabel: message.msg('workflow.am.min.prefer.value'),
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'maxSimilaritiesPerItem',
                    fieldLabel: message.msg('workflow.am.sim.item.num'),
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'maxPrefsInItemSimilarity',
                    fieldLabel: message.msg('workflow.am.max.prefer.sim.item'),
                    allowBlank: true
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.sim.cal.method'),
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'combo',
                            name: 'similarityClassname',
                            value: 'SIMILARITY_LOGLIKELIHOOD',
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
                            tpl: '<tpl for="."><div class="x-boundlist-item" data-qtip="{description}">{name}</div></tpl>',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['name', 'value', 'description'],
                                data: [
                                    {
                                        name: 'SIMILARITY_COOCCURRENCE',
                                        value: 'SIMILARITY_COOCCURRENCE',
                                        description: 'SIMILARITY_COOCCURRENCE'
                                    },
                                    {
                                        name: 'SIMILARITY_EUCLIDEAN_DISTANCE',
                                        value: 'SIMILARITY_EUCLIDEAN_DISTANCE',
                                        description: 'SIMILARITY_EUCLIDEAN_DISTANCE'
                                    },
                                    {
                                        name: 'SIMILARITY_LOGLIKELIHOOD',
                                        value: 'SIMILARITY_LOGLIKELIHOOD',
                                        description: 'SIMILARITY_LOGLIKELIHOOD'
                                    },
                                    {
                                        name: 'SIMILARITY_PEARSON_CORRELATION',
                                        value: 'SIMILARITY_PEARSON_CORRELATION',
                                        description: 'SIMILARITY_PEARSON_CORRELATION'
                                    },
                                    {
                                        name: 'SIMILARITY_TANIMOTO_COEFFICIENT',
                                        value: 'SIMILARITY_TANIMOTO_COEFFICIENT',
                                        description: 'SIMILARITY_TANIMOTO_COEFFICIENT'
                                    },
                                    {
                                        name: 'SIMILARITY_UNCENTERED_COSINE',
                                        value: 'SIMILARITY_UNCENTERED_COSINE',
                                        description: 'SIMILARITY_UNCENTERED_COSINE'
                                    },
                                    {
                                        name: 'SIMILARITY_UNCENTERED_ZERO_ASSUMING_COSINE',
                                        value: 'SIMILARITY_UNCENTERED_ZERO_ASSUMING_COSINE',
                                        description: 'SIMILARITY_UNCENTERED_ZERO_ASSUMING_COSINE'
                                    }
                                ]
                            })
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'threshold',
                    fieldLabel: message.msg('workflow.am.threshold'),
                    allowBlank: true

                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.sim.matrix.output.path'),
                    defaults: {
                        hideLabel: true,
                        margin: "5 0 0 0"
                    },
                    layout: 'hbox',
                    items: [
                        {
                            xtype: '_browserField',
                            name: 'outputPathForSimilarityMatrix',
                            allowBlank: true,
                            readOnly: false,
                            flex: 1
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'randomSeed',
                    fieldLabel: message.msg('workflow.am.random.seed'),
                    allowBlank: true
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.seq.output.path'),
                    defaults: {
                        hideLabel: true,
                        margin: "5 0 0 0"
                    },
                    layout: 'hbox',
                    items: [
                        {
                            xtype: '_browserField',
                            name: 'sequencefileOutput',
                            allowBlank: true,
                            readOnly: false,
                            flex: 1
                        }
                    ]
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.temp.dir'),
                    defaults: {
                        hideLabel: true,
                        margin: "5 0 0 0"
                    },
                    layout: 'hbox',
                    items: [
                        {
                            xtype: '_browserField',
                            name: 'tempDir',
                            allowBlank: true,
                            readOnly: false,
                            flex: 1
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'startPhase',
                    fieldLabel: message.msg('workflow.am.start.phase'),
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'endPhase',
                    fieldLabel: message.msg('workflow.am.end.phase'),
                    allowBlank: true
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
                    value: 'org.apache.mahout.cf.taste.hadoop.item.RecommenderJob',
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
                        hideLabel: true,
                        margin: "5 0 0 0"
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
                    value: '<a href="http://mahout.apache.org/users/algorithms/recommender-overview.html" target="_blank">Recommender Overview</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://mahout.apache.org/users/recommender/userbased-5-minutes.html" target="_blank">Creating a User-Based Recommender in 5 minutes</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="http://mahout.apache.org/users/recommender/userbased-5-minutes.html" target="_blank">Creating a User-Based Recommender in 5 minutes</a>'
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

//        props.mapreduce.params.push("recommenditembased");

        if (props.input) {
            props.mapreduce.params.push("--input", props.input);
        }

        if (props.output) {
            props.mapreduce.params.push("--output", props.output);
        }

        if (props.numrecommendation) {
            props.mapreduce.params.push("--numRecommendations", props.numrecommendation);
        }

        if (props.usersFile) {
            props.mapreduce.params.push("--usersFile", props.usersFile);
        }

        if (props.itemsFile) {
            props.mapreduce.params.push("--itemsFile", props.itemsFile);
        }

        if (props.filterFile) {
            props.mapreduce.params.push("--filterFile", props.filterFile);
        }

        if (props.booleanData) {
            props.mapreduce.params.push("--booleanData", props.booleanData);
        }

        if (props.maxPrefsPerUser) {
            props.mapreduce.params.push("--maxPrefsPerUser", props.maxPrefsPerUser);
        }

        if (props.minPrefsPerUser) {
            props.mapreduce.params.push("--minPrefsPerUser", props.minPrefsPerUser);
        }

        if (props.maxSimilaritiesPerItem) {
            props.mapreduce.params.push("--maxSimilaritiesPerItem", props.maxSimilaritiesPerItem);
        }

        if (props.maxPrefsInItemSimilarity) {
            props.mapreduce.params.push("--maxPrefsInItemSimilarity", props.maxPrefsInItemSimilarity);
        }

        if (props.similarityClassname) {
            props.mapreduce.params.push("--similarityClassname", props.similarityClassname);
        }

        if (props.threshold) {
            props.mapreduce.params.push("--threshold", props.threshold);
        }

        if (props.outputPathForSimilarityMatrix) {
            props.mapreduce.params.push("--outputPathForSimilarityMatrix", props.outputPathForSimilarityMatrix);
        }

        if (props.randomSeed) {
            props.mapreduce.params.push("--randomSeed", props.randomSeed);
        }

        if (props.sequencefileOutput) {
            props.mapreduce.params.push("--sequencefileOutput", props.sequencefileOutput);
        }

        if (props.tempDir) {
            props.mapreduce.params.push("--tempDir", props.tempDir);
        }

        if (props.startPhase) {
            props.mapreduce.params.push("--startPhase", props.startPhase);
        }

        if (props.endPhase) {
            props.mapreduce.params.push("--endPhase", props.endPhase);
        }

        this.callParent(arguments);
    }
});