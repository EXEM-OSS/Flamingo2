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
Ext.define('Flamingo2.view.system.authority.register.HdfsAuthRegisterForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.hdfsAuthRegisterForm',

    requires: [
        'Flamingo2.view.system.authority.combo.UserAuthCombo',
        'Flamingo2.view.system.authority.combo.UserLevelCombo'
    ],

    bodyPadding: '20 50 20 20',
    defaults: {
        labelWidth: 60,
        labelAlign: 'right'
    },
    reference: 'hdfsAuthRegisterForm',
    items: [
        {
            xtype: 'textfield',
            reference: 'hdfsPathPattern',
            itemId: 'hdfsPathPattern',
            name: 'hdfs_path_pattern',
            anchor: '100%',
            fieldLabel: message.msg('common.path'),
            allowBlank: false
        },
        {
            xtype: 'checkboxfield',
            reference: 'applyAll',
            name: 'apply_all',
            boxLabel: message.msg('system.authority.includeSubPath'),
            labelAlign: 'right',
            style: 'margin-left:65px;',
            handler: function (checkbox) {
                var hdfsBrowserTree = query('hdfsBrowserTreePanel');
                var selectedNode = hdfsBrowserTree.getSelectionModel().getLastSelected();
                var currentPath = selectedNode.get('id') == 'root' ? '/' : selectedNode.get('id');
                var hdfsPathPattern = query('hdfsAuthRegisterForm #hdfsPathPattern');
                var pathValue = hdfsPathPattern.getValue();
                var inputValue = pathValue == '/' ? pathValue + '**' : pathValue + '/**';

                if (checkbox.checked) {
                    hdfsPathPattern.setValue(inputValue);
                } else
                    hdfsPathPattern.setValue(currentPath);
            }
        },
        {
            xtype: 'userAuthCombo',
            reference: 'userAuthCombo',
            itemId: 'userAuthCombo',
            anchor: '100%',
            fieldLabel: message.msg('system.authority.common.auth'),
            allowBlank: false,
            bind: {
                store: '{userAuthStore}'
            },
            listeners: {
                select: function (combo) {
                    var user_level_combo = query('hdfsAuthRegisterForm #userLevelCombo');

                    if (combo.value == 1) {
                        user_level_combo.select(1);
                        user_level_combo.setReadOnly(1);
                    } else {
                        user_level_combo.reset();
                        user_level_combo.setReadOnly(0);
                    }
                }
            }
        },
        {
            xtype: 'userLevelCombo',
            reference: 'userLevelCombo',
            itemId: 'userLevelCombo',
            anchor: '100%',
            fieldLabel: message.msg('system.authority.common.level'),
            allowBlank: false,
            bind: {
                store: '{userLevelStore}'
            }
        }
    ]
});