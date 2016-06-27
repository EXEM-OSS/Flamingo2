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
 * Created by Park on 15. 8. 25..
 */
Ext.define('Flamingo2.view.monitoring.spark.StageDetail', {
    extend: 'Ext.window.Window',

    requires: [
        'Flamingo2.view.monitoring.spark.StageDetailController',
        'Flamingo2.view.monitoring.spark.StageDetailModel',
        'Flamingo2.view.monitoring.spark._BarChart'
    ],

    viewModel: {
        type: 'stagedetail'
    },
    controller: 'stagedetail',

    scrollable: true,
    maximizable: true,
    modal: true,

    bodyStyle: {
        background: '#FFFFFF'
    },

    layout: {
        type: 'anchor'
    },

    defaults: {
        margin: '0 0 4 0',
        anchor: '100%'
    },

    items: [{
        xtype: 'container',
        layout: {
            type: 'hbox',
            align: 'stretch'
        },
        defaults: {
            flex: 1,
            height: 250
        },
        items: [{
            xtype: 'sparkBarChart',
            title: 'Executor Run Time (ms)',
            reference: 'executorRunTime',
            margin: '0 0 0 2',
            bind: {
                store: '{executorRunTime}'
            }
        }]
    },{
        xtype: 'container',
        layout: {
            type: 'hbox',
            align: 'stretch'
        },
        defaults: {
            flex: 1,
            height: 250
        },
        items: [/*{
            xtype: 'panel',
            title: 'Result Size',
            margin: '0 2 0 0',
            border: true,
            html: '<div style="text-align: center"><h2 style="margin: 8px 0px 8px 0px;">Minimum: </h2><br><h2 style="margin: 8px 0px 8px 0px;">Maximum: </h2><br><h2 style="margin: 8px 0px 8px 0px;">Average: </h2></div>'
        },*/{
            xtype: 'sparkBarChart',
            title: 'Executor Deserialize Time (ms)',
            reference: 'executorDeserializeTime',
            margin: '0 2 0 0',
            bind: {
                store: '{executorDeserializeTime}'
            }
        },{
            xtype: 'sparkBarChart',
            title: 'Result Serialization Time (ms)',
            reference: 'resultSerializationTime',
            margin: '0 0 0 2',
            bind: {
                store: '{resultSerializationTime}'
            }
        }]
    },{
        xtype: 'container',
        layout: {
            type: 'hbox',
            align: 'stretch'
        },
        defaults: {
            flex: 1,
            height: 250
        },
        items: [{
            xtype: 'sparkBarChart',
            title: 'Getting Result Time (ms)',
            reference: 'gettingResultTime',
            margin: '0 2 0 0',
            bind: {
                store: '{gettingResultTime}'
            }
        },{
            xtype: 'sparkBarChart',
            title: 'JVM GC Time (ms)',
            reference: 'jvmGCTime',
            margin: '0 0 0 2',
            bind: {
                store: '{jvmGCTime}'
            }
        }]
    }/*,{
        xtype: 'container',
        layout: {
            type: 'hbox',
            align: 'stretch'
        },
        defaults: {
            flex: 1,
            height: 250
        },
        items: [{
            xtype: 'panel',
            title: 'Memory Bytes Spilled',
            margin: '0 2 0 0'
        },{
            xtype: 'panel',
            title: 'Disk Bytes Spilled',
            margin: '0 0 0 2'
        }]
    }*/],

    listeners: {
        afterrender: 'onAfterrender'
    }
});