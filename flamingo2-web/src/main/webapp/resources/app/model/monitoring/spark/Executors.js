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
Ext.define('Flamingo2.model.monitoring.spark.Executors', {
    extend: 'Ext.data.Model',
    fields: [
        {
            name: 'appid'
        },
        {
            name: 'executorid'
        },
        {
            name: 'address'
        },
        {
            name: 'port'
        },
        {
            name: 'rdd_blocks'
        },
        {
            name: 'max_memory', convert: byteConverter
        },
        {
            name: 'memory_used', convert: byteConverter
        },
        {
            name: 'disk_used'
        },
        {
            name: 'active_tasks'
        },
        {
            name: 'failed_tasks', convert: convertComma
        },
        {
            name: 'completed_tasks', convert: convertComma
        },
        {
            name: 'total_tasks', convert: convertComma
        },
        {
            name: 'total_duration'
        },
        {
            name: 'total_input_bytes', convert: byteConverter
        },
        {
            name: 'total_shuffle_read', convert: byteConverter
        },
        {
            name: 'total_shuffle_write', convert: byteConverter
        },
        {
            name: 'total_cores', convert: convertComma
        },
        {
            name: 'stdout_url'
        },
        {
            name: 'stderr_url'
        }
    ]
});