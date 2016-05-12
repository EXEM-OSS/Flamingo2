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
Ext.define('Flamingo2.view.visualization.ggplot2.Ggplot2', {
    extend: 'Flamingo2.panel.Panel',
    alias: 'widget.ggplot2',

    requires: [
        'Flamingo2.view.visualization.ggplot2.Ggplot2Controller',
        'Flamingo2.view.visualization.ggplot2.Ggplot2Model',
        'Flamingo2.view.visualization.ggplot2.DataPanel',
        'Flamingo2.view.visualization.ggplot2.LayerPanel',
        'Flamingo2.view.visualization.ggplot2.MenuBar'
    ],

    controller: 'ggplot2Controller',

    viewModel: {
        type: 'ggplot2Model'
    },

    flex: 1,

    layout: 'border',

    items: [
        {
            xtype: 'form',
            layout: 'fit',
            region: 'center',
            border: 1,
            reference: 'workspace',
            dockedItems: [
                {
                    xtype: 'ggplot2MenuBar',
                    dock: 'top'
                }
            ],
            items: [
                {
                    xtype: 'menu',
                    reference: 'plotMenu'
                }
            ]
        },
        {
            xtype: 'ggplot2LayerPanel',
            border: 1,
            title: message.msg('visual.layer'),
            region: 'west',
            split: true,
            collapsible: true,
            width: 200
        },
        {
            xtype: 'ggplot2DataPanel',
            border: 1,
            title: message.msg('common.data'),
            region: 'east',
            split: true,
            collapsible: true,
            width: 200
        }
    ],

    listeners: {
        afterrender: 'onAfterrender'
    }
});