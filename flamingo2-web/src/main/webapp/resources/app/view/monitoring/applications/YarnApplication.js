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
Ext.define('Flamingo2.view.monitoring.applications.YarnApplication', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.yarnApplication',

    requires: [
        'Flamingo2.view.monitoring.applications.YarnApplicationController',
        'Flamingo2.view.monitoring.applications.YarnApplicationModel',

        'Flamingo2.view.monitoring.applications.AllApplications',
        'Flamingo2.view.monitoring.applications.ApplicationSummary',
        'Flamingo2.view.monitoring.applications.ApplicationSumChart',
        'Flamingo2.view.monitoring.applications.Queue',
        'Flamingo2.view.hive.editor.QueryEditor'
    ],

    controller: 'yarnApplicationController',

    viewModel: {
        type: 'yarnApplicationModel'
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    items: [
        {
            iconCls: 'common-view',
            title: message.msg('monitoring.yarn.yarn_app_summary'),
            border: true,
            xtype: 'applicationSumChart'
        },
        {
            iconCls: 'common-view',
            margin: '5 0 0 0',
            height: 210,
            border: true,
            xtype: 'allApplications'
        },
        {
            margin: '5 0 0 0',
            height: 350,
            border: true,
            xtype: 'tabpanel',
            items: [
                {
                    title: message.msg('monitoring.yarn.app_summary'),
                    iconCls: 'common-grid',
                    xtype: 'applicationSummary'
                },
                {
                    title: message.msg('monitoring.yarn.app_log'),
                    tooltip: message.msg('monitoring.msg.app_container_sum'),
                    iconCls: 'common-view',
                    layout: 'fit',
                    itemId: 'logviewer',
                    xtype: 'queryEditor',
                    parser: 'plain_text',
                    highlightActiveLine: false,
                    highlightGutterLine: false,
                    highlightSelectedWord: false,
                    forceFit: true,
                    theme: 'eclipse',
                    printMargin: false,
                    readOnly: true,
                    value: '',
                    listeners: {
                        afterrender: function (comp) {
                            // Hide toolbar
                            comp.down('toolbar').setVisible(false);
                        }
                    }
                },
                {
                    title: message.msg('monitoring.application_master'),
                    tooltip: message.msg('monitoring.msg.app_master_page_view'),
                    itemId: 'applicationMaster',
                    iconCls: 'common-web',
                    xtype: 'panel',
                    disabled: toBoolean(config['monitoring.yarn.appmaster.disabled']),
                    autoScroll: true,
                    layout: 'fit'
                }
            ],
            tabBar: {
                items: [
                    {
                        xtype: 'tbspacer',
                        width: 10
                    },
                    {
                        xtype: 'splitbutton',
                        iconCls: 'common-view',
                        text: message.msg('common.view'),
                        menu: [
                            {
                                text: message.msg('monitoring.yarn.app_log_down'),
                                iconCls: 'common-install',
                                handler: 'onDownloadLogClick'
                            }, '-',
                            {
                                text: message.msg('monitoring.yarn.app_log_view_full'),
                                iconCls: 'common-view',
                                handler: 'onShowApplicationLogClick'
                            }, '-',
                            {
                                text: message.msg('monitoring.yarn.app_master_view_full'),
                                iconCls: 'common-web',
                                disabled: toBoolean(config['monitoring.yarn.appmaster.disabled']),
                                handler: 'onShowApplicationMasterClick'
                            }
                        ]
                    }
                ]
            },
            listeners: {
                tabchange: 'onTabChanged'
            }
        }
    ],

    listeners: {
        engineChanged: 'onEngineChanged'
    }
});