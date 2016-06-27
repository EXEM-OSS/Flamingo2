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
Ext.define('Flamingo2.view.archive.yarn.ArchiveYarnModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.archiveYarnModel',

    data: {
        title: message.msg('monitoring.yarn.title')
    },

    stores: {
        archiveYarnSearchKeyword: {
            fields: ['name', 'value'],
            data: [
                {name: 'All', value: 'ALL'},
                {name: 'Finished', value: 'FINISHED'},
                {name: 'Failed', value: 'FAILED'},
                {name: 'Killed', value: 'KILLED'}
            ]
        },

        archiveYarnSummaryChartStore: {
            fields: ['time', 'sum'],
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.ARCHIVE.YARN.GET_SUMMARY,
                extraParams: {
                    clusterName: ENGINE.id,
                    startDate: '',
                    endDate: '',
                    filter: ''
                },
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total'
                }
            },
            remoteSort: true,
            sorters: [
                {
                    property: 'num',
                    direction: 'ASC'
                }
            ]
        },

        archiveYarnAllAppsStore: {
            model: 'Flamingo2.model.monitoring.resourcemanager.Application',
            autoLoad: false,
            pageSize: 20,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.ARCHIVE.YARN.GET_ALL_APPLICATION,
                extraParams: {
                    clusterName: ENGINE.id,
                    startDate: '',
                    endDate: '',
                    filter: '',
                    appName: ''
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
                url: CONSTANTS.MONITORING.HS.CONF,
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
        },

        queueStore: {
            type: 'tree',
            autoLoad: false,
            rootVisible: true,
            proxy: {
                type: 'ajax',
                url: CONSTANTS.MONITORING.YA.QUEUES,
                extraParams: {
                    clusterName: ENGINE.id
                }
            },
            root: {
                text: 'root',
                expanded: false,
                id: 'root'
            }
        }
    }
});