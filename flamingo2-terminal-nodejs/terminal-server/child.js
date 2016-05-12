#!/usr/bin/env node

var async = require('async');
var cp = require('child_process');
var pty = require('pty.js')
var terminal = require('term.js');

process.title = 'term_child.js';

getArgv = [];
process.argv.forEach(function (val, index, array) {
    if (index >= 2) {
        getArgv.push(val);
    }
});

var term;
var process_user = getArgv[0];
//id -g -n park

async.waterfall([
    function (cb) {
        //process_user 의 소속그룹을 구한다.
        cp.exec('id -g -n ' + process_user + ' -r ',
            function (error, stdout, stderr) {
                var groupname = stdout.replace(new RegExp('\r?\n', 'g'), '');
                console.log('Found: User ' + process_user + 's groupname : ' + groupname);
                cb(null, groupname);
            });
    },
    function (groupname, cb) {
        //process_user 의 유저 홈을 구한다.
        cp.exec('cat /etc/passwd | grep "' + process_user + ':" | awk -F: \'{print $6F}\'',
            function (error, stdout, stderr) {
                var lines = stdout.toString().split('\n');
                var userhome = lines[0].replace(new RegExp('\r?\n', 'g'), '');
                console.log('Found: User ' + process_user + 's userhome : ' + userhome);
                cb(null, groupname, userhome);
            });
    },
    function (groupname, userhome, cb) {
        groupname = groupname.replace(new RegExp('\r?\n', 'g'), '');
        console.log('Found: User ' + process_user + 's groupname : ' + groupname);
        //그룹 , 유저 프로세스로 변환한다.
        process.setgid(groupname);
        process.setuid(process_user);

        process.env.USER = process_user;
        process.env.HOME = userhome;

        console.log(process.env);
        cb();
    },
    function (cb) {
        //쉘을 생성하고 동작과정을 명시한다.
        term = pty.fork(process.env.SHELL || 'sh', [], {
            name: require('fs').existsSync('/usr/share/terminfo/x/xterm-256color')
                ? 'xterm-256color'
                : 'xterm',
            cols: 150,
            rows: 40,
            cwd: process.env.HOME
        });
        term.on('data', function (data) {
            process.send(data);
        });

        process.on('message', function (data) {
            console.log('CHILD got message:', data);
            if (data.indexOf('restyle') == 0) {
                try {
                    data = data.replace('restyle', '');
                    var style = JSON.parse(data);
                    var cols = style[0];
                    var rows = style[1];
                    console.log(cols);
                    console.log(rows);
                    term.resize(cols, rows);
                } catch (e) {
                }
                ;
            } else {
                term.write(data);
            }
        });
        process.on('SIGHUP', function () {
            console.log('Got SIGHUP.  Press Control-D to exit.');
            term.destroy();
            term = null;
            process.exit(0);
        });
        cb();
    }
], function (err, result) {
    if (err != null) {
        try {
            term.destroy();
        } catch (e) {
        }
        finally {
            term = null;
            process.exit(0);
        }
    }
});


var guid = (function () {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }

    return function () {
        return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
            s4() + '-' + s4() + s4() + s4();
    };
})();