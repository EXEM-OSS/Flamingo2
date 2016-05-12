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
Ext.define('Flamingo2.view.designer.canvas.CanvasController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.canvasController',

    requires: [
        'Flamingo2.view.designer.canvas.events.onCanvasRender'
    ],

    highlightById: function (elementId) {
        $("#" + elementId).find("text").attr("fill", "red");
    },
    unhighlightById: function (elementId) {
        $("#" + elementId).find("text").attr("fill", "black");
    },
    setwireEvent: function (shapeElement) {
        var me = this;
        var canvas = query('canvas');
        $("#" + shapeElement.id).hover(function () {
            var nodeData = Ext.clone(canvas.graph.getCustomData(shapeElement));
            if (nodeData && nodeData.metadata) {
                if (nodeData.properties && nodeData.properties.node_from == 'wire') {
                    var wiredId = nodeData.properties.node_display;
                    me.highlightById(wiredId);
                    me.highlightById(shapeElement.id);
                }
            }
        }, function () {
            var nodeData = Ext.clone(canvas.graph.getCustomData(shapeElement));
            if (nodeData && nodeData.metadata) {
                if (nodeData.properties && nodeData.properties.node_from == 'wire') {
                    var wiredId = nodeData.properties.node_display;
                    me.unhighlightById(wiredId);
                    me.unhighlightById(shapeElement.id);
                }
            }
        });
    },
    setwireEventAll: function () {
        var canvas = query('canvas');
        var allNodes = canvas.graph.getElementsByType();
        for (var i = 0; i < allNodes.length; i++) {
            this.setwireEvent(allNodes[i]);
        }
    },
    onCanvasRender: function (component, eOpts) {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasRender').onCanvasRender(component, eOpts);
    },
    onCanvasResize: function (component, width, height, oldWidth, oldHeight, eOpts) {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasRender')
            .onCanvasResize(component, width, height, oldWidth, oldHeight, eOpts);
    },
    onCanvasNodeBeforeConnect: function (canvas, event, edgeElement, fromElement, toElement) {
        return Ext.create('Flamingo2.view.designer.canvas.events.onCanvasNode')
            .onCanvasNodeBeforeConnect(canvas, event, edgeElement, fromElement, toElement);
    },
    onCanvasNodeConnect: function (canvas, event, edgeElement, fromElement, toElement) {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasNode')
            .onCanvasNodeConnect(canvas, event, edgeElement, fromElement, toElement);
    },
    onCanvasNodeDisconnected: function (canvas, event, edgeElement, fromElement, toElement) {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasNode')
            .onCanvasNodeDisconnected(canvas, event, edgeElement, fromElement, toElement);
    },
    onCanvasNodeBeforeRemove: function (canvas, event, element) {
        return Ext.create('Flamingo2.view.designer.canvas.events.onCanvasNode')
            .onCanvasNodeBeforeRemove(canvas, event, element);
    },
    onCanvasBeforeLabelChange: function (canvas, event, shapeElement, afterText, beforeText) {
        return Ext.create('Flamingo2.view.designer.canvas.events.onCanvasLabel')
            .onCanvasBeforeLabelChange(canvas, event, shapeElement, afterText, beforeText);
    },
    onCanvasLabelChanged: function (canvas, event, shapeElement, afterText, beforeText) {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasLabel')
            .onCanvasLabelChanged(canvas, event, shapeElement, afterText, beforeText);
    },
    onCreateClick: function () {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasActionController').onCreateClick();
    },
    onSaveClick: function () {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasActionController').onSaveClick();
    },
    onRunClick: function () {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasActionController').onRunClick();
    },
    onWorkflowXMLClick: function () {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasActionController').onWorkflowXMLClick();
    },
    onWorkflowCopyClick: function () {
        Ext.create('Flamingo2.view.designer.canvas.events.onCanvasActionController').onWorkflowCopyClick();
    },
    onHdfsBrowserClick: function () {
        Ext.create('Flamingo2.view.designer.canvas.HdfsBrowserWindow').center().show();
    },
    onHiveClick: function () {
        Ext.create('Flamingo2.view.designer.canvas.HiveEditorWindow').center().show();
    }
});