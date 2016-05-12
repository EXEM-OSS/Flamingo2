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
Ext.define('Flamingo2.store.engine', {
    extend: 'Ext.data.Store',
    autoLoad: true,
    storeId: 'mainEngine',
    fields: [
        'id',
        'name',
        'ip',
        'port',
        'hsAddress',
        'hsPort',
        'rmAddress',
        'rmPort',
        'nnAddress',
        'nnPort',
        'rmAgentAddress',
        'rmAgentPort',
        'nnAgentAddress',
        'nnAgentPort',
        'hiveServerUrl',
        'hiveServerUsername',
        'hiveMetastoreAddress',
        'hiveMetastorePort',
        'historyServerUrl'
    ],
    proxy: {
        type: 'ajax',
        url: CONSTANTS.CONFIG.ENGINES,
        reader: {
            type: 'json',
            rootProperty: 'list'
        },
        extraParams: { // Workflow Engine 목록 필터링 파라미터. 기본값은 모두 다 보임.
        },
        listeners: {
            load: function () {


            }
        }
    }
});
