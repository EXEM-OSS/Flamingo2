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
 * HDFS Output Property View
 *
 * @class
 * @extends Flamingo2.view.designer.property._NODE_INOUT
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.designer.property.INOUT_HDFS_OUT', {
    extend: 'Flamingo2.view.designer.property._NODE_INOUT',
    alias: 'widget.INOUT_HDFS_OUT',

    requires: [
        'Flamingo2.view.designer.property._DelimiterSelCmbField',
        'Flamingo2.view.designer.property._CompressSelCmbField',
        'Flamingo2.view.designer.property._BrowserField',
        'Flamingo2.view.designer.property._MetaBrowserField',
        'Flamingo2.view.designer.property._ColumnGrid'
    ],

    items: [
        {
            title: message.msg('workflow.title_path_information'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            items: [
                {
                    xtype: 'fieldset',
                    title: message.msg('workflow.title_ouput_path_info'),
                    defaultType: 'textfield',
                    defaults: {
                        anchor: '100%',
                        labelWidth: 100
                    },
                    items: [
                        {
                            name: 'outputPathQualifier',
                            fieldLabel: message.msg('common.identifier'),
                            readOnly: true
                        },
                        {
                            xtype: '_browserField',
                            name: 'outputPath'
                        },
                        {
                            xtype: '_delimiterSelCmbField',
                            readOnly: true
                        }
                    ]
                }
                /*
                 ,
                 {
                 xtype      : 'fieldset',
                 title      : '기타 설정',
                 defaultType: 'textfield',
                 defaults   : {
                 anchor    : '100%',
                 labelWidth: 100
                 },
                 items      : [
                 {
                 xtype: '_compressSelCmbField'
                 }
                 ]
                 }
                 */
            ]
        },
        {
            title: message.msg('workflow.title_col_info'),
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: '_columnGrid',
                    flex: 1,
                    readOnly: true
                }
            ]
        }
    ]
});