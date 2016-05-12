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
Ext.define('Flamingo2.model.hawq.Column', {
    extend: 'Ext.data.Model',
    fields: ['columnName', 'dataType', 'length', 'default', 'isNull', 'check', 'comment', 'distributed'],
    validators: {
        columnName: {type: 'presence', message: message.msg('hawq.msg.warning.invalidcolumnname')},
        dataType: {type: 'presence', message: message.msg('hawq.msg.warning.invaliddatatype')}
    }
});