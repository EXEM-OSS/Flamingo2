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
Ext.define('Flamingo2.view.system.authority.register.HdfsAuthModificationForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.hdfsAuthModificationForm',

    requires: [
        'Flamingo2.view.system.authority.combo.UserAuthCombo',
        'Flamingo2.view.system.authority.combo.UserLevelCombo'
    ],

    bodyPadding: 10,
    reference: 'hdfsAuthModificationForm',
    items: [
        {
            layout: 'anchor',
            defaults: {
                style: 'margin-top:10px; margin-bottom:10px',
                labelWidth: 70,
                labelAlign: 'right'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'id',
                    fieldLabel: 'ID',
                    readOnly: true,
                    anchor: '50%',
                    hidden: true
                },
                {
                    xtype: 'checkboxgroup',
                    fieldLabel: message.msg('common.path'),
                    columns: 2,
                    anchor: '70%',
                    items: [
                        {
                            xtype: 'textfield',
                            reference: 'hdfsPathPattern',
                            itemId: 'hdfsPathPattern',
                            name: 'hdfs_path_pattern',
                            fieldStyle: 'color: black',
                            allowBlank: false,
                            readOnly: true
                        },
                        {
                            xtype: 'checkboxfield',
                            reference: 'newHdfsPathPattern',
                            name: 'new_hdfs_path_pattern',
                            style: 'margin-left:5px;',
                            uncheckedValue: '',
                            inputValue: 'isNewValue',
                            handler: function (checkbox) {
                                var hdfsPathPattern = query('hdfsAuthModificationForm #hdfsPathPattern');
                                if (checkbox.checked) {
                                    hdfsPathPattern.setReadOnly(0);
                                } else
                                    hdfsPathPattern.setReadOnly(1);
                            }
                        }
                    ]
                },
                {
                    xtype: 'checkboxgroup',
                    fieldLabel: message.msg('system.authority.common.auth'),
                    columns: 2,
                    anchor: '45%',
                    items: [
                        {
                            xtype: 'userAuthCombo',
                            reference: 'userAuthCombo',
                            itemId: 'userAuthCombo',
                            fieldStyle: 'color: black',
                            allowBlank: false,
                            readOnly: true,
                            bind: {
                                store: '{userAuthStore}'
                            },
                            listeners: {
                                select: function (combo) {
                                    var userLevelCombo = query('hdfsAuthModificationForm #userLevelCombo');

                                    if (combo.value == 1) {
                                        userLevelCombo.select(1);
                                        userLevelCombo.setReadOnly(1);
                                    } else {
                                        userLevelCombo.reset();
                                        userLevelCombo.setReadOnly(0);
                                    }
                                }
                            }
                        },
                        {
                            xtype: 'checkboxfield',
                            reference: 'newUserAuth',
                            name: 'new_user_auth',
                            style: 'margin-left:5px;',
                            uncheckedValue: '',
                            inputValue: 'isNewValue',
                            handler: function (checkbox) {
                                var userAuthCombo = query('hdfsAuthModificationForm #userAuthCombo');
                                if (checkbox.checked) {
                                    userAuthCombo.setReadOnly(0);
                                } else
                                    userAuthCombo.setReadOnly(1);
                            }
                        }
                    ]
                },
                {
                    xtype: 'checkboxgroup',
                    fieldLabel: message.msg('system.authority.common.level'),
                    columns: 2,
                    anchor: '45%',
                    items: [
                        {
                            xtype: 'userLevelCombo',
                            reference: 'userLevelCombo',
                            itemId: 'userLevelCombo',
                            fieldStyle: 'color: black',
                            allowBlank: false,
                            readOnly: true,
                            bind: {
                                store: '{userLevelStore}'
                            }
                        },
                        {
                            xtype: 'checkboxfield',
                            reference: 'newUserLevel',
                            name: 'new_user_level',
                            style: 'margin-left:5px;',
                            uncheckedValue: '',
                            inputValue: 'isNewValue',
                            handler: function (checkbox) {
                                var userLevelCombo = query('hdfsAuthModificationForm #userLevelCombo');
                                if (checkbox.checked) {
                                    userLevelCombo.setReadOnly(0);
                                } else
                                    userLevelCombo.setReadOnly(1);
                            }
                        }
                    ]
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
            style: 'margin-top:10px; margin-right:10px; margin-bottom:10px; margin-left:10px;',
            items: [
                {
                    xtype: 'checkboxgroup',
                    itemId: 'directoryGroup',
                    style: 'margin-top:10px; margin-bottom:15px; margin-left:20px;',
                    columns: 3,
                    items: [
                        {
                            boxLabel: message.msg('system.authority.common.all'), name: 'dirAll',
                            handler: function (check) {
                                var hdfsBrowserAuthForm = query('hdfsAuthModificationForm #directoryCheckboxField');
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
                            boxLabel: message.msg('system.authority.common.merge'),
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
                    style: 'margin-top:10px; margin-bottom:15px; margin-left:20px;',
                    columns: 3,
                    items: [
                        {
                            boxLabel: message.msg('system.authority.common.all'), name: 'fileAll',
                            handler: function (check) {
                                var hdfsBrowserAuthForm = query('hdfsAuthModificationForm #fileCheckboxField');
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
    ]
});