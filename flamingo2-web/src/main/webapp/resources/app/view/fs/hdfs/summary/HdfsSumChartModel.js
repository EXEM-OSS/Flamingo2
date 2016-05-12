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

Ext.define('Flamingo2.view.fs.hdfs.summary.HdfsSumChartModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.hdfsSumChartModel',

    stores: {
        top5Store: {
            autoLoad: false,
            fields: ['spaceConsumed', 'path'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.GET_HDFS_TOP5,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                },
                extraParams: {
                    clusterName: ENGINE.id
                }
            }
        }
    }
});