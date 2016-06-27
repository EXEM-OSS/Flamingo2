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
Ext.define('Flamingo2.view.fs.hdfs.streaming.RealTimeStreamingForm', {
    extend: 'Ext.form.Panel',
    alias: 'widget.realTimeStreamingForm',

    reference: 'realTimeStreamingForm',
    bodyBorder: false,
    bodyStyle: {
        background: '#FFFFFF'
    },
    bodyPadding: 15,

    items: [
        {
            xtype: 'textfield',
            reference: 'streamingName',
            fieldLabel: '스트리밍명',
            labelAlign: 'right',
            labelWidth: 100,
            width: 450,
            allowBlank: false
        },
        {
            xtype: 'textfield',
            reference: 'streamingId',
            fieldLabel: '스트리밍 ID',
            labelAlign: 'right',
            labelWidth: 100,
            width: 450,
            allowBlank: false
        },
        {
            xtype: 'textarea',
            reference: 'filePath',
            fieldLabel: '파일경로',
            labelAlign: 'right',
            labelWidth: 100,
            name: 'filePath',
            width: 450,
            height: 125,
            allowBlank: true
        },
        {
            xtype: 'fieldcontainer',
            fieldLabel: '시작 지연',
            combineErrors: false,
            defaults: {
                hideLabel: true
            },
            labelAlign: 'right',
            labelWidth: 100,
            layout: 'hbox',
            items: [
                {
                    xtype: 'textfield',
                    reference: 'startDelay',
                    width: 130,
                    vtype: 'numeric',
                    allowBlank: true
                },
                {
                    xtype: 'displayfield',
                    padding: '0 0 0 5',
                    value: '분'
                }
            ]
        },
        {
            xtype: 'fieldcontainer',
            fieldLabel: '스트리밍 추가',
            combineErrors: false,
            defaults: {
                hideLabel: true
            },
            labelAlign: 'right',
            labelWidth: 100,
            layout: 'hbox',
            items: [
                {
                    xtype: 'textfield',
                    reference: 'addStreaming',
                    width: 130,
                    vtype: 'numeric',
                    allowBlank: true,
                    validator: function (value) {
                        if (value < 200) {
                            console.log(value);
                            return false;
                        } else {
                            return true;
                        }
                    }
                },
                {
                    xtype: 'displayfield',
                    padding: '0 0 0 5',
                    value: '밀리초 (200초 이상만 입력가능)'
                }
            ]
        }
    ]
});
