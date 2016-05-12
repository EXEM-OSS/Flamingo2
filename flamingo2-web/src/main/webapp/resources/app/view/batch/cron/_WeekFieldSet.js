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
 * Day of Week Cron Expression Builder FieldSet
 *
 * @class Job.view._WeekFieldSet
 * @extends Ext.form.FieldSet
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.batch.cron._WeekFieldSet', {
    extend: 'Ext.form.FieldSet',
    alias: 'widget._weekFieldSet',

    collapsible: false,
    border: false,
    items: [
        {
            xtype: 'fieldcontainer',
            layout: 'hbox',
            items: [
                {
                    itemId: 'WEEK_TYPE',
                    name: 'week_type',
                    xtype: 'combo',
                    fieldLabel: message.msg('cron.week'),
                    labelAlign: 'right',
                    labelWidth: 50,
                    store: Ext.create('Ext.data.Store', {
                        fields: ['value', 'name', 'desc'],
                        data: [
                            {value: 'NO_SPECIFIC', name: message.msg('cron.type_date_no_specific'), desc: ''},
                            {value: 'ALL', name: message.msg('cron.type_week_all'), desc: ''},
                            {value: 'LAST', name: message.msg('cron.type_week_sat'), desc: ''},
                            {value: 'RANGE', name: message.msg('cron.type_date_range'), desc: ''},
                            {value: 'INCREMENT', name: message.msg('cron.type_date_increment'), desc: ''},
                            {value: 'NTH', name: message.msg('cron.type_week_custom'), desc: ''},
                            {value: 'LAST_WEEK', name: message.msg('cron.type_week_last'), desc: ''},
                            {value: 'SPECIFIC', name: message.msg('cron.type_date_specific'), desc: ''}
                        ]
                    }),
                    tpl: '<tpl for="."><div class="x-boundlist-item" data-qtip="{desc}">{name}</div></tpl>',
                    queryMode: 'local',
                    displayField: 'name',
                    valueField: 'value',
                    value: 'NO_SPECIFIC',
                    width: 200,
                    padding: '0 5 0 0',
                    editable: false,
                    listeners: {
                        change: function (combo, newValue, oldValue, eOpts) {
                            var cronTrigger = combo.up('cronTrigger'),
                                container = combo.up('fieldcontainer'),
                                range = container.getComponent('WEEK_RANGE'),
                                increment = container.getComponent('WEEK_INCREMENT'),
                                specific = container.getComponent('WEEK_SPECIFIC'),
                                nth = container.getComponent('WEEK_NTH'),
                                lastWeek = container.getComponent('WEEK_LAST_WEEK');

                            // hide all
                            range.setDisabled(true);
                            range.hide();
                            increment.setDisabled(true);
                            increment.hide();
                            specific.setDisabled(true);
                            specific.hide();
                            nth.setDisabled(true);
                            nth.hide();
                            lastWeek.setDisabled(true);
                            lastWeek.hide();

                            if (newValue != 'NO_SPECIFIC') {
                                var dayTypeCombo = Ext.ComponentQuery.query('#DAY_TYPE')[0];
                                dayTypeCombo.setValue('NO_SPECIFIC');
                            }

                            // show by type
                            switch (newValue) {
                                case 'RANGE' :
                                    range.setDisabled(false);
                                    range.show();
                                    break;
                                case 'INCREMENT' :
                                    increment.setDisabled(false);
                                    increment.show();
                                    break;
                                case 'SPECIFIC' :
                                    specific.setDisabled(false);
                                    specific.show();
                                    break;
                                case 'NTH' :
                                    nth.setDisabled(false);
                                    nth.show();
                                    break;
                                case 'LAST_WEEK' :
                                    lastWeek.setDisabled(false);
                                    lastWeek.show();
                                    break;
                                default :
                                    break;
                            }

                            // Cron Expression 적용
                            if (cronTrigger) cronTrigger.toCronExpression();
                        }
                    }
                },
                {
                    itemId: 'WEEK_RANGE',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'week_from',
                            xtype: 'combo',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['value', 'name'],
                                data: [
                                    {value: 'SUN', name: message.msg('cron.type_week_sun')},
                                    {value: 'MON', name: message.msg('cron.type_week_mon')},
                                    {value: 'TUE', name: message.msg('cron.type_week_tue')},
                                    {value: 'WED', name: message.msg('cron.type_week_wed')},
                                    {value: 'THU', name: message.msg('cron.type_week_thu')},
                                    {value: 'FRI', name: message.msg('cron.type_week_fri')},
                                    {value: 'SAT', name: message.msg('cron.type_week_sat')}
                                ]
                            }),
                            queryMode: 'local',
                            displayField: 'name',
                            valueField: 'value',
                            width: 80,
                            editable: false,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_week_range_start'),
                            listeners: {
                                change: function (field, newValue, oldValue, eOpts) {
                                    var cronTrigger = field.up('cronTrigger');
                                    if (cronTrigger) cronTrigger.toCronExpression();
                                }
                            }
                        },
                        {
                            xtype: 'displayfield',
                            padding: '0 5 0 5',
                            value: '-'
                        },
                        {
                            name: 'week_to',
                            xtype: 'combo',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['value', 'name'],
                                data: [
                                    {value: 'SUN', name: message.msg('cron.type_week_sun')},
                                    {value: 'MON', name: message.msg('cron.type_week_mon')},
                                    {value: 'TUE', name: message.msg('cron.type_week_tue')},
                                    {value: 'WED', name: message.msg('cron.type_week_wed')},
                                    {value: 'THU', name: message.msg('cron.type_week_thu')},
                                    {value: 'FRI', name: message.msg('cron.type_week_fri')},
                                    {value: 'SAT', name: message.msg('cron.type_week_sat')}
                                ]
                            }),
                            queryMode: 'local',
                            displayField: 'name',
                            valueField: 'value',
                            width: 80,
                            editable: false,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_week_range_end'),
                            listeners: {
                                change: function (field, newValue, oldValue, eOpts) {
                                    var cronTrigger = field.up('cronTrigger');
                                    if (cronTrigger) cronTrigger.toCronExpression();
                                }
                            }
                        }
                    ]
                },
                {
                    itemId: 'WEEK_INCREMENT',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'week_start',
                            xtype: 'combo',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['value', 'name'],
                                data: [
                                    {value: 'SUN', name: message.msg('cron.type_week_sun')},
                                    {value: 'MON', name: message.msg('cron.type_week_mon')},
                                    {value: 'TUE', name: message.msg('cron.type_week_tue')},
                                    {value: 'WED', name: message.msg('cron.type_week_wed')},
                                    {value: 'THU', name: message.msg('cron.type_week_thu')},
                                    {value: 'FRI', name: message.msg('cron.type_week_fri')},
                                    {value: 'SAT', name: message.msg('cron.type_week_sat')}
                                ]
                            }),
                            queryMode: 'local',
                            displayField: 'name',
                            valueField: 'value',
                            width: 80,
                            editable: false,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_week_range_start'),
                            listeners: {
                                change: function (field, newValue, oldValue, eOpts) {
                                    var cronTrigger = field.up('cronTrigger');
                                    if (cronTrigger) cronTrigger.toCronExpression();
                                }
                            }
                        },
                        {
                            xtype: 'displayfield',
                            padding: '0 5 0 5',
                            value: '/'
                        },
                        {
                            name: 'week_increment',
                            xtype: 'numberfield',
                            vtype: 'alphanum',
                            minValue: 1,
                            width: 80,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_date_increment'),
                            listeners: {
                                change: function (field, newValue, oldValue, eOpts) {
                                    var cronTrigger = field.up('cronTrigger');
                                    if (cronTrigger) cronTrigger.toCronExpression();
                                }
                            }
                        }
                    ]
                },
                {
                    itemId: 'WEEK_NTH',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'week_nth',
                            xtype: 'combo',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['value', 'name'],
                                data: [
                                    {value: 'SUN', name: message.msg('cron.type_week_sun')},
                                    {value: 'MON', name: message.msg('cron.type_week_mon')},
                                    {value: 'TUE', name: message.msg('cron.type_week_tue')},
                                    {value: 'WED', name: message.msg('cron.type_week_wed')},
                                    {value: 'THU', name: message.msg('cron.type_week_thu')},
                                    {value: 'FRI', name: message.msg('cron.type_week_fri')},
                                    {value: 'SAT', name: message.msg('cron.type_week_sat')}
                                ]
                            }),
                            queryMode: 'local',
                            displayField: 'name',
                            valueField: 'value',
                            width: 80,
                            editable: false,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_week_of_the_day'),
                            listeners: {
                                change: function (field, newValue, oldValue, eOpts) {
                                    var cronTrigger = field.up('cronTrigger');
                                    if (cronTrigger) cronTrigger.toCronExpression();
                                }
                            }
                        },
                        {
                            xtype: 'displayfield',
                            padding: '0 5 0 5',
                            value: '#'
                        },
                        {
                            name: 'week_nth_num',
                            xtype: 'combo',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['value', 'name'],
                                data: [
                                    {value: '1', name: message.msg('cron.type_week_first')},
                                    {value: '2', name: message.msg('cron.type_week_second')},
                                    {value: '3', name: message.msg('cron.type_week_third')},
                                    {value: '4', name: message.msg('cron.type_week_fourth')},
                                    {value: '5', name: message.msg('cron.type_week_fifth')}
                                ]
                            }),
                            queryMode: 'local',
                            displayField: 'name',
                            valueField: 'value',
                            width: 80,
                            editable: false,
                            emptyText: message.msg('cron.empty_txt_week_custom'),
                            listeners: {
                                change: function (field, newValue, oldValue, eOpts) {
                                    var cronTrigger = field.up('cronTrigger');
                                    if (cronTrigger) cronTrigger.toCronExpression();
                                }
                            }
                        }
                    ]
                },

                {
                    itemId: 'WEEK_LAST_WEEK',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'week_last',
                            xtype: 'combo',
                            store: Ext.create('Ext.data.Store', {
                                fields: ['value', 'name'],
                                data: [
                                    {value: 'SUN', name: message.msg('cron.type_week_sun')},
                                    {value: 'MON', name: message.msg('cron.type_week_mon')},
                                    {value: 'TUE', name: message.msg('cron.type_week_tue')},
                                    {value: 'WED', name: message.msg('cron.type_week_wed')},
                                    {value: 'THU', name: message.msg('cron.type_week_thu')},
                                    {value: 'FRI', name: message.msg('cron.type_week_fri')},
                                    {value: 'SAT', name: message.msg('cron.type_week_sat')}
                                ]
                            }),
                            queryMode: 'local',
                            displayField: 'name',
                            valueField: 'value',
                            width: 80,
                            editable: false,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_week_specify'),
                            listeners: {
                                change: function (field, newValue, oldValue, eOpts) {
                                    var cronTrigger = field.up('cronTrigger');
                                    if (cronTrigger) cronTrigger.toCronExpression();
                                }
                            }
                        },
                        {
                            xtype: 'displayfield',
                            padding: '0 5 0 5',
                            value: message.msg('cron.value_week_specified')
                        }
                    ]
                },
                {
                    itemId: 'WEEK_SPECIFIC',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'week_value',
                            xtype: 'textfield',
                            vtype: 'cronWeekComma',
                            width: 175,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_week_specify_comma'),
                            listeners: {
                                change: function (field, newValue, oldValue, eOpts) {
                                    var cronTrigger = field.up('cronTrigger');
                                    if (cronTrigger) cronTrigger.toCronExpression();
                                }
                            }
                        }
                    ]
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
        var fieldContainer = this.down('fieldcontainer'),
            typeCombo = fieldContainer.getComponent('WEEK_TYPE'),
            rangeContainer = fieldContainer.getComponent('WEEK_RANGE'),
            incrementContainer = fieldContainer.getComponent('WEEK_INCREMENT'),
            specificContainer = fieldContainer.getComponent('WEEK_SPECIFIC'),
            nthContainer = fieldContainer.getComponent('WEEK_NTH'),
            lastWeekContainer = fieldContainer.getComponent('WEEK_LAST_WEEK'),
            value1 = '_', value2 = '_',
            convertWeek = function (value) {
                var week = {
                    SUN: 1,
                    MON: 2,
                    TUE: 3,
                    WED: 4,
                    THU: 5,
                    FRI: 6,
                    SAT: 7
                };

                return week[value];
            };

        switch (typeCombo.getValue()) {
            case 'NO_SPECIFIC':
                return '?';
            case 'ALL':
                return '*';
            case 'LAST':
                return 'L';
            case 'RANGE':
                value1 = rangeContainer.down('combo[name=week_from]').getValue();
                value2 = rangeContainer.down('combo[name=week_to]').getValue();
                return (value1 = Ext.isEmpty(value1) ? '_' : value1) + '-' + (value2 = Ext.isEmpty(value2) ? '_' : value2);
            case 'INCREMENT':
                value1 = incrementContainer.down('combo[name=week_start]').getValue();
                value2 = incrementContainer.down('numberfield[name=week_increment]').getValue();
                return (value1 = Ext.isEmpty(value1) ? '_' : value1) + '/' + (value2 = Ext.isEmpty(value2) ? '_' : value2);
            case 'NTH':
                value1 = nthContainer.down('combo[name=week_nth]').getValue();
                value2 = nthContainer.down('combo[name=week_nth_num]').getValue();
                return (value1 = Ext.isEmpty(value1) ? '_' : value1) + '#' + (value2 = Ext.isEmpty(value2) ? '_' : value2);
            case 'LAST_WEEK':
                value1 = lastWeekContainer.down('combo[name=week_last]').getValue();
                return Ext.isEmpty(value1) ? '_L' : convertWeek(value1) + 'L';
            case 'SPECIFIC':
                value1 = specificContainer.down('textfield[name=week_value]').getValue();
                return Ext.isEmpty(value1) ? '_' : value1;
        }

        return '_';
    },

    /**
     * 주어진 파싱된 Cron Object 을 form 에 설정한다.
     *
     * @param {Object} cronObject 파싱된 Object
     */
    setValue: function (cronObject) {
        var fieldContainer = this.down('fieldcontainer'),
            typeCombo = fieldContainer.getComponent('WEEK_TYPE'),
            rangeContainer = fieldContainer.getComponent('WEEK_RANGE'),
            incrementContainer = fieldContainer.getComponent('WEEK_INCREMENT'),
            specificContainer = fieldContainer.getComponent('WEEK_SPECIFIC'),
            nthContainer = fieldContainer.getComponent('WEEK_NTH'),
            lastWeekContainer = fieldContainer.getComponent('WEEK_LAST_WEEK');

        switch (cronObject.type) {
            case 'NO_SPECIFIC':
                typeCombo.setValue('NO_SPECIFIC');
                break;
            case 'ALL':
                typeCombo.setValue('ALL');
                break;
            case 'LAST':
                typeCombo.setValue('LAST');
                break;
            case 'RANGE':
                typeCombo.setValue('RANGE');
                rangeContainer.down('combo[name=week_from]').setValue(cronObject.value[0]);
                rangeContainer.down('combo[name=week_to]').setValue(cronObject.value[1]);
                break;
            case 'INCREMENT':
                typeCombo.setValue('INCREMENT');
                incrementContainer.down('combo[name=week_start]').setValue(cronObject.value[0]);
                incrementContainer.down('numberfield[name=week_increment]').setValue(cronObject.value[1]);
                break;
            case 'NTH':
                typeCombo.setValue('NTH');
                nthContainer.down('combo[name=week_nth]').setValue(cronObject.value[0]);
                nthContainer.down('combo[name=week_nth_num]').setValue(cronObject.value[1]);
                break;
            case 'LAST_WEEK':
                typeCombo.setValue('LAST_WEEK');
                lastWeekContainer.down('combo[name=week_last]').setValue(cronObject.value[0]);
                break;
            case 'SPECIFIC':
                typeCombo.setValue('SPECIFIC');
                specificContainer.down('textfield[name=week_value]').setValue(cronObject.orgValue);
                break;
        }
    }
});

/**
 * cron vtype
 */
Ext.apply(Ext.form.field.VTypes, {
    cronWeekComma: function (v) {
        var weekExp = /^([1-7]|(SUN|MON|TUE|WED|THU|FRI|SAT)|([1-7],[1-7](,[1-7])*)|((SUN|MON|TUE|WED|THU|FRI|SAT),(SUN|MON|TUE|WED|THU|FRI|SAT)(,(SUN|MON|TUE|WED|THU|FRI|SAT))*))$/;
        return weekExp.test(v);
    },
    cronWeekCommaText: message.msg('batch.msg.quartz_cron_express')
});