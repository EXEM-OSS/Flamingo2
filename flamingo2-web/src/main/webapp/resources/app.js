/* ExtJS Caching */
Ext.Loader.setConfig({
    disableCaching: toBoolean(config['extjs.ajax.disable.cache'])
});

/* ExtJS Ajax Timeout */
Ext.Ajax.setTimeout(parseInt(config['ajax.timeout']));

/*
 * This file is generated and updated by Sencha Cmd. You can edit this file as
 * needed for your application, but these edits will have to be merged by
 * Sencha Cmd when upgrading.
 */
Ext.application({
    name: 'Flamingo2',

    extend: 'Flamingo2.Application',
    
    autoCreateViewport: 'Flamingo2.view.main.Main'
	
    //-------------------------------------------------------------------------
    // Most customizations should be made to Flamingo2.Application. If you need to
    // customize this file, doing so below this section reduces the likelihood
    // of merge conflicts when upgrading to new versions of Sencha Cmd.
    //-------------------------------------------------------------------------
});
