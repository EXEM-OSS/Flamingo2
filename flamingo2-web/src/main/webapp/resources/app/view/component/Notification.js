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
Ext.define('Flamingo2.view.component.Notification', {
    extend: 'Ext.container.Container',
    alias: 'widget.progressnoti',
    id: 'Notification',
    controller: 'notification',
    layout: 'anchor',
    requires: [
        'Flamingo2.view.component.ProgressDetail',
        'Flamingo2.view.component.NotificationController'
    ],
    items: [{
        xtype: 'dataview',
        id: 'grdNotification',
        anchor: '100%',
        style: {
            maxHeight: '190px',
            overflowY: 'auto'
        },
        tplWriteMode: 'insertBefore',
        itemSelector: 'list-group-item',
        store: Ext.create('Ext.data.Store', {
            storeId: 'progressnoti',
            fields: ['id', 'title', 'type', 'cnt'],
            listeners: {
                add: function (store) {
                    $('#badge-count').text(store.getCount());
                },
                remove: function (store) {
                    if (store.getCount() == 0) {
                        $('#badge-count').text('');
                    }
                    else {
                        $('#badge-count').text(store.getCount());
                    }
                },
                clear: function (store) {
                    if (store.getCount() == 0) {
                        $('#badge-count').text('');
                    }
                    else {
                        $('#badge-count').text(store.getCount());
                    }
                }
            }
        }),
        tpl: [
            '<ul class="list-notification list-group">',
            '<tpl for=".">',
            '<tpl if="type == \'progress\'">',
            '<li class="list-group-item">',
            '<div class="progress-title"><span class="sr-only">{title}</span></div>',
            '<div class="progress progress-mini">',
            '<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 100%">',
            '</div>',
            '</li>',
            '</tpl>',
            '<tpl if="type != \'progress\'">',
            '<li class="list-group-item">',
            '<a href="#">',
            '<div class="clearfix">',
            '<span class="pull-left"><i class="btn btn-xs no-hover btn-pink fa fa-exclamation-triangle"></i>{title}</span>',
            '<span class="pull-right badge badge-info">{cnt}</span>',
            '</div>',
            '</a>',
            '</li>',
            '</tpl>',
            '</tpl>',
            '</ul>'
        ],
        listeners: {
            /*            viewready: function (panel) {
             $('#progress-detail').on('click', function () {
             panel.fireEvent('detailClick', panel, this);
             });
             },
             detailClick: function (panel, item) {
             Ext.create('Flamingo2.view.component.ProgressDetail').show();
             },*/
            select: function () {
                console.debug('1');
            },
            itemclick: function () {
                console.debug('click');
            }
        }
    }/*, {
     xtype: 'component',
     html: [
     '<li class="dropdown-footer"><a id="progress-detail" href="#">상세보기 <i class="ace-icon fa fa-arrow-right"></i></a></li>'
     ]
     }*/],

    addNoti: function (id, title, type, cnt) {
        this.items.items[0].getStore().insert(0, {id: id, title: title, type: type, cnt: cnt});
    },

    removeNoti: function (id) {
        var view = this.items.items[0];
        var store = view.getStore();

        store.each(function (record) {
            if (record.get('id') == id) {
                store.remove(record);
                view.refresh();
            }
        });
    },

    merge: function (id, title, type, cnt) {
        var view = this.items.items[0];
        var store = view.getStore();
        var row = store.find('id', id)

        if (row < 0) {
            store.insert(0, {id: id, title: title, type: type, cnt: cnt});
        }
        else {
            var record = store.getAt(row);
            record.set('cnt', cnt);
        }
    }
}, function () {
    Flamingo2.notification = {};
    Flamingo2.notification.update = function (id, title) {
        invokeGet(CONSTANTS.USER.PREFERENCE.EVENT.LIST, {},
            function (r) {
                var res = Ext.decode(r.responseText);
                if (res.success) {
                    Flamingo2.progress.removeAll();
                    Ext.each(res.list, function (event) {
                        Flamingo2.progress.add(event.identifier, event.name);
                    });
                }
            }, function (r) {
            }
        );
    };

    Flamingo2.notification.add = function (id, title, type, cnt) {
        Ext.getCmp('Notification').addNoti(id, title, type, cnt);
    };

    Flamingo2.notification.merge = function (id, title, type, cnt) {
        Ext.getCmp('Notification').merge(id, title, type, cnt);
    };

    Flamingo2.notification.remove = function (id) {
        Ext.getCmp('Notification').removeNoti(id);
    };

    Flamingo2.notification.removeAll = function () {
        try {
            Ext.getCmp('Notification').items.items[0].getStore().removeAll();
            Ext.getCmp('Notification').items.items[0].refresh();
        } catch (e) {
        }
    };

    Flamingo2.notification.getStore = function () {
        return Ext.getCmp('Notification').getStore();
    };
});
