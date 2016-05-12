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

Ext.define('Flamingo2.view.system.user.combo.OrganizationCombo', {
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.organizationCombo',

    name: 'org_id',
    tpl: '<tpl for="."><div class="x-boundlist-item" data-qtip="{org_name}">{org_name}</div></tpl>',
    displayField: 'org_name',
    valueField: 'org_id',
    queryMode: 'local',
    editable: true,
    selectOnFocus: true,
    width: 120,
    collapsible: false
});