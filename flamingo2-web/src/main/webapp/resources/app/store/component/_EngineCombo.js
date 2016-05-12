Ext.define('Flamingo2.store.component._EngineCombo', {
    extend: 'Ext.data.Store',

    fields: [
        'id',
        'name',
        'ip',
        'port'
    ],

    autoLoad: false,

    constructor: function (config) {
        this.callParent(arguments);
    },

    proxy: {
        type: 'ajax',
        url: '/config/engines.json',
        headers: {
            'Accept': 'application/json'
        },
        reader: {
            type: 'json',
            rootProperty: 'list'
        },
        extraParams: { // Workflow Engine 목록 필터링 파라미터. 기본값은 모두 다 보임.
            'type': 'ALL',
            'hadoopVersion': '0'
        }
    }
});