/**
 * Created by Park on 15. 8. 26..
 */
Ext.define('Flamingo2.view.monitoring.spark.TaskTimelineController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.tasktimeline',

    onAfterrender: function(view) {
        var me = this;
        var refs = me.getReferences();

        view.setTitle('Tasks Timeline / ApplicationID: ' + view.appid);

        Ext.defer(function() {
            refs.timlineView.getStore().load({
                params: {
                    clusterName: ENGINE.id,
                    appid: view.appid,
                    jobid: view.jobid,
                    stageid: view.stageid,
                    attemptid: view.attemptid
                }
            });
        }, 300);
    },

    onBeforeclose: function() {
        $('.ui-helper-hidden-accessible').remove();
    }

});