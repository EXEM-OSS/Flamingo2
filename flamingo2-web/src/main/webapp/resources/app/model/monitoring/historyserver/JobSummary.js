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
Ext.define('Flamingo2.model.monitoring.historyserver.JobSummary', {
    extend: 'Ext.data.Model',
    fields: [
        {
            name: 'failedReduceAttempts'
        },
        {
            name: 'state'
        },
        {
            name: 'successfulReduceAttempts'
        },
        {
            name: 'acls'
        },
        {
            name: 'user'
        },
        {
            name: 'reducesTotal'
        },
        {
            name: 'mapsCompleted'
        },
        {
            name: 'startTime', convert: convertDateTime
        },
        {
            name: 'id'
        },
        {
            name: 'avgReduceTime', convert: convertTime
        },
        {
            name: 'avgMapTime', convert: convertTime
        },
        {
            name: 'successfulMapAttempts'
        },
        {
            name: 'name'
        },
        {
            name: 'avgShuffleTime', convert: convertTime
        },
        {
            name: 'reducesCompleted'
        },
        {
            name: 'diagnostics'
        },
        {
            name: 'failedMapAttempts'
        },
        {
            name: 'avgMergeTime', convert: convertTime
        },
        {
            name: 'killedReduceAttempts'
        },
        {
            name: 'mapsTotal'
        },
        {
            name: 'queue'
        },
        {
            name: 'uberized'
        },
        {
            name: 'killedMapAttempts'
        },
        {
            name: 'finishTime', convert: convertDateTime
        },
        {
            name: 'elapsedTime', convert: convertTime
        },
        {
            name: 'mapsPending'
        },
        {
            name: 'mapsRunning'
        },
        {
            name: 'reducesPending'
        },
        {
            name: 'reducesRunning'
        },
        {
            name: 'newReduceAttempts'
        },
        {
            name: 'runningReduceAttempts'
        },
        {
            name: 'newMapAttempts'
        },
        {
            name: 'runningMapAttempts'
        },
        {
            name: 'submitTime', convert: convertDateTime
        }
    ]
});