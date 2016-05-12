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
Ext.define('Flamingo2.view.monitoring.namenode.NamenodeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.namenodeController',

    onNamenodeSummaryAfterrender: function (view) {
        // Table Layout의 colspan 적용시 cell간 간격 조정이되지 않는 문제를 해결하기 위해서 적용함
        setTableLayoutFixed(view);

        invokeGet(CONSTANTS.MONITORING.NAMENODE.INFO, {clusterName: ENGINE.id},
            function (response) {
                var obj = Ext.decode(response.responseText);
                if (obj.success) {
                    obj.map.datanode = message.msg('common.total') + ' ' + obj.map.all + message.msg('monitoring.namenode.count') + ' / ' + message.msg('monitoring.namenode.live') + ' ' + obj.map.live + message.msg('monitoring.namenode.count') + ' / ' + message.msg('monitoring.namenode.dead') + ' ' + obj.map.dead + message.msg('monitoring.namenode.count');

                    query('namenodeSummary').getForm().setValues(obj.map);
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: obj.error.message,
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
    },

    /**
     * Engine Combobox Changed Event
     */
    onEngineChanged: function (engine) {
    }
});