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
Ext.define('Flamingo2.view.system.menu.MenuModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.menuModel',

    data: {
        title: message.msg('menu.title')
    },

    stores: {
        menu: {
            type: 'tree',
            model: 'Flamingo2.model.system.Menu',
            autoLoad: false,
            proxy: {
                url: CONSTANTS.SYSTEM.MENU.SELECT,
                type: 'ajax',
                headers: {
                    'Accept': 'application/json'
                }
            },
            rootVisible: true,
            root: {
                text: message.msg('common.menu'),
                expanded: true,
                id: 'TOP'
            }
        },
        menuNode: {
            autoLoad: false,
            model: 'Flamingo2.model.system.MenuNode',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.SYSTEM.MENU.SELECT_NODE,
                headers: {'Accept': 'application/json'},
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        }
    }
});