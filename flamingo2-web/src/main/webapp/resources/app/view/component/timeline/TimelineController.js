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
 * Created by Park on 15. 8. 21..
 */
Ext.define('Flamingo2.view.component.timeline.TimelineController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.timeline',

    /**
     * Called when the view is created
     */
    init: function() {

    },

    onViewready: function(view) {
        console.debug(view.getStore().getData());

        /*
        var container = document.getElementById(view.id);
        var items = new vis.DataSet([
            {id: 1, content: 'item 1', start: '2014-04-20'},
            {id: 2, content: 'item 2', start: '2014-04-14'},
            {id: 3, content: 'item 3', start: '2014-04-18'},
            {id: 4, content: 'item 4', start: '2014-04-16', end: '2014-04-19'},
            {id: 5, content: 'item 5', start: '2014-04-25'},
            {id: 6, content: 'item 6', start: '2014-04-27', type: 'point'}
        ]);

        // Configuration for the Timeline
        var options = {};

        var timeline = new vis.Timeline(container, items, options);*/
    }
});