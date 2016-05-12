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
Ext.define('Flamingo2.view.designer.nodeList.NodeListController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.nodeListController',

    onNodeListBeforerender: function () {
        var type = this.getView().type;
        var store = this.getViewModel().getStore('nodemeta');
        if (type) {
            var types = type.split(',');
            store.filter({
                filterFn: function (item) {
                    for (var i = 0; i < types.length; i++) {
                        if (item.get("type") === types[i]) {
                            return true;
                        }
                    }
                }
            });
        } else {
            store.filter({
                filterFn: function (item) {
                    if (item.get("type") !== 'START' && item.get("type") !== 'END') {
                        return true;
                    }
                }
            });
        }
    },
    /**
     * 노드리스트 패널 Render 핸들러 : 노드의 DragZone 을 설정하고 Drag 시 노드 메타데이타를 설정한다.
     *
     * @param {Ext.Component} component
     * @param {Object} eOpts The options object passed to Ext.util.Observable.addListener.
     */
    onNodeListRender: function (component, eOpts) {
        component.dragZone = Ext.create('Ext.dd.DragZone', component.getEl(), {
            getDragData: function (e) {
                var sourceEl = e.getTarget(component.itemSelector, 10), d;

                if (sourceEl) {
                    d = sourceEl.cloneNode(true);
                    d.id = Ext.id();
                    var store = component.getViewModel().getStore('nodemeta');
                    return {
                        sourceEl: sourceEl,
                        repairXY: Ext.fly(sourceEl).getXY(),
                        ddel: d,
                        nodeMeta: store.getById(sourceEl.id).data
                    };
                }
            },

            getRepairXY: function () {
                return this.dragData.repairXY;
            }
        });
    }
});