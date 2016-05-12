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
Ext.define('Flamingo2.view.hive.editor.ResultSearchGridPanel', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.resultSearchGridPanel',
    requires: [
        'Ext.toolbar.TextItem',
        'Ext.form.field.Checkbox',
        'Ext.form.field.Text',
        'Flamingo2.view.component.StatusBar'
    ],

    /**
     * @private
     * search value initialization
     */
    searchValue: null,

    /**
     * @private
     * The row indexes where matching strings are found. (used by previous and next buttons)
     */
    indexes: [],

    /**
     * @private
     * The row index of the first search, it could change if next or previous buttons are used.
     */
    currentIndex: null,

    /**
     * @private
     * The generated regular expression used for searching.
     */
    searchRegExp: null,

    /**
     * @private
     * Case sensitive mode.
     */
    caseSensitive: false,

    /**
     * @private
     * Regular expression mode.
     */
    regExpMode: false,

    /**
     * @cfg {String} matchCls
     * The matched string css classe.
     */
    matchCls: 'x-livesearch-match',

    defaultStatusText: message.msg('common.resultgrid.defaultstatustext'),


    // Component initialization override: adds the top and bottom toolbars and setup headers renderer.
    initComponent: function () {
        var me = this;

        Ext.apply(me, {
            selModel: {
                type: 'spreadsheet',
                columnSelect: true
            },
            viewConfig: {
                columnLines: true,
                trackOver: false
            },
            plugins: 'clipboard',
            tbar: [
                message.msg('common.search'),
                { // Search
                    xtype: 'textfield',
                    itemId: 'searchField',
                    name: 'searchField',
                    hideLabel: true,
                    flex: 1,
                    minWidth: 100,
                    listeners: {
                        change: {
                            fn: me.onTextFieldChange,
                            scope: this,
                            buffer: 500
                        }
                    }
                },
                {
                    xtype: 'button',
                    iconCls: 'x-tbar-page-prev',
                    tooltip: message.msg('common.prv'), // Find Previous Row
                    handler: me.onPreviousClick,
                    scope: me
                },
                {
                    xtype: 'button',
                    iconCls: 'x-tbar-page-next',
                    tooltip: message.msg('common.next'), // Find Next Row
                    handler: me.onNextClick,
                    scope: me
                },
                '-',
                {
                    xtype: 'checkbox',
                    itemId: 'regEx',
                    hideLabel: true,
                    margin: '0 0 0 4px',
                    handler: me.regExpToggle,
                    scope: me
                },
                message.msg('common.regexp'),
                { // Regular expression
                    xtype: 'checkbox',
                    itemId: 'case',
                    hideLabel: true,
                    margin: '0 0 0 4px',
                    handler: me.caseSensitiveToggle,
                    scope: me
                },
                message.msg('common.resultgrid.button.casesensitive'),
                '-',
                {
                    xtype: 'button',
                    text: message.msg('common.resultgrid.button.autosize'),
                    itemId: 'autoSize',
                    margin: '0 4 0 4',
                    handler: function () {
                        var grid = query('resultSearchGridPanel');
                        var columns = grid.headerCt.getGridColumns();
                        var i;
                        for (i = 0; i < columns.length; i++) {
                            columns[i].maxWidth = 10000;
                            columns[i].autoSize(i);
                        }
                    }
                },
                {
                    xtype: 'button',
                    text: message.msg('common.download'),
                    margin: '0 4 0 4',
                    handler: 'btnDownloadClick'
                }
            ],
            bbar: [{
                xtype: 'button',
                text: message.msg('common.prv'),
                reference: 'btnPrev',
                disabled: true,
                iconCls: 'x-item-disabled x-tbar-page-prev',
                handler: 'onPrevClick'
            }, {
                xtype: 'displayfield',
                reference: 'dfPage',
                value: message.msg('common.page') + ' 1'
            }, {
                xtype: 'button',
                text: message.msg('common.next'),
                iconAlign: 'right',
                iconCls: 'x-item-disabled x-tbar-page-next',
                handler: 'onNextClick'
            }]
        });

        me.callParent(arguments);
    },

    // afterRender override: it adds textfield and statusbar reference and start monitoring keydown events in textfield input 
    afterRender: function () {
        var me = this;
        me.callParent(arguments);
        me.textField = me.down('textfield[name=searchField]');
        me.statusBar = me.down('statusBar[name=searchStatusBar]');
    },
    // detects html tag
    tagsRe: /<[^>]*>/gm,

    // DEL ASCII code
    tagsProtect: '\x0f',

    // detects regexp reserved word
    regExpProtect: /\\|\/|\+|\\|\.|\[|\]|\{|\}|\?|\$|\*|\^|\|/gm,

    /**
     * In normal mode it returns the value with protected regexp characters.
     * In regular expression mode it returns the raw value except if the regexp is invalid.
     * @return {String} The value to process or null if the textfield value is blank or invalid.
     * @private
     */
    getSearchValue: function () {
        var me = this,
            value = me.textField.getValue();

        if (value === '') {
            return null;
        }
        if (!me.regExpMode) {
            value = value.replace(me.regExpProtect, function (m) {
                return '\\' + m;
            });
        } else {
            try {
                new RegExp(value);
            } catch (error) {
                me.statusBar.setStatus({
                    text: error.message,
                    iconCls: 'x-status-error'
                });
                return null;
            }
            // this is stupid
            if (value === '^' || value === '$') {
                return null;
            }
        }

        return value;
    },

    /**
     * Finds all strings that matches the searched value in each grid cells.
     * @private
     */
    onTextFieldChange: function () {
        var me = this,
            count = 0,
            view = me.view,
            cellSelector = view.cellSelector,
            innerSelector = view.innerSelector;

        view.refresh();
        // reset the statusbar
        me.statusBar.setStatus({
            text: me.defaultStatusText,
            iconCls: ''
        });

        me.searchValue = me.getSearchValue();
        me.indexes = [];
        me.currentIndex = null;

        if (me.searchValue !== null) {
            me.searchRegExp = new RegExp(me.getSearchValue(), 'g' + (me.caseSensitive ? '' : 'i'));


            me.store.each(function (record, idx) {
                if (view.getNode(idx)) {
                    var td = Ext.fly(view.getNode(idx)).down(cellSelector),
                        cell, matches, cellHTML;
                    while (td) {
                        cell = td.down(innerSelector);
                        matches = cell.dom.innerHTML.match(me.tagsRe);
                        cellHTML = cell.dom.innerHTML.replace(me.tagsRe, me.tagsProtect);

                        // populate indexes array, set currentIndex, and replace wrap matched string in a span
                        cellHTML = cellHTML.replace(me.searchRegExp, function (m) {
                            count += 1;
                            if (Ext.Array.indexOf(me.indexes, idx) === -1) {
                                me.indexes.push(idx);
                            }
                            if (me.currentIndex === null) {
                                me.currentIndex = idx;
                            }
                            return '<span class="' + me.matchCls + '">' + m + '</span>';
                        });
                        // restore protected tags
                        Ext.each(matches, function (match) {
                            cellHTML = cellHTML.replace(me.tagsProtect, match);
                        });
                        // update cell html
                        cell.dom.innerHTML = cellHTML;
                        td = td.next();
                    }
                }
            }, me);

            // results found
            if (me.currentIndex !== null) {
                me.getSelectionModel().select(me.currentIndex);
                me.statusBar.setStatus({
                    text: count + ' ' + message.msg('common.resultgrid.matches'),
                    iconCls: 'x-status-valid'
                });
            }
        }

        // no results found
        if (me.currentIndex === null) {
            me.getSelectionModel().deselectAll();
        }

        me.textField.focus();
    },

    /**
     * Selects the previous row containing a match.
     * @private
     */
    onPreviousClick: function () {
        var me = this,
            idx;

        if ((idx = Ext.Array.indexOf(me.indexes, me.currentIndex)) !== -1) {
            me.currentIndex = me.indexes[idx - 1] || me.indexes[me.indexes.length - 1];
            me.getSelectionModel().select(me.currentIndex);
        }
    },

    /**
     * Selects the next row containing a match.
     * @private
     */
    onNextClick: function () {
        var me = this,
            idx;

        if ((idx = Ext.Array.indexOf(me.indexes, me.currentIndex)) !== -1) {
            me.currentIndex = me.indexes[idx + 1] || me.indexes[0];
            me.getSelectionModel().select(me.currentIndex);
        }
    },

    /**
     * Switch to case sensitive mode.
     * @private
     */
    caseSensitiveToggle: function (checkbox, checked) {
        this.caseSensitive = checked;
        this.onTextFieldChange();
    },

    /**
     * Switch to regular expression mode
     * @private
     */
    regExpToggle: function (checkbox, checked) {
        this.regExpMode = checked;
        this.onTextFieldChange();
    }
});