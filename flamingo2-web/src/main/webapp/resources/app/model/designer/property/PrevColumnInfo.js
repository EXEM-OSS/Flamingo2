/*
 * Copyright (C) 2011  Flamingo Project (http://www.cloudine.io).
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
 * PrevColumnInfo Model
 *
 * @class
 * @extends Ext.data.Model
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.model.designer.property.PrevColumnInfo', {
    extend: 'Ext.data.Model',
    fields: ['prevQualifier', 'prevColumnNames', 'prevColumnKorNames', 'prevColumnTypes', 'prevColumnDescriptions'],
    validators: [
        {type: 'presence', field: 'prevColumnNames'},
        {type: 'presence', field: 'prevColumnTypes'}
    ]
});