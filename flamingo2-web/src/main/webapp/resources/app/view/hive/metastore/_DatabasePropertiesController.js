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
 * Created by Park on 15. 7. 27..
 */
Ext.define('Flamingo2.view.hive.metastore._DatabasePropertiesController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hiveDatabaseproperties',

    onAfterrender: function() {
        var me = this;
        var refs = me.getReferences();

        var params = {
            clusterName: ENGINE.id,
            database: me.getView().database
        };

        invokeGet(CONSTANTS.HIVE.METASTORE.GET_DATABASE_SCRIPT, params,
            function (response) {
                var r = Ext.decode(response.responseText);

                if (r.success) {
                    console.debug(refs.script);
                    refs.script.setValue(r.map.script);
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function (response) {
                Ext.MessageBox.show({
                    title: message.msg('common.warn'),
                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    }
});