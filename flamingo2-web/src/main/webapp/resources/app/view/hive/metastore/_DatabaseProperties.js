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
Ext.define('Flamingo2.view.hive.metastore._DatabaseProperties', {
    extend: 'Ext.window.Window',
    layout: 'fit',
    modal: true,
    height: 300,
    width: 400,
    resizable: false,
    title: message.msg('hive.database.script'),
    requires: [
        'Flamingo2.view.hive.metastore._DatabasePropertiesController',
        'Flamingo2.view.hive.metastore.MetastoreModel'
    ],
    controller: 'hiveDatabaseproperties',
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