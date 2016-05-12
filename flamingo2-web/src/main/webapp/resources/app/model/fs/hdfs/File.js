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
Ext.define('Flamingo2.model.fs.hdfs.File', {
    extend: 'Ext.data.Model',
    fields: [
        {
            name: 'id',
            convert: function (value, record) {
                if (record.get('path') == '/') {
                    return record.get('path') + record.get('filename');
                } else {
                    return record.get('path') + '/' + record.get('filename');
                }
            }
        },
        {
            name: 'filename'
        },
        {
            name: 'length'
        },
        {
            name: 'modificationTime',
            convert: function (value) {
                return Ext.Date.format(new Date(value), 'Y-m-d H:i:s');
            }
        },
        {
            name: 'permission'
        },
        {
            name: 'group'
        },
        {
            name: 'owner'
        },
        {
            name: 'replication'
        },
        {
            name: 'blockSize'
        },
        {
            name: 'spaceConsumed'
        },
        {
            name: 'path'
        }
    ]
});