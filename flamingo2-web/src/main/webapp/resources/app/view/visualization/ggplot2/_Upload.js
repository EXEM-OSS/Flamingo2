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
Ext.define('Flamingo2.view.visualization.ggplot2._Upload', {
    extend: 'Ext.window.Window',
    alias: 'widget.ggplo2Upload',

    requires: [
        'Flamingo2.view.visualization.ggplot2._UploadController',
        'Flamingo2.view.visualization.ggplot2._UploadModel'
    ],

    controller: 'ggplo2UploadController',
    viewModel: {
        type: 'ggplot2UploadModel'
    },

    width: 600,
    height: 500,
    resizable: false,
    modal: true,
    title: message.msg('visual.msg.data_upload'),
    border: false,
    layout: 'fit',
    items: [
        {
            xtype: 'form',
            reference: 'frmUpload',
            bodyStyle: 'padding: 5px 10px 5px 10px;',
            labelWidth: 60,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'fieldcontainer',
                    fieldLabel: message.msg('visual.data_location'),
                    defaultType: 'radiofield',
                    defaults: {
                        flex: 1
                    },
                    layout: 'hbox',
                    items: [
                        {
                            boxLabel: message.msg('common.upload'),
                            name: 'location',
                            inputValue: 'local',
                            checked: true
                        },
                        {
                            boxLabel: message.msg('visual.hdfs'),
                            name: 'location',
                            inputValue: 'hdfs'
                        }
                    ]
                },
                {
                    xtype: 'container',
                    layout: 'hbox',
                    margin: '0 0 5 0',
                    items: [
                        {
                            xtype: 'fieldcontainer',
                            flex: 1,
                            reference: 'local',
                            layout: 'hbox',
                            items: [
                                {
                                    xtype: 'filefield',
                                    fieldLabel: message.msg('visual.data_file'),
                                    buttonText: message.msg('common.browse'),
                                    flex: 1,
                                    reference: 'localfile',
                                    name: 'localfile',
                                    margin: '0 3 0 0',
                                    emptyText: '.csv, .txt, .xls, .sav, .dta',
                                    buttonConfig: {
                                        reference: 'btnLocalBrowse'
                                    },
                                    listeners: {
                                        change: 'onLocalFileChange'
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'fieldcontainer',
                            flex: 1,
                            reference: 'hdfs',
                            hidden: true,
                            layout: 'hbox',
                            items: [
                                {
                                    xtype: 'textfield',
                                    fieldLabel: message.msg('visual.data_file'),
                                    flex: 1,
                                    name: 'datafile',
                                    reference: 'hdfsDatafile',
                                    emptyText: '.csv, .txt, .xls, .sav, .dta',
                                    readOnly: true,
                                    margin: '0 3 0 0'
                                },
                                {
                                    xtype: 'button',
                                    reference: 'btnHdfsBrowse',
                                    text: message.msg('common.browse'),
                                    margin: '0 3 0 0',
                                    handler: 'onHdfsBrowseClick'
                                }
                            ]
                        },
                        {
                            xtype: 'button',
                            text: message.msg('visual.load'),
                            handler: 'onBtnLoadClick'
                        }
                    ]
                },
                {
                    xtype: 'panel',
                    reference: 'rowPanel',
                    disabled: true,
                    flex: 1,
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [
                        {
                            xtype: 'fieldset',
                            title: message.msg('visual.option'),
                            layout: 'hbox',
                            defaults: {
                                editable: false,
                                flex: 1
                            },
                            items: [
                                {
                                    xtype: 'combobox',
                                    fieldLabel: '헤더',
                                    margin: '0 5 5 0',
                                    value: false,
                                    reference: 'cbxHeader',
                                    displayField: 'name',
                                    valueField: 'value',
                                    bind: {
                                        store: '{header}'
                                    },
                                    listeners: {
                                        select: 'onCbxHeaderSelect'
                                    }
                                },
                                {
                                    xtype: 'combobox',
                                    fieldLabel: message.msg('visual.delimter'),
                                    margin: '0 0 5 5',
                                    value: ',',
                                    reference: 'cbxDelimiter',
                                    displayField: 'name',
                                    valueField: 'value',
                                    bind: {
                                        store: '{delimiter}'
                                    },
                                    listeners: {
                                        select: 'onCbxDelimiterSelect'
                                    }
                                }
                            ]
                        },
                        {
                            xtype: 'panel',
                            layout: 'fit',
                            height: 60,
                            reference: 'pnlHeader',
                            items: [
                                {
                                    xtype: 'fieldset',
                                    reference: 'fldHeader',
                                    title: message.msg('visual.column_header'),
                                    layout: 'fit',
                                    height: 60,
                                    items: [
                                        {
                                            xtype: 'grid',
                                            hideHeaders: true,
                                            reference: 'grdHeader',
                                            columnLines: true,
                                            store: new Ext.data.ArrayStore(),
                                            plugins: {
                                                ptype: 'cellediting',
                                                clicksToEdit: 1
                                            }
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            xtype: 'fieldset',
                            title: message.msg('visual.row'),
                            flex: 1,
                            layout: 'fit',
                            items: [
                                {
                                    xtype: 'grid',
                                    reference: 'grdGuess',
                                    columnLines: true,
                                    hideHeaders: true,
                                    store: new Ext.data.ArrayStore({fields: ['dummy'], id: 'dataStore'})
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ],
    buttons: [
        {
            text: message.msg('visual.import'),
            reference: 'btnImport',
            disabled: true,
            handler: 'onBtnImportClick'
        }
    ]
});