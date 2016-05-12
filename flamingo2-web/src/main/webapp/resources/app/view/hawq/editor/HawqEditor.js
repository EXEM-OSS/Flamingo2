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
 * HAWQ > Editor
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.editor.HawqEditorController
 * @see Flamingo2.view.hawq.editor.HawqEditorModel
 */
Ext.define('Flamingo2.view.hawq.editor.HawqEditor', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.hawqEditor',

    controller: 'hawqEditorController',
    viewModel: {
        type: 'hawqEditorModel'
    },

    layout: 'border',
    split: true,
    border: false,

    listeners: {
        afterrender: 'hawqEditorAfterrender',
        beforeclose: 'onBeforeclose',
        beforedestroy: 'onEditorTabBeforedestroy',
        destroy: 'onDestroy',
        resize: 'onResize'
    },

    items: [
        {
            xtype: 'hawqQueryEditor',
            reference: 'queryEditor',
            layout: 'fit',
            region: 'center',
            theme: 'eclipse',
            forceFit: true,
            printMargin: false,
            parser: 'pgsql',
            flex: 1
        },
        {
            xtype: 'tabpanel',
            region: 'south',
            layout: 'fit',
            flex: 1,
            border: false,
            reference: 'hawqQueryResultTabpanel',
            items: [
                {
                    xtype: 'hawqQueryEditor',
                    title: message.msg('hawq.title.editor.log'),
                    layout: 'fit',
                    reference: 'hawqResultLogPanel',
                    parser: 'plain_text',
                    forceFit: true,
                    theme: 'eclipse',
                    printMargin: false,
                    readOnly: true,
                    useWrapMode: 80,
                    value: '',
                    listeners: {
                        afterrender: 'hawqResultLogPanelRemoveToolbar'
                    }
                },
                {
                    xtype: 'panel',
                    title: message.msg('hawq.title.editor.data'),
                    layout: 'fit',
                    reference: 'hawqResultDataPanel',
                    border: true,
                    items: [
                        {
                            xtype: 'hawqResultSearchGridPanel',
                            reference: 'hawqResultSearchGridPanel',
                            bind: {
                                store: '{hawqResultSetData}'
                            },
                            columns: []
                        }
                    ]
                },
                {
                    xtype: 'hawqQueryEditor',
                    title: message.msg('hawq.title.editor.message'),
                    layout: 'fit',
                    reference: 'hawqResultMessagePanel',
                    parser: 'plain_text',
                    forceFit: true,
                    theme: 'eclipse',
                    printMargin: false,
                    readOnly: true,
                    useWrapMode: 80,
                    value: '',
                    listeners: {
                        afterrender: 'hawqResultLogPanelRemoveToolbar'
                    }
                },
                {
                    xtype: 'panel',
                    title: message.msg('hawq.title.editor.donut'),
                    layout: 'fit',
                    reference: 'hawqDonutChartPanel',
                    items: []
                },
                {
                    xtype: 'panel',
                    title: message.msg('hawq.title.editor.bar'),
                    layout: 'fit',
                    reference: 'hawqBarChartPanel',
                    items: []
                },
                {
                    xtype: 'panel',
                    title: message.msg('hawq.title.editor.area'),
                    layout: 'fit',
                    reference: 'hawqAreaChartPanel',
                    items: []
                },
                {
                    xtype: 'panel',
                    title: message.msg('hawq.title.editor.line'),
                    layout: 'fit',
                    reference: 'hawqLineChartPanel',
                    items: []
                }
            ],
            listeners: {
                tabchange: 'hawqQueryResultTabpanelTabchange'
            }
        }
    ]
});