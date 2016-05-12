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
 * ViewController for Flamingo2.view.system.hawq.role.HawqRole
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.system.hawq.role.HawqRole
 */
Ext.define('Flamingo2.view.system.hawq.role.HawqRoleController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hawqRoleController',

    requires: [
        'Flamingo2.view.system.hawq.role._RoleRegist'
    ],

    listen: {
        controller: {
            hawqRoleController: {
                hawqDropRoleClick: 'hawqDropRoleClick'
            },
            roleregist: {
                refreshRole: 'onBtnRefreshClick'
            }
        }
    },

    onAfterrender: function () {
        Ext.each(this.getReferences().hawqRoleViewForm.query('field'), function (field, index, allFields) {
            field.setReadOnly(true);
        });
    },

    /**
     * 신규등록 버튼 이벤트 처리
     * */
    onBtnNewClick: function () {
        Ext.create('Flamingo2.view.system.hawq.role._RoleRegist', {
            modal: true,
            width: 550,
            height: 550,
            resizable: false
        }).show();
    },

    onRoleItemclick: function (grid, record, item, index, e, eOpts) {
        var me = this,
            refs = me.getReferences(),
            formPanel = refs.hawqRoleViewForm;

        formPanel.setLoading(true);

        if (record.get('oid')) {
            invokeGet(
                CONSTANTS.HAWQ.AUTH.ROLE,
                {
                    clusterName: ENGINE.id,
                    oid: record.get('oid')
                },
                function (response) {
                    var result = Ext.decode(response.responseText);
                    if (result.success) {
                        var form = formPanel.getForm(),
                            roleDetail = result.object;

                        form.setValues(roleDetail);
                        me.getViewModel().setData({
                            originalRole: roleDetail
                        });
                        formPanel.setLoading(false);
                        Ext.each(formPanel.query('field'), function (field, index, allFields) {
                            field.setReadOnly(false);
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: message.msg('hawq.title.fail'),
                            message: message.msg('hawq.msg.fail.getrole') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING,
                            fn: function () {
                                formPanel.setLoading(false);
                                Ext.each(formPanel.query('field'), function (field, index, allFields) {
                                    field.setReadOnly(false);
                                });
                            }
                        });
                    }
                },
                function (response) {
                    Ext.MessageBox.show({
                        title: message.msg('hawq.title.fail'),
                        message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.ERROR,
                        fn: function () {
                            formPanel.setLoading(false);
                            Ext.each(formPanel.query('field'), function (field, index, allFields) {
                                field.setReadOnly(false);
                            });
                        }
                    });
                }
            );
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.invalidrole'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING,
                fn: function () {
                    formPanel.setLoading(false);
                }
            });
        }
    },

    hawqRoleGridRowcontextmenu: function (grid, record, tr, rowIndex, e, eOpts) {
        e.stopEvent();
        var me = this,
            refs = me.getReferences(),
            menu = Ext.create('Ext.menu.Menu', {
                items: [
                    {
                        text: message.msg('hawq.button.role.drop'),
                        iconCls: 'common-delete',
                        handler: function (item, e) {
                            me.fireEvent('hawqDropRoleClick', grid, refs);
                        }
                    }
                ]
            });
        menu.showAt(e.pageX - 5, e.pageY - 5);
    },

    hawqDropRoleClick: function (grid, refs) {
        var record = grid.getSelectionModel().getSelection()[0];
        if (record) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.drop'),
                message: format(message.msg('hawq.msg.question.droprole'), record.get('rolname')),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        invokePostByMap(
                            CONSTANTS.HAWQ.AUTH.DROP_ROLE,
                            {
                                clusterName: ENGINE.id,
                                rolname: record.get('rolname')
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.droprole'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            grid.getStore().reload();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.droprole') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING
                                    });
                                }
                            },
                            function (response) {
                                Ext.MessageBox.show({
                                    title: message.msg('hawq.title.fail'),
                                    message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.ERROR
                                });
                            }
                        );
                    }
                }
            });
        } else {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.droprole'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    onBtnRefreshClick: function (button) {
        var refs = this.getReferences(),
            vm = this.getViewModel(),
            formPanel = refs.hawqRoleViewForm;

        button.up('grid').getStore().reload();
        formPanel.getForm().reset();
        Ext.each(formPanel.query('field'), function (field, index, allFields) {
            field.setReadOnly(true);
        });
        delete vm.getData().originalRole;
    },

    onBtnSaveClick: function (owner, tool) {
        var me = this,
            refs = me.getReferences(),
            vm = me.getViewModel(),
            formPanel = refs.hawqRoleViewForm,
            form = formPanel.getForm();

        if (form.isValid() && vm.getData().originalRole) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.alter'),
                message: message.msg('hawq.msg.question.alterrole'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        formPanel.setLoading(true);

                        var fieldValues = form.getFieldValues();

                        fieldValues.clusterName = ENGINE.id;
                        fieldValues.denyFromTime = form.findField('denyFromTime').time;
                        fieldValues.denyToTime = form.findField('denyToTime').time;
                        if (fieldValues.rolvaliduntil) {
                            fieldValues.rolvaliduntil = dateFormat(fieldValues.rolvaliduntil.getTime(), 'yyyy-MM-dd');
                        }

                        invokePostByMap(
                            CONSTANTS.HAWQ.AUTH.ALTER_ROLE,
                            {
                                clusterName: ENGINE.id,
                                newRoleName: fieldValues.rolname,
                                originalRole: vm.getData().originalRole,
                                updatedRole: fieldValues
                            },
                            function (response) {
                                var result = Ext.decode(response.responseText);
                                if (result.success) {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.success'),
                                        message: message.msg('hawq.msg.success.alterrole'),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.INFO,
                                        fn: function () {
                                            delete vm.getData().originalRole;
                                            formPanel.setLoading(false);
                                            refs.hawqGroupRoleGrid.getStore().reload();
                                            refs.hawqLoginRoleGrid.getStore().reload();
                                            form.reset();
                                        }
                                    });
                                } else {
                                    Ext.MessageBox.show({
                                        title: message.msg('hawq.title.fail'),
                                        message: message.msg('hawq.msg.fail.alterrole') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message),
                                        buttons: Ext.MessageBox.OK,
                                        icon: Ext.MessageBox.WARNING,
                                        fn: function () {
                                            formPanel.setLoading(false);
                                        }
                                    });
                                }
                            },
                            function (response) {
                                Ext.MessageBox.show({
                                    title: message.msg('hawq.title.fail'),
                                    message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.ERROR,
                                    fn: function () {
                                        formPanel.setLoading(false);
                                    }
                                });
                            }
                        );
                    }
                }
            });
        } else if (!vm.getData().originalRole) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.warning'),
                message: message.msg('hawq.msg.warning.alterrole'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    hawqRoleDenyTimeChange: function (timefield, newValue, oldValue, eOpts) {
        if (newValue) {
            timefield.time = newValue.getHours() + ':' + newValue.getMinutes();
        }
    }
});