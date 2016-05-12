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
 * Day of Month Cron Expression Builder FieldSet
 *
 * @class Job.view._DayFieldSet
 * @extends Ext.form.FieldSet
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.batch.cron._DayFieldSet', {
    extend: 'Ext.form.FieldSet',
    alias: 'widget._dayFieldSet',

    collapsible: false,
    border: false,
    items: [
        {
            xtype: 'fieldcontainer',
            layout: 'hbox',
            items: [
                {
                    itemId: 'DAY_TYPE',
                    name: 'day_type',
                    xtype: 'combo',
                    fieldLabel: message.msg('cron.day'),
                    labelAlign: 'right',
                    labelWidth: 50,
                    store: Ext.create('Ext.data.Store', {
                        fields: ['value', 'name', 'desc'],
                        data: [
                            {value: 'NO_SPECIFIC', name: message.msg('cron.type_date_no_specific'), desc: ''},
                            {value: 'ALL', name: message.msg('cron.type_day_all'), desc: ''},
                            {value: 'LAST', name: message.msg('cron.type_day_last'), desc: ''},
                            {value: 'LAST_WEEK', name: message.msg('cron.type_day_last_week'), desc: ''},
                            {value: 'RANGE', name: message.msg('cron.type_date_range'), desc: ''},
                            {value: 'INCREMENT', name: message.msg('cron.type_date_increment'), desc: ''},
                            {value: 'NEAREST_WEEK', name: message.msg('cron.type_day_nearest_week'), desc: ''},
                            {value: 'SPECIFIC', name: message.msg('cron.type_date_specific'), desc: ''}
                        ]
                    }),
                    tpl: '<tpl for="."><div class="x-boundlist-item" data-qtip="{desc}">{name}</div></tpl>',
                    queryMode: 'local',
                    displayField: 'name',
                    valueField: 'value',
                    value: 'ALL',
                    width: 200,
                    padding: '0 5 0 0',
                    editable: false,
                    listeners: {
                        change: function (combo, newValue, oldValue, eOpts) {
                            var cronTrigger = combo.up('cronTrigger'),
                                container = combo.up('fieldcontainer'),
                                range = container.getComponent('DAY_RANGE'),
                                increment = container.getComponent('DAY_INCREMENT'),
                                specific = container.getComponent('DAY_SPECIFIC'),
                                nearest = container.getComponent('DAY_NEAREST_WEEK');

                            // hide all
                            range.setDisabled(true);
                            range.hide();
                            increment.setDisabled(true);
                            increment.hide();
                            specific.setDisabled(true);
                            specific.hide();
                            nearest.setDisabled(true);
                            nearest.hide();

                            if (newValue != 'NO_SPECIFIC') {
                                var weekTypeCombo = Ext.ComponentQuery.query('#WEEK_TYPE')[0];
                                weekTypeCombo.setValue('NO_SPECIFIC');
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
                                case 'NEAREST_WEEK' :
                                    nearest.setDisabled(false);
                                    nearest.show();
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
                    itemId: 'DAY_RANGE',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'day_from',
                            xtype: 'numberfield',
                            vtype: 'alphanum',
                            minValue: 1,
                            maxValue: 31,
                            width: 110,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_date_range_start'),
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
                            name: 'day_to',
                            xtype: 'numberfield',
                            vtype: 'alphanum',
                            minValue: 1,
                            maxValue: 31,
                            width: 110,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_date_range_end'),
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
                    itemId: 'DAY_INCREMENT',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'day_start',
                            xtype: 'numberfield',
                            vtype: 'alphanum',
                            minValue: 1,
                            maxValue: 31,
                            width: 110,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_date_range_start'),
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
                            name: 'day_increment',
                            xtype: 'numberfield',
                            vtype: 'alphanum',
                            minValue: 1,
                            width: 110,
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
                    itemId: 'DAY_NEAREST_WEEK',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'day_nearest',
                            xtype: 'numberfield',
                            vtype: 'alphanum',
                            minValue: 1,
                            maxValue: 31,
                            width: 110,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_date_specify'),
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
                            value: message.msg('cron.value_nearest_day')
                        }
                    ]
                },
                {
                    itemId: 'DAY_SPECIFIC',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'day_value',
                            xtype: 'textfield',
                            vtype: 'cronDayComma',
                            width: 175,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_date_specify_comma'),
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
            typeCombo = fieldContainer.getComponent('DAY_TYPE'),
            rangeContainer = fieldContainer.getComponent('DAY_RANGE'),
            incrementContainer = fieldContainer.getComponent('DAY_INCREMENT'),
            specificContainer = fieldContainer.getComponent('DAY_SPECIFIC'),
            nearestContainer = fieldContainer.getComponent('DAY_NEAREST_WEEK'),
            value1 = '_', value2 = '_';

        switch (typeCombo.getValue()) {
            case 'NO_SPECIFIC':
                return '?';
            case 'ALL':
                return '*';
            case 'LAST':
                return 'L';
            case 'LAST_WEEK':
                return 'LW';
            case 'RANGE':
                value1 = rangeContainer.down('numberfield[name=day_from]').getValue();
                value2 = rangeContainer.down('numberfield[name=day_to]').getValue();
                return (value1 = Ext.isEmpty(value1) ? '_' : value1) + '-' + (value2 = Ext.isEmpty(value2) ? '_' : value2);
            case 'INCREMENT':
                value1 = incrementContainer.down('numberfield[name=day_start]').getValue();
                value2 = incrementContainer.down('numberfield[name=day_increment]').getValue();
                return (value1 = Ext.isEmpty(value1) ? '_' : value1) + '/' + (value2 = Ext.isEmpty(value2) ? '_' : value2);
            case 'NEAREST_WEEK':
                value1 = nearestContainer.down('numberfield[name=day_nearest]').getValue();
                return Ext.isEmpty(value1) ? '_W' : value1 + 'W';
            case 'SPECIFIC':
                value1 = specificContainer.down('textfield[name=day_value]').getValue();
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
            typeCombo = fieldContainer.getComponent('DAY_TYPE'),
            rangeContainer = fieldContainer.getComponent('DAY_RANGE'),
            incrementContainer = fieldContainer.getComponent('DAY_INCREMENT'),
            specificContainer = fieldContainer.getComponent('DAY_SPECIFIC'),
            nearestContainer = fieldContainer.getComponent('DAY_NEAREST_WEEK');

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
            case 'LAST_WEEK':
                typeCombo.setValue('LAST_WEEK');
                break;
            case 'RANGE':
                typeCombo.setValue('RANGE');
                rangeContainer.down('numberfield[name=day_from]').setValue(cronObject.value[0]);
                rangeContainer.down('numberfield[name=day_to]').setValue(cronObject.value[1]);
                break;
            case 'INCREMENT':
                typeCombo.setValue('INCREMENT');
                incrementContainer.down('numberfield[name=day_start]').setValue(cronObject.value[0]);
                incrementContainer.down('numberfield[name=day_increment]').setValue(cronObject.value[1]);
                break;
            case 'NEAREST_WEEK':
                typeCombo.setValue('NEAREST_WEEK');
                nearestContainer.down('numberfield[name=day_nearest]').setValue(cronObject.value[0]);
                break;
            case 'SPECIFIC':
                typeCombo.setValue('SPECIFIC');
                specificContainer.down('textfield[name=day_value]').setValue(cronObject.orgValue);
                break;
        }
    }
});

/**
 * cron vtype
 */
Ext.apply(Ext.form.field.VTypes, {
    cronDayComma: function (v) {
        var dayExp = /^(([1-9]|1[0-9]|2[0-9]|3[0-1])|((([1-9]|1[0-9]|2[0-9]|3[0-1]),)([1-9]|1[0-9]|2[0-9]|3[0-1])(,([1-9]|1[0-9]|2[0-9]|3[0-1]))*))$/;
        return dayExp.test(v);
    },
    cronDayCommaText: 'Invalid Cron Expression'
});