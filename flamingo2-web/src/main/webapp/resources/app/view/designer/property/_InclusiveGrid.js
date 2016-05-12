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
Ext.define('Flamingo2.view.designer.property._InclusiveGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget._inclusiveGrid',

    stripeRows: true,

    margins: '0 0 0 0',

    selectableShapes: [],

    store: Ext.create('Ext.data.Store', {
        fields: [
            {name: 'node'},
            {name: 'id'}
        ],
        data: []
    }),

    columns: [
        {text: message.msg('common.node'), flex: 1, sortable: true, dataIndex: 'node', align: 'center'},
        {text: message.msg('common.node') + ' ID', flex: 1, sortable: true, dataIndex: 'id', align: 'center'},
        {text: "Provider", hidden: true, flex: 1, sortable: true, dataIndex: 'provider', align: 'center'}
    ],

    tipUse: 'on',

    listeners: {
        drawGrid: function () {
            var me = this;
            var mydata = [];
            var list = query('BASE_NODE').getNextNodeDataAndShapeId();
            for (var i = 0; i < list.length; i++) {
                var nodeData = list[i];
                if (nodeData && nodeData.metadata) {
                    var name = nodeData.metadata.name;
                    var provider = nodeData.metadata.provider;
                    var id = nodeData.shapeId;
                    mydata.push({
                        node: name,
                        id: id,
                        provider: provider
                    });
                }
            }
            me.getStore().loadData(mydata);
        }
    }
});