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
 * NodeMeta Model
 *
 * @class
 * @extends Ext.data.Model
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.model.designer.NodeMeta', {
    extend: 'Ext.data.Model',
    idProperty: 'identifier',
    fields: [
        {name: 'type', type: 'string'},
        {name: 'icon', type: 'string'},
        {name: 'jobType', type: 'string'},
        {name: 'identifier', type: 'string'},
        {name: 'name', type: 'string'},
        {name: 'qualifierLabel', type: 'string'},
        {name: 'description', type: 'string'},
        {name: 'isCheckColumns', type: 'boolean', defaultValue: false},
        {name: 'fixedInputColumns', type: 'boolean', defaultValue: false},
        {name: 'fixedOutputColumns', type: 'boolean', defaultValue: false},
        {name: 'readOnlyOutputColumns', type: 'boolean', defaultValue: false},
        {name: 'ignoreInput', type: 'boolean', defaultValue: false},
        {name: 'ignoreOutput', type: 'boolean', defaultValue: false},
        {name: 'minPrevNodeCounts', type: 'int', defaultValue: -1},
        {name: 'maxPrevNodeCounts', type: 'int', defaultValue: -1},
        {name: 'minNextNodeCounts', type: 'int', defaultValue: -1},
        {name: 'maxNextNodeCounts', type: 'int', defaultValue: -1},
        {name: 'notAllowedPrevTypes', type: 'string', defaultValue: ''},
        {name: 'notAllowedNextTypes', type: 'string', defaultValue: ''},
        {name: 'notAllowedPrevNodes', type: 'string', defaultValue: ''},
        {name: 'notAllowedNextNodes', type: 'string', defaultValue: ''},
        {name: 'className', type: 'string'},
        {name: 'classpaths', type: 'string'},
        {name: 'disabled', type: 'boolean', defaultValue: false},
        {name: 'defaultProperties'}
    ],
    validators: [
        {type: 'presence', field: 'type'},
        {type: 'presence', field: 'identifier'},
        {type: 'presence', field: 'name'},
        {
            type: 'inclusion',
            field: 'type',
            list: ['START', 'END', 'IN', 'OUT', 'HADOOP', 'ETL', 'STATISTICS', 'MINING', 'ALG', 'OTHERS']
        }
    ]
});