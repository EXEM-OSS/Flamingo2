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
Ext.ns('Flamingo2.view.designer.shape');
Ext.ns('Flamingo2.view.designer.shape.ankus');
Ext.ns('Flamingo2.view.designer.shape.bpmn');
Ext.ns('Flamingo2.view.designer.shape.etl');
Ext.ns('Flamingo2.view.designer.shape.giraph');
Ext.ns('Flamingo2.view.designer.shape.mahout');

var shapes = [
    'Flamingo2.view.designer.shape.HADOOP_HIVE',
    'Flamingo2.view.designer.shape.HADOOP_MR',
    'Flamingo2.view.designer.shape.HADOOP_JAVA',
    'Flamingo2.view.designer.shape.HADOOP_SHELL',
    'Flamingo2.view.designer.shape.HADOOP_PYTHON',
    'Flamingo2.view.designer.shape.HADOOP_PIG',
    'Flamingo2.view.designer.shape.HADOOP_SQOOP_IMPORT',
    'Flamingo2.view.designer.shape.HADOOP_SQOOP_EXPORT',
    'Flamingo2.view.designer.shape.HADOOP_SPARK',
    'Flamingo2.view.designer.shape.HADOOP_R',

    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_CERTAIN_FACTOR_SUM',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_CF_ITEM_RECOMMEND',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_CF_SIM',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_CF_USER_RECOMMEND',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_CONTENT_SIM',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_CORR_BOOL',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_CORR_NUMERIC',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_CORR_STRING',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_KMEANS',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_EM',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_ID3',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_NOMINAL_STATISTICS',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_NORMAL',
    'Flamingo2.view.designer.shape.ankus.ALG_ANKUS_NUMERIC_STATISTICS',

    'Flamingo2.view.designer.shape.bpmn.BPMN_INCLUSIVE_FORK',
    'Flamingo2.view.designer.shape.bpmn.BPMN_INCLUSIVE_JOIN',
    'Flamingo2.view.designer.shape.bpmn.BPMN_JOIN',
    'Flamingo2.view.designer.shape.bpmn.BPMN_PARALLEL',
    'Flamingo2.view.designer.shape.bpmn.SUBPROCESS',

    'Flamingo2.view.designer.shape.etl.ETL_UIMA_APP',
    'Flamingo2.view.designer.shape.etl.ETL_UIMA_SEQ',
    'Flamingo2.view.designer.shape.etl.ETL_APACHE_ACCESS',
    'Flamingo2.view.designer.shape.etl.ETL_CHAR_REMOVER',

    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_CF_ITEM',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_KMEANS',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_FUZZY_K_MEANS',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_MINHASH',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_CANOPY',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_PARALLEL_FP_MINING',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_MATRIX_FACT_ALS',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_PARALLEL_ALS',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_NAIVE_MATRIX',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_SPECTRAL_K_MEANS',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_STREAMING_K_MEANS',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_SEQ2SPARSE',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_TESTNB',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_TRAINNB',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_QUALCLUSTER',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_CLUSTER_DUMP',
    'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_SEQDIRECTORY'
];

