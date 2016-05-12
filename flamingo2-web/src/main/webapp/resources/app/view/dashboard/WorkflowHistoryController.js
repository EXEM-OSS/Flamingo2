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
Ext.define('Flamingo2.view.dashboard.WorkflowHistoryController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.workflowHistoryController',

    listen: {
        controller: {
            websocketController: {
                workflowMessage: 'onWorkflowMessage'
            }
        }
    },

    destroyProgresses: function () {
        var progresses = Ext.ComponentQuery.query('progressbar[name=taskProgress]');
        Ext.each(progresses, function (progrese) {
            progrese.destroy();
        });
        $("[name=taskProgress]").remove();
    },

    onWorkflowMessage: function (message) {
        var me = this;
        var data = Ext.decode(message.body);
        var store = me.getViewModel().getStore('workflowHistoryStore');

        if (data.command == 'taskHistories') {
            var taskhistories = data.data;
            for (var i = 0; i < taskhistories.length; i++) {
                var taskhistory = taskhistories[i];
                var identifier = taskhistory.identifier;
                var taskId = taskhistory.taskId;
                var status = taskhistory.status;

                var records = store.queryBy(function (record) {
                    return (record.get('identifier') == identifier && record.get('taskId') == taskId);
                });
                for (var r = 0; r < records.items.length; r++) {
                    var record = records.items[r];
                    record.set('status', status);
                    record.commit();
                }
            }
        }
        if (data.command == 'workflowHistory') {
            var workflowHistory = data.data;

            var sf_parentIdentifier = workflowHistory.sf_parentIdentifier;
            var sf_taskId = workflowHistory.sf_taskId;
            var jobStringId = workflowHistory.jobStringId;
            var status = workflowHistory.status;

            var updateTasks = store.queryBy(function (record) {
                return (record.get('identifier') == sf_parentIdentifier && record.get('taskId') == sf_taskId );
            });
            for (var r = 0; r < updateTasks.items.length; r++) {

                var progreses = Ext.ComponentQuery.query('progressbar[name=taskProgress][identifier=' + sf_parentIdentifier + '][taskId=' + sf_taskId + ']');
                Ext.each(progreses, function (progrese) {
                    progrese.destroy();
                });

                var record = updateTasks.items[r];
                workflowHistory.startDate = workflowHistory.startDateSimple;
                workflowHistory.endDate = workflowHistory.endDateSimple;
                workflowHistory.text = workflowHistory.workflowName;
                workflowHistory.cls = "folder";
                workflowHistory.leaf = false;
                workflowHistory.type = "workflow";
                workflowHistory.rowid = workflowHistory.id;
                delete workflowHistory.id;
                workflowHistory.identifier = null;
                workflowHistory.taskId = null;

                for (var key in workflowHistory) {
                    record.set(key, workflowHistory[key]);
                }
                var id = record.get('id');
                var list = id.split('/');
                var removeStr = list[list.length - 1];
                id = id.replace(removeStr, workflowHistory.jobStringId);
                record.set('id', id);
                record.commit();
            }


            var records = store.queryBy(function (record) {
                return (record.get('jobStringId') == jobStringId);
            });
            for (var r = 0; r < records.items.length; r++) {
                var record = records.items[r];
                record.set('status', status);

                record.set('startDate', workflowHistory.startDateSimple);
                record.set('endDate', workflowHistory.endDateSimple);
                record.set('elapsed', workflowHistory.elapsed);
                record.set('currentStep', workflowHistory.currentStep);
                record.set('totalStep', workflowHistory.totalStep);
                record.set('currentAction', workflowHistory.currentAction);
                record.commit();
            }
        }
    }
});