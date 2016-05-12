/*
 * Copyright (C) 2011  Flamingo Project (http://www.opencloudengine.org).
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
 * 기본 View Panel
 * 헤더 텍스트 및 네비 텍스트 표시
 * */
Ext.define('Flamingo2.panel.Panel', {
    extend: 'Ext.panel.Panel',
    border: false,
    bodyBorder: false,
    margin: '0 5',
    bodyStyle: {
        background: '#ffffff'
    },

    listeners: {
        beforerender: function(panel) {
            var header, title;

            if (LICENSE.TRIAL == 'true') {
                title = '<h1 style="display: inline">{title}</h1><p style="display: inline;color: red;">Trial Version</p>'
            }
            else {
                title = '<h1>{title}</h1>'
            }

            if (this.layout.type == 'border') {
                header = Ext.create('Ext.container.Container', {
                    region: 'north',
                    itemId: 'page-header',
                    height: 45,
                    margin: '0 0 10 0',
                    cls: 'page-header',
                    bind: {
                        html: title
                    }

                });

                panel.add(header);
            }
            else {
                header = Ext.create('Ext.container.Container', {
                    margin: '0 0 10 0',
                    height: 45,
                    itemId: 'page-header',
                    cls: 'page-header',
                    bind: {
                        html: title
                    }
                });

                panel.insert(0, header);
            }
        }
    }
});