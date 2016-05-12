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
Ext.define('Flamingo2.view.fs.audit.AuditTop10', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.auditTop10',

    columns: [
        {
            text: '',
            width: 30,
            dataIndex: 'no',
            align: 'center'
        },
        {
            text: message.msg('hdfs.audit.column.top10.name'),
            width: 110,
            dataIndex: 'name',
            align: 'center'
        },
        {
            text: message.msg('hdfs.audit.column.top10.cnt'),
            flex: 0.1,
            dataIndex: 'cnt',
            align: 'center'
        }
    ],
    /*    viewConfig: {
     stripeRows: true,
     columnLines: true,
     getRowClass: function () {
     return 'cell-height-25';
     }
     },*/
    listeners: {
        afterrender: 'onAuditTop10AfterRender'
    }
});