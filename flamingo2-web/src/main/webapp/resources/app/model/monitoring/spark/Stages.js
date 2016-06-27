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
Ext.define('Flamingo2.model.monitoring.spark.Stages', {
    extend: 'Ext.data.Model',
    fields: [
        {
            name: 'appid'
        },
        {
            name: 'jobid'
        },
        {
            name: 'stageid'
        },
        {
            name: 'attemptid'
        },
        {
            name: 'submitted'
        },
        {
            name: 'completed'
        },
        {
            name: 'name'
        },
        {
            name: 'tasks', convert: convertComma
        },
        {
            name: 'task_complete'
        },
        {
            name: 'task_failed'
        },
        {
            name: 'input_bytes', convert: byteConverter
        },
        {
            name: 'input_records', convert: convertComma
        },
        {
            name: 'output_bytes', convert: byteConverter
        },
        {
            name: 'output_records', convert: convertComma
        },
        {
            name: 'shuffle_read_bytes', convert: byteConverter
        },
        {
            name: 'shuffle_read_records', convert: convertComma
        },
        {
            name: 'shuffle_write_bytes', convert: byteConverter
        },
        {
            name: 'shuffle_write_recrods', convert: convertComma
        },
        {
            name: 'sorter'
        }
    ]
});