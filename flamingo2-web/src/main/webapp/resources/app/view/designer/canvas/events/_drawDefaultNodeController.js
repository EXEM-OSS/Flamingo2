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
Ext.define('Flamingo2.view.designer.canvas.events._drawDefaultNodeController', {
    extend: 'Ext.app.ViewController',

    /**
     * 디폴트 시작, 끝 노드를 드로잉한다.
     * @private
     */
    run: function () {
        var canvas = query('canvas');
        var startNode, endNode;

        if (canvas.graph) {
            startNode = canvas.graph.drawShape([100, 100], new OG.E_Start(message.msg('common.start')), [30, 30]);
            endNode = canvas.graph.drawShape([500, 100], new OG.E_End(message.msg('common.end')), [30, 30]);

            canvas.graph.setCustomData(startNode, {
                metadata: {
                    "type": "START",
                    "icon": "/resources/image/app/designer/others/btn_start.png",
                    "identifier": "START",
                    "name": message.msg('common.start'),
                    "minPrevNodeCounts": 0,
                    "maxPrevNodeCounts": 0,
                    "minNextNodeCounts": 1,
                    "maxNextNodeCounts": -1,
                    "notAllowedPrevTypes": "",
                    "notAllowedNextTypes": "END,IN,OUT",
                    "notAllowedPrevNodes": "",
                    "notAllowedNextNodes": "END"
                }
            });

            canvas.graph.setCustomData(endNode, {
                metadata: {
                    "type": "END",
                    "identifier": "END",
                    "icon": "/resources/image/app/designer/others/btn_end.png",
                    "name": message.msg('common.end'),
                    "minPrevNodeCounts": 1,
                    "maxPrevNodeCounts": -1,
                    "minNextNodeCounts": 0,
                    "maxNextNodeCounts": 0,
                    "notAllowedPrevTypes": "START,IN,OUT",
                    "notAllowedNextTypes": "",
                    "notAllowedPrevNodes": "START",
                    "notAllowedNextNodes": ""
                }
            });
        }
    }
});