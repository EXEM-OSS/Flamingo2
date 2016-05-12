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
Ext.define('Flamingo2.view.admin.share.Model', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.admin.shareModel',

    stores: {
        userEvent: {
            autoLoad: false,
            pageSize: 30,

            fields: [
                {name: 'id', type: 'integer'},
                {name: 'name', type: 'string'},
                {name: 'registrationDate', type: 'string'},
                {name: 'status', type: 'string'},
                {name: 'message', type: 'string'},
                {name: 'isSee', type: 'boolean'},
                {name: 'identifier', type: 'string'},
                {name: 'referenceId', type: 'integer'},
                {name: 'username', type: 'string'},
                {name: 'yyyy', type: 'string'},
                {name: 'mm', type: 'string'}
            ],

            proxy: {
                type: 'ajax',
                url: CONSTANTS.USER.PREFERENCE.EVENT.LIST,
                method: 'GET',
                extraParams: {
                    'conditionKey': '',
                    'conditionValue': ''
                },
                headers: {
                    'Accept': 'application/json'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            }
        }
    }
});