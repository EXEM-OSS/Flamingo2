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
Ext.apply('Ext.data.Types', {
    FLOATORSTRING: {
        convert: function (v, n) {
            v = Ext.isNumeric(v) ? Number(v) : v;
            return v;
        },
        sortType: function (v) {
            v = Ext.isNumeric(v) ? Number(v) : v;
            return v;
        },
        type: 'floatOrString'
    }
});

Ext.define('Flamingo2.view.component.DynamicReader', {
    extend: 'Ext.data.reader.Json',
    alias: 'reader.dynamicReader',
    alternateClassName: 'Ext.data.reader.DynamicReader',

    readRecords: function (data) {
        var me = this;
        var total = data.total;

        data = me.getRoot(data); // for JSON root feature

        if (data.length > 0) {
            var item = data[0];
            var fields = new Array();
            var columns = new Array();
            var p;

            for (p in item) {
                if (p && p != undefined) {
                    // floatOrString type is only an option
                    // You can make your own data type for more complex situations
                    // or set it just to 'string'
                    fields.push({name: p, type: 'string'});
                    columns.push({text: p, dataIndex: p, flex: 1, maxWidth: 400});
                }
            }

            data.metaData = {fields: fields, columns: columns};
            data.total = total;
        }

        return this.callParent([data]);
    }
});