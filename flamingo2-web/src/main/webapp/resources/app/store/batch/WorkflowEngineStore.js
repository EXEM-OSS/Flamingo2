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
Ext.define('Flamingo2.store.batch.WorkflowEngineStore', {
    extend: 'Ext.data.Store',
    alias: 'store.workflowEngineStore',

    autoLoad: false,

    fields: [
        'engine',
        'date',
        'running',
        'jobs',
        'maxMemory',
        'totalMemory',
        'freeMemory'
    ],

    constructor: function (config) {
        this.proxy = Ext.create('Ext.data.proxy.Ajax', {
            url: CONSTANTS.CONTEXT_PATH + CONSTANTS.BATCH.WORKFLOW_ENGINE,
            reader: {
                type: 'json',
                rootProperty: 'list',
                totalProperty: 'total',
                idProperty: 'id'
            },
            extraParams: {
                clusterName: ENGINE.id,
                limit: parseInt(config.hadoop_monitoring_display_count)
            }
        });
        this.callParent(arguments);
    },

    sorters: [
        {
            property: 'date',
            direction: 'asc'
        }
    ]
});
