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
 *  Inner FieldContainer : BrowserField
 *
 * @class
 * @extends Ext.form.FieldContainer
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.designer.property._BrowserField', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget._browserField',

    layout: 'hbox',

    defaults: {
        hideLabel: true
    },

    items: [],

    initComponent: function () {
        var me = this;
        Ext.apply(this, {
            items: [
                {
                    xtype: 'textfield',
                    flex: 1,
                    name: this.name,
                    listeners: {
                        errorchange: function (comp, error, eOpts) {
                            comp.focus(false, 50);
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: message.msg('workflow.common.find'),
                    tooltip: message.msg('workflow.common.hdfsbrowser.tooltip'),
                    margin: '0 0 0 3',
                    handler: function () {
                        var popWindow = Ext.create('Ext.Window', {
                            title: message.msg('workflow.common.hdfsbrowser'),
                            width: 800,
                            height: 400,
                            modal: true,
                            constrain: true,
                            layout: 'fit',
                            items: [
                                {
                                    xtype: 'hdfsBrowserPanelForDesigner'
                                }
                            ],
                            buttonAlign: 'center',
                            buttons: [
                                {
                                    text: message.msg('workflow.common.confirm'),

                                    handler: function () {
                                        var dirSelection = Ext.ComponentQuery.query('hdfsDirectoryPanelForDesigner')[0].getSelectionModel().getSelection();
                                        var fileSelection = Ext.ComponentQuery.query('hdfsFilePanelForDesigner')[0].getSelectionModel().getSelection();
                                        var textfield = me.down('textfield');
                                        if (fileSelection.length > 0) {
                                            if ('/' == fileSelection[0].get('path')) {
                                                textfield.setValue('/' + fileSelection[0].get('filename'));
                                            } else {
                                                textfield.setValue(fileSelection[0].get('path') + '/' + fileSelection[0].get('filename'));
                                            }
                                            popWindow.close();
                                        } else if (dirSelection.length > 0) {
                                            if (!dirSelection[0].get('fullyQualifiedPath')) {
                                                textfield.setValue('/');
                                            } else {
                                                textfield.setValue(dirSelection[0].get('fullyQualifiedPath'));
                                            }
                                            popWindow.close();
                                        } else {
                                            error(message.msg('workflow.common.warn'), message.msg('workflow.common.hdfsbrowser.filedir.warn'));
                                        }
                                    }
                                },
                                {
                                    text: message.msg('workflow.common.cancel'),

                                    handler: function () {
                                        popWindow.close();
                                    }
                                }
                            ]
                        }).center().show();
                    }
                }
            ]
        });
        this.callParent();
    }
});