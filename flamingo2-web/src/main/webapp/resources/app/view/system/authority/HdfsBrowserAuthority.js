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
Ext.define('Flamingo2.view.system.authority.HdfsBrowserAuthority', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.hdfsBrowserAuthPanel',

    requires: [
        'Flamingo2.view.system.authority.HdfsBrowserAuthController',
        'Flamingo2.view.system.authority.HdfsBrowserAuthModel',
        'Flamingo2.view.system.authority.HdfsBrowserAuthGrid',
        'Flamingo2.view.system.authority.HdfsBrowserAuthForm',
        'Flamingo2.view.system.authority.HdfsBrowserTree'
    ],

    controller: 'hdfsBrowserAuthViewController',

    viewModel: {
        type: 'hdfsBrowserAuthModel'
    },

    layout: {
        type: 'border'
    },
    flex: 1,
    items: [
        {
            region: 'west',
            split: true,
            xtype: 'hdfsBrowserTreePanel'
        },
        {
            region: 'center',
            layout: {
                type: 'vbox',
                pack: 'start',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'hdfsBrowserAuthGridPanel'
                },
                {
                    xtype: 'hdfsBrowserAuthFormPanel'
                }
            ]
        }
    ],
    listeners: {
        beforerender: 'onBeforeRender',
        afterrender: 'onAfterRender'
    }
});