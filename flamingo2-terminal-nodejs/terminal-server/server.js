#!/usr/bin/env node


var http = require('http')
    , express = require('express')
    , io = require('socket.io');
var cp = require('child_process');
var fs = require('fs');
var async = require('async');

var guid = (function () {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }

    return function () {
        return s4() + s4() + '_' + s4() + '_' + s4() + '_' +
            s4() + '_' + s4() + s4() + s4();
    };
})();

process.title = 'term.js';
/**
 * App & Server
 */

var app = express()
    , server = http.createServer(app);

app.use(function (req, res, next) {
    var setHeader = res.setHeader;
    res.setHeader = function (name) {
        switch (name) {
            case 'Cache-Control':
            case 'Last-Modified':
            case 'ETag':
                return;
        }
        return setHeader.apply(res, arguments);
    };
    next();
});

server.listen(9191);

var clients = {};
io = io.listen(server, {
    log: true
});
io.sockets.on('connection', function (connection) {

    connection.on('disconnect', function () {
        endConnection(connection);
    });

    connection.on('login', function (data) {
        addClientConnection(data, connection);
        login(data);
    });

    connection.on('create', function (data) {
        console.log('Recieving Create Terminal order..');
        console.log(data);
        console.log('');
        createTerm(data, connection);
    });

    connection.on('data', function (data) {
        var clientname = data.clientname;
        var termdata = data.termdata;
        var termid = data.termid;

        var conid = connection.id;
        console.log("get from client: " + data.termdata);
        if (clients[conid] && clients[conid].terms && clients[conid].terms[termid]) {
            var childproc = clients[conid].terms[termid];
            childproc.send(termdata);
        }
    });
    connection.on('restyle', function (data) {
        var clientname = data.clientname;
        var termid = data.termid;
        var cols = data.cols;
        var rows = data.rows;

        var conid = connection.id;
        console.log("get style from client: " + data.cols + " , " + data.rows);
        if (clients[conid] && clients[conid].terms && clients[conid].terms[termid]) {
            var childproc = clients[conid].terms[termid];
            var style = [cols, rows];
            childproc.send("restyle" + JSON.stringify(style));
        }
    });
    connection.on('delete', function (data) {
        var termid = data.termid;
        var conid = connection.id;
        destroyTerm(conid, termid, connection)
    });


    function login(data) {
        var loginData = {
            username: data.username
        }
        if (!data.keyfile && !data.password) {
            console.log('Identify 가 없습니다.');
            connection.emit('login', 'failed');
        } else {
            async.waterfall([
                function (cb) {
                    if (data.keyfile) {
                        var fd = fs.openSync(__dirname + '/key.pem', 'w');
                        var buff = data.keyfile
                        fs.write(fd, buff, 0, buff.length, 0, function (err, written) {
                            loginData.keyfile = __dirname + '/key.pem';
                        });
                    }
                    else if (data.password) {
                        loginData.password = data.password;
                    }
                    cb();
                },
                function (cb) {
                    var loginproc = cp.fork(__dirname + '/login.js');
                    loginproc.send(loginData);
                    loginproc.on('message', function (msg) {
                        console.log("login_parent_got: " + msg);
                        if (msg == 'success')
                            connection.emit('login', 'success');
                        else {
                            connection.emit('login', 'failed');
                        }
                        loginproc.kill('SIGHUP');
                        loginproc = null;
                    });
                    loginproc.on('error', function (err) {
                        console.log('err');
                        loginproc = null;
                        connection.emit('login', 'failed');
                    })
                    cb();
                },
            ], function (err, result) {
                console.log("err : " + err);
                console.log("result : " + result);
                if (err != null) {
                    connection.emit('login', 'failed');
                }
            });
        }
    }
});

function addClientConnection(data, connection) {
    //클라이언트 객체에 세션아이디를 key 로 삼아서 커넥션과 클라이언트 이름을 저장한다.
    var conid = connection.id;
    var clientname = data.clientname;
    if (!clients[conid])
        clients[conid] = {};

    clients[conid] = {
        connection: connection,
        clientname: clientname
    }

    console.log('client : ' + clientname + ' got a new session. ID: ' + conid);
}

function endConnection(connection) {
    var conid = connection.id;
    var clientname = null;
    if (clients[conid]) {
        clientname = clients[conid].clientname;
        if (clients[conid].terms) {
            for (var key in clients[conid].terms) {
                var childproc = clients[conid].terms[key];
                try {
                    childproc.kill('SIGHUP');
                } catch (e) {
                }
            }
        }
        delete clients[conid];
        console.log('client : ' + clientname + ' disconnected. ID :' + conid);
    } else {
        console.log('Trying disconnect failed. ID :' + conid + ' not found.');
    }
}

function createTerm(data, connection) {
    var conid = connection.id;
    var clientname = data.clientname;
    var username = data.username;
    var termid = guid();
    connection.emit('create', {
        termid: termid
    });
    var childproc = cp.fork(__dirname + '/child.js', [username]);
    childproc.on('message', function (data) {
        console.log("parent_got: " + data);
        return connection.emit('data', {
            termdata: data,
            termid: termid
        });
    });
    childproc.on('close', function (code, signal) {
        console.log('child process terminated due to receipt of signal ' + signal);
        destroyTerm(conid, termid, connection);
    });
    if (!clients[conid]) {
        clients[conid] = {
            connection: connection,
            clientname: clientname
        }
    }
    if (!clients[conid].terms)
        clients[conid].terms = {};

    clients[conid].terms[termid] = childproc;
    traceTermList(conid);
}

function traceTermList(conid) {
    if (clients[conid] && clients[conid].terms) {
        console.log('session ID: ' + conid + ' has next term list.');
        for (var key in clients[conid].terms) {
            console.log('Terminal ID: ' + key);
        }
    } else {
        console.log('None Terminal list');
    }
}

function destroyTerm(conid, termid, connection) {
    if (clients[conid] && clients[conid].terms && clients[conid].terms[termid]) {
        var childproc = clients[conid].terms[termid];
        try {
            childproc.kill('SIGHUP');
        } catch (e) {
        }
        finally {
            delete clients[conid].terms[termid];
        }
        console.log('Terminal : ' + termid + ' deleted. session ID :' + conid);
    } else {
        console.log('Delete failed. Terminal ID :' + termid + ' not found. session ID :' + conid);
    }
    connection.emit('termdestroyed', {
        termid: termid
    });
    traceTermList(conid);
}