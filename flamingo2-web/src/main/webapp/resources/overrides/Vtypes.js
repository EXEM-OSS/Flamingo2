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
Ext.define('Override.form.field.VTypes', {
    override: 'Ext.form.field.VTypes',

    exceptdoublecolon: function (v) {
        return !/::/g.test(v);
    },
    exceptdoublecolonText: 'Cannot use double colon.',
    exceptdoublecolonMask: /[^::]/,

    exceptcomma: function (v) {
        return !/,/g.test(v);
    },
    exceptcommaText: 'Cannot use comma.',
    exceptcommaMask: /[^,]/,

    exceptcommaspace: function (v) {
        return !/,|\s/g.test(v);
    },
    exceptcommaspaceText: 'Cannot use comma and space',
    exceptcommaspaceMask: /[^,\s]/,

    ipaddress: function (v) {
        return /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/.test(v);
    },
    ipaddressText: 'Must be a numeric IP address',
    ipaddressMask: /[\d\.]/i,

    port: function (v) {
        return /[0-9]/.test(v);
    },
    portText: 'Must be a numeric 0 - 65535',

    commaseperatednum: function (v) {
        return /^(-?)\d+(,\d+)*$/.test(v);
    },
    commaseperatednumText: 'Must be comma separated numerics. e.g. 1,2,3',

    numeric: function (v) {
        return /^\d+$/.test(v);
    },
    numericText: 'Must be a numeric',

    decimalpoint: function (v) {
        return /^[0-9]+(.[0-9]+)?$/.test(v);
    },
    decimalpointText: 'Must be a numeric or a decimal point',

    dateRange: function (val, field) {
        var date = field.parseDate(val);

        if (!date) {
            return false;
        }

        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) { // for EndDate
            var start = Ext.ComponentQuery.query('#' + field.startDateField)[0];
            start.setMaxValue(date);
            start.validate();
            this.dateRangeMax = date;
        } else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) { // for StartDate
            var end = Ext.ComponentQuery.query('#' + field.endDateField)[0];
            end.setMinValue(date);
            end.validate();
            this.dateRangeMin = date;
        }
        return true;
    }
});