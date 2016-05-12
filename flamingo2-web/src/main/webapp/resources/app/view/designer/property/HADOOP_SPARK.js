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
 * Spark Job Property View
 * spark-submit
 * --class 'Reasoner'
 * --master yarn-cluster
 * --executor-memory 64g
 * --executor-cores 24
 * target/scala-2.10/reasoner_2.10-1.0.jar
 *
 * @class
 * @extends Flamingo2.view.designer.property.HADOOP_SPARK
 */
Ext.define('Flamingo2.view.designer.property.HADOOP_SPARK', {
    extend: 'Flamingo2.view.designer.property._NODE_HADOOP',
    alias: 'widget.HADOOP_SPARK',

    requires: [
        'Flamingo2.view.designer.property._JarBrowserField',
        'Flamingo2.view.designer.property._BrowserField',
        'Flamingo2.view.designer.property._InputGrid',
        'Flamingo2.view.designer.property._JarGrid',
        'Flamingo2.view.designer.property._KeyValueGrid'
    ],

    width: 600,
    height: 360,

    items: [
        {
            title: message.msg('workflow.common.spark'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                labelWidth: 120
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'displayfield',
                    value: message.msg('workflow.he.spark.display.spark'),
                    height: 40
                },
                {
                    xtype: '_jarBrowserField',
                    name: 'jar',
                    fieldLabel: message.msg('workflow.he.spark.jar'),
                    allowBlank: false
                },
                {
                    xtype: 'textfield',
                    name: 'driver',
                    fieldLabel: message.msg('workflow.he.spark.driver'),
                    emptyText: 'org.apache.spark.examples.SparkPi',
                    allowBlank: false
                },
                {
                    xtype: 'checkboxfield',
                    name: 'yarn',
                    itemId: 'yarn',
                    checked: true,
                    fieldLabel: message.msg('workflow.he.spark.yarn'),
                    boxLabel: message.msg('workflow.common.use'),
                    handler: function (check) {
                        var sparkMasterUrl = check.nextSibling('textfield');

                        if (check.checked) {
                            sparkMasterUrl.setValue('yarn-cluster');
                        } else {
                            // 처음 로딩한 url 값이 호스트네임 패턴일 경우 한 번더 체크
                            if (sparkMasterUrl.lastValue != config['spark.master.url']
                                && sparkMasterUrl.lastValue != sparkMasterUrl.initialValue) {
                                sparkMasterUrl.setValue(sparkMasterUrl.lastValue);
                            } else {
                                sparkMasterUrl.setValue(config['spark.master.url']);
                            }
                        }
                    }
                },
                {
                    xtype: 'textfield',
                    name: 'sparkMasterUrl',
                    itemId: 'sparkMasterUrl',
                    fieldLabel: message.msg('workflow.he.spark.masterurl'),
                    emptyText: 'Standalone Mode',
                    allowBlank: false,
                    value: 'yarn-cluster'
                },
                {
                    xtype: 'textfield',
                    name: 'totalExecutorCores',
                    fieldLabel: message.msg('workflow.he.spark.totalexecores'),
                    emptyText: 'Standalone Mode (--total-executor-cores)',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'executorMemory',
                    fieldLabel: message.msg('workflow.he.spark.exememory'),
                    emptyText: 'YARN & Standalone Mode (--executor-memory)',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'numExecutors',
                    fieldLabel: message.msg('workflow.he.spark.exe'),
                    emptyText: 'YARN & Standalone Mode (--num-executors)',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'driverMemory',
                    fieldLabel: message.msg('workflow.he.spark.drivermemory'),
                    emptyText: 'YARN Mode (--driver-memory)',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'queue',
                    fieldLabel: message.msg('workflow.he.spark.queue'),
                    emptyText: 'YARN Mode (--queue)',
                    allowBlank: true
                },
                {
                    xtype: 'textfield',
                    name: 'executorCores',
                    fieldLabel: message.msg('workflow.he.spark.execores'),
                    emptyText: 'YARN Mode (--executor-cores)',
                    allowBlank: true
                }
            ]
        },
        {
            title: message.msg('workflow.common.dependency.jar'),
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
                    value: message.msg('workflow.he.spark.display.jar')
                },
                {
                    xtype: '_jarGrid',
                    title: message.msg('workflow.common.dependency.jar'),
                    name: 'dependencies',
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
                    value: message.msg('workflow.he.spark.display.conf')
                },
                {
                    xtype: '_keyValueGrid',
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
                    value: message.msg('workflow.he.spark.display.com')
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
                    value: '<a href="https://spark.apache.org/docs/latest/" target="_blank">Apache Spark</a>'
                },
                {
                    xtype: 'displayfield',
                    height: 20,
                    value: '<a href="https://spark.apache.org/examples.html" target="_blank">Spark Examples</a>'
                }
            ]
        }
    ],
    listeners: {
        afterrender: function () {
            var sparkMasterUrl = query('HADOOP_SPARK #sparkMasterUrl');
            var yarn = query('HADOOP_SPARK #yarn');

            if (sparkMasterUrl.lastValue == sparkMasterUrl.initialValue) {
                yarn.setValue(1);
            } else {
                sparkMasterUrl.setValue(sparkMasterUrl.lastValue);
                yarn.setValue(0);
            }
        }
    }
});