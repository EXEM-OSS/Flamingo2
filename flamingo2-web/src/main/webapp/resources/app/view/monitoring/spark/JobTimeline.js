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
 * Created by Park on 15. 8. 25..
 */
Ext.define('Flamingo2.view.monitoring.spark.JobTimeline', {
    extend: 'Ext.window.Window',

    requires: [
        'Flamingo2.view.monitoring.spark.SparkModel',
        'Flamingo2.view.monitoring.spark.JobTimelineController',
        'Flamingo2.view.component.timeline.Timeline'
    ],

    controller: 'jobtimeline',
    viewModel: {
        type: 'sparkModel'
    },

    maximizable: true,
    modal: true,
    scrollable: true,
    bodyStyle: {
        background: '#FFFFFF'
    },

    items: [{
        xtype: 'timeline',
        reference: 'timlineView',
        width: '100%',
        bind: {
            store: '{jobTimeline}'
        },
        options: {
            zoomable: false,
            editable: false,
            showCurrentTime: false
        },
        groups: [{
            id: 'executors',
            content: '<div>Executors</div><div class="legend-area"><svg width="150px" height="85px"><rect class="succeeded-job-legend" x="5px" y="5px" width="20px" height="15px" rx="2px" ry="2px"></rect><text x="35px" y="17px">Succeeded</text><rect class="failed-job-legend" x="5px" y="30px" width="20px" height="15px" rx="2px" ry="2px"></rect><text x="35px" y="42px">Failed</text><rect class="running-job-legend" x="5px" y="55px" width="20px" height="15px" rx="2px" ry="2px"></rect><text x="35px" y="67px">Running</text></svg></div>'
        },
        {
            id: 'stages',
            content: '<div>Stages</div><div class="legend-area"><svg width="150px" height="85px"><rect class="completed-stage-legend" x="5px" y="5px" width="20px" height="15px" rx="2px" ry="2px"></rect><text x="35px" y="17px">Completed</text><rect class="failed-stage-legend" x="5px" y="30px" width="20px" height="15px" rx="2px" ry="2px"></rect> <text x="35px" y="42px">Failed</text><rect class="active-stage-legend" x="5px" y="55px" width="20px" height="15px" rx="2px" ry="2px"></rect><text x="35px" y="67px">Active</text></svg></div>'
        }]
    }],
    listeners: {
        afterrender: 'onAfterrender',
        beforeclose: 'onBeforeclose'
    }
});

