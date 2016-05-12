/*
 * Copyright (C) 2011  Flamingo Project (http://www.cloudine.io).
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

Ext.define('Flamingo2.view.batch.workflow.WorkflowTreeController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.batchWorkflowtree',

    onSelect: function (panel, record, index) {
        var me = this;
        var params = {
            clusterName: ENGINE.id,
            treeId: record.get('id')
        };

        invokeGet(CONSTANTS.DESIGNER.GET, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    me.fireEvent('workflowLoaded', obj.map);
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.fail'),
                        message: message.msg('batch.msg.cannot_get_workflow_job'),
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