for (var i = 0; i < shapes.length; i++) {
    var text = shapes[i];
    var splitText = shapes[i].split('.');
    var alias = splitText[splitText.length - 1];
    var ref = (splitText.length == 5) ? alias : splitText[splitText.length - 2] + "." + [alias];
    makeShape(ref, alias, text);
}
// ref
function makeShape(ref, alias, text) {
    var refArr = ref.split('.');
    if (refArr.length == 1) {
        Flamingo2.view.designer.shape[ref] = function (image, label) {
            Flamingo2[alias].superclass.call(this, image, label);
            this.SHAPE_ID = text;
        };

        Flamingo2.view.designer.shape[ref].prototype = new OG.shape.ImageShape();
        Flamingo2.view.designer.shape[ref].superclass = OG.shape.ImageShape;
        Flamingo2.view.designer.shape[ref].prototype.constructor = text;
        Flamingo2.view.designer.shape[ref].className = text;
        Flamingo2[alias] = Flamingo2.view.designer.shape[ref];
    } else if (refArr.length == 2) {
        Flamingo2.view.designer.shape[refArr[0]][refArr[1]] = function (image, label) {
            Flamingo2[alias].superclass.call(this, image, label);
            this.SHAPE_ID = text;
        };

        Flamingo2.view.designer.shape[refArr[0]][refArr[1]].prototype = new OG.shape.ImageShape();
        Flamingo2.view.designer.shape[refArr[0]][refArr[1]].superclass = OG.shape.ImageShape;
        Flamingo2.view.designer.shape[refArr[0]][refArr[1]].prototype.constructor = text;
        Flamingo2.view.designer.shape[refArr[0]][refArr[1]].className = text;
        Flamingo2[alias] = Flamingo2.view.designer.shape[refArr[0]][refArr[1]];
    }
}

Ext.define('Flamingo2.view.designer.canvas.Canvas', {
    extend: 'Ext.form.Panel',
    alias: 'widget.canvas',

    /**
     * Workflow Designer의 UI 노드
     */
    requires: [
        'Flamingo2.view.designer.canvas.CanvasController',

        'Flamingo2.view.designer.property.HADOOP_HIVE',
        'Flamingo2.view.designer.property.HADOOP_MR',
        'Flamingo2.view.designer.property.HADOOP_JAVA',
        'Flamingo2.view.designer.property.HADOOP_SHELL',
        'Flamingo2.view.designer.property.HADOOP_PYTHON',
        'Flamingo2.view.designer.property.HADOOP_PIG',
        'Flamingo2.view.designer.property.HADOOP_SQOOP_IMPORT',
        'Flamingo2.view.designer.property.HADOOP_SQOOP_EXPORT',
        'Flamingo2.view.designer.property.HADOOP_SPARK',
        'Flamingo2.view.designer.property.HADOOP_R',

        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_CERTAIN_FACTOR_SUM',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_CF_ITEM_RECOMMEND',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_CF_SIM',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_CF_USER_RECOMMEND',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_CONTENT_SIM',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_CORR_BOOL',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_CORR_NUMERIC',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_CORR_STRING',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_KMEANS',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_EM',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_ID3',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_NOMINAL_STATISTICS',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_NORMAL',
        'Flamingo2.view.designer.property.ankus.ALG_ANKUS_NUMERIC_STATISTICS',

        'Flamingo2.view.designer.property.bpmn.BPMN_INCLUSIVE_FORK',
        'Flamingo2.view.designer.property.bpmn.BPMN_INCLUSIVE_JOIN',
        'Flamingo2.view.designer.property.bpmn.BPMN_PARALLEL',
        'Flamingo2.view.designer.property.bpmn.BPMN_JOIN',
        'Flamingo2.view.designer.property.bpmn.SUBPROCESS',

        'Flamingo2.view.designer.property.etl.ETL_UIMA_APP',
        'Flamingo2.view.designer.property.etl.ETL_UIMA_SEQ',
        'Flamingo2.view.designer.property.etl.ETL_APACHE_ACCESS',
        'Flamingo2.view.designer.property.etl.ETL_CHAR_REMOVER',

        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_CF_ITEM',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_KMEANS',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_FUZZY_K_MEANS',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_MINHASH',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_CANOPY',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_PARALLEL_FP_MINING',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_MATRIX_FACT_ALS',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_PARALLEL_ALS',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_NAIVE_MATRIX',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_SPECTRAL_K_MEANS',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_STREAMING_K_MEANS',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_SEQ2SPARSE',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_TESTNB',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_TRAINNB',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_QUALCLUSTER',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_CLUSTER_DUMP',
        'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_SEQDIRECTORY'
    ],

    controller: 'canvasController',

    layout: 'fit',

    autoScroll: true,

    forceLayout: true,

    cls: 'canvas_contents',

    graph: null,

    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    xtype: 'textfield',
                    width: 400,
                    labelWidth: 100,
                    maxLength: 100,
                    fieldLabel: message.msg('common.workflowName'),
                    id: 'wd_fld_name',
                    name: 'name',
                    emptyText: message.msg('designer.emptyText.name'),
                    allowBlank: false
                },
                {
                    xtype: 'hidden',
                    id: 'wd_fld_id',
                    name: 'id',
                    allowBlank: true
                },
                {
                    xtype: 'hidden',
                    id: 'wd_fld_tree_id',
                    name: 'tree_id',
                    allowBlank: true
                },
                {
                    xtype: 'hidden',
                    id: 'wd_fld_status',
                    name: 'status',
                    allowBlank: true
                },
                {
                    xtype: 'hidden',
                    id: 'wd_fld_process_id',
                    name: 'process_id',
                    allowBlank: true
                },
                {
                    xtype: 'hidden',
                    id: 'wd_fld_process_definition_id',
                    name: 'process_definition_id',
                    allowBlank: true
                },
                {
                    xtype: 'hidden',
                    id: 'wd_fld_deployment_id',
                    name: 'deployment_id',
                    allowBlank: true
                },
                {
                    xtype: 'hidden',
                    id: 'wd_fld_desc',
                    name: 'desc',
                    allowBlank: true
                }
            ]
        },
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    id: 'wd_btn_create',
                    text: message.msg('common.new'),
                    iconCls: 'common-new',
                    listeners: {
                        click: 'onCreateClick'
                    }
                },
                {
                    id: 'wd_btn_save',
                    text: message.msg('common.save'),
                    iconCls: 'common-save',
                    listeners: {
                        click: 'onSaveClick'
                    }
                },
                {
                    id: 'wd_btn_run',
                    text: message.msg('common.run'),
                    iconCls: 'common-execute',
                    disabled: true,
                    listeners: {
                        click: 'onRunClick'
                    }
                },
