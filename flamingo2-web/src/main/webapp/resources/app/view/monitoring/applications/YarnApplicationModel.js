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
Ext.define('Flamingo2.view.monitoring.applications.YarnApplicationModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.yarnApplicationModel',

    data: {
        title: message.msg('monitoring.yarn.title')
    },

    stores: {
        queueStore: {
            type: 'tree',
            autoLoad: false,
            rootVisible: true,
            proxy: {
                type: 'ajax',
                url: '/monitoring/resourcemanager/queues.json',
                extraParams: {
                    clusterName: ENGINE.id
                }
            },
            root: {
                text: 'root',
                expanded: false,
                id: 'root'
            }
        },

        allApplicationsStore: {
            model: 'Flamingo2.model.monitoring.resourcemanager.Application',
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: '/monitoring/resourcemanager/apps/all.json',
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            remoteSort: false,  // groupField가 설정되면 groupField 기준으로 정렬이 됨
            sorters: [
                {
                    property: 'startTime',
                    direction: 'DESC'
                }
            ]
        },

        configurationStore: {
            autoLoad: false,
            remoteSort: false,
            fields: ['name', 'source', 'value'],
            proxy: {
                type: 'ajax',
                url: '/monitoring/application/history/jobs/job/configuration.json',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                extraParams: {
                    clusterName: ENGINE.id
                },
                reader: {
                    type: 'json',
                    rootProperty: 'conf.property',
                    totalProperty: 'total'
                }
            },
            sorters: [
                {
                    property: 'name',
                    direction: 'DESC'
                }
            ]
        }
    }
});