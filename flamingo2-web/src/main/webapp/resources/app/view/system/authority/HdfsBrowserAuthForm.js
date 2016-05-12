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
Ext.define('Flamingo2.view.system.authority.HdfsBrowserAuthForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.hdfsBrowserAuthFormPanel',

    title: message.msg('system.authority.information'),
    reference: 'hdfsBrowserAuthForm',
    border: true,
    bodyPadding: 20,
    height: 400,
    items: [
        {
            layout: 'anchor',
            defaults: {
                style: 'margin-bottom:10px',
                labelWidth: 60,
                labelAlign: 'right'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'id',
                    fieldLabel: message.msg('system.authority.common.id'),
                    readOnly: true,
                    anchor: '50%',
                    hidden: true
                },
                {
                    xtype: 'textfield',
                    name: 'hdfs_path_pattern',
                    fieldLabel: message.msg('common.path'),
                    readOnly: true,
                    anchor: '50%',
                    fieldStyle: 'color: black'
                },
                {
                    xtype: 'textfield',
                    name: 'auth_id',
                    fieldLabel: message.msg('system.authority.common.authId'),
                    readOnly: true,
                    anchor: '25%',
                    fieldStyle: 'color: black',
                    hidden: true
                },
                {
                    xtype: 'textfield',
                    name: 'auth_name',
                    fieldLabel: message.msg('system.authority.common.auth'),
                    readOnly: true,
                    anchor: '25%',
                    fieldStyle: 'color: black'
                },
                {
                    xtype: 'textfield',
                    name: 'level',
                    fieldLabel: message.msg('system.authority.common.levelId'),
                    readOnly: true,
                    anchor: '25%',
                    fieldStyle: 'color: black',
                    hidden: true
                },
                {
                    xtype: 'textfield',
                    name: 'level_name',
                    fieldLabel: message.msg('system.authority.common.level'),
                    readOnly: true,
                    anchor: '25%',
                    fieldStyle: 'color: black'
                }
            ]
        },
        {
            xtype: 'fieldset',
            title: message.msg('common.directory'),
            reference: 'directoryCheckboxField',
            itemId: 'directoryCheckboxField',
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            style: 'margin-top:20px; margin-left:10px; margin-bottom:10px; margin-right:10px',
            items: [
                {
                    xtype: 'checkboxgroup',
                    itemId: 'directoryGroup',
                    style: 'margin-top:10px; margin-bottom:15px; margin-left:60px;',
                    columns: 5,
                    items: [
                        {
                            boxLabel: message.msg('system.authority.common.all'), name: 'dirAll',
                            handler: function (check) {
                                var hdfsBrowserAuthForm = query('hdfsBrowserAuthFormPanel #directoryCheckboxField');
                                var checkItems = hdfsBrowserAuthForm.query('[isCheckbox]');

                                if (check.checked) {
                                    checkItems.forEach(function (checkbox) {
                                        checkbox.setValue(1);
                                    });
                                } else {
                                    checkItems.forEach(function (checkbox) {
                                        checkbox.setValue(0);
                                    });
                                }
                            }
                        },
                        {
                            boxLabel: message.msg('system.authority.common.create'),
                            name: 'create_dir',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.copy'),
                            name: 'copy_dir',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.move'),
                            name: 'move_dir',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.rename'),
                            name: 'rename_dir',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.delete'),
                            name: 'delete_dir',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.delete'),
                            name: 'merge_dir',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.permission'),
                            name: 'permission_dir',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.db'),
                            name: 'create_db_dir',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.table'),
                            name: 'create_table_dir',
                            uncheckedValue: 0,
                            inputValue: 1
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'fieldset',
            title: message.msg('common.file'),
            reference: 'fileCheckboxField',
            itemId: 'fileCheckboxField',
            layout: 'anchor',
            defaults: {
                anchor: '100%'
            },
            style: 'margin-left:10px; margin-right:10px;',
            items: [
                {
                    xtype: 'checkboxgroup',
                    itemId: 'fileGroup',
                    style: 'margin-top:10px; margin-bottom:15px; margin-left:60px;',
                    columns: 5,
                    items: [
                        {
                            boxLabel: message.msg('system.authority.common.all'), name: 'fileAll',
                            handler: function (check) {
                                var hdfsBrowserAuthForm = query('hdfsBrowserAuthFormPanel #fileCheckboxField');
                                var checkItems = hdfsBrowserAuthForm.query('[isCheckbox]');

                                if (check.checked) {
                                    checkItems.forEach(function (checkbox) {
                                        checkbox.setValue(1);
                                    });
                                } else {
                                    checkItems.forEach(function (checkbox) {
                                        checkbox.setValue(0);
                                    });
                                }
                            }
                        },
                        {
                            boxLabel: message.msg('system.authority.common.copy'),
                            name: 'copy_file',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.move'),
                            name: 'move_file',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.rename'),
                            name: 'rename_file',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.delete'),
                            name: 'delete_file',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.upload'),
                            name: 'upload_file',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.download'),
                            name: 'download_file',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.view'),
                            name: 'view_file',
                            uncheckedValue: 0,
                            inputValue: 1
                        },
                        {
                            boxLabel: message.msg('system.authority.common.permission'),
                            name: 'permission_file',
                            uncheckedValue: 0,
                            inputValue: 1
                        }
                    ]
                }
            ]
        }
    ],
    dockedItems: [
        {
            xtype: 'toolbar',
            items: [
                {
                    xtype: 'tbfill'
                },
                {
                    text: message.msg('system.authority.common.modify'),
                    iconCls: 'common-rename',
                    tooltip: message.msg('system.authority.modify.tip'),
                    handler: 'onModifyHdfsBrowserAuth'
                }
            ]
        }
    ]
});
