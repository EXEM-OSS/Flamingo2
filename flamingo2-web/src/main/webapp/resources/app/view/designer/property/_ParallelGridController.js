Ext.define('Flamingo2.view.designer.property._ParallelGridController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.designer._parallelGridController',

    onItemSelect: function () {
        this.lookupReference('flowUp').enable();
        this.lookupReference('flowDown').enable();
    },

    formfieldForSave: function (formfield) {
        this.formfieldForSave = formfield;
    },

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
        me.getView().getStore().loadData(mydata);
        me.sortData();
    },

    //sequenceData 가 있을경우 sequenceData 대로 그리드를 재구성한다.
    sortData: function () {
        var me = this;
        var sequenceData = me.getView().sequenceData;
        var store = me.getView().getStore();
        if (!sequenceData) {
            for (var i = 0; i < store.data.items.length; i++) {
                var record = store.data.items[i];
                record.set('sequence', i);
                record.commit();
            }
        } else {
            for (var i = 0; i < store.data.items.length; i++) {
                var hasData = false;
                var record = store.data.items[i];
                var id = record.data.id;
                for (var key in sequenceData) {
                    if (key == id) {
                        hasData = true;
                        record.set('sequence', sequenceData[key]);
                        record.commit();
                    }
                }
                if (!hasData) {
                    record.set('sequence', i);
                    record.commit();
                }
            }
        }
        this.setParallelRun();
    },

    setParallelRun: function () {
        var me = this;
        var store = me.getView().getStore();
        var sequenceStore = {};
        for (var i = 0; i < store.data.items.length; i++) {
            var record = store.data.items[i];
            var sequence = record.data.sequence;
            if (!sequenceStore[sequence]) {
                sequenceStore[sequence] = [];
            }
            sequenceStore[sequence].push(record);
        }
        for (var key in sequenceStore) {
            var parallel = false;
            var list = sequenceStore[key];
            if (list.length > 1)
                parallel = true;

            for (var c = 0; c < list.length; c++) {
                var record = list[c];
                record.set('parallel', parallel);
                record.commit();
            }
        }
        me.setNewSequenceData();
    },
    setNewSequenceData: function () {
        var sequenceData = {};
        var me = this;
        var store = me.getView().getStore();
        for (var i = 0; i < store.data.items.length; i++) {
            var record = store.data.items[i];
            var sequence = record.data.sequence;
            sequenceData[record.data.id] = sequence;
        }
        me.getView().sequenceData = sequenceData;

        if (me.formfieldForSave) {
            me.formfieldForSave.setValue(JSON.stringify(sequenceData));
        }

        //등록된 shapeElement 가 있다면 shapeElement에 연결된 라벨에 시퀀스 정보를 넣어준다.
        var shapeElement = me.getView().shapeElement;
        if (shapeElement) {
            var canvas = query('canvas');
            var edges = canvas.graph.getNextEdges(shapeElement);
            for (var e = 0; e < edges.length; e++) {
                var _connectInfo = canvas.graph.getRelatedElementsFromEdge(edges[e]);
                var nextnodeId = _connectInfo.to.id;
                for (var r = 0; r < store.data.items.length; r++) {
                    var record = store.data.items[r];
                    var recordId = record.data.id;
                    var sequence = record.data.sequence;
                    var parallel = record.data.parallel;
                    var parallelText = ' (' + message.msg('workflow.etc.flows.column.parallel.async') + ')';
                    if (parallel)
                        parallelText = ' (' + message.msg('workflow.etc.flows.column.parallel.sync') + ')';
                    if (recordId == nextnodeId) {
                        canvas.graph.drawLabel(edges[e], sequence + parallelText);
                    }
                }
            }
        }
    },

    onCellEdit: function () {
        this.setParallelRun();
    },

    onFlowUpClick: function () {
        var me = this;
        var selected = me.getView().getSelectionModel().getSelection()[0];
        var sequence = selected.data.sequence;

        if (sequence <= 0)
            return;
        else
            sequence--;
        selected.set('sequence', sequence);
        selected.commit();
        this.setParallelRun();
    },

    onFlowDownClick: function () {
        var me = this;
        var store = me.getView().getStore();
        var selected = me.getView().getSelectionModel().getSelection()[0];
        var sequence = selected.data.sequence;

        var max = store.data.items.length - 1;
        if (sequence >= max)
            return;
        else
            sequence++;
        selected.set('sequence', sequence);
        selected.commit();
        this.setParallelRun();
    }
});