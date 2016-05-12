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
Ext.define('Flamingo2.model.fs.audit.AuditList', {
    extend: 'Ext.data.Model',
    fields: [
        'id',
        'clusterId',
        'clusterName',
        'fileSystemType',
        {
            name: 'fileType',
            convert: function (value) {
                return value == 'FILE' ? message.msg('common.file') : message.msg('common.directory');
            }
        },
        {
            name: 'auditType',
            convert: function (value) {
                switch (value) {
                    case 'CREATE':
                        return message.msg('hdfs.audit.model.create');
                    case 'DELETE':
                        return message.msg('hdfs.audit.model.delete');
                    case 'RENAME':
                        return message.msg('hdfs.audit.model.rename');
                    case 'COPY':
                        return message.msg('hdfs.audit.model.copy');
                    case 'MOVE':
                        return message.msg('hdfs.audit.model.move');
                    case 'MERGE':
                        return message.msg('hdfs.audit.model.merge');
                    case 'UPLOAD':
                        return message.msg('hdfs.audit.model.upload');
                    case 'DOWNLOAD':
                        return message.msg('hdfs.audit.model.download');
                    case 'VIEW':
                        return message.msg('hdfs.audit.model.view');
                    case 'PERMISSION':
                        return message.msg('hdfs.audit.model.permission');
                    case 'HIVE_DB':
                        return message.msg('hdfs.audit.model.hiveDB');
                    case 'HIVE_TABLE':
                        return message.msg('hdfs.audit.model.hiveTable');
                    case 'VISUALIZATION':
                        return message.msg('hdfs.audit.model.visualization');
                    default:
                        return message.msg('hdfs.audit.model.notExist');
                }
            }
        },
        {
            name: 'from',
            convert: function (value, record) {
                if (isBlank(record.data.to)) {
                    return value;
                } else {
                    var msg = Ext.String.format(
                        'From: {0}<br/>To: {1}',
                        record.data.from,
                        record.data.to
                    );
                    return msg;
                }
            }
        },
        'to',
        {
            name: 'length',
            convert: function (value) {
                return fileSize(value);
            }
        },
        {
            name: 'workDate',
            convert: function (value) {
                return dateFormat(new Date(value), 'yyyy-MM-dd HH:mm:ss');
            }

        },
        'username'
    ]
});