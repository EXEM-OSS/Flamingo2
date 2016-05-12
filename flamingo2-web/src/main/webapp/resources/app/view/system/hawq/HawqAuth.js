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
 * System > HAWQ
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.role.HawqRole
 * @see Flamingo2.view.system.hawq.queue.HawqRQueue
 * @see Flamingo2.view.system.hawq.queue.HawqSession
 */
Ext.define('Flamingo2.view.system.hawq.HawqAuth', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.hawqAuthWindow',

    requires: [
        'Flamingo2.view.system.hawq.HawqAuthController',
        'Flamingo2.view.system.hawq.HawqAuthModel',
        'Flamingo2.view.system.hawq.queue.*',
        'Flamingo2.view.system.hawq.role.*',
        'Flamingo2.view.system.hawq.session.*',
        'Flamingo2.view.system.hawq.table.*'
    ],

    controller: 'hawqAuthController',
    viewModel: {
        type: 'hawqAuthModel'
    },

    layout: 'border',
    border: false,
    flex: 1,

    items: [
        {
            xtype: 'tabpanel',
            reference: 'hawqAuthTabpanel',
            region: 'center',
            split: true,
            activeTab: 0,
            items: [
                {
                    xtype: 'hawqRole',
                    reference: 'hawqRolePanel',
                    title: message.msg('hawq.title.role')
                },
                {
                    xtype: 'hawqRQueue',
                    reference: 'hawqRQueuePanel',
                    title: message.msg('hawq.title.rqueue')
                },
                {
                    xtype: 'hawqSession',
                    reference: 'hawqSessionPanel',
                    title: message.msg('hawq.title.session')
                },
                {
                    xtype: 'hawqLTable',
                    reference: 'hawqLTablePanel',
                    title: message.msg('hawq.title.locktable')
                }
            ]
        }
    ]
});