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
Ext.define('Flamingo2.view.realtime.spark.streaming.SparkStreamingController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.sparkStreamingViewController',

    /**
     * Engine Combo Box Changed Event
     */
    onEngineChanged: function () {
        var me = this;
        me.onSparkStreamingGridAfterRender();
    },

    /**
     * IoT Service 목록을 가져온다.
     */
    onIoTServiceTreeAfterRender: function () {
        var ioTServiceTree = query('ioTServiceTree');

        setTimeout(function () {
            ioTServiceTree.getStore().proxy.extraParams.clusterName = ENGINE.id;
            ioTServiceTree.getStore().load({
                callback: function () {
                    var rootNode = ioTServiceTree.getStore().getNodeById('IoT');
                    ioTServiceTree.getRootNode().expand();
                    ioTServiceTree.getSelectionModel().select(rootNode);
                }
            });
        }, 200);
    },

    /**
     * IoT Service 목록을 갱신한다.
     */
    onIoTServiceTreeRefreshClick: function () {
        var ioTServiceTree = query('ioTServiceTree');

        setTimeout(function () {
            ioTServiceTree.getStore().proxy.extraParams.clusterName = ENGINE.id;
            ioTServiceTree.getStore().proxy.extraParams.depth = 0;
            ioTServiceTree.getStore().proxy.extraParams.node = 'IoT';
            ioTServiceTree.getStore().proxy.extraParams.serviceId = '';
            ioTServiceTree.getStore().proxy.extraParams.serviceTypeId = '';
            ioTServiceTree.getStore().proxy.extraParams.deviceTypeId = '';
            ioTServiceTree.getStore().load({
                callback: function () {
                    var rootNode = ioTServiceTree.getStore().getNodeById('IoT');
                    ioTServiceTree.getRootNode().expand();
                    ioTServiceTree.getSelectionModel().select(rootNode);
                }
            });
        }, 200);
    },

    /**
     * 등록된 Spark Streaming 애플리케이션 목록을 가져온다.
     */
    onSparkStreamingGridAfterRender: function () {
        var sparkStreamingManagementGrid = query('sparkStreaming #sparkStreamingManagementGrid');

        sparkStreamingManagementGrid.setLoading(true);
        setTimeout(function () {
            sparkStreamingManagementGrid.getStore().getProxy().extraParams.clusterName = ENGINE.id;
            sparkStreamingManagementGrid.getStore().load({
                callback: function (records, operation, success) {
                    if (success) {
                        sparkStreamingManagementGrid.setLoading(false);
                    } else {
                        sparkStreamingManagementGrid.setLoading(false);
                    }
                }
            });
        }, 10);
    },

    /**
     * Spark Streaming Summary Chart 정보를 가져온다
     */
    onAfterRenderSparkStreamingSumChart: function () {
        var sparkStreamingSumChart = query('sparkStreamingSumChart');

        Ext.defer(function () {
            sparkStreamingSumChart.getStore().proxy.extraParams = {
                clusterName: ENGINE.id,
                server: '',
                applicationId: '',
                startDate: new Date(),
                endDate: new Date()
            };
            sparkStreamingSumChart.getStore().load();
        }, 300);
    },

    /**
     * Spark Streaming Management 목록을 업데이트한다.
     */
    onSparkStreamingGridRefreshClick: function () {
        var me = this;

        me.onSparkStreamingGridAfterRender();
    },

    /**
     * 선택한 실행 중인 Spark Streaming 애플리케이션의 CPU 사용량을 업데이트한다.
     */
    onSparkStreamingSumChartRefreshClick: function () {
        var me = this;
        var sparkStreamingManagementGridGrid = query('sparkStreaming #sparkStreamingManagementGrid');
        var selectedItem = sparkStreamingManagementGridGrid.getSelectionModel().getSelection()[0];

        if (selectedItem) {
            me.getRunningSparkStreamingAppSummary(selectedItem);
        }
    },

    /**
     * IoT Tree 목록에서 노드의 좌측 확장 아이콘(+)을 클릭했을 때 선택한 노드에 대한 자식 노드의 정보를 가져온다
     */
    onBeforeIoTServiceTreeItemClick: function (view, record, item, index, e, eOpts) {
        var ioTServiceTree = query('ioTServiceTree');

        /**
         * IoT 서비스 목록에서 선택한 서비스에 해당하는 결과를 필터링 해서 UI로 전달해야 한다.
         *
         * Case 1. depth 0: serviceName | serviceId             ex) 스포츠 | 10000001
         * Case 2. depth 1: serviceTypeName | serviceTypeId     ex) 수영 | SW
         * Case 3. depth 2: deviceTypeName | deviceTypeId       ex) 수영속도센서 | DEV_TYPE_SW1
         */
        switch (record.data.depth) {
            case 0:
                ioTServiceTree.getStore().getProxy().extraParams.serviceId = record.data.serviceId;
                break;
            case 1:
                ioTServiceTree.getStore().getProxy().extraParams.serviceId = record.data.serviceId;
                break;
            case 2:
                ioTServiceTree.getStore().getProxy().extraParams.serviceId = record.data.serviceId;
                ioTServiceTree.getStore().getProxy().extraParams.serviceTypeId = record.data.serviceTypeId;
                break;
        }

        ioTServiceTree.getStore().getProxy().extraParams.clusterName = ENGINE.id;
        ioTServiceTree.getStore().getProxy().extraParams.depth = record.data.depth;
        ioTServiceTree.getStore().getProxy().extraParams.node = record.data.id;
    },

    /**
     * IoT 서비스 목록에서 선택한 서비스의 컬럼 정보를 가져온다.
     */
    onIoTServiceTreeItemClick: function () {
        var ioTServiceTree = query('ioTServiceTree');
        var selectedNode = ioTServiceTree.getSelectionModel().getLastSelected();
        var ioTServiceTab = query('ioTServiceTab');
        var activeTab = ioTServiceTab.getActiveTab();
        var depth = selectedNode.data.depth;

        if (depth == 3) {
            setTimeout(function () {
                activeTab.getStore().load({
                    params: {
                        clusterName: ENGINE.id,
                        node: selectedNode.data.serviceId,
                        depth: selectedNode.data.depth,
                        columnsType: activeTab.id,
                        serviceId: selectedNode.data.serviceId,
                        serviceTypeId: selectedNode.data.serviceTypeId,
                        deviceTypeId: selectedNode.data.deviceTypeId
                    }
                })
            }, 10);
        }
    },

    /**
     * IoT Service 목록에서 선택한 탭에 해당하는 서비스의 컬럼 정보를 가져온다
     */
    onIoTServiceTabChanged: function () {
        var me = this;
        me.onIoTServiceTreeItemClick();
    },

    /**
     * Spark Streaming 애플리케이션 등록창을 생성한다.
     */
    onApplicationRegisterClick: function () {
        Ext.create('Flamingo2.view.realtime.spark.steaming.management.ApplicationRegisterWindow').show();
    },

    /**
     * Spark Streaming 애플리케이션을 등록한다.
     */
    onApplicationRegisterOK: function () {
        var me = this;
        var refs = me.getReferences();
        var valueGrid = query('applicationRegisterForm #valueGrid');
        var variables = valueGrid.getStore().getData().items;
        var commandLineParams = [];
        var jarFile = refs.jarFile.getValue();

        for (var i = 0; i < variables.length; i++) {
            commandLineParams[i] = variables[i].data.variableValues + ':' + variables[i].data.variableDescriptions;
        }

        var url = CONSTANTS.REALTIME.SPARK.STREAMING.CREATE_SPARK_STREAMING_APP;
        var params = {
            clusterName: ENGINE.id,
            username: SESSION.USERNAME,
            jarFile: jarFile,
            applicationName: refs.applicationName.getValue(),
            streamingClass: refs.streamingClass.getValue(),
            javaOpts: refs.javaOpts.getValue(),
            variables: commandLineParams.join()
        };
        var appRegisterForm = refs.applicationRegisterForm.getForm();

        if (appRegisterForm.isValid()) {
            if (Ext.isEmpty(jarFile)) {
                appRegisterForm.submit({
                    url: url,
                    params: params,
                    waitMsg: '애플리케이션 등록 중...',
                    success: function (fp, o) {
                        Ext.Msg.alert('Success', 'Application has been registered.');

                        me.getView().close();
                        me.onSparkStreamingGridRefreshClick();
                    },
                    failure: function (fp, o) {
                        Ext.Msg.alert('Failed', '애플리케이션을 등록할 수 없습니다. cause: ' + o.result.error.cause);
                    }
                });
            } else {
                appRegisterForm.submit({
                    url: url,
                    params: params,
                    waitMsg: '파일 업로드 중...',
                    success: function (fp, o) {
                        Ext.Msg.alert('Success', 'Jar file has been uploaded.');

                        me.getView().close();
                        me.onSparkStreamingGridRefreshClick();
                    },
                    failure: function (fp, o) {
                        Ext.Msg.alert('Failed', '파일을 업로드할 수 없습니다. cause: ' + o.result.error.cause);
                    }
                });
            }
        }
    },

    /**
     * Spark Streaming 애플리케이션 등록창을 종료한다.
     */
    onApplicationRegisterCancel: function () {
        this.getView().close();
    },

    /**
     * 선택한 Spark Streaming 애플리케이션을 시작한다.
     */
    onApplicationStartClick: function () {
        var me = this;
        var sparkStreamingManagementGridGrid = query('sparkStreaming #sparkStreamingManagementGrid');
        var selectedItem = sparkStreamingManagementGridGrid.getSelectionModel().getSelection();

        if (selectedItem.length < 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: '시작할 애플리케이션을 선택하십시오.',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var url = CONSTANTS.REALTIME.SPARK.STREAMING.START_SPARK_STREAMING_APP;
        var params = {
            clusterName: ENGINE.id,
            server: selectedItem[0].get('server'),
            sparkUserWorkingPath: selectedItem[0].get('sparkUserWorkingPath'),
            jarFileFQP: selectedItem[0].get('jarFileFQP'),
            applicationId: selectedItem[0].get('applicationId'),
            applicationName: selectedItem[0].get('applicationName'),
            streamingClass: selectedItem[0].get('streamingClass'),
            javaOpts: selectedItem[0].get('javaOpts'),
            variables: selectedItem[0].get('variables')
        };

        Ext.MessageBox.show({
            title: message.msg('common.info'),
            message: '선택한 애플리케이션을 시작하시겠습니까?',
            width: 300,
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.INFO,
            scope: this,
            fn: function (btn, text, eOpts) {
                if (btn === 'yes') {
                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                info('Run Application', 'Running Spark Streaming Application');

                                me.onSparkStreamingGridAfterRender();
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 선택한 Spark Streaming 애플리케이션을 중지한다.
     */
    onApplicationStopClick: function () {
        var me = this;
        var sparkStreamingManagementGridGrid = query('sparkStreaming #sparkStreamingManagementGrid');
        var selectedItem = sparkStreamingManagementGridGrid.getSelectionModel().getSelection();
        var state = selectedItem[0].get('state');

        if (selectedItem.length < 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: '중지할 애플리케이션을 선택하십시오.',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        /*        if (state != 'RUNNING') {
         Ext.MessageBox.show({
         title: message.msg('common.notice'),
         message: '실행 중인 애플리케이션만 중지할 수 있습니다.',
         buttons: Ext.MessageBox.OK,
         icon: Ext.MessageBox.WARNING
         });
         return false;
         }*/

        var url = CONSTANTS.REALTIME.SPARK.STREAMING.STOP_SPARK_STREAMING_APP;
        var params = {
            clusterName: ENGINE.id,
            server: selectedItem[0].get('server'),
            sparkUserWorkingPath: selectedItem[0].get('sparkUserWorkingPath'),
            applicationId: selectedItem[0].get('applicationId'),
            state: state
        };

        Ext.MessageBox.show({
            title: message.msg('common.info'),
            message: '선택한 애플리케이션을 중지하시겠습니까?',
            width: 300,
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.INFO,
            scope: this,
            fn: function (btn, text, eOpts) {
                if (btn === 'yes') {
                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                info('Stop Application', 'Stopped Spark Streaming Application');

                                Ext.defer(function () {
                                    me.onSparkStreamingGridAfterRender();
                                }, 500);
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 선택한 Spark Streaming 애플리케이션을 종료 후 삭제한다.
     */
    onApplicationKillClick: function () {
        var me = this;
        var sparkStreamingManagementGridGrid = query('sparkStreaming #sparkStreamingManagementGrid');
        var selectedItem = sparkStreamingManagementGridGrid.getSelectionModel().getSelection();

        if (selectedItem.length < 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: '종료할 애플리케이션을 선택하십시오.',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        var url = CONSTANTS.REALTIME.SPARK.STREAMING.KILL_SPARK_STREAMING_APP;
        var params = {
            clusterName: ENGINE.id,
            server: selectedItem[0].get('server'),
            applicationId: selectedItem[0].get('applicationId'),
            sparkUserWorkingPath: selectedItem[0].get('sparkUserWorkingPath'),
            state: selectedItem[0].get('state')
        };
        var applicationName = selectedItem[0].get('applicationName');

        Ext.MessageBox.show({
            title: message.msg('common.info'),
            message: '선택한 애플리케이션이 실행 중일 경우 강제종료되고 해당 정보가 삭제됩니다. 선택한 애플리케이션을 종료하시겠습니까?',
            width: 300,
            buttons: Ext.MessageBox.YESNO,
            icon: Ext.MessageBox.INFO,
            scope: this,
            fn: function (btn, text, eOpts) {
                if (btn === 'yes') {
                    invokePostByMap(url, params,
                        function (response) {
                            var obj = Ext.decode(response.responseText);

                            if (obj.success) {
                                info('Kill Application', 'Killed Spark Streaming Application.');

                                me.onSparkStreamingGridAfterRender();
                            } else if (obj.error.cause) {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.cause,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            } else {
                                Ext.MessageBox.show({
                                    title: message.msg('common.notice'),
                                    message: obj.error.message,
                                    buttons: Ext.MessageBox.OK,
                                    icon: Ext.MessageBox.WARNING
                                });
                            }
                        },
                        function () {
                            Ext.MessageBox.show({
                                title: message.msg('common.warning'),
                                message: format(message.msg('common.failure'), config['system.admin.email']),
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    );
                }
            }
        });
    },

    /**
     * 선택한 Running 중인 Spark Streaming 애플리케이션의 CPU 사용량을 가져온다.
     */
    onSparkStreamingGridItemClick: function () {
        var me = this;
        var sparkStreamingManagementGridGrid = query('sparkStreaming #sparkStreamingManagementGrid');
        var selectedItem = sparkStreamingManagementGridGrid.getSelectionModel().getSelection()[0];

        me.getRunningSparkStreamingAppSummary(selectedItem);
    },

    /**
     * 실행 중인 Spark Streaming 애플리케이션의 Summary Chart 정보를 가져온다.
     */
    getRunningSparkStreamingAppSummary: function (selectedItem) {
        var sparkStreamingSumChart = query('sparkStreamingSumChart');

        Ext.defer(function () {
            sparkStreamingSumChart.getStore().proxy.extraParams = {
                clusterName: ENGINE.id,
                server: selectedItem.get('server'),
                applicationId: selectedItem.get('applicationId'),
                startDate: new Date(),
                endDate: new Date()
            };
            sparkStreamingSumChart.getStore().load();
        }, 300);
    },

    /**
     * 선택한 시작일 및 종료일에 해당하는 실행 중인 선택한 Spark Streaming 애플리케이션의 CPU 사용량을 가져온다.
     */
    onSparkStreamingSumChartFindClick: function () {
        var startDate = query('sparkStreamingSumChart #sparkStartDate').getValue();
        var endDate = query('sparkStreamingSumChart #sparkEndDate').getValue();
        var convertedStartDate = '';
        var convertedEndDate = '';

        /**
         * 1. 시작일 미입력
         * 2. 종료일 미입력
         * 3. 시작일 and 종료일 미입력 -> ex. 1970-01-01 09:00:00 ~ Current Time
         * 4. 시작일 종료일 입력
         */
        if (!startDate && endDate) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('hdfs.audit.date.start.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        } else if (startDate && !endDate) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: message.msg('hdfs.audit.date.end.select'),
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        } else if (!startDate && !endDate) {
            convertedStartDate = startDate;
            convertedEndDate = endDate;
        } else {
            var start = dateFormat(startDate, 'yyyy-MM-dd');
            var end = dateFormat(endDate, 'yyyy-MM-dd');

            // 시작일과 종료일 당일 일때 -> ex. 2015.05.05 00:00:00 ~ Current Time
            /**
             * 4.1 시작일과 종료일이 같을 때
             *  4.1.1 시작일과 종료일이 오늘일 때 -> ex. 2015.05.05 00:00:00 ~ Current Time
             *  4.1.2 시작일과 종료일이 특정 날짜일 때 -> ex. 2015.05.05 00:00:00 ~ 2015.05.05 23:59:59
             * 4.2 시작일이 종료일보다 빠를 때
             * 4.3 시작일이 종료일보다 늦을 때
             */
            if (start == end) {
                var today = dateFormat(new Date(), 'yyyy-MM-dd');

                convertedStartDate = startDate;
                convertedEndDate = start == today ? new Date() : new Date(endDate.setHours(23, 59, 59, 999));
            } else if (start > end) {
                Ext.MessageBox.show({
                    title: message.msg('common.notice'),
                    message: message.msg('hdfs.audit.date.start.msg'),
                    buttons: Ext.MessageBox.OK,
                    icon: Ext.MessageBox.WARNING
                });
                return false;
            } else {
                convertedStartDate = startDate;
                convertedEndDate = endDate;
            }
        }

        var sparkStreamingManagementGridGrid = query('sparkStreaming #sparkStreamingManagementGrid');
        var selectedItem = sparkStreamingManagementGridGrid.getSelectionModel().getSelection();
        var sparkStreamingSumChart = query('sparkStreamingSumChart');

        if (selectedItem.length < 1) {
            Ext.MessageBox.show({
                title: message.msg('common.notice'),
                message: '조회할 애플리케이션을 관리 목록에서 선택하십시오.',
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
            return false;
        }

        Ext.defer(function () {
            sparkStreamingSumChart.getStore().proxy.extraParams = {
                clusterName: ENGINE.id,
                server: selectedItem[0].get('server'),
                applicationId: selectedItem[0].get('applicationId'),
                startDate: convertedStartDate,
                endDate: convertedEndDate
            };
            sparkStreamingSumChart.getStore().load();
        }, 300);
    },

    /**
     * 현재 날짜를 기준으로 해당되는 달의 첫번째 날짜 정보를 가져온다.
     *
     * @returns {Date}
     */
    onGetFirstDay: function () {
        var today = new Date();
        return new Date(today.getFullYear(), today.getMonth(), 1);
    }
});