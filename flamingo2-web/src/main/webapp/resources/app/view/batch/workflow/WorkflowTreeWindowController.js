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
Ext.define('Flamingo2.view.batch.workflow.WorkflowTreeWindowController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.workflowTreeWindowController',

    /**
     * 워크플로우 디자이너에 저장된 Job 목록을 보여준다.
     */
    onWorkflowTreeAfterRender: function (tree) {
        setTimeout(function () {
            tree.getStore().proxy.extraParams.clusterName = ENGINE.id;
            tree.getStore().load({
                callback: function () {
                    tree.getRootNode().expand();
                    var rootNode = tree.getStore().getNodeById('/');
                    tree.getSelectionModel().select(rootNode);
                }
            });
        }, 500);
    },

    /**
     * 워크플로우 Job 목록에서 선택한 Job에 대한 정보를 배치 작업 등록창에 보여준다.
     */
    onWorkflowClickOK: function () {
        var me = this;
        var treePanel = query('workflowTreePanel');
        var selectedNode = treePanel.getSelectionModel().getLastSelected();

        if (selectedNode.childNodes.length > 0) {
            Ext.MessageBox.show({
                title: message.msg('batch.workflow_select'),
                message: message.msg('batch.msg.workflow_select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return;
        }

        var url = CONSTANTS.DESIGNER.GET;
        var params = {
            clusterName: ENGINE.id,
            treeId: selectedNode.get('id')
        };

        invokeGet(CONSTANTS.DESIGNER.GET, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    me.fireEvent('workflowLoaded', obj.map);
                    me.getView().close();
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.fail'),
                        message: message.msg('batch.msg.workflow_get_fail'),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function (response) {
                Ext.MessageBox.show({
                    title: message.msg('common.warning'),
                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });

            }
        );
    },

    /**
     * 배치 작업 등록 창을 종료한다.
     */
    onWorkflowClickCancel: function () {
        this.getView().close();
    },


    /**
     * Cron Expression 정보를 등록할 수 있는 창을 보여준다.
     */
    onClickCronSetButton: function () {
        Ext.create('Flamingo2.view.batch.cron.CronExpressionWindow').center().show();
    }
});