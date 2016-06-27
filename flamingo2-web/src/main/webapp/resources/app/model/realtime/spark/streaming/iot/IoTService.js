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
Ext.define('Flamingo2.model.realtime.spark.streaming.iot.IoTService', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'id', type: 'string', mapping: 'treeId'
        },
        {
            name: 'treeId', mapping: 'id'
        },
        {
            name: 'text'
        },
        {
            name: 'nodeName'
        },
        {
            name: 'leaf'
        },
        {
            name: 'serviceId'
        },
        {
            name: 'serviceName'
        },
        {
            name: 'serviceTypeId'
        },
        {
            name: 'serviceTypeName'
        },
        {
            name: 'deviceTypeId'
        },
        {
            name: 'deviceTypeName'
        },
        {
            name: 'columnsType'
        },
        {
            name: 'columnName'
        },
        {
            name: 'columnType'
        },
        {
            name: 'filtering'
        },
        {
            name: 'masking'
        },
        {
            name: 'orderby'
        },
        {
            name: 'workDate',
            convert: function (value) {
                return dateFormat2(value);
            }
        }
    ]
});

