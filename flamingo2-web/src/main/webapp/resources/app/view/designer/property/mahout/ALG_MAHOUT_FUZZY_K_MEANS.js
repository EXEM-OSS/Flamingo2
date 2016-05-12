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
Ext.define('Flamingo2.view.designer.property.mahout.ALG_MAHOUT_FUZZY_K_MEANS', {
    extend: 'Flamingo2.view.designer.property._NODE_ALG',
    alias: 'widget.ALG_MAHOUT_FUZZY_K_MEANS',

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
                    name: 'distanceMeasure',
                    fieldLabel: message.msg('workflow.am.fuzzy.distanse_measure'),
                    value: 'org.apache.mahout.common.distance.SquaredEuclidean',
                    allowBlank: true
                },
                {
                    xtype: '_browserField',
                    name: 'clusters',
                    fieldLabel: message.msg('workflow.am.fuzzy.clusters'),
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'numClusters',
                    fieldLabel: message.msg('workflow.am.fuzzy.num_cluster'),
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'convergenceDelta',
                    fieldLabel: message.msg('workflow.am.kmeans.convergence.delta'),
                    value: '0.5',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'maxIter',
                    fieldLabel: message.msg('workflow.am.spectral.max_iteration'),
                    vtype: 'numeric',
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
                    xtype: 'textfield',
                    name: 'm',
                    fieldLabel: message.msg('workflow.am.fuzzy.coefficient.normalization'),
                    vtype: 'decimalpoint',
                    allowBlank: true
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.fuzzy.clustering'),
                    defaultType: 'checkboxfield',
                    items: [
                        {
                            name: 'clustering'
                        }
                    ]
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: message.msg('workflow.am.fuzzy.emit.most.likely'),
                    columns: 2,
                    items: [
                        {
                            xtype: 'radiofield',
                            boxLabel: 'True',
                            name: 'emitMostLikely',
                            inputValue: 'true'
                        },
                        {
                            xtype: 'radiofield',
                            boxLabel: 'False',
                            name: 'emitMostLikely',
                            inputValue: 'false'
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'threshold',
                    fieldLabel: message.msg('workflow.am.kmeans.outlier.threshold'),
                    vtype: 'numeric',
                    value: '0',
                    allowBlank: true
                },
                {
                    xtype: 'radiogroup',
                    fieldLabel: message.msg('workflow.am.kmeans.execution.method'),
                    columns: 2,
                    itemId: 'kxecutionMethod',
                    items: [
                        {
                            xtype: 'radiofield',
                            boxLabel: 'MapReduce',
                            name: 'method',
                            inputValue: 'mapreduce'
                        },
                        {
                            xtype: 'radiofield',
                            boxLabel: 'Sequential',
                            name: 'method',
                            inputValue: 'sequential'
                        }
                    ]
                },
                {
                    xtype: '_browserField',
                    name: 'tempDir',
                    fieldLabel: message.msg('workflow.am.testnb.tempdirectory'),
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'startPhase',
                    fieldLabel: message.msg('workflow.am.testnb.startphase'),
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'endPhase',
                    fieldLabel: message.msg('workflow.am.testnb.endphase'),
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
                    value: 'org.apache.mahout.clustering.fuzzykmeans.FuzzyKMeansDriver',
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
                    value: '<a href="http://mahout.apache.org/users/clustering/fuzzy-k-means.html" target="_blank">Fuzzy K-Means</a>'
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

//        props.mapreduce.params.push("fkmeans");

        if (props.input) {
            props.mapreduce.params.push("--input", props.input);
        }

        if (props.output) {
            props.mapreduce.params.push("--output", props.output);
        }

        if (props.distanceMeasure) {
            props.mapreduce.params.push("--distanceMeasure", props.distanceMeasure);
        }

        if (props.clusters) {
            props.mapreduce.params.push("--clusters", props.clusters);
        }

        if (props.numClusters) {
            props.mapreduce.params.push("--numClusters", props.numClusters);
        }

        if (props.convergenceDelta) {
            props.mapreduce.params.push("--convergenceDelta", props.convergenceDelta);
        }

        if (props.maxIter) {
            props.mapreduce.params.push("--maxIter", props.maxIter);
        }

        if (props.overwrite) {
            props.mapreduce.params.push("--overwrite");
        }

        if (props.m) {
            props.mapreduce.params.push("--m", props.m);
        }

        if (props.clustering) {
            props.mapreduce.params.push("--clustering", props.clustering);
        }

        if (props.emitMostLikely) {
            props.mapreduce.params.push("--emitMostLikely", props.emitMostLikely);
        }

        if (props.threshold) {
            props.mapreduce.params.push("--threshold", props.threshold);
        }

        if (props.method) {
            props.mapreduce.params.push("--method", props.method);
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