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
 * ViewModel for Flamingo2.view.hawq.browser._TableCreate
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser._TableCreate
 */
Ext.define('Flamingo2.view.hawq.browser._TableCreateModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel._hawqTableCreateModel',

    requires: ['Flamingo2.view.component.DynamicReader'],

    stores: {
        hawqTablespace: {
            fields: ['spcname'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.TABLESPACES,
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
        },

        hawqUser: {
            autoLoad: false,
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
        },

        hawqDatabase: {
            fields: ['databaseName'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.DATABASES,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },

        hawqSchema: {
            fields: ['schemaName'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.SCHEMAS,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },

        hawqColumn: {
            model: 'Flamingo2.model.hawq.Column',
            data: []
        },

        hawqAppendOnly: {
            fields: ['displ', 'value'],
            data: [
                {displ: 'TRUE', value: true}
            ]
        },

        hawqOrientation: {
            fields: ['displ', 'value'],
            data: [
                {displ: 'COLUMN', value: 'COLUMN'},
                {displ: 'ROW', value: 'ROW'},
                {displ: 'PARQUET', value: 'PARQUET'}
            ]
        },

        hawqCompressType: {
            fields: ['displ', 'value'],
            data: [
                {displ: 'ZLIB', value: 'ZLIB'},
                {displ: 'QUICKLZ', value: 'QUICKLZ'},
                {displ: 'SNAPPY', value: 'SNAPPY'},
                {displ: 'GZIP', value: 'GZIP'},
                {displ: 'NONE', value: 'NONE'}
            ]
        },

        hawqDataType: {
            fields: ['name', 'alias', 'value',
                {
                    name: 'displ',
                    convert: function (value, record) {
                        var name = trim(record.get('name')),
                            alias = trim(record.get('alias')),
                            displ = '';
                        if (!isBlank(alias)) {
                            displ = name + '(=' + alias + ')';
                        } else {
                            displ = name;
                        }
                        return displ;
                    }
                }
            ],
            data: [
                {name: 'bigint', alias: 'int8', value: 'bigint'},
                {name: 'character [ (n) ]', alias: 'char', value: 'character'},
                {name: 'character varying [ (n) ]', alias: 'varchar', value: 'character varying'},
                {name: 'date', alias: ' ', value: 'date'},
                {name: 'decimal [ (p, s) ]', alias: 'numeric', value: 'decimal'},
                {name: 'integer', alias: 'int, int4', value: 'integer'},
                {name: 'real(float)', alias: 'float4', value: 'real'},
                {name: 'smallint', alias: 'int2', value: 'smallint'},
                {name: 'text', alias: ' ', value: 'text'},
                {name: 'time [ (p) ] [ without time zone ]', alias: ' ', value: 'time without time zone'},
                {name: 'time [ (p) ] with time zone', alias: 'timetz', value: 'time with time zone'},
                {name: 'timestamp [ (p) ] [without time zone ]', alias: ' ', value: 'timestamp without time zone'},
                {name: 'timestamp [ (p) ] with time zone', alias: 'timestamptz', value: 'timestamp with time zone'}
            ]
        },

        hawqPartitionRange: {
            model: 'Flamingo2.model.hawq.PartitionRange',
            data: []
        },

        hawqPartitionList: {
            model: 'Flamingo2.model.hawq.PartitionList',
            data: []
        }
    }
});