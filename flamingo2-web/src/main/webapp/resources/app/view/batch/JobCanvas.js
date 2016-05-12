/*
 * Copyright (C) 2011  Flamingo Project (http://www.cloudine.io).
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

Ext.define('Flamingo2.view.batch.JobCanvas', {
    extend: 'Ext.form.Panel',
    alias: 'widget.jobCanvas',

    /**
     * Workflow Designer의 UI 노드
     */
    requires: [
        /*
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

         'Flamingo2.view.designer.shape.giraph.ALG_GIRAPH',

         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_CF_ITEM',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_KMEANS',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_MINHASH',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_CANOPY',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_PARALLEL_FP_MINING',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_FUZZY_K_MEANS',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_FUZZY_MATRIX_FACT_ALS',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_SPECTRAL_K_MEANS',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_STREAMING_K_MEANS',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_NAIVE_MATRIX',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_SEQ2SPARSE',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_TESTNB',
         'Flamingo2.view.designer.shape.mahout.ALG_MAHOUT_TRAINNB',
         */

        /*
         'Flamingo2.view.designer.shape.HADOOP_HIVE',
         'Flamingo2.view.designer.shape.HADOOP_MR',
         'Flamingo2.view.designer.shape.HADOOP_JAVA',
         'Flamingo2.view.designer.shape.HADOOP_SHELL',
         'Flamingo2.view.designer.shape.HADOOP_PYTHON',
         'Flamingo2.view.designer.shape.HADOOP_PIG',
         */

        /*
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

         'Flamingo2.view.designer.property.giraph.ALG_GIRAPH',

         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_CF_ITEM',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_KMEANS',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_MINHASH',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_CANOPY',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_PARALLEL_FP_MINING',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_MATRIX_FACT_ALS',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_PARALLEL_ALS',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_FUZZY_K_MEANS',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_NAIVE_MATRIX',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_SEQ2SPARSE',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_SPECTRAL_K_MEANS',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_STREAMING_K_MEANS',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_TESTNB',
         'Flamingo2.view.designer.property.mahout.ALG_MAHOUT_TRAINNB',
         */

        /*
         'Flamingo2.view.designer.property.HADOOP_HIVE',
         'Flamingo2.view.designer.property.HADOOP_MR',
         'Flamingo2.view.designer.property.HADOOP_JAVA',
         'Flamingo2.view.designer.property.HADOOP_SHELL',
         'Flamingo2.view.designer.property.HADOOP_PYTHON',
         'Flamingo2.view.designer.property.HADOOP_PIG'
         */
    ],

    layout: 'fit',

    border: false,

    autoScroll: true,

    forceLayout: true,

    cls: 'canvas_contents',

    graph: null

});