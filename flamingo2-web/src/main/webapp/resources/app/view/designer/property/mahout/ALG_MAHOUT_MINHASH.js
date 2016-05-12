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
Ext.define('Flamingo2.view.designer.property.mahout.ALG_MAHOUT_MINHASH', {
    extend: 'Flamingo2.view.designer.property._NODE_ALG',
    alias: 'widget.ALG_MAHOUT_MINHASH',

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
                    xtype: '_browserField',
                    name: 'vectorFile',
                    fieldLabel: message.msg('workflow.label_vector_path'),
                    allowBlank: true
                },
                {
                    xtype: '_browserField',
                    name: 'outputFile',
                    fieldLabel: message.msg('workflow.common.output.path'),
                    allowBlank: true
                },
                {
                    xtype: '_browserField',
                    name: 'overwrite',
                    fieldLabel: message.msg('workflow.label_modify_directory_path'),
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'minClusterSize',
                    fieldLabel: message.msg('workflow.label_min_cluster_size'),
                    tooltip: message.msg('workflow.tip_min_cluster_size'),
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'minVectorSize',
                    fieldLabel: message.msg('workflow.label_min_vector_size'),
                    tooltip: message.msg('workflow.tip_min_vector_size'),
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.label_hash_type'),
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'combo',
                            name: 'algorithmOption',
                            value: 'linear',
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
                                    {
                                        name: message.msg('workflow.option_linear'),
                                        value: 'linear',
                                        description: message.msg('workflow.option_linear')
                                    },
                                    {
                                        name: message.msg('workflow.option_polynomial'),
                                        value: 'polynomial',
                                        description: message.msg('workflow.option_polynomial')
                                    },
                                    {
                                        name: message.msg('workflow.option_murmur'),
                                        value: 'murmur',
                                        description: message.msg('workflow.option_murmur')
                                    }
                                ]
                            }),
                            listeners: {
                                change: function (combo, newValue, oldValue, eOpts) {
                                    // 콤보 값에 따라 관련 textfield 를 enable | disable 처리한다.
                                    var customValueField = combo.nextSibling('textfield');
                                    if (newValue === 'CUSTOM') {
                                        customValueField.enable();
                                        customValueField.isValid();
                                    } else {
                                        customValueField.disable();
                                        customValueField.setValue('');
                                    }
                                }
                            }
                        }
                    ]
                },
                {
                    xtype: 'textfield',
                    name: 'numHashFunctions',
                    fieldLabel: message.msg('workflow.label_num_hash_functions'),
                    tooltip: message.msg('workflow.tip_num_hash_functions'),
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'keyGroups',
                    fieldLabel: message.msg('workflow.label_key_groups'),
                    tooltip: message.msg('workflow.tip_key_groups'),
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'numReducers',
                    fieldLabel: message.msg('workflow.label_num_reducers'),
                    tooltip: message.msg('workflow.tip_num_reducers'),
                    vtype: 'numeric',
                    value: 2,
                    allowBlank: true
                },
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('workflow.common.delimiter'),
                    tooltip: message.msg('workflow.tip_column_delimiter'),
                    layout: 'hbox',
                    items: [
                        {
                            xtype: 'fieldcontainer',
                            layout: 'hbox',
                            items: [
                                {
                                    xtype: 'combo',
                                    name: 'delimiter',
                                    value: '\'\\t\'',
                                    flex: 1,
                                    forceSelection: true,
                                    multiSelect: false,
                                    editable: false,
                                    readOnly: this.readOnly,
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
                                                value: '\'\\t\'',
                                                description: '\'\\t\''
                                            },
                                            {
                                                name: message.msg('workflow.common.delimiter.blank'),
                                                value: '\'\\s\'',
                                                description: '\'\\s\''
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
                                            var customValueField = combo.nextSibling('textfield');
                                            if (newValue === 'CUSTOM') {
                                                customValueField.enable();
                                                customValueField.isValid();
                                            } else {
                                                customValueField.disable();
                                                if (newValue) {
                                                    customValueField.setValue(newValue);
                                                } else {
                                                    customValueField.setValue('::');
                                                }
                                            }
                                        }
                                    }
                                },
                                {
                                    xtype: 'textfield',
                                    name: 'delimiterValue',
                                    vtype: 'exceptcommaspace',
                                    flex: 1,
                                    disabled: true,
                                    readOnly: this.readOnly,
                                    allowBlank: false,
                                    value: '\\t'
                                }
                            ]
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
                    allowBlank: false
                },
                {
                    xtype: 'textfield',
                    name: 'driver',
                    fieldLabel: message.msg('workflow.common.mapreduce.driver'),
                    value: 'org.apache.mahout.clustering.minhash.MinHashDriver',
                    disabledCls: 'disabled-plain',
                    allowBlank: false
                },
                {
                    xtype: '_dependencyGrid',
                    title: message.msg('workflow.common.mapreduce.jar.title'),
                    flex: 1
                }
            ]
        },
        {
            title: message.msg('workflow.title_input_col'),
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
                    xtype: '_prevColumnGrid',
                    readOnly: true,
                    flex: 1
                }
            ]
        },
        {
            title: message.msg('workflow.title_output_col'),
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
                    xtype: 'hidden',
                    name: 'outputPathQualifier'
                },
                /*{
                 xtype : '_delimiterSelCmbField',
                 hidden: true
                 },*/
                {
                    xtype: '_columnGrid',
                    readOnly: true,
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
                    value: '<a href="http://mahout.apache.org/users/algorithms/recommender-overview.html" target="_blank">Recommender Overview</a>'
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

        if (props.vectorFile) {
            props.mapreduce.params.push("-vectorFile", props.vectorFile);
        }

        if (props.outputFile) {
            props.mapreduce.params.push("-outputFile", props.outputFile);
        }

        if (props.overwrite) {
            props.mapreduce.params.push("-overwrite", props.overwrite);
        }

        if (props.minClusterSize) {
            props.mapreduce.params.push("-minClusterSize", props.minClusterSize);
        }

        if (props.minVectorSize) {
            props.mapreduce.params.push("-minVectorSize", props.minVectorSize);
        }

        if (props.algorithmOption) {
            props.mapreduce.params.push("-algorithmOption", props.algorithmOption);
        }

        if (props.numHashFunctions) {
            props.mapreduce.params.push("-numHashFunctions", props.numHashFunctions);
        }

        if (props.keyGroups) {
            props.mapreduce.params.push("-keyGroups", props.keyGroups);
        }

        if (props.numReducers) {
            props.mapreduce.params.push("-numReducers", props.numReducers);
        }

        if (props.delimiter) {
            if (props.delimiter == 'CUSTOM') {
                props.mapreduce.params.push("-delimiter", props.delimiterValue);
            } else {
                props.mapreduce.params.push("-delimiter", props.delimiter);
            }
        }

        this.callParent(arguments);
    }
});