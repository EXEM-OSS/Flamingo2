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
Ext.define('Flamingo2.model.system.User', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'id', type: 'string', mapping: 'user_id'
        },
        {
            name: 'username', type: 'string', mapping: 'username'
        },
        {
            name: 'email', type: 'string', mapping: 'email'
        },
        {
            name: 'name', type: 'string', mapping: 'name'
        },
        {
            name: 'level', type: 'int', mapping: 'level'
        },
        {
            name: 'user_description', type: 'string', mapping: 'user_description'
        },
        {
            name: 'register_date', type: 'string', mapping: 'register_date',
            convert: function (value) {
                return dateFormat(new Date(value), 'yyyy-MM-dd HH:mm:ss');
            }
        },
        {
            name: 'update_date', type: 'string', mapping: 'update_date',
            convert: function (value) {
                return dateFormat(new Date(value), 'yyyy-MM-dd HH:mm:ss');
            }
        },
        {
            name: 'enabled', type: 'boolean', mapping: 'enabled'
        },
        {
            name: 'org_id', type: 'int', mapping: 'org_id'
        },
        {
            name: 'org_code', type: 'string', mapping: 'org_code'
        },
        {
            name: 'org_name', type: 'string', mapping: 'org_name'
        },
        {
            name: 'org_description', type: 'string', mapping: 'org_description'
        },
        {
            name: 'auth_id', type: 'int', mapping: 'auth_id'
        },
        {
            name: 'authority', type: 'string', mapping: 'authority'
        },
        {
            name: 'auth_name', type: 'string', mapping: 'auth_name'
        },
        {
            name: 'linux_user_home', type: 'string', mapping: 'linuxUserHome'
        },
        {
            name: 'hdfs_user_home', type: 'string', mapping: 'hdfsUserHome'
        },
        {
            name: 'user_group', type: 'string', mapping: 'userGroup'
        }
    ]
});