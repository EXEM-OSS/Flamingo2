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
Ext.define('Flamingo2.view.visualization.ggplot2.Ggplot2Controller', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ggplot2Controller',

    requires: [
        'Flamingo2.view.visualization.ggplot2._Upload',
        'Flamingo2.view.fs.hdfs.simple.SimpleHdfsFileBrowser'
    ],

    listen: {
        controller: {
            ggplo2UploadController: {
                setWorkspace: 'onsSetWorkspace'
            },
            ggplot2GeomMenuController: {
                geomMenuClick: 'onGeomMenuClick'
            }
        }
    },

    onAfterrender: function () {
        var me = this;

        me.getViewModel().setData({
            plotConfig: new PlotConfig()
        });
    },

    onLoadBtnClick: function () {
        Ext.create('Flamingo2.view.visualization.ggplot2._Upload').show();
    },

    onsSetWorkspace: function (varListing) {
        var me = this;
        var refs = me.getReferences();
        var variables, dataVariables, thisV;

        me.getViewModel().setData({varListing: varListing});
        variables = varListing.variables[0].children.concat(varListing.variables[1].children);
        dataVariables = new Array();

        for (thisV = 0; thisV < variables.length; thisV++) {
            dataVariables[thisV] = variables[thisV].text;
        }

        me.dataVariables = dataVariables;
        me.createMenu(dataVariables);

        refs.workspace.getEl().on('contextmenu', me.imageContext, me);

        for (var idx in varListing.variablelist) {
            var record = new Flamingo2.model.visualization.dataList(varListing.variablelist[idx]);
            refs.dataList.getStore().insert(0, record);
        }

        me.getViewModel().getData().plotConfig["dataFile"] = varListing.dataFile;
        me.updateGeneralDetails(me.getViewModel().getData().plotConfig);

        refs.btnDataLoad.setDisabled(true);
        refs.btnDataClose.setDisabled(false);
        refs.btnDrawPlot.setDisabled(false);
        refs.btnResetPlot.setDisabled(false);
    },

    imageContext: function (e, t, o) {
        var me = this;
        me.getViewModel().getData().plotMenu.showAt(e.pageX - 5, e.pageY - 5);
        e.stopEvent();
    },

    createMenu: function (dataVariables) {
        var me = this;
        var refs = me.getReferences();
        var menuData = me.getViewModel().getData();
        var Defaults = menuData.Defaults;
        var i;
        var plotMenu;

        if (Ext.isEmpty(me.getViewModel().getData().plotMenu)) {
            plotMenu = Ext.create('Ext.menu.Menu');
            me.getViewModel().setData({plotMenu: plotMenu});
        }
        else {
            plotMenu = me.getViewModel().getData().plotMenu
        }

        for (i = 0; i < Defaults.length; i++) {
            if (Defaults[i] == "-") {

                plotMenu.add("-");

            } else {
                if (Defaults[i].required) {
                    plotMenu.add({
                        reference: "mapping" + Defaults[i].name,
                        text: "Map " + Defaults[i].name + " (required)",
                        iconCls: "icon_map",
                        handler: function () {
                            return false;
                        },
                        menu: me.makeVarMenu(Defaults[i].name, Ext.util.JSON.decode(Ext.util.JSON.encode(dataVariables)), "default", false, null, "map_")
                    });
                } else {
                    plotMenu.add({
                        "reference": "mapping" + Defaults[i].name,
                        "text": "Map " + Defaults[i].name,
                        "iconCls": "icon_map",
                        handler: function () {
                            return false;
                        },
                        menu: me.makeVarMenu(Defaults[i].name, Ext.util.JSON.decode(Ext.util.JSON.encode(dataVariables)), "default", true, null, "map_")
                    });
                }
            }
        }

        var facetMenu = plotMenu.add({
            reference: "facet",
            text: "Facet",
            iconCls: 'icon_facet',
            handler: function () {
                return false;
            },
            menu: {}
        });

        var facetProperties = menuData.Facet

        for (thisaes = 0; thisaes < facetProperties.length; thisaes++) {

            var thisProp = facetProperties[thisaes];

            if (thisProp.set) {
                facetMenu.menu.add({
                    text: thisProp.name,
                    handler: function () {
                        return false
                    },
                    iconCls: 'icon_set',
                    key: thisProp.name,
                    menu: me.makeVarMenu(thisProp.name, thisProp.values, "default", true, "facet", "set_")
                });
            }

            else if (thisProp.map) {
                facetMenu.menu.add({
                    text: thisProp.name,
                    handler: function () {
                        return false
                    },
                    iconCls: 'icon_map',
                    key: thisProp.name,
                    menu: me.makeVarMenu(thisProp.name, dataVariables, "default", true, "facet", "map_")
                });
            }
        }
    },

    makeVarMenu: function (key, values, selected, addDefault, layer, prefix, addCustom) {
        var me = this;
        var refs = me.getReferences();
        var data = me.getViewModel().getData();
        if (key == "y") {
            values = new Array('..count..', '..density..', '-').concat(values);
        }
        var plotConfig = data.plotConfig;
        var varMenu = Ext.create('Ext.menu.Menu', {
            //defaults: {hideOnClick: false},
            items: []
        });

        if (addDefault) {
            var myDefault = varMenu.add({
                text: "default",
                key: key,
                value: "default",
                layer: layer,
                group: layer + key,
                checked: selected == "default",
                handler: function () {
                    plotConfig.set(this.key, this.value, this.layer);

                    if ((this.key == "colour" || this.key == "fill") && this.layer != null) {
                        this.myCP.clear();//this.myCP.unselect(false);
                        refs.plotMenu.hide();
                    }

                    if (this.layer != null && this.layer != "facet") {
                        var selectRecord = refs.layerList.getSelectionModel().getSelection()[0];
                        if (!Ext.isEmpty(selectRecord)) {
                            me.onLayerItemclick(refs.layerList, selectRecord);
                        }
                    } else {
                        me.updateGeneralDetails(plotConfig);
                    }
                }
            });
            varMenu.add("-");
        }
        var myCP;

        if ((key == "colour" || key == "fill") & prefix == "set_") {
            varMenu.add(
                myCP = myDefault.myCP = new Ext.ColorPalette({
                    layer: layer,
                    key: key,
                    handler: function (pl, pickedColor) {
                        // pl == this
                        this.customChoise.enable();
                        this.customChoise.value = "set_#" + pickedColor;
                        this.customChoise.setText("custom: #" + pickedColor);
                        this.customChoise.setChecked(true);

                        plotConfig.set(this.key, "set_#" + pickedColor, this.customChoise.layer);
                        var selectRecord = refs.layerList.getSelectionModel().getSelection()[0];
                        if (!Ext.isEmpty(selectRecord)) {
                            me.onLayerItemclick(refs.layerList, selectRecord);
                        }
                        refs.plotMenu.hide();
                        this.customChoise.getEl().highlight(pickedColor);
                    }
                })
            );
        }

        for (thisVal = 0; thisVal < values.length; thisVal++) {
            if (values[thisVal] == '-') {
                varMenu.add('-');
            } else {
                varMenu.add({
                    text: values[thisVal],
                    key: key,
                    value: prefix + values[thisVal],
                    layer: layer,
                    group: layer + key,
                    checked: values[thisVal] == selected,
                    handler: function () {
                        plotConfig.set(this.key, this.value, this.layer);

                        if (this.key == "y") {
                            refs.btnLayerAdd.setDisabled(false);
                            refs.btnLayerRemove.setDisabled(false);
                            if (this.value.substring(4) == "..count.." || this.value.substring(4) == "..density..") {
                                refs.btnLayerAdd.menu.items.items[1].setVisible(true);
                                refs.btnLayerAdd.menu.items.items[2].setVisible(false);
                                refs.btnLayerAdd.menu.items.items[3].setVisible(false);
                            } else {
                                refs.btnLayerAdd.menu.items.items[1].setVisible(false);
                                refs.btnLayerAdd.menu.items.items[2].setVisible(true);
                                refs.btnLayerAdd.menu.items.items[3].setVisible(true);
                            }
                            me.getViewModel().setData({isGeomMenuShow: true});
                        }

                        if (this.layer != null && this.layer != "facet") {

                        } else {
                            me.updateGeneralDetails(plotConfig);
                        }
                    }
                });
            }
        }

        if (addCustom) {
            varMenu.add("-");
            customChoise = varMenu.add({
                text: "custom: (no value set)",
                key: key,
                disabled: true,
                value: "default",
                layer: layer,
                myCP: myCP,
                group: layer + key,
                checked: false,
                handler: function () {
                    plotConfig.set(this.key, this.value, this.layer);
                    activeRecord = Ext.getCmp('layerList').getStore().getById(this.layer);
                    Ext.getCmp('layerList').getSelectionModel().selectRecords([activeRecord]);
                },
                listeners: {
                    'checkchange': function (item, checked) {
                        if (!checked) {
                            if (this.key == "colour" || this.key == "fill")
                                this.myCP.clear();//this.myCP.unselect(false);

                            this.setText("custom: (no value set)");
                            this.disable();
                        }
                    }
                }
            });

            if (key == "colour" || key == "fill") {
                myCP.customChoise = customChoise;
            }

            var thisField = varMenu.add({
                xtype: "buttongroup",
                autoWidth: true,
                columns: 2,
                defaults: {
                    iconAlign: 'left'
                },
                items: []
            });

            var customValueField
            if (me.getViewModel().getData().Aesthetics[key].type == "numeric") {
                customValueField = thisField.add({
                    xtype: 'numberfield',
                    allowBlank: false,
                    validateOnBlur: false,
                    text: 'custom value'
                });
            } else {
                customValueField = thisField.add({
                    xtype: 'textfield',
                    allowBlank: false,
                    validateOnBlur: false,
                    text: 'custom value'
                });
            }

            thisField.add({
                customChoise: customChoise,
                myCP: myCP,
                key: key,
                layer: layer,
                varMenu: varMenu,
                customValueField: customValueField,
                text: 'set value',
                handler: function () {

                    if (!this.customValueField.validate()) {
                        return false;
                    }
                    if (this.key == "colour" || this.key == "fill") {
                        this.myCP.unselect(false);
                    }
                    var theValue = this.customValueField.getValue();
                    this.customChoise.enable();
                    this.customChoise.value = "set_" + theValue;
                    this.customChoise.setText("custom: " + theValue);
                    this.customChoise.getEl().highlight();
                    this.customChoise.setChecked(true);

                    if (this.customChoise.checked) {
                        plotConfig.set(this.customChoise.key, this.customChoise.value, this.customChoise.layer);
                        var activeRecord = Ext.getCmp('layerList').getStore().getById(this.layer);
                        Ext.getCmp('layerList').getSelectionModel().selectRecords([activeRecord]);
                    }

                    this.customValueField.reset();
                    //Ext.getCmp('plotMenu').hide();
                }
            });
        }

        return varMenu;
    },

    updateGeneralDetails: function (plotConfig) {
        var me = this;
        var refs = me.getReferences();

        refs.grdGeneralOptions.getStore().removeAll();

        refs.grdGeneralOptions.getStore().insert(refs.grdGeneralOptions.getStore().getCount(), {
            name: 'X_Axis',
            value: plotConfig.x
        });
        refs.grdGeneralOptions.getStore().insert(refs.grdGeneralOptions.getStore().getCount(), {
            name: 'Y_Axis',
            value: plotConfig.y
        });

        if (plotConfig.weight)
            refs.grdGeneralOptions.getStore().insert(refs.grdGeneralOptions.getStore().getCount(), {
                name: 'Weight',
                value: plotConfig.weight
            });

        if (plotConfig.group)
            refs.grdGeneralOptions.getStore().insert(refs.grdGeneralOptions.getStore().getCount(), {
                name: 'Group',
                value: plotConfig.group
            });

        if (plotConfig.colour)
            refs.grdGeneralOptions.getStore().insert(refs.grdGeneralOptions.getStore().getCount(), {
                name: 'Colour',
                value: plotConfig.colour
            });

        if (plotConfig.facet.map)
            refs.grdGeneralOptions.getStore().insert(refs.grdGeneralOptions.getStore().getCount(), {
                name: 'Facet',
                value: plotConfig.facet.map
            });

        if (plotConfig.facet.nrow)
            refs.grdGeneralOptions.getStore().insert(refs.grdGeneralOptions.getStore().getCount(), {
                name: 'Facet_nrow',
                value: plotConfig.facet.nrow
            });

        if (plotConfig.facet.scales)
            refs.grdGeneralOptions.getStore().insert(refs.grdGeneralOptions.getStore().getCount(), {
                name: 'Facet_scales',
                value: plotConfig.facet.scales
            });
    },

    onLayerRowcontextmenu: function (grid, record, tr, rowIndex, e, eOpts) {
        grid.getSelectionModel().select([record]);
        e.stopEvent();
        grid.getStore().getAt(rowIndex).layerMenu.menu.showAt(e.getPoint());
    },

    newLayer: function (geom, aes) {
        var me = this;
        var refs = me.getReferences();
        var plotConfig = me.getViewModel().getData().plotConfig;
        var randomKey = "layer" + (Math.floor(Math.random() * 90000) + 10000);

        plotConfig.addLayer(geom, randomKey);

        var newMenu = me.newLayerMenu(geom, aes, randomKey);
        var newRecord = me.addToList(geom, randomKey);
        newRecord.layerMenu = newMenu;

        me.updateGeneralDetails(me.getViewModel().getData().plotConfig);
        me.onLayerItemclick(refs.layerList, refs.layerList.getSelectionModel().getSelection()[0]);
    },

    newLayerMenu: function (geom, aes, layerKey) {
        var me = this;
        var refs = me.getReferences();
        var dataVariables = me.dataVariables;
        var plotConfig = me.getViewModel().getData().plotConfig;

        var thisLayerMenu = new Ext.menu.Item({
            id: layerKey,
            text: "Layer: " + geom,
            iconCls: "icon_" + geom,
            handler: function () {
                return false
            },
            menu: {items: ['<b class="menu-title">Layer: ' + geom + '</b>']}
        });

        for (thisaes = 0; thisaes < aes.length; thisaes++) {
            var myAes = aes[thisaes];
            var myValues = me.getViewModel().getData().Aesthetics[myAes].values;

            var mapSetMenu = thisLayerMenu.menu.add({
                text: myAes,
                handler: function () {
                    return false
                },
                iconCls: 'icon_prop',
                key: myAes,
                menu: {}
            });

            if (me.getViewModel().getData().Aesthetics[myAes].set) {
                mapSetMenu.menu.add({
                    text: "Set",
                    iconCls: 'icon_set',
                    handler: function () {
                        return false
                    },
                    menu: me.makeVarMenu(myAes, myValues, "default", !me.getViewModel().getData().Aesthetics[myAes].required, layerKey, "set_", me.getViewModel().getData().Aesthetics[myAes].custom)
                });
            }

            var newDataVariables = dataVariables;
            if (me.getViewModel().getData().Statvar[geom]) {
                newDataVariables = me.getViewModel().getData().Statvar[geom].concat('-').concat(newDataVariables);
            }

            if (me.getViewModel().getData().Aesthetics[myAes].map) {
                mapSetMenu.menu.add({
                    iconCls: 'icon_map',
                    text: "Map",
                    handler: function () {
                        return false
                    },
                    menu: me.makeVarMenu(myAes, newDataVariables, "default", !me.getViewModel().getData().Aesthetics[myAes].set, layerKey, "map_", false)
                });
            }

        }

        return thisLayerMenu;
    },

    addToList: function (geom, layerKey) {
        var me = this;
        var refs = me.getReferences();
        refs.layerList.getStore().loadData([[layerKey, geom]], true);

        var activeRecord = refs.layerList.getStore().getById(layerKey);
        refs.layerList.getSelectionModel().select([activeRecord]);
        return activeRecord;
    },

    onLayerItemclick: function (grid, record, item, index, e) {
        var me = this;
        var plotConfig = me.getViewModel().getData().plotConfig;
        me.updateLayerDetails(plotConfig.layers[record.id]);
    },

    updateLayerDetails: function (layerConfig) {
        var refs = this.getReferences();

        refs.grdLayerOptions.getStore().removeAll();
        for (var prop in layerConfig) {
            refs.grdLayerOptions.getStore().insert(refs.grdLayerOptions.getStore().getCount(), {
                name: prop,
                value: layerConfig[prop]
            });
        }
    },

    onDrawPlotClick: function () {
        var me = this
        var refs = this.getReferences();
        var plotConfig = me.getViewModel().getData().plotConfig;
        var varListing = me.getViewModel().getData().varListing;


        if (plotConfig.x == null || plotConfig.y == null)
            alert("You need to set X and Y first!");
        else if (me.verifyRequiredAes(plotConfig)) {
            width = refs.workspace.getSize().width;
            height = refs.workspace.getSize().height;
            plotConfig.width = width;
            plotConfig.height = height - 40;

            var i
            var strPlotConfig = Ext.encode(plotConfig);
            for (i = 0; i < varListing.variablelist.length; i++) {
                var replaceStr = '_' + varListing.variablelist[i].value + '"'
                var regExp = new RegExp(eval('/_' + varListing.variablelist[i].text + '"/gi'));

                strPlotConfig = strPlotConfig.replace(regExp, replaceStr);
            }
            var params = {
                plotRequest: strPlotConfig,
                clusterName: ENGINE.id
            };

            me.getView().setLoading(true);
            invokePostByMap(CONSTANTS.VISUAL.CREATE_PNG, params,
                function (response) {
                    var obj = Ext.decode(response.responseText);

                    if (obj.success) {
                        var img = Ext.decode(obj.map.image);
                        $('#ggplot_img').remove();
                        refs.workspace.update(img.imgurl);
                    } else if (obj.error.cause) {
                        Ext.MessageBox.show({
                            title: "오류",
                            message: obj.error.cause,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: "오류",
                            message: obj.error.message,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    }
                    me.getView().setLoading(false);
                },
                function (response) {
                    Ext.MessageBox.show({
                        title: '경고',
                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                    me.getView().setLoading(false);
                }
            );

            /*
             refs.workspace.getForm().submit({
             timeout: 60, //timeout for load is in seconds
             url: 'http://yeroon.net/R/ggplot-png',
             method: "POST",
             params: {
             "plotRequest" : Ext.encode(plotConfig)
             },
             success: function(uploadForm, o) {
             Ext.getCmp('drawButton').enable();
             Ext.getCmp('pdfbutton').enable();
             Ext.getCmp('svgbutton').enable();
             //var plotResult = me.tryDecode(response.responseText);
             //if(plotResult && !plotResult.success) addToTerminal("Error: " + plotResult.error);
             }
             });
             */
        }
    },

    tryDecode: function (jsonString) {
        try {
            return Ext.util.JSON.decode(jsonString);
        }
        catch (err) {
            //alert("Error in decoding JSON: " + jsonString);
            return false;
        }
    },

    verifyRequiredAes: function (plotConfig) {
        for (var layerID in plotConfig.layers) {
            var thisLayer = plotConfig.layers[layerID];
            var thisGeom = thisLayer.geom;
            var menuData = this.getViewModel().getData();
            if (menuData.Required[thisGeom]) {
                var requirements = menuData.Required[thisGeom];
                for (var reqID = 0; reqID < requirements.length; reqID++) {
                    var thisReq = requirements[reqID];
                    if (!thisLayer[thisReq]) {
                        alert("Aesthetic '" + thisReq + "' is required for 'geom_" + thisGeom + "'. Please set/map it in it's menu, and try again.");
                        return false;
                    }
                }
            }
        }
        return true;
    },

    onGeomMenuClick: function (menu) {
        this.newLayer(menu.geom, menu.aes);
    },

    onBtnLayerRemoveClick: function () {
        var me = this;
        var refs = this.getReferences();
        var plotConfig = me.getViewModel().getData().plotConfig;
        var record = refs.layerList.getSelectionModel().getSelection()[0];

        refs.layerList.getStore().remove([record]);
        plotConfig.removeLayer(record.id);
        me.updateLayerDetails(null);
        me.updateGeneralDetails(plotConfig);
    },

    onResetClick: function () {
        var me = this;
        var refs = me.getReferences();

        var dataFile = me.getViewModel().getData().plotConfig.dataFile;

        me.getViewModel().getData().plotConfig = new PlotConfig();
        me.getViewModel().getData().plotConfig.dataFile = dataFile;
        me.updateGeneralDetails(me.getViewModel().getData().plotConfig);
        me.updateLayerDetails(null);

        refs.layerList.getStore().removeAll();
        refs.workspace.removeAll();

        me.getViewModel().getData().plotMenu.removeAll();
        me.getViewModel().getData().plotMenu = null;
        me.createMenu(me.dataVariables);
        refs.btnLayerAdd.setDisabled(true);
        refs.btnLayerRemove.setDisabled(true);
        $('#ggplot_img').remove();
    },

    onBtnCloseClick: function () {
        var me = this;
        var refs = me.getReferences();

        var dataFile = me.getViewModel().getData().plotConfig.dataFile;

        me.getViewModel().getData().plotConfig = new PlotConfig();
        me.getViewModel().getData().plotConfig.dataFile = dataFile;
        me.updateGeneralDetails(me.getViewModel().getData().plotConfig);
        me.updateLayerDetails(null);

        refs.layerList.getStore().removeAll();
        refs.dataList.getStore().removeAll();
        refs.grdGeneralOptions.getStore().removeAll();
        refs.workspace.removeAll();

        me.getViewModel().getData().plotMenu.removeAll();
        me.getViewModel().getData().plotMenu = null;

        refs.workspace.getEl().removeListener('contextmenu', me.imageContext, me);
        $('#ggplot_img').remove();

        refs.btnDataLoad.setDisabled(false);
        refs.btnDataClose.setDisabled(true);
        refs.btnDrawPlot.setDisabled(true);
        refs.btnResetPlot.setDisabled(true);
    }
});