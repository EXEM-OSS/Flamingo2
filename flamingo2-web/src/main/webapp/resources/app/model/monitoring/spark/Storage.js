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
Ext.define('Flamingo2.model.monitoring.spark.Storage', {
    extend: 'Ext.data.Model',
    fields: [
        {
            name: 'appid'
        },
        {
            name: 'rddid'
        },
        {
            name: 'rdd_name'
        },
        {
            name: 'cached_partitions', convert: convertComma
        },
        {
            name: 'partitions', convert: convertComma
        },
        {
            name: 'fraction_cached'
        },
        {
            name: 'memory', convert: byteConverter
        },
        {
            name: 'external_block_store', convert: byteConverter
        },
        {
            name: 'disk', convert: byteConverter
        },
        {
            name: 'use_disk'
        },
        {
            name: 'use_memory'
        },
        {
            name: 'use_external_block_store'
        },
        {
            name: 'deserializaed'
        },
        {
            name: 'replication'
        },
        {
            name: 'storage_level',
            convert: function(value, record) {
                var result = '';

                result += record.get('use_disk')?"Disk ":'';
                result += record.get('use_memory')?"Memory ":'';
                result += record.get('use_external_block_store')?"ExternalBlockStore ":'';
                result += record.get('deserializaed')?"Deserialized ":'';
                result += record.get('replication') + 'x Replicated';

                return result;
            }
        }
    ]
});

