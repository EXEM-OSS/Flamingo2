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
Ext.define('Flamingo2.view.hive.metastore._TableProperties', {
    extend: 'Ext.window.Window',
    layout: 'fit',
    modal: true,
    height: 400,
    width: 700,
    resizable: false,
    title: message.msg('hive.properties'),
    requires: [
        'Flamingo2.view.hive.metastore._TablePropertiesController',
        'Flamingo2.view.hive.metastore.MetastoreModel',
        'Flamingo2.view.component.editor.AbstractEditor'
    ],
    controller: 'hiveTableproperties',
    viewModel: {
        type: 'hiveMetastoreModel'
    },

    items: [
        {
            xtype: 'abstractEditor',
            parser: 'hive',
            readOnly: true,
            reference: 'script'
        }
    ],
    listeners: {
        afterrender: 'onAfterrender'
    }
});