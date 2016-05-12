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
Ext.define('Flamingo2.view.monitoring.applications.ApplicationSummary', {
    extend: 'Ext.form.Panel',
    alias: 'widget.applicationSummary',

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
            colspan: 2,
            fieldLabel: message.msg('monitoring.application_name'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'name'
        },
        {
            colspan: 2,
            fieldLabel: message.msg('monitoring.application_type'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'applicationType'
        },
        {
            colspan: 2,
            fieldLabel: message.msg('monitoring.application_master'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'trackingUrl',
            valueToRaw: function (value) {
                if (value && value != 'N/A') {
                    return '<a href="' + value + '" target="_blank">' + value + "</a>";
                } else if (value == 'N/A') {
                    return message.msg('monitoring.msg.do_not_have_application_master');
                }
                return '';
            }
        },
        {
            colspan: 2,
            fieldLabel: message.msg('monitoring.application_id'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'applicationId'
        },
        {
            fieldLabel: message.msg('common.workflowName'),
            xtype: 'displayfield',
            labelAlign: 'right',
            hidden: true,
            name: 'wname',
            valueToRaw: function (value) {
                return value ? value : message.msg('common.na');
            }
        },
        {
            fieldLabel: message.msg('dashboard.common.workflowId'),
            xtype: 'displayfield',
            labelAlign: 'right',
            hidden: true,
            name: 'wid',
            valueToRaw: function (value) {
                return value ? value : message.msg('common.na');
            }
        },
        {
            fieldLabel: message.msg('common.user'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'user'
        },
        {
            fieldLabel: message.msg('common.queue'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'queue'
        },
        {
            fieldLabel: message.msg('common.status'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'yarnApplicationState'
        },
        {
            fieldLabel: message.msg('common.final_status'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'finalApplicationStatus'
        },
        {
            fieldLabel: message.msg('dashboard.wdetail.column.duration'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'elapsedTime'
        },
        {
            fieldLabel: message.msg('monitoring.vcore_num'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'numVcore'
        },
        {
            fieldLabel: message.msg('monitoring.memory_num'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'numMemory'
        },
        {
            fieldLabel: message.msg('monitoring.container_num'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'numContainer'
        },
        {
            fieldLabel: message.msg('common.start'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'startTime',
            valueToRaw: function (value) {
                if (value) {
                    return dateFormat2(value);
                }
            }
        },
        {
            fieldLabel: message.msg('common.finish'),
            xtype: 'displayfield',
            labelAlign: 'right',
            name: 'finishTime',
            valueToRaw: function (value) {
                if (value) {
                    if (value == 0) {
                        return '';
                    }
                    return dateFormat2(value);
                }
            }
        }
    ]
});