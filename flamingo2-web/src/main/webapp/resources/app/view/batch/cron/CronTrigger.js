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

/**
 * Cron Expression Builder Panel
 *
 * @example
 * .
 * .
 * items : [
 *     {
 *         xtype : 'cronTrigger',
 *         border: false,
 *         value : '0 * * * * ? *'
 *     }
 * ]
 * .
 * .
 *
 * or
 *
 * var cronTrigger = Ext.create('Job.view.CronTriggerPanel', {
 *     border : false,
 *     value : '0 * * * * ? *'
 * });
 *
 * cronTrigger.setValue('0 15 10 ? * 6L 2002-2005');
 *
 * console.log(cronTrigger.getValue());
 *
 * @class Flamingo.view.job.CronTriggerPanel
 * @extends Ext.form.Panel
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.batch.cron.CronTrigger', {
    extend: 'Ext.form.Panel',
    alias: 'widget.cronTrigger',

    requires: [
        'Flamingo2.view.batch.cron._YearFieldSet',
        'Flamingo2.view.batch.cron._WeekFieldSet',
        'Flamingo2.view.batch.cron._MonthFieldSet',
        'Flamingo2.view.batch.cron._DayFieldSet',
        'Flamingo2.view.batch.cron._HourFieldSet',
        'Flamingo2.view.batch.cron._MinFieldSet'
    ],

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    //bodyPadding: '15 10 10 10',
    bodyPadding: '5 0 0 0',

    /**
     * 추가된 파라미터 : Cron Expression 값
     */
    //value: '',

    items: [
        {
            itemId: 'FORM_CRON_CONFIG',
            xtype: 'form',
            border: false,
            items: [
                {
                    xtype: 'fieldset',
                    collapsible: false,
                    border: false,
                    items: {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'textfield',
                                fieldLabel: 'Cron',
                                reference: 'triggerCronExpression',
                                name: 'cron_exp',
                                labelWidth: 50,
                                labelAlign: 'right',
                                vtype: 'cron',
                                allowBlank: false,
                                value: '0 * * * * ? *',
                                listeners: {
                                    change: function (textfield, newValue, oldValue, eOpts) {
                                        if (textfield.isValid()) {
                                            query('cronTrigger').setValue(newValue)
                                        }
                                    }
                                }
                            }
                        ]
                    }
                },
                {
                    xtype: '_minFieldSet'
                },
                {
                    xtype: '_hourFieldSet'
                },
                {
                    xtype: '_dayFieldSet'
                },
                {
                    xtype: '_monthFieldSet'
                },
                {
                    xtype: '_weekFieldSet'
                },
                {
                    xtype: '_yearFieldSet'
                }
            ]
        }
    ],

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
    }
});

/**
 * cron vtype
 */
Ext.apply(Ext.form.field.VTypes, {
    cron: function (v) {
        return Ext.isObject(App.Util.CronParser.parse(v));
    },
    cronText: message.msg('batch.msg.quartz_cron_express')
});