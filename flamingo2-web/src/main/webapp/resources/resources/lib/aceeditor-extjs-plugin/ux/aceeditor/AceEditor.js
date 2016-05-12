Ext.define('Ext.ux.aceeditor.AceEditor', {
	extend: 'Ext.ux.aceeditor.Panel',
	alias: 'widget.aceEditor',

	requires: ['Ext.ux.aceeditor.Editor'],

	initComponent: function () {
		var me = this, toolbar = [
			{
				text: '실행 취소',
				iconCls: 'common-undo',
				itemId: 'undoButton',
				handler: me.undo,
				scope: me
			},
			{
				text: '다시 실행',
				iconCls: 'common-redo',
				itemId: 'redoButton',
				handler: me.redo,
				scope: me
			},
			'->',
			{
				text: '설정',
				iconCls: 'common-setting',
				menu: {
					xtype: 'menu',
					plain: true,
					items: [
						{
							text: '보이지 않는 문자 표시',
							handler: function () {
								me.showInvisible = (me.showInvisible) ? false : true;
								me.editor.setShowInvisibles(me.showInvisible);
							},
							checked: (me.showInvisible),
							scope: me
						},
						{
							text: '줄넘김',
							handler: function () {
								me.useWrapMode = (me.useWrapMode) ? false : true;
								me.editor.getSession().setUseWrapMode(me.useWrapMode);
							},
							checked: (me.useWrapMode),
							scope: me
						},
						{
							text: '코드 접기',
							handler: function () {
								me.codeFolding = (me.codeFolding) ? false : true;
								me.editor.setShowFoldWidgets(me.codeFolding);
							},
							checked: (me.codeFolding),
							scope: me
						},
						{
							text: '현재 라인 하이라이트',
							handler: function () {
								me.highlightActiveLine = (me.highlightActiveLine) ? false : true;
								me.editor.setHighlightActiveLine(me.highlightActiveLine);
							},
							checked: (me.highlightActiveLine),
							scope: me
						},
						{
							text: '줄 번호 표시',
							handler: function () {
								me.showGutter = (me.showGutter) ? false : true;
								me.editor.renderer.setShowGutter(me.showGutter);
							},
							checked: (me.showGutter),
							scope: me
						},
						{
							text: '선택한 문자 하이라이트',
							handler: function () {
								me.highlightSelectedWord = (me.highlightSelectedWord) ? false : true;
								me.editor.setHighlightSelectedWord(me.highlightSelectedWord);
							},
							checked: (me.highlightSelectedWord),
							scope: me
						},
						{
							xtype: 'menuseparator'
						},
						Ext.create('Ext.container.Container', {
							layout: {
								type: 'hbox'
							},
							items: [
								{
									xtype: 'menuitem',
									text: '글꼴 크기',
									handler: function () {
									},
									flex: 1,
									checked: (me.highlightSelectedWord),
									scope: me
								},
								{
									fieldStyle: 'text-align: right',
									hideLabel: true,
									xtype: 'numberfield',
									value: me.fontSize,
									minValue: 6,
									maxValue: 72,
									width: 50,
									flex: 0,
									plain: true,
									listeners: {
										change: function (field, value) {
											me.fontSize = value;
											me.setFontSize(me.fontSize + "px");
										}
									}
								}]
						}),
						Ext.create('Ext.container.Container', {
							layout: {
								type: 'hbox'
							},
							width: 200,
							items: [{
								xtype: 'menuitem',
								text: '인쇄 여백 표시',
								handler: function () {
								},
								flex: 1,
								checked: (me.highlightSelectedWord),
								scope: me
							},
								{
									fieldStyle: 'text-align: right',
									hideLabel: true,
									xtype: 'numberfield',
									value: me.printMarginColumn,
									minValue: 1,
									maxValue: 200,
									width: 50,
									flex: 0,
									plain: true,
									listeners: {
										change: function (field, value) {
											me.printMarginColumn = value;
											me.editor.setPrintMarginColumn(me.printMarginColumn);
										}
									}
								}]
						}),
						{
							xtype: 'menuseparator'
						},
						{
							xtype: 'container',
							layout: {
								type: 'hbox'
							},
							width: 240,
							items: [{
								xtype: 'menuitem',
								text: '테마'
							},
								{
									xtype: 'combo',
									mode: 'local',
									flex: 1,
									value: me.theme,
									triggerAction: 'all',
									editable: false,
									name: 'Theme',
									displayField: 'name',
									valueField: 'value',
									queryMode: 'local',
									store: Ext.create('Ext.data.Store', {
										fields: ['name', 'value'],
										data: [
											{
												value: 'ambiance',
												name: 'Ambiance'
											},
											{
												value: 'chrome',
												name: 'Chrome'
											},
											{
												value: 'clouds',
												name: 'Clouds'
											},
											{
												value: 'clouds_midnight',
												name: 'Clouds Midnight'
											},
											{
												value: 'cobalt',
												name: 'Cobalt'
											},
											{
												value: 'crimson_editor',
												name: 'Crimson Editor'
											},
											{
												value: 'dawn',
												name: 'Dawn'
											},
											{
												value: 'dreamweaver',
												name: 'Dreamweaver'
											},
											{
												value: 'eclipse',
												name: 'Eclipse'
											},
											{
												value: 'idle_fingers',
												name: 'idleFingers'
											},
											{
												value: 'kr_theme',
												name: 'krTheme'
											},
											{
												value: 'merbivore',
												name: 'Merbivore'
											},
											{
												value: 'merbivore_soft',
												name: 'Merbivore Soft'
											},
											{
												value: 'mono_industrial',
												name: 'Mono Industrial'
											},
											{
												value: 'monokai',
												name: 'Monokai'
											},
											{
												value: 'pastel_on_dark',
												name: 'Pastel on dark'
											},
											{
												value: 'solarized_dark',
												name: 'Solarized Dark'
											},
											{
												value: 'solarized_light',
												name: 'Solarized Light'
											},
											{
												value: 'textmate',
												name: 'TextMate'
											},
											{
												value: 'twilight',
												name: 'Twilight'
											},
											{
												value: 'tomorrow',
												name: 'Tomorrow'
											},
											{
												value: 'tomorrow_night',
												name: 'Tomorrow Night'
											},
											{
												value: 'tomorrow_night_blue',
												name: 'Tomorrow Night Blue'
											},
											{
												value: 'tomorrow_night_bright',
												name: 'Tomorrow Night Bright'
											},
											{
												value: 'tomorrow_night_eighties',
												name: 'Tomorrow Night 80s'
											},
											{
												value: 'vibrant_ink',
												name: 'Vibrant Ink'
											}]
									}),
									listeners: {
										change: function (field, value) {
											me.theme = value;
											me.setTheme(me.theme);
										}
									}
								}]
						}]
				}
			}];

		var wordCount = Ext.create('Ext.toolbar.TextItem', {text: '위치: 0'}),
			lineCount = Ext.create('Ext.toolbar.TextItem', {text: '줄수: 0'});

		Ext.apply(me, {
			tbar: toolbar,
			bbar: Ext.create('Bahamas.view.component.StatusBar', {
				//defaultText: 'Default status',
				//statusAlign: 'right', // the magic config
				items: [lineCount, wordCount]
			})
		});

		// 생성자를 통해서 값이 넘어오면 설정한다.
		var content = this.value;

		me.on('editorcreated', function () {
			// 생성자를 통해서 내용이 넘어오면 생성된 이후에 내용을 채운다
			if (content) {
				me.editor.getSession().setValue(content);
			}

			me.editor.selection.on("changeCursor", function (e) {
				var c = me.editor.selection.getCursor(),
					l = c.row + 1;

				wordCount.update('위치: ' + c.column);
				lineCount.update('줄수: ' + l);

			}, me);
		});

		me.callParent(arguments);
	}
});