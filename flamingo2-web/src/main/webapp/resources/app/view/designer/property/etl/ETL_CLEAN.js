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
 * Clean ETL Property View
 *
 * @class
 * @extends Flamingo2.view.designer.property._NODE_ETL
 * @author <a href="mailto:hrkenshin@gmail.com">Seungbaek Lee</a>
 */
Ext.define('Flamingo2.view.designer.property.etl.ETL_CLEAN', {
    extend: 'Flamingo2.view.designer.property._NODE_ETL',
    alias: 'widget.ETL_CLEAN',

    requires: [
        'Flamingo2.view.designer.property._DelimiterSelCmbField',
        'Flamingo2.view.designer.property._BrowserField',
        'Flamingo2.view.designer.property._ColumnGrid_Prev',
        'Flamingo2.view.designer.property._ColumnGrid',
        'Flamingo2.view.designer.property._NameValueGrid',
        'Flamingo2.view.designer.property._KeyValueGrid'
    ],

    items: [
        {
            title: 'Clean',
            xtype: 'form',
            border: false,
            autoScroll: true,
            defaults: {
                anchor: '100%',
                labelWidth: 100
            },
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            items: [
                {
                    xtype: 'hidden',
                    name: 'columnsToClean'
                },
                {
                    xtype: 'hidden',
                    name: 'jar',
                    value: 'org.openFlamingo2:Flamingo2-mapreduce:0.2'
                },
                {
                    xtype: 'hidden',
                    name: 'driver',
                    value: 'clean'
                },
                {
                    xtype: '_prevColumnGrid',
                    title: message.msg('workflow.title_delete_column'),
                    selModel: {
                        selType: 'checkboxmodel',
                        mode: 'MULTI',
                        listeners: {
                            selectionchange: function (sm, selections, eOpts) {
                                // 삭제할 컬럼 체크시 출력 컬럼에 적용
                                var columnArray = [],
                                    columnsToClean = [],
                                    columnGrid = Ext.ComponentQuery.query('ETL_CLEAN > form > _columnGrid')[0],
                                    columnsToCleanField = Ext.ComponentQuery.query('ETL_CLEAN > form > hidden[name=columnsToClean]')[0];
                                if (columnGrid) {
                                    sm.getStore().each(function (record, idx) {
                                        if (!this.isSelected(record)) {
                                            columnArray.push({
                                                columnNames: record.get('prevColumnNames'),
                                                columnKorNames: record.get('prevColumnKorNames'),
                                                columnTypes: record.get('prevColumnTypes'),
                                                columnDescriptions: record.get('prevColumnDescriptions')
                                            });
                                        } else {
                                            columnsToClean.push(idx);
                                        }
                                    }, sm);

                                    columnsToCleanField.setValue(columnsToClean.join(','));
                                    columnGrid.getStore().loadData(columnArray);
                                }
                            }
                        }
                    },
                    readOnly: true,
                    flex: 1
                }
            ]
        },
        {
            title: message.msg('workflow.title_output_col'),
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
                    xtype: 'hidden',
                    name: 'outputPathQualifier'
                },
                {
                    xtype: '_delimiterSelCmbField'
                },
                {
                    xtype: '_columnGrid',
                    readOnly: true,
                    flex: 1
                }
            ]
        },
        {
            title: message.msg('common.variable'),
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
                    xtype: '_nameValueGrid',
                    flex: 1
                }
            ]
        },
        {
            title: message.msg('workflow.common.hadoop.conf'),
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
                    xtype: 'displayfield',
                    height: 20,
                    value: message.msg('workflow.common.hadoop.conf.guide')
                },
                {
                    xtype: '_keyValueGrid',
                    flex: 1
                }
            ]
        }
    ],

    initComponent: function () {
        this.callParent(arguments);

        // TODO Insert initialization
    },

    /**
     * 주어진 프라퍼티정보로 노드의 프라퍼티를 설정한다.
     *
     * @param {Object} nodeProperties Property(name:value) Object
     * @override
     */
    setNodeProperties: function (nodeProperties) {
        this.callParent(arguments);

        // _prevColumnGrid 삭제할 항목 체크 처리
        var prevColumnGrid = this.query('_prevColumnGrid')[0],
            columnGridStore = this.query('_columnGrid')[0].getStore(),
            checkboxModel = prevColumnGrid.getSelectionModel(),
            selectedColumns = [];

        prevColumnGrid.getStore().each(function (prevColumnInfo, idx) {
            if (columnGridStore.findExact('columnNames', prevColumnInfo.get('prevColumnNames')) === -1) {
                selectedColumns.push(prevColumnInfo);
            }
        });

        // TODO : ext-js 의 grid 로딩시 checkbox selection bug 로 인해 defer 로 처리함
        Ext.defer(function () {
            checkboxModel.select(selectedColumns);
        }, 200);
    }
});