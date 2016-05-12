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
 * ViewController form Flamingo2.view.hawq.editor.HawqEditor
 *
 * @author Ha Neul, Kim
 * @since 2.0
 * @see Flamingo2.view.hawq.editor.HawqEditor
 */
Ext.define('Flamingo2.view.hawq.editor.HawqEditorController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.hawqEditorController',

    hawqEditorAfterrender: function (panel) {
        var me = this,
            refs = me.getReferences();
        setTimeout(function () {
            if (refs.queryEditor) {
                refs.queryEditor.editor.focus();
            }
        }, 300);
    },

    hawqResultLogPanelRemoveToolbar: function (panel) {
        // Toolbar를 없앤다.
        panel.down('toolbar').setVisible(false);
    },

    hawqQueryResultTabpanelTabchange: function (tabPanel, newCard, oldCard, eOpts) {
        var me = this,
            refs = me.getReferences();

        switch (newCard.reference) {
            case 'hawqDonutChartPanel':
            case 'hawqBarChartPanel':
            case 'hawqAreaChartPanel':
            case 'hawqLineChartPanel':
                me.hawqChartDraw(newCard);
                break;
            default:
                break;
        }
    },

    onResize: function () {
        var refs = this.getReferences();
        if (refs.queryEditor.editor) {
            refs.queryEditor.editor.resize();
        }
    },

    onDestroy: function (tabpanel, component, eOpts) {
        this.fireEvent('editorDestroyed');
    },

    onBeforeclose: function (component, eOpts) {
        var queryString = component.getReferences().queryEditor.getValue();
        if (component.isRunning) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.stop'),
                message: message.msg('hawq.msg.question.stopquery'),
                buttons: Ext.MessageBox.YESNO,
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        component.destroy();
                    } else if (buttonId === 'no') {
                        return false;
                    }
                }
            });
        } else if (trim(queryString)) {
            Ext.MessageBox.show({
                title: message.msg('hawq.title.close'),
                message: message.msg('hawq.msg.question.closeeditortab'),// FIXME 작성중인 쿼리가 있습니다. 쿼리를 저장하시겠습니까?
                buttons: Ext.MessageBox.YESNO,// FIXME YESNOCANCEL
                icon: Ext.MessageBox.QUESTION,
                fn: function (buttonId) {
                    if (buttonId === 'yes') {
                        component.destroy();
                    } else if (buttonId === 'no') {
                        return false;
                    }
                }
            });
        } else {
            return true;
        }
        return false;
    },

    onEditorTabBeforedestroy: function (tabpanel, eOpts) {
        var queryString = trim(tabpanel.getReferences().queryEditor.getValue());
        if (tabpanel.isRunning) {
            tabpanel.isRunning = false;
            invokePostByMap(
                CONSTANTS.HAWQ.EDITOR.KILL_SESSION,
                {
                    clusterName: ENGINE.id,
                    pid: tabpanel.pid
                },
                function (response) {
                    var result = Ext.decode(response.responseText);
                    if (result.success) {
                        var killSuccess = result.object;
                        if (killSuccess) {
                            Ext.toast({
                                title: message.msg('hawq.title.success'),
                                html: message.msg('hawq.msg.success.stopquery'),
                                align: 'br',
                                iconCls: 'fa fa-exclamation-circle fa-lg',
                                slideInDuration: 500,
                                hideDuration: 5000,
                                minWidth: 150,
                                buttons: [
                                    {
                                        text: message.msg('hawq.button.viewquery'),
                                        iconCls: 'common-detail',
                                        handler: function () {
                                            Ext.create('Ext.window.Window', {
                                                title: message.msg('hawq.title.viewquery'),
                                                titleCollapse: false,
                                                modal: true,
                                                closeAction: 'destroy',
                                                width: 300,
                                                height: 500,
                                                closable: true,
                                                layout: 'fit',
                                                bodyStyle: {
                                                    background: '#fff'
                                                },
                                                items: [
                                                    {
                                                        xtype: 'textareafield',
                                                        value: queryString,
                                                        readOnly: true
                                                    }
                                                ]
                                            }).center().show();
                                        }
                                    }
                                ]
                            });
                        } else {
                            App.UI.errormsg(message.msg('hawq.title.fail'), message.msg('hawq.msg.fail.notrunning'));
                        }
                    } else {
                        App.UI.errormsg(message.msg('hawq.title.fail'), message.msg('hawq.msg.fail.stopquery') + '<br/>' + format(message.msg('hawq.msg.cause'), result.error.message));
                    }
                },
                function (response) {
                    Ext.MessageBox.show({
                        title: message.msg('hawq.title.fail'),
                        message: format(message.msg('hawq.msg.fail.servererror'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.ERROR
                    });
                }
            );
        }
    },

    /**
     * Ctrl + Enter 단축키 이벤트
     */
    onExecuteQuery: function () {
        this.fireEvent('executeQuery');
    },

    btnDownloadClick: function (button) {
        var grid = button.up('grid'),
            store = grid.getStore(),
            columns = grid.getHeaderContainer().getGridColumns(),
            columnCount = grid.getHeaderContainer().getColumnCount(),
            fields = [],
            dataIndexes = [],
            datas = [],
            count = store.getCount(),
            i;

        for (i = 1; i < columnCount; i++) {
            var column = columns[i];
            fields.push(column.text);
            dataIndexes.push(column.dataIndex);
        }

        for (i = 0; i < count; i++) {
            var storeData = store.getAt(i),
                data = [];
            for (var index in dataIndexes) {
                var dataIndex = dataIndexes[index];
                data.push(storeData.get(dataIndex));
            }
            datas.push(data);
        }

        var form = Ext.get('hawqDownloadForm'),
            fieldsInput = form.child('input#hawqDownloadFields'),
            datasInput = form.child('input#hawqDownloadDatas');

        fieldsInput.set({value: escape(Ext.encode(fields))});
        datasInput.set({value: escape(Ext.encode(datas))});

        form.dom.submit();
    },

    hawqChartDraw: function (newCard) {
        var me = this,
            refs = me.getReferences(),
            panel = refs[newCard.reference],
            fields = panel.chartFields;

        if (!panel.items.items.length && fields && fields.length >= 2) {// 차트가 그려져있지 않고 x, y축 필드가 있다면
            switch (newCard.reference) {
                case 'hawqDonutChartPanel':
                    me.donutDraw(refs[newCard.reference]);
                    break;
                case 'hawqBarChartPanel':
                    me.barDraw(refs[newCard.reference]);
                    break;
                case 'hawqAreaChartPanel':
                    me.areaDraw(refs[newCard.reference]);
                    break;
                case 'hawqLineChartPanel':
                    me.lineDraw(refs[newCard.reference]);
                    break;
                default:
                    break;
            }
        }
    },

    donutDraw: function (panel) {
        var resultStore = panel.up('tabpanel').getComponent(1).down('grid').getStore(),
            fields = panel.chartFields;

        var donut = {
            xtype: 'polar',
            store: resultStore,
            animate: true,
            shadow: false,
            tbar: [
                '->',
                {
                    xtype: 'button',
                    text: message.msg('hawq.button.fullscreen'),
                    handler: function (button) {
                        Ext.create('Ext.window.Window', {
                            maximized: true,
                            layout: 'fit',
                            items: [
                                donut
                            ]
                        }).show();
                    }
                }
            ],
            legend: {
                field: fields[0].dataIndex,
                docked: 'right'
            },
            series: [
                {
                    type: 'pie',
                    xField: fields[1].dataIndex,
                    donut: 50,
                    showInLegend: true,
                    tips: {
                        trackMouse: true,
                        style: 'background: #FFF',
                        height: 20,
                        renderer: function (storeItem, item) {
                            this.setTitle(storeItem.get(fields[0].dataIndex) + ': ' + storeItem.get(fields[1].dataIndex));
                        }
                    }
                }
            ]
        };
        panel.add(donut);
    },

    barDraw: function (panel) {
        var resultStore = panel.up('tabpanel').getComponent(1).down('grid').getStore(),
            fields = panel.chartFields;

        var bar = {
            xtype: 'cartesian',
            animate: true,
            shadow: true,
            store: resultStore,
            tbar: [
                '->',
                {
                    xtype: 'button',
                    text: message.msg('hawq.button.fullscreen'),
                    handler: function (button) {
                        Ext.create('Ext.window.Window', {
                            maximized: true,
                            layout: 'fit',
                            items: [
                                bar
                            ]
                        }).show();
                    }
                }
            ],
            axes: [
                {
                    type: 'category',
                    position: 'bottom',
                    fields: [fields[0].dataIndex],
                    title: fields[0].dataIndex,
                    renderer: function (label, layout, lastLabel) {
                        return label.replace(/&nbsp;/g, ' ');
                    }
                },
                {
                    type: 'numeric',
                    position: 'left',
                    fields: [fields[1].dataIndex],
                    title: fields[1].dataIndex,
                    renderer: Ext.util.Format.numberRenderer('0,0')
                }
            ],
            series: [
                {
                    type: 'bar',
                    axis: 'bottom',
                    highlight: true,
                    xField: fields[0].dataIndex,
                    yField: fields[1].dataIndex,
                    tips: {
                        trackMouse: true,
                        style: 'background: #FFF',
                        height: 20,
                        renderer: function (storeItem, item) {
                            this.setTitle(storeItem.get(fields[0].dataIndex) + ': ' + storeItem.get(fields[1].dataIndex));
                        }
                    }
                }
            ]
        };
        panel.add(bar);
    },

    areaDraw: function (panel) {
        var resultStore = panel.up('tabpanel').getComponent(1).down('grid').getStore(),
            fields = panel.chartFields;

        var area = {
            xtype: 'cartesian',
            animate: true,
            shadow: true,
            store: resultStore,
            tbar: [
                '->',
                {
                    xtype: 'button',
                    text: message.msg('hawq.button.fullscreen'),
                    handler: function (button) {
                        Ext.create('Ext.window.Window', {
                            maximized: true,
                            layout: 'fit',
                            items: [
                                area
                            ]
                        }).show();
                    }
                }
            ],
            axes: [
                {
                    type: 'category',
                    position: 'bottom',
                    fields: [fields[0].dataIndex],
                    title: fields[0].dataIndex,
                    renderer: function (label, layout, lastLabel) {
                        return label.replace(/&nbsp;/g, ' ');
                    }
                },
                {
                    type: 'numeric',
                    position: 'left',
                    fields: [fields[1].dataIndex],
                    title: fields[1].dataIndex,
                    renderer: Ext.util.Format.numberRenderer('0,0')
                }
            ],
            series: [
                {
                    type: 'area',
                    axis: 'bottom',
                    highlight: true,
                    xField: fields[0].dataIndex,
                    yField: fields[1].dataIndex,
                    tips: {
                        trackMouse: true,
                        style: 'background: #FFF',
                        height: 20,
                        renderer: function (storeItem, item) {
                            this.setTitle(storeItem.get(fields[0].dataIndex) + ': ' + storeItem.get(fields[1].dataIndex));
                        }
                    }
                }
            ]
        };
        panel.add(area);
    },

    lineDraw: function (panel) {
        var resultStore = panel.up('tabpanel').getComponent(1).down('grid').getStore(),
            fields = panel.chartFields;

        var line = {
            xtype: 'cartesian',
            animate: true,
            shadow: true,
            store: resultStore,
            tbar: [
                '->',
                {
                    xtype: 'button',
                    text: message.msg('hawq.button.fullscreen'),
                    handler: function (button) {
                        Ext.create('Ext.window.Window', {
                            maximized: true,
                            layout: 'fit',
                            items: [
                                line
                            ]
                        }).show();
                    }
                }
            ],
            axes: [
                {
                    type: 'category',
                    position: 'bottom',
                    fields: [fields[0].dataIndex],
                    title: fields[0].dataIndex,
                    renderer: function (label, layout, lastLabel) {
                        return label.replace(/&nbsp;/g, ' ');
                    }
                },
                {
                    type: 'numeric',
                    position: 'left',
                    fields: [fields[1].dataIndex],
                    title: fields[1].dataIndex,
                    renderer: Ext.util.Format.numberRenderer('0,0')
                }
            ],
            series: [
                {
                    type: 'line',
                    axis: 'bottom',
                    highlight: true,
                    xField: fields[0].dataIndex,
                    yField: fields[1].dataIndex,
                    tips: {
                        trackMouse: true,
                        style: 'background: #FFF',
                        height: 20,
                        renderer: function (storeItem, item) {
                            this.setTitle(storeItem.get(fields[0].dataIndex) + ': ' + storeItem.get(fields[1].dataIndex));
                        }
                    }
                }
            ]
        };
        panel.add(line);
    }
});