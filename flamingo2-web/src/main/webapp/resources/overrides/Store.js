/*
 * Copyright (C) 2011  Flamingo Project (http://www.opencloudengine.org).
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
Ext.define("Flamingo2.script.data.Store", {
    override : "Ext.data.Store",
    records2Json : function(records) {
        var result = [];

        for (var j = 0; j < records.length; j++) {
            var models = {};
            var items = records[j].fields.items;
            for (var i = 0; i < items.length; i++) {
                var name = items[i].name;
                models[name] = records[j].get(name);
            }
            result.push(models);
        }

        return Ext.encode(result);

    },
    getAllJson : function() {
        var jsons = {
            newRecords: this.records2Json(this.getNewRecords()),
            updatedRecords: this.records2Json(this.getUpdatedRecords()),
            removedRecords: this.records2Json(this.getRemovedRecords())
        };

        return jsons;
    },

    getNewJson : function() {
        return this.records2Json(this.getNewRecords());
    },
    getUpdatedJson : function() {
        return this.records2Json(this.getUpdatedRecords());
    },
    getRemovedJson : function() {
        return this.records2Json(this.getRemovedRecords());
    },
    isValid : function() {
        var i;
        var me = this;

        for ( i = 0; i < me.getCount(); i++) {
            if (me.getAt(i).isValid() == false) {
                var errors = me.getAt(i).validate();
                Ext.Msg.alert("확인", String(i + 1) + '행에 ' + errors.items[0].message);
                return false;
            };
        }

        return true;
    }
});