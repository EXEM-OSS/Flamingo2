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
Ext.define('Flamingo2.view.hive.HiveModel', {
    extend: 'Ext.app.ViewModel',

    alias: 'viewmodel.hiveModel',

    data: {
        title: message.msg('hive.title'),
        panelCount: 1
    },

    stores: {
        examples: {
            fields: ['name', 'value'],
            data: [
                {
                    name: 'CREATE TABLE',
                    value: 'CREATE%20EXTERNAL%20TABLE%20TBL%5fNAME%20%28%0a%09col1%20TINYINT%2c%0a%09col2%20SMALLINT%2c%0a%09col3%20INT%2c%0a%09col4%20BIGINT%2c%0a%09col5%20FLOAT%2c%0a%09col6%20DOUBLE%2c%0a%09col7%20DECIMAL%2c%0a%09col8%20TIMESTAMP%2c%0a%09col9%20DATE%2c%0a%09col10%20STRING%2c%0a%09col11%20VARCHAR%2810%29%2c%0a%09col12%20CHAR%2810%29%2c%0a%09col13%20BOOLEAN%2c%0a%09col14%20BINARY%2c%0a%09col15%20ARRAY%3cSTRING%3e%2c%0a%09col16%20MAP%3cSTRING%2c%20STRING%3e%2c%0a%09col17%20STRUCT%3cfirstname%3aSTRING%2c%20nickname%3aSTRING%2c%20lastname%3aSTRING%3e%0a%29%20%0aROW%20FORMAT%20DELIMITED%20FIELDS%20TERMINATED%20BY%20%27%5ct%27%20%0aSTORED%20AS%20TEXTFILE%20%0aLOCATION%20%22PATH%22%3b'
                },
                {
                    name: 'CREATE TABLE PARTITION',
                    value: 'CREATE%20TABLE%20invites%20%28foo%20INT%2c%20bar%20STRING%29%20PARTITIONED%20BY%20%28ds%20STRING%29%3b'
                },
                {
                    name: 'RENAME TABLE',
                    value: 'ALTER%20TABLE%20events%20RENAME%20TO%20newevents%3b'
                },
                {
                    name: 'ADD COLUMN',
                    value: 'ALTER%20TABLE%20invites%20ADD%20COLUMNS%20%28new%5fcol2%20INT%20COMMENT%20%27a%20comment%27%29%3b'
                },
                {
                    name: 'REPLACE COLUMN',
                    value: 'ALTER%20TABLE%20invites%20REPLACE%20COLUMNS%20%28foo%20INT%2c%20bar%20STRING%2c%20baz%20INT%20COMMENT%20%27baz%20replaces%20new%5fcol2%27%29%3b'
                },
                {
                    name: 'LOAD DATA LOCAL',
                    value: 'LOAD%20DATA%20LOCAL%20INPATH%20%27%2e%2fexamples%2ffiles%2fkv1%2etxt%27%20OVERWRITE%20INTO%20TABLE%20pokes%3b'
                },
                {
                    name: 'LOAD DATA HDFS',
                    value: 'LOAD%20DATA%20INPATH%20%27%2fuser%2fmyname%2fkv2%2etxt%27%20OVERWRITE%20INTO%20TABLE%20invites%20PARTITION%20%28ds%3d%272008%2d08%2d15%27%29%3b'
                },
                {
                    name: 'INSERT HDFS DIRECTORY',
                    value: 'INSERT%20OVERWRITE%20DIRECTORY%20%27%2ftmp%2fhdfs%5fout%27%20SELECT%20a%2e%2a%20FROM%20invites%20a%20WHERE%20a%2eds%3d%272008%2d08%2d15%27%3b'
                },
                {
                    name: 'INSERT LOCAL DIRECTORY',
                    value: 'INSERT%20OVERWRITE%20LOCAL%20DIRECTORY%20%27%2ftmp%2flocal%5fout%27%20SELECT%20a%2e%2a%20FROM%20pokes%20a%3b'
                },
                {
                    name: 'SELECT - INSERT',
                    value: 'INSERT%20OVERWRITE%20TABLE%20events%20SELECT%20a%2ebar%2c%20count%28%2a%29%20FROM%20invites%20a%20WHERE%20a%2efoo%20%3e%200%20GROUP%20BY%20a%2ebar%3b'
                },
                {
                    name: 'SERDE',
                    value: '%0aCREATE%20TABLE%20apachelog%20%28%0a%20%20host%20STRING%2c%0a%20%20identity%20STRING%2c%0a%20%20user%20STRING%2c%0a%20%20time%20STRING%2c%0a%20%20request%20STRING%2c%0a%20%20status%20STRING%2c%0a%20%20size%20STRING%2c%0a%20%20referer%20STRING%2c%0a%20%20agent%20STRING%29%0aROW%20FORMAT%20SERDE%20%27org%2eapache%2ehadoop%2ehive%2eserde2%2eRegexSerDe%27%0aWITH%20SERDEPROPERTIES%20%28%0a%20%20%22input%2eregex%22%20%3d%20%22%28%5b%5e%5d%2a%29%20%28%5b%5e%5d%2a%29%20%28%5b%5e%5d%2a%29%20%28%2d%7c%5c%5c%5b%5e%5c%5c%5d%2a%5c%5c%5d%29%20%28%5b%5e%20%5c%22%5d%2a%7c%5c%22%5b%5e%5c%22%5d%2a%5c%22%29%20%28%2d%7c%5b0%2d9%5d%2a%29%20%28%2d%7c%5b0%2d9%5d%2a%29%28%3f%3a%20%28%5b%5e%20%5c%22%5d%2a%7c%5c%22%2e%2a%5c%22%29%20%28%5b%5e%20%5c%22%5d%2a%7c%5c%22%2e%2a%5c%22%29%29%3f%22%0a%29%0aSTORED%20AS%20TEXTFILE%3b'
                },
                {
                    name: 'STORED AS ORC',
                    value: 'ROW%20FORMAT%20SERDE%0a%20%20%27org%2eapache%2ehadoop%2ehive%2eql%2eio%2eorc%2eOrcSerde%27%0a%20%20STORED%20AS%20INPUTFORMAT%0a%20%20%27org%2eapache%2ehadoop%2ehive%2eql%2eio%2eorc%2eOrcInputFormat%27%0a%20%20OUTPUTFORMAT%0a%20%20%27org%2eapache%2ehadoop%2ehive%2eql%2eio%2eorc%2eOrcOutputFormat%27'
                },
                {
                    name: 'STORED AS PARQUET',
                    value: 'ROW%20FORMAT%20SERDE%0a%20%20%27org%2eapache%2ehadoop%2ehive%2eql%2eio%2eparquet%2eserde%2eParquetHiveSerDe%27%0a%20%20STORED%20AS%20INPUTFORMAT%0a%20%20%27org%2eapache%2ehadoop%2ehive%2eql%2eio%2eparquet%2eMapredParquetInputFormat%27%0a%20%20OUTPUTFORMAT%0a%20%20%27org%2eapache%2ehadoop%2ehive%2eql%2eio%2eparquet%2eMapredParquetOutputFormat%27'
                }
            ]
        }
    }
});