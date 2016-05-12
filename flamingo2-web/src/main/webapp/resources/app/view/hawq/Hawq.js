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
 * HAWQ
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.HawqController
 * @see Flamingo2.view.hawq.HawqModel
 * @see Flamingo2.view.hawq.editor.HawqEditor
 * @see Flamingo2.view.hawq.browser.HawqBrowser
 */
Ext.define('Flamingo2.view.hawq.Hawq', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.hawqViewport',

    requires: [
        'Flamingo2.view.hawq.HawqController',
        'Flamingo2.view.hawq.HawqModel',
        'Flamingo2.view.hawq.browser.*',
        'Flamingo2.view.hawq.editor.*',
        'Ext.data.Model'
    ],

    controller: 'hawqController',
    viewModel: {
        type: 'hawqModel'
    },

    layout: 'border',
    border: false,
    flex: 1,

    listeners: {
        afterrender: 'onAfterrender'
    },

    items: [
        {
            xtype: 'hawqBrowser',
            region: 'west',
            width: 320
        },
        {
            xtype: 'panel',
            region: 'center',
            layout: 'fit',
            tbar: [
                {
                    text: message.msg('hawq.button.new'),
                    iconCls: 'common-new',
                    reference: 'hawqEditorTabAddButton',
                    handler: 'onAddHawqEditorTab'
                },
                '-',
                {
                    text: message.msg('hawq.button.execquery'),
                    reference: 'hawqExecuteButton',
                    itemId: 'hawqExecuteButton',
                    handler: 'onExecuteQuery',
                    iconCls: 'common-execute'
                },
                {
                    text: message.msg('hawq.button.stopquery'),
                    reference: 'hawqStopButton',
                    itemId: 'hawqStopButton',
                    handler: 'stopHawqQuery',
                    iconCls: 'common-stop'
                },
                '-',
                {
                    text: message.msg('hawq.button.undo'),
                    reference: 'hawqUndoButton',
                    itemId: 'hawqUndoButton',
                    iconCls: 'common-undo',
                    handler: 'undoEditHawqQuery'
                },
                {
                    text: message.msg('hawq.button.redo'),
                    reference: 'hawqRedoButton',
                    itemId: 'hawqRedoButton',
                    iconCls: 'common-redo',
                    handler: 'redoEditHawqQuery'
                },
                '->',
                {
                    xtype: 'numberfield',
                    fieldLabel: message.msg('hawq.button.maxrows'),
                    labelAlign: 'right',
                    labelWidth: 60,
                    reference: 'hawqMaxRows',
                    itemId: 'hawqMaxRows',
                    minValue: -1,
                    value: 1000,
                    maxValue: 2147483647,
                    width: 180,
                    inputAttrTpl: message.msg('hawq.button.maxrows.tooltip')
                },
                {
                    text: message.msg('hawq.button.explainquery'),
                    reference: 'hawqViewPlanButton',
                    itemId: 'hawqViewPlanButton',
                    iconCls: 'common-hawq-plan',
                    handler: 'viewPlan'
                },
                {
                    text: message.msg('hawq.button.template'),
                    iconCls: 'common-question',
                    reference: 'hawqTemplateButton',
                    menu: {
                        xtype: 'menu',
                        plain: true,
                        items: [
                            {
                                xtype: 'container',
                                layout: {
                                    type: 'hbox'
                                },
                                width: 250,
                                items: [
                                    {
                                        xtype: 'menuitem',
                                        text: message.msg('hawq.button.template')
                                    },
                                    {
                                        xtype: 'combo',
                                        mode: 'local',
                                        triggerAction: 'all',
                                        editable: false,
                                        name: 'template',
                                        displayField: 'name',
                                        valueField: 'value',
                                        queryMode: 'local',
                                        bind: {
                                            store: '{hawqTemplate}'
                                        },
                                        listeners: {
                                            change: 'hawqTemplateComboboxChange'
                                        }
                                    }
                                ]
                            }
                        ]
                    }
                }
            ],
            items: [
                {
                    xtype: 'tabpanel',
                    itemId: 'hawqEditorTab',
                    reference: 'editorTab',
                    listeners: {
                        tabchange: 'onEditorTabChange'
                    }
                }
            ]
        },
        {
            xtype: 'component',
            hidden: true,
            html: '<form id="hawqDownloadForm" method="POST" action="' + CONSTANTS.HAWQ.EDITOR.DOWNLOAD_RESULT + '" enctype="multipart/form-data" target="_blank">' +
            '<input type="hidden" id="clusterName" name="clusterName">' +
            '<input type="hidden" id="hawqDownloadFields" name="fields">' +
            '<input type="hidden" id="hawqDownloadDatas" name="datas">' +
            '</form>'
        }
    ],
    bbar: [
        {
            xtype: 'displayfield',
            fieldLabel: message.msg('hawq.label.autocommit'),
            labelAlign: 'right',
            labelWidth: 80,
            reference: 'autoCommitDisplayfield'
        },
        '-',
        {
            xtype: 'displayfield',
            fieldLabel: message.msg('hawq.label.encoding'),
            labelAlign: 'right',
            labelWidth: 55,
            reference: 'encodingDisplayfield'
        },
        '-',
        {
            xtype: 'displayfield',
            fieldLabel: message.msg('hawq.label.username'),
            labelAlign: 'right',
            labelWidth: 55,
            reference: 'userNameDisplayfield'
        },
        '-',
        {
            xtype: 'displayfield',
            fieldLabel: message.msg('hawq.label.hawqversion'),
            labelAlign: 'right',
            labelWidth: 90,
            reference: 'hawqVersionDisplayfield'
        }
    ]
});