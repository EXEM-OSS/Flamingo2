Ext.define('Flamingo2.model.system.Organization', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'id', type: 'string', mapping: 'org_id'
        },
        {
            name: 'org_code', type: 'string', mapping: 'org_code'
        },
        {
            name: 'org_name', type: 'string', mapping: 'org_name'
        },
        {
            name: 'org_description', type: 'string', mapping: 'org_description'
        },
        {
            name: 'register_date', type: 'string', mapping: 'register_date',
            convert: function (value) {
                return dateFormat(new Date(value), 'yyyy-MM-dd HH:mm:ss');
            }
        },
        {
            name: 'update_date', type: 'string', mapping: 'update_date',
            convert: function (value) {
                return dateFormat(new Date(value), 'yyyy-MM-dd HH:mm:ss');
            }
        }
    ]
});