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
Ext.define('Flamingo2.view.system.license.License', {
    extend: 'Flamingo2.panel.Panel',

    viewModel: {
        type: 'license'
    },
    controller: 'license',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    flex: 1,

    defaults: {
        margin: '0 0 20 0'
    },
    items: [
        {
            xtype: 'form',
            title: '라이센스 요약',
            reference: 'frmLicense',
            border: true,
            layout: {
                type: 'table',
                columns: 2,
                tableAttrs: {
                    style: {
                        width: '100%'
                    }
                }
            },
            bodyPadding: '10 10 10 10',
            height: 170,
            defaultType: 'displayfield',
            defaults: {
                labelAlign: 'right'
            },
            items: [{
                fieldLabel: '등록일자',
                name: 'issueDate'
            }, {
                fieldLabel: '만료일자',
                name: 'goodBeforeDate'
            }, {
                fieldLabel: '제품명',
                name: 'PRODUCT_NAME'
            }, {
                fieldLabel: '버전',
                name: 'PRODUCT_VERSION'
            }, {
                fieldLabel: '라이센스 종류',
                name: 'LICENSE_TYPE'
            }, {
                fieldLabel: '사용국가',
                name: 'COUNTRY'
            }, {
                fieldLabel: '최대 노드 수',
                name: 'MAX_NODE'
            }]
        },
        {
            xtype: 'panel',
            title: '라이센스키',
            border: true,
            height: 350,
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            bodyPadding: '10 10 10 10',
            items: [{
                xtype: 'textareafield',
                reference: 'serverId',
                fieldLabel: 'Server ID',
                readOnly: true,
                labelWidth: 70,
                labelAlign: 'top',
                fieldStyle: 'color: red; display: inherit'
            }, {
                xtype: 'textareafield',
                reference: 'licenseKey',
                flex: 1,
                fieldLabel: '라이센스키',
                labelAlign: 'top'
            }, {
                xtype: 'container',
                layout: 'hbox',
                items: [{
                    xtype: 'component',
                    flex: 1
                }, {
                    xtype: 'button',
                    iconCls: 'common-refresh',
                    scale: 'medium',
                    handler: 'onBtnRefreshClick',
                    text: '갱신'
                }]
            }]
        }
    ],
    listeners: {
        afterrender: 'onAfterrender'
    }
});