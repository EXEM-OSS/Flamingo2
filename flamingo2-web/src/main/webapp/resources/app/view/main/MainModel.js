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
Ext.define('Flamingo2.view.main.MainModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.main',

    data: {
        name: 'Main',
        engineLoaded: false,
        menuLoaded: false
    },

    formulas: {
        license: {
            get: function (get) {
                var html = '';

                if (LICENSE.DAYS < 15) {
                    html += format('<div style="float: right;color: red;margin-right: 10px;">Flamingo License 기간이 {0}일 남았습니다.</div>', LICENSE.DAYS);
                }

                if (LICENSE.TRIAL == 'true') {
                    html += '<div style="float: right;color: red;margin-right: 10px;">Trial Version</div>';
                }

                return html;
            }
        }
    },

    stores: {
        menu: {
            autoLoad: true,

            fields: ['menu_id', 'menu_ns', 'menu_nm', 'icon_css_nm', 'leaf', 'children'],

            proxy: {
                type: 'ajax',
                url: CONSTANTS.SYSTEM.MENU.SELECT,
                headers: {'Accept': 'application/json'},
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            },
            listeners: {
                load: 'onMenuStoreLoad'
            }
        },

        engine: {
            storeId: 'mainEngine',
            itemId: 'mainEngine', // FIXME
            fields: [
                'id',
                'name',
                'ip',
                'port',
                'hsAddress',
                'hsPort',
                'rmAddress',
                'rmPort',
                'nnAddress',
                'nnPort',
                'rmAgentAddress',
                'rmAgentPort',
                'nnAgentAddress',
                'nnAgentPort',
                'hiveServerUrl',
                'hiveServerUsername',
                'hiveMetastoreAddress',
                'hiveMetastorePort',
                'historyServerUrl'
            ],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.CONFIG.ENGINES,
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                },
                extraParams: { // Workflow Engine 목록 필터링 파라미터. 기본값은 모두 다 보임.
                }
            },
            listeners: {
                load: 'onEngineStoreLoad'
            }
        }
    }
});