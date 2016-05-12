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
Ext.define('Flamingo2.view.designer.property._ShapesGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget._shapesGrid',

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
        {text: message.msg('workflow.common.nodeid'), flex: 1, sortable: true, dataIndex: 'id', align: 'center'},
        {text: message.msg('workflow.common.provider'), flex: 1, sortable: true, dataIndex: 'provider', hidden: true}
    ],

    /*
     tools: [
     {
     type: 'refresh',
     tooltip: '워크플로우 목록을 갱신합니다.',
     handler: function (event, toolEl, panel) {
     query('_shapesGrid').getStore().load();
     }
     }
     ],
     */

    tipUse: 'on',

    listeners: {
        afterrender: function () {
            var me = this;
            var mydata = [];
            var list = query('BASE_NODE').getSelectableNode();
            var canvas = query('canvas');
            for (var i = 0; i < list.length; i++) {
                nodeData = Ext.clone(canvas.graph.getCustomData(list[i]));
                if (nodeData && nodeData.metadata) {
                    var name = nodeData.metadata.name;
                    var provider = nodeData.metadata.provider;
                    var id = list[i].id
                    mydata.push({
                        node: name,
                        id: id,
                        provider: provider
                    });
                }
            }
            me.getStore().loadData(mydata);

            var theElem = query('BASE_NODE').getEl();
            var theTip = Ext.create('Ext.tip.Tip', {
                html: message.msg('workflow.msg_shape_grid_dblclick'),
                margin: '25 0 0 150',
                shadow: false
            });

            me.addListener('itemmouseenter', function (grid, selRow, selHtml) {
                if (me.tipUse == 'on')
                    theTip.showAt(theElem.getX(), theElem.getY());
                canvas.highlightById(selRow.data.id);
            });
            me.addListener('itemmouseleave', function (grid, selRow, selHtml) {
                if (me.tipUse == 'on')
                    theTip.hide();
                canvas.unhighlightById(selRow.data.id);
            });
        }
    }
});