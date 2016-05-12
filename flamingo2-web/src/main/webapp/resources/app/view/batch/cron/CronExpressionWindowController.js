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
Ext.define('Flamingo2.view.batch.cron.CronExpressionWindowController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.cronExpressionWindowController',

    onAfterRender: function () {
        var me = this;
        var refs = me.getReferences();

        //refs.cronExpression.setValue('0 * * * * ? *');
    },

    /**
     * 설정된 Cron Expression 값을 반환한다.
     *
     * @return {String} cron expression
     */
    getValue: function () {
        var cronExp = this.down('textfield[name=cron_exp]');
        return cronExp.getValue();
    },

    /**
     * 주어진 Cron Expression 을 form 에 설정한다.
     *
     * @param {String} cronExpression
     */
    setValue: function (cronExpression) {
        var cronExp = this.down('textfield[name=cron_exp]'),
            cronConfig = this.getComponent('FORM_CRON_CONFIG'),
            minFieldSet = cronConfig.down('_minFieldSet'),
            hourFieldSet = cronConfig.down('_hourFieldSet'),
            dayFieldSet = cronConfig.down('_dayFieldSet'),
            monthFieldSet = cronConfig.down('_monthFieldSet'),
            weekFieldSet = cronConfig.down('_weekFieldSet'),
            yearFieldSet = cronConfig.down('_yearFieldSet'),
            cronObj = App.Util.CronParser.parse(cronExpression);

        cronExp.setValue(cronExpression);

        if (Ext.isObject(cronObj)) {
            minFieldSet.setValue(cronObj.min);
            hourFieldSet.setValue(cronObj.hour);
            dayFieldSet.setValue(cronObj.day);
            monthFieldSet.setValue(cronObj.month);
            weekFieldSet.setValue(cronObj.week);
            yearFieldSet.setValue(cronObj.year);
        }
    },

    /**
     * UI 설정을 Cron Expression 값으로 변환한다.
     */
    toCronExpression: function () {
        var cronExp = this.down('textfield[name=cron_exp]'),
            cronConfig = this.getComponent('FORM_CRON_CONFIG'),
            minFieldSet = cronConfig.down('_minFieldSet'),
            hourFieldSet = cronConfig.down('_hourFieldSet'),
            dayFieldSet = cronConfig.down('_dayFieldSet'),
            monthFieldSet = cronConfig.down('_monthFieldSet'),
            weekFieldSet = cronConfig.down('_weekFieldSet'),
            yearFieldSet = cronConfig.down('_yearFieldSet'),
            cronExpression;

        cronExpression = '0 ' + minFieldSet.getValue() + ' ' + hourFieldSet.getValue() + ' ' + dayFieldSet.getValue() +
            ' ' + monthFieldSet.getValue() + ' ' + weekFieldSet.getValue() + ' ' + yearFieldSet.getValue();

        cronExp.setValue(cronExpression);
    },

    /**
     * CronExpression창에서 설정한 정보를 배치 작업 등록창의 Cron Expression 필드에 Overriding.
     */
    onCronClickOK: function () {
        var me = this;
        var refs = me.getReferences();
        var cronExpr = query('jobRegisterForm #cronExpression');

        cronExpr.setValue(refs.triggerCronExpression.getValue());
        me.getView().close();
    },

    onCronClickCancel: function () {
        this.getView().close();
    }
});