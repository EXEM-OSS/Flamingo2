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
Ext.define('Flamingo2.model.dashboard.WorkflowHistory', {
    extend: 'Ext.data.Model',
    fields: [
        //트리 식별자
        {name: 'id', type: 'string'},

        //워크플로우 히스토리
        {name: 'workflowId', type: 'string'},
        {name: 'jobId', type: 'int'},
        {name: 'jobStringId', type: 'string'},
        {name: 'workflowName', type: 'string'},
        {name: 'workflowXml', type: 'string'},
        {name: 'currentAction', type: 'string'},
        {name: 'jobName', type: 'string'},
        {name: 'variable', type: 'string'},
        {name: 'startDate', type: 'string'},
        {name: 'endDate', type: 'string'},
        {name: 'elapsed', type: 'int'},
        {name: 'cause', type: 'string'},
        {name: 'currentStep', type: 'int'},
        {name: 'totalStep', type: 'int'},
        {name: 'exception', type: 'string'},
        {name: 'username', type: 'string'},
        {name: 'jobType', type: 'string'},
        {name: 'logPath', type: 'string'},
        {name: 'sf_parentIdentifier', type: 'string'},
        {name: 'sf_rootIdentifier', type: 'string'},
        {name: 'sf_depth', type: 'int'},
        {name: 'sf_taskId', type: 'string'},
//        {name: 'progress', type: 'int' , default: 1},

        //타스크 히스토리
        {name: 'identifier', type: 'string', mapping: 'jobStringId'},
        {name: 'taskId', type: 'string'},

        //공통
        {name: 'rowid', type: 'string'},
        {name: 'text', type: 'string'},
        {name: 'type', type: 'string'},
        {name: 'status', type: 'string'},
        {name: 'iconCls', type: 'string', defaultValue: 'none'}
    ]
});