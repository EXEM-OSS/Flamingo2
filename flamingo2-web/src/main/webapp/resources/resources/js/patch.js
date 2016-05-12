var format = {};
function setComplexTable(view) {
    var i;
    var columns = Number(view.layout.columns) / 2;

    for (i=0; i<columns; i++) {
        $('#'+view.id + ' .x-table-layout tbody:first').before('<col class="table-layout-label"><col>');
    }
}

function setTableLayoutFixed(view) {
    var i;
    var columns = view.layout.columns;
    var width = 100 / columns;

    for (i=0; i<columns; i++) {
        $('#'+view.id + ' .x-table-layout tbody:first').before('<col width="'+width+'%">');
    }
}

var convertTime = function (value) {
    var millis = value % 1000;
    value = parseInt(value / 1000);
    var seconds = value % 60;
    value = parseInt(value / 60);
    var minutes = value % 60;
    value = parseInt(value / 60);
    var hours = value % 24;
    var out = "";
    if (hours && hours > 0) out += hours + "" + ((hours == 1) ? "h" : "h") + " ";
    if (minutes && minutes > 0) out += minutes + "" + ((minutes == 1) ? "m" : "m") + " ";
    if (seconds && seconds > 0) out += seconds + "" + ((seconds == 1) ? "s" : "s") + " ";
    return out.trim();
}

var convertDateTime = function (value) {
    var date = new Date(value);
    return Ext.Date.format(date, 'Y-m-d H:i:s');
}

var convertComma = function (value) {
    return Ext.util.Format.number(value, '0,000')
}

function Layer(geom){
    this.geom = geom;
    if(geom=="bar") this.stat= "set_identity"; // this overrides the default stat="bin" in ggplot2.
}

function PlotConfig(x,y,group,colour,weight,facet){
    this.x = x;
    this.y = y;
    this.group = group;
    this.colour = colour;
    this.weight = weight;
    this.facet = new Object();
    this.layers = new Object();
}

PlotConfig.prototype.set = function(key,value,layerKey) {
    if(layerKey==null){
        if(value == "default"){
            delete this[key];
        } else {
            this[key] = value;
        }
    } else if (layerKey=="facet") {
        if(value == "default"){
            delete this.facet[key];
        } else {
            this.facet[key] = value;
        }
    } else {
        if(value == "default"){
            delete this.layers[layerKey][key];
        } else {
            this.layers[layerKey][key] = value;
        }
    }
}

PlotConfig.prototype.addLayer = function(geom,layerKey) {
    return this.layers[layerKey] = new Layer(geom);
}

PlotConfig.prototype.removeLayer = function(layerKey) {
    delete this.layers[layerKey];
}
/*
PlotConfig.prototype.toJSON = function() {
    return Ext.encode(this);
}

PlotConfig.prototype.toString = function() {
    return Ext.encode(this);
}*/

var UUIDGenerate = function() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}

var records2Json = function(records) {
    var result = [];

    for (var j = 0; j < records.length; j++) {
        var models = {};
        var items = records[j].fields.items;
        for (var i = 0; i < items.length; i++) {
            var name = items[i].name;
            models[name] = records[j].get(name);
        }
        result.push(models);
    }

    return Ext.encode(result);

};

var byteConverter = function(value) {

    var byteLimit = 1024,
        kbLimit = 1048576,
        mbLimit = 1073741824;
        gbLimit = 1099511627776;

    var out;
    if (value < byteLimit) {
        if (value < 1) {
            out = '0 Byte';
        }else if (value === 1) {
            out = '1 Byte';
        } else {
            out = value + ' Bytes';
        }
    } else if (value < kbLimit) {
        out = (Math.round(((value*10) / byteLimit))/10) + ' KB';
    } else if (value < mbLimit) {
        out = (Math.round(((value*10) / kbLimit))/10) + ' MB';
    } else if (Number(value) / 1024 < mbLimit) {
        out = (Math.round(((value*10) / mbLimit))/10) + ' GB';
    } else {
        out = (Math.round(((value*10) / gbLimit))/10) + ' TB';
    }
    return out;
}

var megaByteConverter = function(value) {
    var mbLimit = 1024,
        gbLimit = 1048576;

    var out;
    if (value < mbLimit) {
        if (value < 1) {
            out = '0 MB';
        }else if (value === 1) {
            out = '1 MB';
        } else {
            out = value + ' MB';
        }
    } else if (Number(value) / 1024 < mbLimit) {
        out = (Math.round(((value*10) / mbLimit))/10) + ' GB';
    }

    return out;
}