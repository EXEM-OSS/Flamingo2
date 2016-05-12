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
Ext.define('Flamingo2.view.monitoring.cldb.CldbSummary', {
    extend: 'Ext.form.Panel',
    alias: 'widget.cldbSummary',

    listeners: {
        //afterrender: 'onNamenodeSummaryAfterrender'
    },

    layout: {
        type: 'table',
        columns: 2,
        tableAttrs: {
            style: {
                width: '100%'
            }
        }
    },

    defaults: {
        labelAlign: 'right',
        anchor: '100%',
        labelWidth: 150,
        margins: '10 10 10 10'
    },

    defaultType: 'textfield',

    bodyPadding: '10',

    items: [
        {
            fieldLabel: message.msg('monitoring.namenode.hostname'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'hostName'
        },
        {
            fieldLabel: message.msg('monitoring.namenode.port'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'port'
        },
        {
            fieldLabel: message.msg('monitoring.namenode.default_block_size'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'defaultBlockSize',
            valueToRaw: function (value) {
                if (value) {
                    return fileSize(value);
                }
                return '';
            }
        },
        {
            fieldLabel: message.msg('monitoring.namenode.default_replication'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'dfsReplication'
        },
        {
            fieldLabel: message.msg('monitoring.namenode.datanode'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'datanode'
        },
        {
            fieldLabel: message.msg('monitoring.namenode.corrupt_block'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'corruptReplicaBlocks',
            valueToRaw: function (value) {
                if (value == 0) {
                    return '0';
                } else if (value && value != 0) {
                    return toCommaNumber(value);
                } else
                    return '';
            }
        },
        {
            fieldLabel: message.msg('monitoring.namenode.thread'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'threads'
        },
        {
            fieldLabel: message.msg('dashboard.wdetail.column.startdate'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'startTime',
            valueToRaw: function (value) {
                if (value) {
                    return dateFormat2(value);
                }
                return '';
            }
        }
    ]
});