//                '-',
                {
                    id: 'wd_btn_xml',
                    text: message.msg('common.view'),
                    iconCls: 'hdfs-directory-info',
                    disabled: true,
                    hidden: true,
                    listeners: {
                        click: 'onWorkflowXMLClick'
                    }
                },
//                '-',
                {
                    id: 'wd_btn_copy',
                    text: message.msg('common.copy'),
                    iconCls: 'common-file-copy',
                    disabled: true,
                    listeners: {
                        click: 'onWorkflowCopyClick'
                    }
                },
                '->',
                {
                    text: message.msg('fs.hdfs.common.browser'),
                    iconCls: 'designer-filesystem',
                    listeners: {
                        click: 'onHdfsBrowserClick'
                    }
                },
                {
                    text: message.msg('hive.title'),
                    iconCls: 'designer-hive',
                    listeners: {
                        click: 'onHiveClick'
                    }
                }
            ]
        }
    ],
    listeners: {
        highlightById: 'highlightById',
        unhighlightById: 'unhighlightById',
        getwiredIdByElement: 'getwiredIdByElement',
        setwireEvent: 'setwireEvent',
        setwireEventAll: 'setwireEventAll',

        render: 'onCanvasRender',
        resize: 'onCanvasResize',

        nodeBeforeConnect: 'onCanvasNodeBeforeConnect',
        nodeConnect: 'onCanvasNodeConnect',
        nodeDisconnected: 'onCanvasNodeDisconnected',
        nodeBeforeRemove: 'onCanvasNodeBeforeRemove',

        beforeLabelChange: 'onCanvasBeforeLabelChange',
        labelChanged: 'onCanvasLabelChanged'
    },
    highlightById: function (elementId) {
        this.fireEvent('highlightById', elementId);
    },
    unhighlightById: function (elementId) {
        this.fireEvent('unhighlightById', elementId);
    },
    setwireEvent: function (shapeElement) {
        this.fireEvent('setwireEvent', shapeElement);
    },
    setwireEventAll: function () {
        this.fireEvent('setwireEventAll');
    }
});