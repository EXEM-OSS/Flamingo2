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
Ext.define('Flamingo2.view.designer.property.bpmn.BPMN_PARALLEL', {
    extend: 'Flamingo2.view.designer.property._NODE_SHELL',
    alias: 'widget.BPMN_PARALLEL',

    width: 600,
    height: 350,

    requires: [
        'Flamingo2.view.designer.property.bpmn.BPMN_controller',
        'Flamingo2.view.designer.property._ParallelGrid'
    ],

    controller: 'designer.bpmn_controller',

    items: [
        {
            title: message.msg('workflow.etc.pararelljoin.flows.title'),
            xtype: '_parallelGrid',
            reference: '_parallelGrid'
        },
        {
            mainform: null,
            xtype: 'form',
            hidden: true,
            items: [
                {
                    xtype: 'hidden',
                    name: 'sequenceData',
                    reference: 'sequenceData',
                    value: '',
                    allowBlank: true
                }
            ]
        }
    ],
    listeners: {
        afterrender: 'onBPMN_PARALLELafterrender'
    }
});