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
 * Minute Cron Expression Builder FieldSet
 *
 * @class Job.view._MinFieldSet
 * @extends Ext.form.FieldSet
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.batch.cron._MinFieldSet', {
    extend: 'Ext.form.FieldSet',
    alias: 'widget._minFieldSet',

    collapsible: false,
    border: false,
    items: [
        {
            xtype: 'fieldcontainer',
            layout: 'hbox',
            items: [
                {
                    itemId: 'MIN_TYPE',
                    name: 'min_type',
                    xtype: 'combo',
                    fieldLabel: message.msg('cron.minute'),
                    labelAlign: 'right',
                    labelWidth: 50,
                    store: Ext.create('Ext.data.Store', {
                        fields: ['value', 'name', 'desc'],
                        data: [
                            {value: 'ALL', name: message.msg('cron.type_minute_all'), desc: ''},
                            {value: 'RANGE', name: message.msg('cron.type_date_range'), desc: ''},
                            {value: 'INCREMENT', name: message.msg('cron.type_date_increment'), desc: ''},
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
                                range = container.getComponent('MIN_RANGE'),
                                increment = container.getComponent('MIN_INCREMENT'),
                                specific = container.getComponent('MIN_SPECIFIC');

                            // hide all
                            range.setDisabled(true);
                            range.hide();
                            increment.setDisabled(true);
                            increment.hide();
                            specific.setDisabled(true);
                            specific.hide();

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
                                default :
                                    break;
                            }

                            // Cron Expression 적용
                            if (cronTrigger) cronTrigger.toCronExpression();
                        }
                    }
                },
                {
                    itemId: 'MIN_RANGE',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'min_from',
                            xtype: 'numberfield',
                            vtype: 'alphanum',
                            minValue: 0,
                            maxValue: 59,
                            width: 110,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_time_range_start'),
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
                            name: 'min_to',
                            xtype: 'numberfield',
                            vtype: 'alphanum',
                            minValue: 0,
                            maxValue: 59,
                            width: 110,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_time_range_end'),
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
                    itemId: 'MIN_INCREMENT',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'min_start',
                            xtype: 'numberfield',
                            vtype: 'alphanum',
                            minValue: 0,
                            maxValue: 59,
                            width: 110,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_time_range_start'),
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
                            name: 'min_increment',
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
                    itemId: 'MIN_SPECIFIC',
                    xtype: 'fieldcontainer',
                    layout: 'hbox',
                    disabled: true,
                    hidden: true,
                    items: [
                        {
                            name: 'min_value',
                            xtype: 'textfield',
                            vtype: 'cronMinComma',
                            width: 175,
                            allowBlank: false,
                            emptyText: message.msg('cron.empty_txt_minute_specify_comma'),
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
            typeCombo = fieldContainer.getComponent('MIN_TYPE'),
            rangeContainer = fieldContainer.getComponent('MIN_RANGE'),
            incrementContainer = fieldContainer.getComponent('MIN_INCREMENT'),
            specificContainer = fieldContainer.getComponent('MIN_SPECIFIC'),
            value1 = '_', value2 = '_';

        switch (typeCombo.getValue()) {
            case 'ALL':
                return '*';
            case 'RANGE':
                value1 = rangeContainer.down('numberfield[name=min_from]').getValue();
                value2 = rangeContainer.down('numberfield[name=min_to]').getValue();
                return (value1 = Ext.isEmpty(value1) ? '_' : value1) + '-' + (value2 = Ext.isEmpty(value2) ? '_' : value2);
            case 'INCREMENT':
                value1 = incrementContainer.down('numberfield[name=min_start]').getValue();
                value2 = incrementContainer.down('numberfield[name=min_increment]').getValue();
                return (value1 = Ext.isEmpty(value1) ? '_' : value1) + '/' + (value2 = Ext.isEmpty(value2) ? '_' : value2);
            case 'SPECIFIC':
                value1 = specificContainer.down('textfield[name=min_value]').getValue();
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
            typeCombo = fieldContainer.getComponent('MIN_TYPE'),
            rangeContainer = fieldContainer.getComponent('MIN_RANGE'),
            incrementContainer = fieldContainer.getComponent('MIN_INCREMENT'),
            specificContainer = fieldContainer.getComponent('MIN_SPECIFIC');

        switch (cronObject.type) {
            case 'ALL':
                typeCombo.setValue('ALL');
                break;
            case 'RANGE':
                typeCombo.setValue('RANGE');
                rangeContainer.down('numberfield[name=min_from]').setValue(cronObject.value[0]);
                rangeContainer.down('numberfield[name=min_to]').setValue(cronObject.value[1]);
                break;
            case 'INCREMENT':
                typeCombo.setValue('INCREMENT');
                incrementContainer.down('numberfield[name=min_start]').setValue(cronObject.value[0]);
                incrementContainer.down('numberfield[name=min_increment]').setValue(cronObject.value[1]);
                break;
            case 'SPECIFIC':
                typeCombo.setValue('SPECIFIC');
                specificContainer.down('textfield[name=min_value]').setValue(cronObject.orgValue);
                break;
        }
    }
});

/**
 * cron vtype
 */
Ext.apply(Ext.form.field.VTypes, {
    cronMinComma: function (v) {
        var minExp = /^(([0-5]?[0-9])|(([0-5]?[0-9],)([0-5]?[0-9])(,[0-5]?[0-9])*))$/;
        return minExp.test(v);
    },
    cronMinCommaText: message.msg('batch.msg.quartz_cron_express')
});