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
Ext.define('Flamingo2.view.monitoring.spark.JobTimelineController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.jobtimeline',

    onAfterrender: function(view) {
        var me = this;
        var refs = me.getReferences();

        view.setTitle('Event Timeline / ApplicationID: ' + view.appid + ' / JobID: ' + view.jobid);

        Ext.defer(function() {
            refs.timlineView.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    appid: view.appid,
                    jobid: view.jobid
                }
            });
        }, 300);
    },

    onBeforeclose: function() {
        $('.ui-helper-hidden-accessible').remove();
    }
});