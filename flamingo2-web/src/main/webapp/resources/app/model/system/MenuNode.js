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
Ext.define('Flamingo2.model.system.MenuNode', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'menu_id', type: 'string', mapping: 'menu_id'
        },
        {
            name: 'menu_nm',
            type: 'string',
            mapping: 'menu_nm',
            validators: {type: 'presence', message: message.msg('system.menu.message.menu_nm')}
        },
        {
            name: 'prnts_menu_id', type: 'string', mapping: 'prnts_menu_id'
        },
        {
            name: 'sort_ordr',
            type: 'int',
            mapping: 'sort_ordr',
            validators: {type: 'presence', message: message.msg('system.menu.message.sort_ordr')}
        },
        {
            name: 'menu_ns',
            type: 'string',
            mapping: 'menu_ns',
            validators: {type: 'presence', message: message.msg('system.menu.message.menu_ns')}
        },
        {
            name: 'use_yn', type: 'string', mapping: 'use_yn', defaultValue: 'Y'
        },
        {
            name: 'icon_css_nm', type: 'string', mapping: 'icon_css_nm'
        },
        {
            name: 'menu_nm_ko_kr', type: 'string', mapping: 'menu_nm_ko_kr'
        },
        {
            name: 'menu_nm_en_us', type: 'string', mapping: 'menu_nm_en_us'
        },
        {
            name: 'menu_nm_ja_jp', type: 'string', mapping: 'menu_nm_ja_jp'
        },
        {
            name: 'menu_nm_zh_cn', type: 'string', mapping: 'menu_nm_zh_cn'
        }
    ]
});