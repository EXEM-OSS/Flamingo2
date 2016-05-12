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
Ext.define('Flamingo2.view.visualization.ggplot2._UploadController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.ggplo2UploadController',

    listen: {
        component: {
            'ggplo2Upload radio': {
                change: 'onRadioChange'
            }
        },
        controller: {
            simpleHdfsFileBrowserController: {
                hdfsFileClose: 'onHdfsFileClose'
            }
        }
    },

    /**
     * 데이터 로드 버튼 클릭이벤트
     * 로컬데이터의 경우 데이터 업로드 후 Rdata변환
     * HDFS데이터의 경우 HDFS에서 데이터 직접 읽어서 Rdata변환
     * */
    onBtnLoadClick: function () {
        var me = this;
        var refs = me.getReferences();
        var values = refs.frmUpload.getForm().getValues();

        if (Ext.isEmpty(values.datafile) && Ext.isEmpty(refs.localfile.getValue())) {
            Ext.MessageBox.show({
                title: message.msg('common.check'),
                message: message.msg('visual.msg.choose_data_file'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.INFO
            });
            return;
        }

        refs.btnHdfsBrowse.setDisabled(true);
        refs.btnLocalBrowse.setDisabled(true);
        me.getView().setLoading(true);
        if (values.location == 'local') {
            var xhr = new XMLHttpRequest();
            var formData = new FormData();
            var options = {
                dataDelimiter: refs.cbxDelimiter.getValue(),
                dataHeader: refs.cbxHeader.getValue()
            }
            //formData.append("username", CONSTANTS.SESSION.USERNAME);
            formData.append('file', refs.localfile.file);
            formData.append('clusterName', ENGINE.id);
            formData.append('options', Ext.encode(options));
            formData.append('path', CONSTANTS.VISUAL.LOCAL_UPLOAD);

            var progressBar = Ext.create('Ext.ProgressBar').show();
            xhr.upload.addEventListener("progress", function (evt) {
                var percentComplete = Math.round(evt.loaded * 100 / evt.total);

                progressBar.updateProgress(percentComplete);
            }, false);

            xhr.addEventListener("load", function (evt) {
                var response = Ext.decode(evt.target.responseText);
                if (response.success) {
                    var r = Ext.decode(response.map.rData);
                    me.getViewModel().setData({dataFile: response.map.filename});
                    me.reconfigureGrid(r.output);
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.caution'),
                        message: response.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
                me.getView().setLoading(false);
            }, false);

            xhr.addEventListener("error", function (evt) {
                Ext.MessageBox.show({
                    title: message.msg('common.caution'),
                    message: message.msg('visual.msg.file_upload_error'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
                me.getView().setLoading(false);
            }, false);

            xhr.addEventListener("abort", function (evt) {
                Ext.MessageBox.show({
                    title: message.msg('common.caution'),
                    message: message.msg('visual.msg.file_upload_stop'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
                me.getView().setLoading(false);
            }, false);

            xhr.addEventListener("loadend", function (evt) {
                var response = Ext.decode(evt.target.responseText);
                if (evt.target.status != 200) {
                    if (response.error.message == message.msg('visual.msg.already_exists_file')) {
                        return;
                    }
                }
                me.getView().setLoading(false);
            }, false);

            xhr.open("POST", CONSTANTS.VISUAL.LOCAL_UPLOAD);
            xhr.send(formData);
        }
        else {
            var params = Ext.merge(values, {clusterName: ENGINE.id});
            invokePostByMap(CONSTANTS.VISUAL.LOAD_HDFS, params,
                function (response) {
                    var obj = Ext.decode(response.responseText);

                    if (obj.success) {
                        refs.btnImport.setDisabled(false);
                        me.getViewModel().setData({dataFile: obj.map.filename});
                        var r = Ext.decode(obj.map.rData);
                        me.reconfigureGrid(r.output);

                    } else if (obj.error.cause) {
                        Ext.MessageBox.show({
                            title: message.msg('common.error'),
                            message: obj.error.cause,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: message.msg('common.error'),
                            message: obj.error.message,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    }
                    me.getView().setLoading(false);
                },
                function (response) {
                    me.getView().setLoading(false);
                    Ext.MessageBox.show({
                        title: message.msg('common.warn'),
                        message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            );
        }
    },

    onBtnImportClick: function () {
        var me = this;
        var refs = me.getReferences();
        var values = refs.frmUpload.getForm().getValues();
        var header = refs.grdHeader.getStore().getAt(0);
        var rData = me.getViewModel().getData().rData;
        var columns = rData.variableNames;
        var variableList = [];
        var i;

        for (i = 0; i < columns.length; i++) {
            if (Ext.isEmpty(header.get(columns[i]))) {
                Ext.MessageBox.show({
                    title: message.msg('common.confirm'),
                    message: message.msg('visual.msg.enter_header'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.INFO
                });
                return;
            }
            else {
                variableList[i] = {header: rData.variableNames[i], mapHeader: header.get(columns[i])};
            }

            if (refs.cbxHeader.getValue()) {
                Ext.merge(header.data, Ext.decode('{' + header.get(columns[i]) + ': "' + header.get(columns[i]) + '"}'));
            }
        }
        var options = {
            dataDelimiter: refs.cbxDelimiter.getValue()
        };

        var params = {
            dataFile: values.location == 'local' ? me.getViewModel().getData().dataFile : refs.hdfsDatafile.getValue(),
            options: Ext.encode(options),
            header: refs.cbxHeader.getValue(),
            rData: rData,
            variableList: variableList,
            clusterName: ENGINE.id
        };

        var url;
        if (values.location == 'local') {
            url = CONSTANTS.VISUAL.LIST_VARIABLES_LOCAL;
        }
        else {
            url = CONSTANTS.VISUAL.LIST_VARIABLES_HDFS;
        }
        invokePostByMap(url, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    var r = Ext.decode(obj.map.listVariables);
                    var i, j;

                    for (i = 0; i < r.variablelist.length; i++) {
                        r.variablelist[i].text = header.get(columns[i]);
                    }

                    for (i = 0; i < r.variables.length; i++) {
                        var children = r.variables[i].children;
                        for (j = 0; j < children.length; j++) {
                            r.variables[i].children[j].text = header.get(r.variables[i].children[j].value);
                        }
                    }

                    me.fireEvent('setWorkspace', r);
                    //close window:
                    me.getView().close();
                } else if (obj.error.cause) {
                    Ext.MessageBox.show({
                        title: message.msg('common.error'),
                        message: obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.error'),
                        message: obj.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
            },
            function (response) {
                Ext.MessageBox.show({
                    title: message.msg('common.warn'),
                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
            }
        );
    },

    reconfigureGrid: function (output) {
        var me = this;
        var refs = this.getReferences();
        this.getViewModel().setData({rData: output});

        var columns = new Array();
        for (var i = 0; i < output.variableData[0].length; i++) {
            columns[i] = {
                header: output.variableNames[i],
                dataIndex: output.variableNames[i],
                editor: 'textfield'
            };
        }

        var newStore = new Ext.data.ArrayStore({
            fields: output.variableNames,
            data: output.variableData
            //idIndex: 0 // id for each record will be the first element
        });

        var headerStore = new Ext.data.ArrayStore({
            fields: output.variableNames
        });

        refs.grdGuess.reconfigure(newStore, columns);
        refs.grdHeader.reconfigure(headerStore, columns);
        refs.grdHeader.getStore().insert(0, refs.grdHeader.getStore().getModel());

        refs.rowPanel.setDisabled(false);
        refs.btnImport.setDisabled(false);

        if (refs.cbxHeader.getValue()) {
            me.onCbxHeaderSelect(refs.cbxHeader);
        }
    },

    onRadioChange: function (radio, newValue, oldValue, eOpts) {
        if (newValue) {
            var refs = this.getReferences();
            var me = this;

            refs.btnLocalBrowse.setDisabled(false);
            refs.btnHdfsBrowse.setDisabled(false);

            if (radio.inputValue == 'local') {
                refs.local.setVisible(true);
                refs.hdfs.setVisible(false);
                refs.hdfsDatafile.setValue(null);
            } else {
                refs.localfile.file = null;
                refs.localfile.reset();
                refs.local.setVisible(false);
                refs.hdfs.setVisible(true);
            }
            me.resetOption();
        }
    },

    resetOption: function () {
        var me = this;
        var refs = me.getReferences();

        refs.grdGuess.getStore().removeAll();
        refs.grdHeader.getStore().removeAll();
        refs.rowPanel.setDisabled(true);
        refs.cbxHeader.setValue(false);
        refs.cbxDelimiter.setValue(',');
    },

    /**
     * Delimiter combobox select Event
     *
     * */
    onCbxDelimiterSelect: function (combo, record) {
        var me = this;
        var refs = me.getReferences();
        var values = refs.frmUpload.getForm().getValues();
        var options = {
            dataDelimiter: refs.cbxDelimiter.getValue(),
            dataHeader: refs.cbxHeader.getValue()
        };
        var params = {
            location: values.location,
            dataFile: values.location == 'local' ? me.getViewModel().getData().dataFile : refs.hdfsDatafile.getValue(),
            clusterName: ENGINE.id,
            options: Ext.encode(options)
        };

        me.getView().setLoading(true);
        invokeGet(CONSTANTS.VISUAL.RELOAD_DATA, params,
            function (response) {
                var obj = Ext.decode(response.responseText);

                if (obj.success) {
                    var r = Ext.decode(obj.map.rData);
                    me.reconfigureGrid(r.output);
                } else if (obj.error.cause) {
                    Ext.MessageBox.show({
                        title: message.msg('common.error'),
                        message: obj.error.cause,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                } else {
                    Ext.MessageBox.show({
                        title: message.msg('common.error'),
                        message: obj.error.message,
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                }
                me.getView().setLoading(false);
            },
            function (response) {
                Ext.MessageBox.show({
                    title: message.msg('common.warn'),
                    message: format(message.msg('common.msg.server_error'), config['system.admin.email']),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
                me.getView().setLoading(false);
            }
        );
    },

    onHdfsBrowseClick: function () {
        Ext.create('Flamingo2.view.fs.hdfs.simple.SimpleHdfsFileBrowser').show();
    },

    onHdfsFileClose: function (record) {
        var refs = this.getReferences();

        refs.hdfsDatafile.setValue(record.get('id'));
    },

    onLocalFileChange: function (field, value) {
        var refs = this.getReferences();
        //refs.localfile.setValue(field.fileInputEl.dom.files[0].name);
        refs.localfile.file = field.fileInputEl.dom.files[0];
    },

    onCbxHeaderSelect: function (combo, record) {
        var refs = this.getReferences();
        var value = combo.getValue();
        if (value) {
            refs.grdHeader.getStore().removeAll();
            refs.grdHeader.getStore().insert(0, refs.grdGuess.getStore().getAt(0));
            refs.grdGuess.getStore().removeAt(0);
            refs.pnlHeader.setDisabled(true);
        } else {
            refs.grdGuess.getStore().insert(0, refs.grdHeader.getStore().getAt(0));
            refs.grdHeader.getStore().removeAt(0);
            refs.grdHeader.getStore().insert(0, refs.grdHeader.getStore().getModel());
            refs.pnlHeader.setDisabled(false);
        }
    }
});