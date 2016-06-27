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
 * Created by Park on 15. 8. 19..
 */
Ext.define('Flamingo2.view.monitoring.spark.StorageGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.sparkStorageGrid',

    bind: {
        store: '{storage}'
    },

    columnLines: true,

    columns: [{
        text: 'RDD Name',
        dataIndex: 'rdd_name',
        align: 'center'
    },{
        text: 'Storage Level',
        style: 'text-align:center',
        flex: 1,
        dataIndex: 'storage_level'
    },{
        text: 'Cached Partitions',
        style: 'text-align:center',
        width: 120,
        align: 'right',
        dataIndex: 'cached_partitions'
    },{
        text: 'Fraction Cached',
        style: 'text-align:center',
        width: 120,
        align: 'right',
        dataIndex: 'fraction_cached'
    },{
        text: 'Size in Memory',
        style: 'text-align:center',
        width: 120,
        align: 'right',
        dataIndex: 'memory'
    },{
        text: 'Size in ExternalBlockStore',
        style: 'text-align:center',
        width: 120,
        align: 'right',
        dataIndex: 'external_block_store'
    },{
        text: 'Size on Disk',
        style: 'text-align:center',
        width: 120,
        align: 'right',
        dataIndex: 'disk'
    }],
    listeners: {
        dataLoad: 'onGridDataLoad'
    }
});