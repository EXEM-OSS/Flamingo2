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
Ext.define('Flamingo2.view.designer.ux.Toast', {
    extend: 'Ext.window.Window',
    alias: 'widget.designerToast',

    requires: [
        'Flamingo2.view.component.ProgressBar',
        'Flamingo2.view.designer.editor.AceEditor'
    ],

    autoClose: true,
    autoHeight: true,
    plain: false,
    draggable: false,
    shadow: false,
    focus: Ext.emptyFn,

    // For alignment and to store array of rendered notifications. Defaults to document if not set.
    manager: null,

    useXAxis: false,

    // Options: br, bl, tr, tl, t, l, b, r
    position: 'br',

    // Pixels between each notification
    spacing: 6,

    // Pixels from the managers borders to start the first notification
    paddingX: 30,
    paddingY: 10,

    slideInAnimation: 'easeIn',
    slideBackAnimation: 'bounceOut',
    slideInDuration: 1500,
    slideBackDuration: 1000,
    hideDuration: 500,
    autoCloseDelay: 7000,
    stickOnClick: true,
    stickWhileHover: true,

    // Private. Do not override!
    isHiding: false,
    readyToHide: false,
    destroyAfterHide: false,
    closeOnMouseOut: false,

    running: true,

    // Caching coordinates to be able to align to final position of siblings being animated
    xPos: 0,
    yPos: 0,

    statics: {
        defaultManager: {
            el: null
        }
    },

    initComponent: function () {
        var me = this;
        this.runner = new Ext.util.TaskRunner();
        this.task = {
            run: function () {
                if (me.running) {
                    var win = me;
                    var progress = me.down('#progressbar');
                    invokePostByMap(CONSTANTS.DESIGNER.STATUS, {clusterName: ENGINE.id, jobId: me.jobId},
                        function (response) {
                            var res = Ext.decode(response.responseText);
                            if (res.success) {
                                if (res.object) {
                                    if (res.object.status == 'FINISHED') {
                                        win.running = false;
                                        progress.status = 'FINISHED';
                                        progress.updateProgress(1, message.msg('workflow.msg_toast_success'));
                                        win.down('#showButton').enable();
                                    } else if (res.object.status == 'FAILED') {
                                        win.running = false;
                                        progress.status = 'FAILED';
                                        progress.updateProgress(1, message.msg('workflow.msg_toast_fail'));
                                        win.down('#showButton').enable();
                                    } else {
                                        progress.status = 'RUNNING';
                                        progress.progress = res.object.current / res.object.steps;
                                        progress.updateProgress(res.object.current / res.object.steps, res.object.current + '/' + res.object.steps + ' ' + message.msg('workflow.msg_toast_run'));
                                    }
                                } else {
                                    progress.status = 'WAITING';
                                    progress.progress = 1;
                                    progress.updateProgress(1, message.msg('workflow.msg_toast_wait'));
                                }
                            } else if (!res.success) {
                                win.running = false;
                                progress.status = 'FAILED';
                                progress.updateProgress(1, message.msg('workflow.msg_toast_fail'));
                            }
                        },
                        function (response) {
                            progress.status = 'FAILED';
                            progress.updateProgress(1, message.msg('workflow.msg_toast_fail'));
                            win.down('#showButton').enable();
                        }
                    );
                } else {
                    if (progress)
                        progress.updateProgress(progress.progress, message.msg('workflow.msg_toast_success'));
                }
            },
            interval: 1000
        };

        Ext.apply(me, {
            items: [
                {
                    xtype: 'form',
                    bodyPadding: '2',
                    defaults: {
                        xtype: 'textfield',
                        anchor: '100%'
                    },
                    items: [
                        {
                            xtype: 'progressBarCustom',
                            flex: 1,
                            border: 1,
                            progress: 0,
                            min: 0.30,
                            ave: 0.50,
                            max: 0.90,
                            itemId: 'progressbar'
                        },
                        {
                            xtype: 'button',
                            disabled: true,
                            margin: '2 0 0 0',
                            flex: 1,
                            text: message.msg('workflow.title_show_exec_log'),
                            itemId: 'showButton',
                            handler: function () {
                                var win = this.up('workflowToast');
                                var jobId = win.jobId;
                                win.close();

                                var popup = Ext.create('Ext.Window', {
                                    title: message.msg('workflow.title_exec_log'),
                                    width: 850,
                                    height: 500,
                                    layout: 'fit',
                                    maximizable: true,
                                    items: {
                                        xtype: 'workflowDetail',
                                        jobId: win.jobId
                                    },
                                    listeners: {
                                        resize: function (win, width, height, eOpts) {
                                        }
                                    }
                                }).center().show();
                            }
                        }
                    ]
                }
            ]
        });

        // Backwards compatibility
        if (Ext.isDefined(me.corner)) {
            me.position = me.corner;
        }
        if (Ext.isDefined(me.slideDownAnimation)) {
            me.slideBackAnimation = me.slideDownAnimation;
        }
        if (Ext.isDefined(me.autoDestroyDelay)) {
            me.autoCloseDelay = me.autoDestroyDelay;
        }
        if (Ext.isDefined(me.autoHideDelay)) {
            me.autoCloseDelay = me.autoHideDelay;
        }
        if (Ext.isDefined(me.autoHide)) {
            me.autoClose = me.autoHide;
        }
        if (Ext.isDefined(me.slideInDelay)) {
            me.slideInDuration = me.slideInDelay;
        }
        if (Ext.isDefined(me.slideDownDelay)) {
            me.slideBackDuration = me.slideDownDelay;
        }
        if (Ext.isDefined(me.fadeDelay)) {
            me.hideDuration = me.fadeDelay;
        }

        // 'bc', lc', 'rc', 'tc' compatibility
        me.position = me.position.replace(/c/, '');

        me.updateAlignment(me.position);

        me.setManager(me.manager);

        me.callParent(arguments);
    },

    onRender: function () {
        var me = this;

        me.el.hover(
            function () {
                me.mouseIsOver = true;
            },
            function () {
                me.mouseIsOver = false;
                if (me.closeOnMouseOut) {
                    me.closeOnMouseOut = false;
                    me.close();
                }
            },
            me
        );

        this.callParent(arguments);

    },

    updateAlignment: function (position) {
        var me = this;

        switch (position) {
            case 'br':
                me.paddingFactorX = -1;
                me.paddingFactorY = -1;
                me.siblingAlignment = "br-br";
                if (me.useXAxis) {
                    me.managerAlignment = "bl-br";
                } else {
                    me.managerAlignment = "tr-br";
                }
                break;
            case 'bl':
                me.paddingFactorX = 1;
                me.paddingFactorY = -1;
                me.siblingAlignment = "bl-bl";
                if (me.useXAxis) {
                    me.managerAlignment = "br-bl";
                } else {
                    me.managerAlignment = "tl-bl";
                }
                break;
            case 'tr':
                me.paddingFactorX = -1;
                me.paddingFactorY = -5; // 속도
                me.siblingAlignment = "tr-tr";
                if (me.useXAxis) {
                    me.managerAlignment = "tl-tr";
                } else {
                    me.managerAlignment = "br-tr";
                }
                break;
            case 'tl':
                me.paddingFactorX = 1;
                me.paddingFactorY = 1;
                me.siblingAlignment = "tl-tl";
                if (me.useXAxis) {
                    me.managerAlignment = "tr-tl";
                } else {
                    me.managerAlignment = "bl-tl";
                }
                break;
            case 'b':
                me.paddingFactorX = 0;
                me.paddingFactorY = -1;
                me.siblingAlignment = "b-b";
                me.useXAxis = 0;
                me.managerAlignment = "t-b";
                break;
            case 't':
                me.paddingFactorX = 0;
                me.paddingFactorY = 1;
                me.siblingAlignment = "t-t";
                me.useXAxis = 0;
                me.managerAlignment = "b-t";
                break;
            case 'l':
                me.paddingFactorX = 1;
                me.paddingFactorY = 0;
                me.siblingAlignment = "l-l";
                me.useXAxis = 1;
                me.managerAlignment = "r-l";
                break;
            case 'r':
                me.paddingFactorX = -1;
                me.paddingFactorY = 0;
                me.siblingAlignment = "r-r";
                me.useXAxis = 1;
                me.managerAlignment = "l-r";
                break;
        }
    },

    getXposAlignedToManager: function () {
        var me = this;

        var xPos = 0;

        // Avoid error messages if the manager does not have a dom element
        if (me.manager && me.manager.el && me.manager.el.dom) {
            if (!me.useXAxis) {
                // Element should already be aligned verticaly
                return me.el.getLeft();
            } else {
                // Using getAnchorXY instead of getTop/getBottom should give a correct placement when document is used
                // as the manager but is still 0 px high. Before rendering the viewport.
                if (me.position == 'br' || me.position == 'tr' || me.position == 'r') {
                    xPos += me.manager.el.getAnchorXY('r')[0];
                    xPos -= (me.el.getWidth() + me.paddingX);
                } else {
                    xPos += me.manager.el.getAnchorXY('l')[0];
                    xPos += me.paddingX;
                }
            }
        }

        return xPos;
    },

    getYposAlignedToManager: function () {
        var me = this;

        var yPos = 0;

        // Avoid error messages if the manager does not have a dom element
        if (me.manager && me.manager.el && me.manager.el.dom) {
            if (me.useXAxis) {
                // Element should already be aligned horizontaly
                return me.el.getTop();
            } else {
                // Using getAnchorXY instead of getTop/getBottom should give a correct placement when document is used
                // as the manager but is still 0 px high. Before rendering the viewport.
                if (me.position == 'br' || me.position == 'bl' || me.position == 'b') {
                    yPos += me.manager.el.getAnchorXY('b')[1];
                    yPos -= (me.el.getHeight() + me.paddingY);
                } else {
                    yPos += me.manager.el.getAnchorXY('t')[1];
                    yPos += me.paddingY + 130; // Y축 위치 변경
                }
            }
        }

        return yPos;
    },

    getXposAlignedToSibling: function (sibling) {
        var me = this;

        if (me.useXAxis) {
            if (me.position == 'tl' || me.position == 'bl' || me.position == 'l') {
                // Using sibling's width when adding
                return (sibling.xPos + sibling.el.getWidth() + sibling.spacing);
            } else {
                // Using own width when subtracting
                return (sibling.xPos - me.el.getWidth() - me.spacing);
            }
        } else {
            return me.el.getLeft();
        }

    },

    getYposAlignedToSibling: function (sibling) {
        var me = this;

        if (me.useXAxis) {
            return me.el.getTop();
        } else {
            if (me.position == 'tr' || me.position == 'tl' || me.position == 't') {
                // Using sibling's width when adding
                return (sibling.yPos + sibling.el.getHeight() + sibling.spacing);
            } else {
                // Using own width when subtracting
                return (sibling.yPos - me.el.getHeight() - sibling.spacing);
            }
        }
    },

    getNotifications: function (alignment) {
        var me = this;

        if (!me.manager.notifications[alignment]) {
            me.manager.notifications[alignment] = [];
        }

        return me.manager.notifications[alignment];
    },

    setManager: function (manager) {
        var me = this;

        me.manager = manager;

        if (typeof me.manager == 'string') {
            me.manager = Ext.getCmp(me.manager);
        }

        // If no manager is provided or found, then the static object is used and the el property pointed to the body document.
        if (!me.manager) {
            me.manager = me.statics().defaultManager;

            if (!me.manager.el) {
                me.manager.el = Ext.getBody();
            }
        }

        if (typeof me.manager.notifications == 'undefined') {
            me.manager.notifications = {};
        }
    },

    beforeShow: function () {
        var me = this;

        if (me.stickOnClick) {
            if (me.body && me.body.dom) {
                Ext.fly(me.body.dom).on('click', function () {
                    me.cancelAutoClose();
                    me.addCls('notification-fixed');
                }, me);
            }
        }

        if (me.autoClose) {
            me.task = new Ext.util.DelayedTask(me.doAutoClose, me);
            me.task.delay(me.autoCloseDelay);
        }

        // Shunting offscreen to avoid flicker
        me.el.setX(-10000);
        me.el.setOpacity(1);

    },

    afterShow: function () {
        var me = this;

        var notifications = me.getNotifications(me.managerAlignment);

        if (notifications.length) {
            me.el.alignTo(notifications[notifications.length - 1].el, me.siblingAlignment, [0, 0]);
            me.xPos = me.getXposAlignedToSibling(notifications[notifications.length - 1]);
            me.yPos = me.getYposAlignedToSibling(notifications[notifications.length - 1]);
        } else {
            me.el.alignTo(me.manager.el, me.managerAlignment, [(me.paddingX * me.paddingFactorX), (me.paddingY * me.paddingFactorY)], false);
            me.xPos = me.getXposAlignedToManager();
            me.yPos = me.getYposAlignedToManager();
        }

        Ext.Array.include(notifications, me);

        me.el.animate({
            to: {
                x: me.xPos,
                y: me.yPos,
                opacity: 1
            },
            easing: me.slideInAnimation,
            duration: me.slideInDuration,
            dynamic: true
        });

        this.callParent(arguments);
    },

    slideBack: function () {
        var me = this;

        var notifications = me.getNotifications(me.managerAlignment);
        var index = Ext.Array.indexOf(notifications, me)

        // Not animating the element if it already started to hide itself or if the manager is not present in the dom
        if (!me.isHiding && me.el && me.manager && me.manager.el && me.manager.el.dom && me.manager.el.isVisible()) {

            if (index) {
                me.xPos = me.getXposAlignedToSibling(notifications[index - 1]);
                me.yPos = me.getYposAlignedToSibling(notifications[index - 1]);
            } else {
                me.xPos = me.getXposAlignedToManager();
                me.yPos = me.getYposAlignedToManager();
            }

            me.stopAnimation();

            me.el.animate({
                to: {
                    x: me.xPos,
                    y: me.yPos
                },
                easing: me.slideBackAnimation,
                duration: me.slideBackDuration,
                dynamic: true
            });
        }
    },

    cancelAutoClose: function () {
        var me = this;

        if (me.autoClose) {
            me.task.cancel();
        }
    },

    doAutoClose: function () {
        var me = this;

        if (!(me.stickWhileHover && me.mouseIsOver)) {
            // Close immediately
            me.close();
        } else {
            // Delayed closing when mouse leaves the component.
            me.closeOnMouseOut = true;
        }
    },

    removeFromManager: function () {
        var me = this;

        if (me.manager) {
            var notifications = me.getNotifications(me.managerAlignment);
            var index = Ext.Array.indexOf(notifications, me);
            if (index != -1) {
                Ext.Array.erase(notifications, index, 1);

                // Slide "down" all notifications "above" the hidden one
                for (; index < notifications.length; index++) {
                    notifications[index].slideBack();
                }
            }
        }
    },

    hide: function () {
        var me = this;

        // Avoids restarting the last animation on an element already underway with its hide animation
        if (!me.isHiding && me.el) {

            me.isHiding = true;

            me.cancelAutoClose();
            me.stopAnimation();

            me.el.animate({
                to: {
                    opacity: 0
                },
                easing: 'easeIn',
                duration: me.hideDuration,
                dynamic: false,
                listeners: {
                    afteranimate: function () {
                        me.removeFromManager();
                        me.readyToHide = true;
                        me.hide(me.animateTarget, me.doClose, me);
                    }
                }
            });
        }

        // Calling parent's hide function to complete hiding
        if (me.readyToHide) {
            me.isHiding = false;
            me.readyToHide = false;
            me.removeCls('notification-fixed');
            me.callParent(arguments);
            if (me.destroyAfterHide) {
                me.destroy();
            }
        }
    },

    destroy: function () {
        var me = this;

        if (!me.hidden) {
            me.destroyAfterHide = true;
            me.hide(me.animateTarget, me.doClose, me);
        } else {
            me.callParent(arguments);
        }
    },

    listeners: {
        render: function (comp) {
            comp.runner.start(comp.task);
        },
        beforeclose: function (win) {
            // user has already answered yes
            if (win.closeMe) {
                win.closeMe = false;
                return true;
            }

            win.closeMe = true;
            win.close();
            return true;
        },
        destroy: function (comp) {
            comp.runner.stop(comp.task);
        }
    }

});
