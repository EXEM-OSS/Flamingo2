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
 * ViewModel for Flamingo2.view.hawq.browser.HawqBrowser
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.browser.HawqBrowser
 */
Ext.define('Flamingo2.view.hawq.browser.HawqBrowserModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.hawqBrowserModel',

    requires: ['Flamingo2.view.component.DynamicReader'],

    data: {
        title: 'HAWQ Editor'
    },

    stores: {
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
            },

            listeners: {
                beforeload: function (store, operation, eOpts) {
                    store.getProxy().extraParams.databaseName = query('hawqBrowser #hawqDatabaseCombobox').getValue();
                }
            }
        },

        hawqTable: {
            fields: ['table_catalog', 'table_schema', 'table_name', 'table_type', 'is_insertable_into', 'is_typed', 'relstorage', 'oid', 'object_type', 'table_size', 'size'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.TABLES,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },

        hawqView: {
            fields: ['table_catalog', 'table_schema', 'table_name', 'table_type', 'is_insertable_into', 'is_typed', 'object_type'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.VIEWS,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },

        hawqFunction: {
            fields: ['oid', 'rolname', 'current_database', 'nspname', 'proname', 'lanname', 'pronargs', 'prorettype', 'proargtypes', 'prosrc', 'procdesc', 'object_type'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.FUNCTIONS,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },

        hawqExternalTable: {
            fields: ['table_catalog', 'table_schema', 'table_name', 'table_type', 'is_insertable_into', 'is_typed', 'relstorage', 'oid', 'object_type'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.EXTERNAL_TABLES,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },

        hawqColumn: {
            fields: ['table_catalog', 'table_schema', 'table_name', 'column_name', 'ordinal_position', 'column_default', 'is_nullable', 'data_type', 'character_maximum_length', 'column_comment'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.COLUMNS,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },

        hawqObjectMetadata: {
            fields: ['key', 'value'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HAWQ.BROWSER.OBJECT_METADATAS,
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },

        hawqTemplate: {
            fields: ['name', 'value'],
            data: [
                {
                    name: 'CREATE TABLE',
                    value: "CREATE%20TABLE%20public.test_table%0A(%0A%20%20%20%20test_column%20integer%20DEFAULT%20'1'%20CHECK%20(test_column%20%3E%200)%2C%0A%20%20%20%20test_column2%20text%0A)%0AWITH%0A(%0A%20%20%20%20oids%3Dtrue%0A)%0ADISTRIBUTED%20RANDOMLY%3B"
                },
                {
                    name: 'CREATE EXTERNAL TABLE file',
                    value: "CREATE%20EXTERNAL%20TABLE%20retail_demo.categories_dim_file%0A(%0A%20%20%20%20category_id%20integer%2C%0A%20%20%20%20category_name%20character%20varying(400)%0A)%0ALOCATION%20('file%3A%2F%2Fpivhdsne.localdomain%2Fpivotal-samples%2Fsample-data%2Fretail_demo%2Fcategories_dim.tsv')%20%0AFORMAT%20'TEXT'%20(DELIMITER%20%3D%20E'%5Ct')%3B"
                },
                {
                    name: 'CREATE EXTERNAL TABLE gpfdist',
                    value: "CREATE%20EXTERNAL%20TABLE%20ext_customer%0A(%0A%20%20%20%20id%20int%2C%0A%20%20%20%20name%20text%2C%0A%20%20%20%20sponsor%20text%0A)%0ALOCATION%20(%20'gpfdist%3A%2F%2Fpivhdsne%3A8081%2F*.txt'%20)%20%0AFORMAT%20'TEXT'%20(%20DELIMITER%20'%20'%20NULL%20'%7C')%0ALOG%20ERRORS%20INTO%20err_customer%20SEGMENT%20REJECT%20LIMIT%205%3B"
                },
                {
                    name: 'CREATE EXTERNAL TABLE hbase',
                    value: "CREATE%20EXTERNAL%20TABLE%20retail_demo.categories_dim_hbase%0A(%0A%20%20%20%20--category_id%20integer%2C%0A%20%20%20%20recordkey%20integer%2C%0A%20%20%20%20%22cf1%3Acategory_name%22%20character(400)%0A)%0ALOCATION%20('pxf%3A%2F%2Fpivhdsne%3A50070%2Fcategories_dim%3FPROFILE%3Dhbase')%0AFORMAT%20'CUSTOM'%20(formatter%3D'pxfwritable_import')%3B"
                },
                {
                    name: 'CREATE EXTERNAL TABLE hdfs',
                    value: "CREATE%20EXTERNAL%20TABLE%20retail_demo.categories_dim_pxf%0A(%0A%20%20%20%20category_id%20integer%2C%0A%20%20%20%20category_name%20character%20varying(400)%0A)%0ALOCATION%20('pxf%3A%2F%2Fpivhdsne%3A50070%2Fretail_demo%2Fcategories_dim%2Fcategories_dim.tsv.gz%3Fprofile%3DHdfsTextSimple')%20%0AFORMAT%20'TEXT'%20(DELIMITER%20%3D%20E'%5Ct')%3B"
                },
                {
                    name: 'CREATE EXTERNAL WEB TABLE',
                    value: "CREATE%20EXTERNAL%20WEB%20TABLE%20web_table_test%20(street%20text,city%20text,zip%20text,state%20text,beds%20int,baths%20int,sq__ft%20int,type%20text,sale_date%20timestamp%20with%20time%20zone,price%20money,latitude%20double%20precision,longitude%20double%20precision%20)%20LOCATION%20('http://samplecsvs.s3.amazonaws.com/Sacramentorealestatetransactions.csv')%20FORMAT%20'CSV'%20(HEADER);"
                }
            ]
        },

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