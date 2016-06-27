/**
 * Created by Park on 15. 8. 26..
 */
Ext.define('Flamingo2.view.monitoring.spark.TaskTimeline', {
    extend: 'Ext.window.Window',

    requires: [
        'Flamingo2.view.monitoring.spark.SparkModel',
        'Flamingo2.view.monitoring.spark.TaskTimelineController',
        'Flamingo2.view.component.timeline.Timeline'
    ],

    controller: 'tasktimeline',
    viewModel: {
        type: 'sparkModel'
    },

    maximizable: true,
    modal: true,
    scrollable: true,
    bodyStyle: {
        background: '#FFFFFF'
    },

    items: [{
        xtype: 'component',
        width: '100%',
        height: 65,
        html: '<div class="task-legend-area">' +
                '<svg>' +
                    '<rect x="5px" y="10px" width="10px" height="10px" class="scheduler-delay-proportion"></rect><text x="25px" y="20px">Scheduler Delay</text><rect x="5px" y="25px" width="10px" height="10px" class="deserialization-time-proportion"></rect><text x="25px" y="35px">Task Deserialization Time</text><rect x="5px" y="40px" width="10px" height="10px" class="shuffle-read-time-proportion"></rect><text x="25px" y="50px">Shuffle Read Time</text><rect x="215px" y="10px" width="10px" height="10px" class="executor-runtime-proportion"></rect><text x="235px" y="20px">Executor Computing Time</text><rect x="215px" y="25px" width="10px" height="10px" class="shuffle-write-time-proportion"></rect><text x="235px" y="35px">Shuffle Write Time</text><rect x="215px" y="40px" width="10px" height="10px" class="serialization-time-proportion"></rect><text x="235px" y="50px">Result Serialization TIme</text><rect x="425px" y="10px" width="10px" height="10px" class="getting-result-time-proportion"></rect><text x="445px" y="20px">Getting Result Time</text>'+
                '</svg>' +
            '</div>'
    },{
        xtype: 'timeline',
        reference: 'timlineView',
        width: '100%',
        bind: {
            store: '{taskTimeline}'
        },
        options: {
            editable: false,
            align: 'left',
            selectable: false,
            showCurrentTime: false,
            zoomable: false
        }
    }],
    listeners: {
        afterrender: 'onAfterrender',
        beforeclose: 'onBeforeclose'
    }
});