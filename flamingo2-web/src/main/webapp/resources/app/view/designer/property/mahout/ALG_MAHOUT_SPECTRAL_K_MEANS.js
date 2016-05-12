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
Ext.define('Flamingo2.view.designer.property.mahout.ALG_MAHOUT_SPECTRAL_K_MEANS', {
    extend: 'Flamingo2.view.designer.property._NODE_ALG',
    alias: 'widget.ALG_MAHOUT_SPECTRAL_K_MEANS',

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
                    name: 'dimensions',
                    fieldLabel: message.msg('workflow.am.spectralkmeans.dimension'),
                    tooltip: '',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'clusters',
                    fieldLabel: message.msg('workflow.am.spectralkmeans.num.clusters.eigenvectors'),
                    tooltip: '',
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'distanceMeasure',
                    fieldLabel: message.msg('workflow.am.spectralkmeans.distance.measure'),
                    tooltip: '',
                    allowBlank: false,
                    value: 'SquaredEuclidean'
                },
                {
                    xtype: 'textfield',
                    name: 'convergenceDelta',
                    fieldLabel: message.msg('workflow.am.spectralkmeans.convergence.delta'),
                    tooltip: '',
                    vtype: 'numeric',
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
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.am.spectralkmeans.uses.ssvd'),
                    defaultType: 'checkboxfield',
                    items: [
                        {
                            name: 'usessvd'
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'reduceTasks',
                    fieldLabel: message.msg('workflow.am.spectralkmeans.num.reducer.ssvd'),
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'outerProdBlockHeight',
                    fieldLabel: message.msg('workflow.am.spectralkmeans.outer.prod.block.height'),
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'oversampling',
                    fieldLabel: message.msg('workflow.am.spectralkmeans.over.sampling'),
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'powerIter',
                    fieldLabel: message.msg('workflow.am.spectralkmeans.power.iter'),
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'tempDir',
                    fieldLabel: message.msg('workflow.am.temp.dir'),
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
                    value: 'org.apache.mahout.clustering.spectral.kmeans.SpectralKMeansDriver',
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
                    xtype: '_inputGrid',
                    title: message.msg('workflow.common.input.path'),
                    flex: 1
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
                    value: '<a href="http://mahout.apache.org/users/clustering/spectral-clustering.html" target="_blank">Spectral Clustering</a>'
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

//        props.mapreduce.params.push("spectralkmeans");

        if (props.input) {
            props.mapreduce.params.push("--input", props.input);
        }

        if (props.output) {
            props.mapreduce.params.push("--output", props.output);
        }

        if (props.dimensions) {
            props.mapreduce.params.push("--dimensions", props.dimensions);
        }

        if (props.clusters) {
            props.mapreduce.params.push("--clusters", props.clusters);
        }

        if (props.distanceMeasure) {
            props.mapreduce.params.push("--distanceMeasure", props.distanceMeasure);
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

        if (props.usessvd) {
            props.mapreduce.params.push("--usessvd");
        }

        if (props.reduceTasks) {
            props.mapreduce.params.push("--reduceTasks", props.reduceTasks);
        }
////
        if (props.outerProdBlockHeight) {
            props.mapreduce.params.push("--outerProdBlockHeight", props.outerProdBlockHeight);
        }

        if (props.oversampling) {
            props.mapreduce.params.push("--oversampling", props.oversampling);
        }

        if (props.powerIter) {
            props.mapreduce.params.push("--powerIter", props.powerIter);
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