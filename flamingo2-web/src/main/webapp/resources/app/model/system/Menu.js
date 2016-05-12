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
Ext.define('Flamingo2.model.system.Menu', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'menu_id', type: 'string', mapping: 'menu_id'
        },
        {
            name: 'menu_nm', type: 'string', mapping: 'menu_nm'
        },
        {
            name: 'prnts_menu_id', type: 'string', mapping: 'prnts_menu_id'
        },
        {
            name: 'leaf', type: 'boolean', mapping: 'leaf'
        },
        {
            name: 'lvl', type: 'string', mapping: 'lvl'
        },
        {
            name: 'sort_ordr', type: 'int', mapping: 'sort_ordr'
        },
        {
            name: 'menu_ns', type: 'string', mapping: 'menu_ns'
        },
        {
            name: 'use_yn', type: 'string', mapping: 'use_yn'
        },
        {
            name: 'text', type: 'string', mapping: 'text'
        },
        {
            name: 'children', mapping: 'children'
        }
    ]
});