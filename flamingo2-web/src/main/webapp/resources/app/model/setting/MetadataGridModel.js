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
Ext.define('Flamingo2.model.setting.MetadataGridModel', {
    extend: 'Ext.data.Model',

    fields: [
        {mapping: 'cd_id', name: 'cd_id', type: 'string'},
        {mapping: 'cd_nm', name: 'cd_nm', type: 'string'},
        {mapping: 'cd_value', name: 'cd_value', type: 'string'},
        {mapping: 'prnts_cd_id', name: 'prnts_cd_id', type: 'string'},
        {mapping: 'sort_ordr', name: 'sort_ordr', type: 'int'},
        {mapping: 'rmark', name: 'rmark', type: 'string'},
        {mapping: 'use_yn', name: 'use_yn', type: 'string'},
        {mapping: 'children', name: 'children'},
        {mapping: 'leaf', name: 'leaf', type: 'boolean'}
    ],

    validators: {
        cd_nm: {type: 'presence', message: message.msg('menu.msg.enter_code_name')},
        cd_value: {type: 'presence', message: message.msg('menu.msg.enter_value')},
        sort_ordr: {type: 'format', matcher: /[0-9]+/, message: message.msg('menu.msg.enter_order')},
        use_yn: {type: 'inclusion', list: ['Y', 'N'], message: message.msg('menu.msg.select_use_yn')}
    }
});