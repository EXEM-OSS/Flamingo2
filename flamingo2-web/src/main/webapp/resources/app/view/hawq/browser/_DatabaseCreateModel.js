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
/**
 * ViewModel for Flamingo2.view.hawq.browser._DatabaseCreate
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._DatabaseCreate
 */
Ext.define('Flamingo2.view.hawq.browser._DatabaseCreateModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel._hawqDatabaseCreateModel',

    stores: {
        hawqEncoding: {
            fields: ['displ', 'value'],
            data: [
                {displ: 'EUC_KR', value: 'EUC_KR'},
                {displ: 'LATIN1', value: 'LATIN1'},
                {displ: 'UTF8', value: 'UTF8'}
            ]
        },

        hawqUser: {
            fields: ['_username'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.USERS,
                method: 'GET',
                extraParams: {
                    clusterName: ENGINE.id
                },
                headers: {
                    'Accept': 'application/json'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        }
    }
});