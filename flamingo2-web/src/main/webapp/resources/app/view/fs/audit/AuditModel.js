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
Ext.define('Flamingo2.view.fs.audit.AuditModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.auditModel',

    data: {
        title: message.msg('hdfs.audit.title')
    },

    stores: {
        auditTop10Store: {
            autoLoad: false,
            fields: ['no', 'name', 'cnt'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.FS.AUDIT.TOP10,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total',
                    successProperty: 'success'
                }
            },
            sorters: [
                {
                    property: 'no',
                    direction: 'ASC'
                }
            ]
        },

        auditNowStatusStore: {
            autoLoad: false,
            fields: ['name', 'cnt'],
            proxy: {
                type: 'ajax',
                url: CONSTANTS.FS.AUDIT.STATUS,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total',
                    successProperty: 'success'
                }
            }
        },

        auditTrendStore: {
            autoLoad: false,
            model: 'Flamingo2.model.fs.audit.AuditTrend',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.FS.AUDIT.TREND,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total',
                    successProperty: 'success',
                    messageProperty: 'map'
                }
            }
        },

        auditGridStore: {
            autoLoad: false,
            pageSize: 10,
            model: 'Flamingo2.model.fs.audit.AuditList',
            proxy: {
                type: 'ajax',
                url: CONSTANTS.FS.AUDIT.LIST,
                reader: {
                    type: 'json',
                    rootProperty: 'list',
                    totalProperty: 'total',
                    idProperty: 'id'
                },
                extraParams: {
                    clusterName: '',
                    startDate: '',
                    endDate: '',
                    auditType: '',
                    path: '',
                    nextPage: ''
                }
            }
        },
        keyword: {
            fields: ['name', 'value'],
            data: [
                {name: message.msg('hdfs.audit.model.action'), value: 'ACT'},
                {name: message.msg('hdfs.audit.model.user'), value: 'USER'},
                {name: message.msg('hdfs.audit.model.file_type'), value: 'FILE_TYPE'},
                {name: message.msg('hdfs.audit.model.method'), value: 'METHOD'}
            ]
        },
        auditListKeyword: {
            fields: ['name', 'value'],
            data: [
                {name: message.msg('hdfs.audit.model.all'), value: 'ALL'},
                {name: message.msg('hdfs.audit.model.create'), value: 'CREATE'},
                {name: message.msg('hdfs.audit.model.delete'), value: 'DELETE'},
                {name: message.msg('hdfs.audit.model.rename'), value: 'RENAME'},
                {name: message.msg('hdfs.audit.model.copy'), value: 'COPY'},
                {name: message.msg('hdfs.audit.model.move'), value: 'MOVE'},
                {name: message.msg('hdfs.audit.model.merge'), value: 'MERGE'},
                {name: message.msg('hdfs.audit.model.upload'), value: 'UPLOAD'},
                {name: message.msg('hdfs.audit.model.download'), value: 'DOWNLOAD'},
                {name: message.msg('hdfs.audit.model.view'), value: 'VIEW'},
                {name: message.msg('hdfs.audit.model.permission'), value: 'PERMISSION'},
                {name: message.msg('hdfs.audit.model.hiveDB'), value: 'HIVE_DB'},
                {name: message.msg('hdfs.audit.model.hiveTable'), value: 'HIVE_TABLE'},
                {name: message.msg('hdfs.audit.model.visualization'), value: 'VISUALIZATION'}
            ]
        }
    }
});