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

Ext.define('Flamingo2.store.admin.engine.WorkflowEngineStore', {
    extend: 'Ext.data.Store',

    fields: [
        'id',
        'instanceName',
        'status',
        'serverUrl',
        'hadoopClusterName',
        'hadoopVersion',
        'mapreduceFramework',
        'hdfsUrl',
        'hiveServerName',
        'schedulerName',
        'schedulerId',
        'hostAddress',
        'hostName',
        'runningJob'
    ],

    autoLoad: false,

    constructor: function (config) {
        this.callParent(arguments);

        /**
         * Workflow Engine 목록 필터링 파라미터.
         */
        if (config && config.type) {
            this.getProxy().extraParams.type = config.type;
        }

        /**
         * Workflow Engine 목록 필터링 파라미터.
         */
        if (config && config.hadoopVersion) {
            this.getProxy().extraParams.hadoopVersion = config.hadoopVersion;
        }
    },

    proxy: {
        type: 'ajax',
        url: CONSTANTS.CONFIG.ENGINES,
        reader: {
            type: 'json',
            rootProperty: 'list'
        },
        extraParams: { // Workflow Engine 목록 필터링 파라미터. 기본값은 모두 다 보임.
            'type': 'ALL',
            'hadoopVersion': '0'
        }
    }
});