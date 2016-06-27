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
/**
 * Created by Park on 15. 8. 7..
 */
Ext.define('Flamingo2.view.monitoring.spark.SparkController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.sparkController',

    onJobSumChartAfterRender: function(view) {
        var me = this;
        Ext.defer(function() {
            me.jobChartLoad();
            me.appGridLoad();
        }, 300);
    },

    jobChartLoad: function() {
        var refs = this.getReferences();

        refs.jobChart.setLoading(true);
        refs.jobChart.getStore().load({
            callback: function() {
                refs.jobChart.setLoading(false);
            }
        });
    },

    appGridLoad: function() {
        var refs = this.getReferences();

        refs.appGrid.setLoading(true);
        refs.appGrid.getStore().load({
            callback: function() {
                refs.appGrid.setLoading(false);
            }
        });

    },

    onAppGridSelect: function(grid, record, index) {
        var me = this;
        var refs = me.getReferences();
        var activeTab = refs.detailTab.getActiveTab();

        activeTab.fireEvent('dataLoad', activeTab, record);

        refs.btnTimeline.setDisabled(false);

        refs.btnStagesDetail.setDisabled(true);
        refs.btnStagesTimeline.setDisabled(true);
    },

    onGridDataLoad: function(view, record) {
        var me = this;
        var refs = me.getReferences();
        var appid = record.get('appid');

        refs.btnJobsTimeline.setDisabled(true);
        refs.btnStagesDetail.setDisabled(true);
        refs.btnStagesTimeline.setDisabled(true);

        view.setLoading(true);
        view.getStore().load({
            params: {
                appid: appid,
                clusterName: ENGINE.id
            },
            callback: function(records, operation, success) {
                view.setLoading(false);

                if (!success) {
                    try {
                        Ext.Msg.alert(message.msg('common.error'), view.getStore().getProxy().reader.rawData.error.cause);
                    } catch(err) {
                        Ext.Msg.alert(message.msg('common.error'), '오류가 발생하였습니다.');
                    }

                }
            }
        });
    },

    onDetailTabchange: function(tab, newCard, oldCard, eOpts ) {
        var me = this;
        var refs = me.getReferences();
        var selection = refs.appGrid.getSelectionModel().getSelection();

        if (selection.length > 0) {
            newCard.fireEvent('dataLoad', newCard, selection[0]);
        }
    },

    onAppEventTimelineClick: function(button) {
        var me = this;
        var refs = me.getReferences();
        var selection = refs.appGrid.getSelectionModel().getSelection();

        if (selection.length == 0) {
            Ext.Msg.alert(message.msg('common.warn'), 'Spark 어플리케이션을 선택하시오.');
            return;
        }

        var record = selection[0];

        Ext.create('Flamingo2.view.monitoring.spark.AppTimeline', {
            animateTarget: button,
            width: $(window).width() * 0.85,
            height: $(window).height() * 0.85,
            appid: record.get('appid')
        }).show();
    },

    onJobEventTimelineClick: function(button) {
        var me = this;
        var refs = me.getReferences();
        var selection = refs.jobGrid.getSelectionModel().getSelection();

        if (selection.length == 0) {
            Ext.Msg.alert(message.msg('common.warn'), 'Spark Job을 선택하시오.');
            return;
        }

        var record = selection[0];

        Ext.create('Flamingo2.view.monitoring.spark.JobTimeline', {
            animateTarget: button,
            width: $(window).width() * 0.85,
            height: $(window).height() * 0.85,
            appid: record.get('appid'),
            jobid: record.get('jobid')
        }).show();
    },

    onJobGridSelect: function(grid, record, index) {
        var me = this;
        var refs = me.getReferences();

        if (record.get('status') == 'JobSucceeded') {
            refs.btnJobsTimeline.setDisabled(false);
        } else {
            refs.btnJobsTimeline.setDisabled(true);
        }
    },

    onStageGridSelect: function(grid, record, index) {
        var me = this;
        var refs = me.getReferences();

        if (record.get('status') == 'Succeeded') {
            refs.btnStagesDetail.setDisabled(false);
            refs.btnStagesTimeline.setDisabled(false);
        } else {
            refs.btnStagesDetail.setDisabled(true);
            refs.btnStagesTimeline.setDisabled(true);
        }
    },

    onStageDetailClick: function(button) {
        var me = this;
        var refs = me.getReferences();
        var selection = refs.stageGrid.getSelectionModel().getSelection();

        if (selection.length == 0) {
            Ext.Msg.alert(message.msg('common.warn'), 'Spark Stage를 선택하시오.');
            return;
        }

        var record = selection[0];

        Ext.create('Flamingo2.view.monitoring.spark.StageDetail', {
            animateTarget: button,
            width: $(window).width() * 0.85,
            height: $(window).height() * 0.85,
            appid: record.get('appid'),
            jobid: record.get('jobid'),
            stageid: record.get('stageid'),
            attemptid: record.get('attemptid')
        }).show();
    },

    onStageTimelineClick: function(button) {
        var me = this;
        var refs = me.getReferences();
        var selection = refs.stageGrid.getSelectionModel().getSelection();

        if (selection.length == 0) {
            Ext.Msg.alert(message.msg('common.warn'), 'Spark Stage를 선택하시오.');
            return;
        }

        var record = selection[0];

        Ext.create('Flamingo2.view.monitoring.spark.TaskTimeline', {
            animateTarget: button,
            width: $(window).width() * 0.85,
            height: $(window).height() * 0.85,
            appid: record.get('appid'),
            jobid: record.get('jobid'),
            stageid: record.get('stageid'),
            attemptid: record.get('attemptid')
        }).show();
    }
});