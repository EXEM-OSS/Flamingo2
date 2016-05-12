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
Ext.define('Flamingo2.view.main.Main', {
    extend: 'Ext.container.Container',
    requires: [
        'Ext.chart.*',
        'Ext.data.*',
        'Ext.draw.*',
        'Ext.form.*',
        'Ext.grid.*',
        'Ext.tree.*',
        'Ext.toolbar.*',
        'Ext.util.*',
        'Ext.scroll.Scroller',
        'Flamingo2.*'
    ],

    xtype: 'app-main',

    reference: 'viewMain',

    controller: 'main',

    viewModel: {
        type: 'main'
    },

    listeners: {
        afterrender: 'onAfterrender'
    },

    layout: {
        type: 'border'
    },

    items: [{
        xtype: 'container',
        region: 'north',
        height: 45,
        style: {
            backgroundColor: '#2F4050',
            borderBottom: '1px solid #29343f'
        },
        html: [
            '<div class="north-header">',
            '<nav class="navbar navbar-default">',
            '<div class="navbar-header">',
            '<a class="navbar-brand" href="/main">' + config['application.title'] + '</a>',
            '</div>',
            '<ul class="nav navbar-nav navbar-right">',
            '<li>',
            '<div class="engine-selector"><select id="engineSelector" name="myselect" data-placeholder="Hadoop Cluster" class="chosen-select form-control"></select></div>',
            '</li>',
            '<li class="dropdown">',
            '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i style="margin-right: 5px;" class="fa fa-user fa-lg"></i>' + SESSION.NAME + ' <span class="caret"></span></a>',
            '<ul class="dropdown-menu" role="menu">',
            '<li><a id="logout" href="#"><i class="fa fa-sign-out fa-lg"></i>&nbsp;&nbsp;' + message.msg('setting.menu.logout') + '</a></li>',
            '<li><a id="setting" href="#"><i class="fa fa-cog fa-lg"></i>&nbsp;&nbsp;' + message.msg('setting.menu.setting') + '</a></li>',
            '</ul>',
            '</li>',
            '<li class="dropdown">',
            '<a id="notification" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-bell fa-lg"></i> <span class="badge" id="badge-count"></span></a>',
            '<ul class="dropdown-notification dropdown-menu" role="menu">',
            '<div id="notification-grid"></div>',
            '</ul>',
            '</li>',
            '<li class="dropdown">',
            '<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false" title="' + message.msg('common.help') + '"><i style="margin-right: 5px;" class="fa fa-question-circle fa-lg"></i></a>',
            '<ul class="dropdown-menu" role="menu">',
            '<li><a id="help" href="#"><i class="fa fa-book"></i>&nbsp;&nbsp;' + config['application.title'] + ' ' + message.msg('common.help') + '</a></li>',
            '<li><a id="hadoop" href="https://developer.yahoo.com/hadoop/tutorial/" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;Yahoo! Hadoop Tutorial</a></li>',
            '<li><a id="hive" href="https://cwiki.apache.org/confluence/display/Hive/Home" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;Apache Hive</a></li>',
            '<li><a id="hivegetting" href="https://cwiki.apache.org/confluence/display/Hive/GettingStarted" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;Apache Hive - Getting Started</a></li>',
            '<li><a id="pig" href="http://pig.apache.org/docs/r0.15.0" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;Apache Pig</a></li>',
            '<li><a id="spark" href="https://spark.apache.org/documentation.html" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;Apache Spark</a></li>',
            '<li><a id="cdh" href="http://www.cloudera.com/content/cloudera/en/documentation.html" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;Cloudera CDH</a></li>',
            '<li><a id="hdp" href="http://docs.hortonworks.com/HDPDocuments/HDP2/HDP-2.2.6/index.html" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;Hortonworks HDP</a></li>',
            '<li><a id="phd" href="http://pivotalhd.docs.pivotal.io/" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;Pivotal HD</a></li>',
            '<li><a id="phd" href="http://doc.mapr.com/display/MapR/Home" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;MapR</a></li>',
            '<li><a id="hawq" href="http://hawq.docs.pivotal.io/" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;Pivotal HAWQ</a></li>',
            '<li><a id="r" href="http://cran.r-project.org/manuals.html" target="_blank"><i class="fa fa-bookmark"></i>&nbsp;&nbsp;R</a></li>',
            '</ul>',
            '</li>',
            '<li class=""><a id="about" href="#" title="' + message.msg('common.about') + '"><i class="fa fa-exclamation-circle fa-lg"></i></a></li>',
            '<li class=""><a id="external" href="#" title="' + config['external.name'] + '"><i class="fa fa-cube fa-lg"></i></a></li>',
            '</ul>',
            '</nav>',
            '</div>'
        ]
    }, {
        xtype: 'dataview',
        region: 'west',
        width: 220,
        layout: 'fit',
        reference: 'pnlWest',
        itemId: 'pnlWest',
        itemSelector: 'selector',
        border: false,
        bind: {
            store: '{menu}'
        },
        cls: 'no-skin skin-3',
        tpl: [
            '<div id="sidebar" class="sidebar" style="width: 100%">',
            '<ul class="nav nav-list">',
            '<tpl for=".">',
            '<tpl if="leaf == true">',
            '<li>',
            '<a href="#" id="{menu_id}" alt="{menu_ns}" class="selector"><i class="menu-icon fa {icon_css_nm}"></i><span class="menu-text"> {menu_nm} </span></a><b class="arrow"></b>',
            '</li>',
            '</tpl>',
            '<tpl if="leaf != true">',
            '<li class="">',
            '<a href="#" class="dropdown-toggle"><i class="menu-icon fa {icon_css_nm}"></i><span class="menu-text"> {menu_nm} </span><b class="arrow fa fa-angle-down"></b></a><b class="arrow"></b>',
            '<ul class="submenu">',
            '<tpl for="children">',
            '<li class="">',
            '<a href="#" id="{menu_id}" alt="{menu_ns}" class="selector"><i class="menu-icon fa fa-caret-right"></i> {menu_nm} </a><b class="arrow"></b>',
            '</li>',
            '</tpl>',
            '</ul>',
            '</li>',
            '</tpl>',
            '</tpl>',
            '</ul>',
            '<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">',
            '<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>',
            '</div>',
            '<script type="text/javascript">',
            'try{ace.settings.check("sidebar" , "collapsed")}catch(e){}',
            '</script>',
            '</div>'
        ],
        listeners: {
            viewready: 'onMenuViewready'
        }

    }, {
        region: 'center',
        xtype: 'panel',
        reference: 'pnlCenter',
        itemId: 'pnlCenter',
        layout: {
            type: 'vbox',
            align: 'stretch'
        },
        style: {
            background: '#FFFFFF'
        },
        defaults: {
            padding: '5 5 5 5'
        },
        scrollable: true
    }, {
        region: 'south',
        xtype: 'component',
        height: 15,
        bind: {
            html: '{license}'
        }
    }]
});
