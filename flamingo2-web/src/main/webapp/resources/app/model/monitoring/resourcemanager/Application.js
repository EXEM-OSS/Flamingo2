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
Ext.define('Flamingo2.model.monitoring.resourcemanager.Application', {
    extend: 'Ext.data.Model',
    fields: [
        {
            name: 'applicationId'
        },
        {
            name: 'user'
        },
        {
            name: 'name'
        },
        {
            name: 'queue'
        },
        {
            name: 'yarnApplicationState'
        },
        {
            name: 'finalApplicationStatus',
            convert: function (value, model) {
                if (value == 'UNDEFINED') {
                    return '';
                }
                return value;
            }
        },
        {
            name: 'progress',
            convert: function (value, model) {
                if (value) {
                    return value * 100;
                }
                return value;
            }
        },
        {
            name: 'trackingUrl'
        },
        {
            name: 'applicationType'
        },
        {
            name: 'applicationTags'
        },
        {
            name: 'startTime',
            convert: function (value, model) {
                return dateFormat2(value);
            }
        },
        {
            name: 'finishTime',
            convert: function (value, model) {
                return dateFormat2(value);
            }
        },
        {
            name: 'elapsedTime',
            convert: function (value, model) {
                var startTime = model.get('startTime');
                var finishTime = model.get('finishTime');
                if (finishTime && startTime) {
                    var s = new Date(model.get('startTime'));
                    var f = new Date(model.get('finishTime'));
                    var diff = parseInt((f.getTime() - s.getTime()) / (1000));
                    return toHumanReadableTime(diff);
                } else {
                    return '';
                }
            }
        },
        {
            name: 'neededResourcesMemory',
            convert: function (value, model) {
                if (value < 0) {
                    return fileSize(model.get('memorySeconds') * 1024 * 1024);
                }
                return fileSize(value * 1024 * 1024);
            }
        },
        {
            name: 'neededResourcesVcores',
            convert: function (value, model) {
                if (value < 0) {
                    return toCommaNumber(model.get('vcoreSeconds'));
                }
                return toCommaNumber(value);
            }
        }
    ]
});