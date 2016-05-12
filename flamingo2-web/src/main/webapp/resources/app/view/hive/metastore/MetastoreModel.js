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
 * This class is the view model for the Main view of the application.
 */
Ext.define('Flamingo2.view.hive.metastore.MetastoreModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.hiveMetastoreModel',

    data: {
        btnOKText: message.msg('common.create')
    },

    stores: {
        databases: {
            autoLoad: false,
            fields: [
                'database'
            ],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HIVE.METASTORE.GET_DATABASES,
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },
        listeners: {
            load: 'onDatabasesLoad'
        },
        dbproperties: {
            fields: ['key', 'value']
        },
        tables: {
            autoLoad: false,
            fields: [
                'tableName',
                'createTime',
                'owner',
                'lastAccessTime',
                'retention',
                'viewOriginalText',
                'viewExpandedText',
                'tableType'
            ],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HIVE.METASTORE.GET_TABLES,
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },
        columns: {
            autoLoad: false,
            model: 'Flamingo2.model.hive.ColumnInfo',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HIVE.METASTORE.GET_COLUMNS,
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },
        partitions: {
            autoLoad: false,
            model: 'Flamingo2.model.hive.ColumnInfo',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.HIVE.METASTORE.GET_PARTITIONS,
                reader: {
                    type: 'json',
                    rootProperty: 'list'
                }
            }
        },
        dataType: {
            fields: ['typeId', 'typeString'],
            data: [
                ['tinyint', 'tinyint'],
                ['smallint', 'smallint'],
                ['int', 'int'],
                ['bigint', 'bigint'],
                ['boolean', 'boolean'],
                ['float', 'float'],
                ['double', 'double'],
                ['string', 'string'],
                ['timestamp', 'timestamp'],
                ['binary', 'binary'],
                ['struct', 'struct'],
                ['map', 'map'],
                ['array', 'array']
            ]
        },
        dataTypeWithoutComplex: {
            fields: ['typeId', 'typeString'],
            data: [
                ['tinyint', 'tinyint'],
                ['smallint', 'smallint'],
                ['int', 'int'],
                ['bigint', 'bigint'],
                ['boolean', 'boolean'],
                ['float', 'float'],
                ['double', 'double'],
                ['string', 'string']
            ]
        },
        structType: {
            model: 'Flamingo2.model.hive.StructType'
        },
        delimiter: {
            autoLoad: true,
            fields: ['octal', 'value', 'symbol', 'description'],
            data: [
                {
                    "octal": "011",
                    "symbol": "HT",
                    value: '\t',
                    "description": "Tab"
                },
                {
                    "octal": "015",
                    value: '\n',
                    "symbol": "CR",
                    "description": "Carriage Return"
                },
                {
                    "octal": "054",
                    "symbol": ",",
                    value: '\054',
                    "description": "Comma"
                },
                {
                    "octal": "073",
                    "symbol": ";",
                    value: '\073',
                    "description": "Semicolon"
                },
                {
                    octal: '000',
                    value: '\000',
                    symbol: 'NUL',
                    description: 'Null char'
                },
                {
                    "octal": "001",
                    value: '\001',
                    "symbol": "SOH",
                    "description": "Start of Heading"
                },
                {
                    "octal": "002",
                    value: '\002',
                    "symbol": "STX",
                    "description": "Start of Text"
                },
                {
                    "octal": "003",
                    value: '\003',
                    "symbol": "ETX",
                    "description": "End of Text"
                },
                {
                    "octal": "004",
                    value: '\004',
                    "symbol": "EOT",
                    "description": "End of Transmission"
                },
                {
                    "octal": "005",
                    value: '\005',
                    "symbol": "ENQ",
                    "description": "Enquiry"
                },
                {
                    "octal": "006",
                    value: '\006',
                    "symbol": "ACK",
                    "description": "Acknowledgment"
                },
                {
                    "octal": "007",
                    value: '\007',
                    "symbol": "BEL",
                    "description": "Bell"
                },
                {
                    "octal": "010",
                    value: '\010',
                    "symbol": "BS",
                    "description": "Back Space"
                },
                {
                    "octal": "012",
                    value: '\012',
                    "symbol": "LF",
                    "description": "Line Feed"
                },
                {
                    "octal": "013",
                    value: '\013',
                    "symbol": "VT",
                    "description": "Vertical Tab"
                },
                {
                    "octal": "014",
                    "symbol": "FF",
                    "description": "Form Feed"
                },
                {
                    "octal": "016",
                    "symbol": "SO",
                    "description": "Shift Out / X-On"
                },
                {
                    "octal": "017",
                    "symbol": "SI",
                    "description": "Shift In / X-Off"
                },
                {
                    "octal": "020",
                    "symbol": "DLE",
                    "description": "Data Line Escape"
                },
                {
                    "octal": "021",
                    "symbol": "DC1",
                    "description": "Device Control 1 (oft. XON)"
                },
                {
                    "octal": "022",
                    "symbol": "DC2",
                    "description": "Device Control 2"
                },
                {
                    "octal": "023",
                    "symbol": "DC3",
                    "description": "Device Control 3 (oft. XOFF)"
                },
                {
                    "octal": "024",
                    "symbol": "DC4",
                    "description": "Device Control 4"
                },
                {
                    "octal": "025",
                    "symbol": "NAK",
                    "description": "Negative Acknowledgement"
                },
                {
                    "octal": "026",
                    "symbol": "SYN",
                    "description": "Synchronous Idle"
                },
                {
                    "octal": "027",
                    "symbol": "ETB",
                    "description": "End of Transmit Block"
                },
                {
                    "octal": "030",
                    "symbol": "CAN",
                    "description": "Cancel"
                },
                {
                    "octal": "031",
                    "symbol": "EM",
                    "description": "End of Medium"
                },
                {
                    "octal": "032",
                    "symbol": "SUB",
                    "description": "Substitute"
                },
                {
                    "octal": "033",
                    "symbol": "ESC",
                    "description": "Escape"
                },
                {
                    "octal": "034",
                    "symbol": "FS",
                    "description": "File Separator"
                },
                {
                    "octal": "035",
                    "symbol": "GS",
                    "description": "Group Separator"
                },
                {
                    "octal": "036",
                    "symbol": "RS",
                    "description": "Record Separator"
                },
                {
                    "octal": "037",
                    "symbol": "US",
                    "description": "Unit Separator"
                },
                {
                    "octal": "040",
                    "symbol": " ",
                    "description": "Space"
                },
                {
                    "octal": "041",
                    "symbol": "!",
                    "description": "Exclamation mark"
                },
                {
                    "octal": "042",
                    "symbol": "",
                    "description": "Double quotes (or speech marks)"
                },
                {
                    "octal": "043",
                    "symbol": "#",
                    "description": "Number"
                },
                {
                    "octal": "044",
                    "symbol": "$",
                    "description": "Dollar"
                },
                {
                    "octal": "045",
                    "symbol": "%",
                    "description": "Procenttecken"
                },
                {
                    "octal": "046",
                    "symbol": "&",
                    "description": "Ampersand"
                },
                {
                    "octal": "047",
                    "symbol": "'",
                    "description": "Single quote"
                },
                {
                    "octal": "052",
                    "symbol": "*",
                    "description": "Asterisk"
                },
                {
                    "octal": "053",
                    "symbol": "+",
                    "description": "Plus"
                },

                {
                    "octal": "055",
                    "symbol": "-",
                    "description": "Hyphen"
                },
                {
                    "octal": "056",
                    "symbol": ".",
                    "description": "Period, dot or full stop"
                },
                {
                    "octal": "057",
                    "symbol": "/",
                    "description": "Slash or divide"
                },
                {
                    "octal": "072",
                    "symbol": ":",
                    "description": "Colon"
                },
                {
                    "octal": "074",
                    "symbol": "<",
                    "description": "Less than (or open angled bracket)"
                },
                {
                    "octal": "075",
                    "symbol": "=",
                    "description": "Equals"
                },
                {
                    "octal": "076",
                    "symbol": ">",
                    "description": "Greater than (or close angled bracket)"
                },
                {
                    "octal": "077",
                    "symbol": "?",
                    "description": "Question mark"
                },
                {
                    "octal": "100",
                    "symbol": "@",
                    "description": "At symbol"
                },
                {
                    "octal": "134",
                    "symbol": "\\",
                    "description": "Backslash"
                },
                {
                    "octal": "136",
                    "symbol": "^",
                    "description": "Caret-circumflex"
                },
                {
                    "octal": "137",
                    "symbol": "_",
                    "description": "Underscore"
                },
                {
                    "octal": "140",
                    "symbol": "`",
                    "description": "Graveaccent"
                },
                {
                    "octal": "174",
                    "symbol": "|",
                    "description": "Verticalbar"
                },
                {
                    "octal": "176",
                    "symbol": "~",
                    "description": "Equivalencysign-tilde"
                },
                {
                    "octal": "177",
                    "symbol": "",
                    "description": "Delete"
                }
            ]
        }
    }
});