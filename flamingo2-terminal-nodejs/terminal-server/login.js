#!/usr/bin/env node


var cp = require('child_process');
var fs = require('fs');
var async = require('async');
var pty = require('pty.js')

process.title = 'term_login.js';


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


//process.send(data);

process.on('message', function (data) {
    var username = data.username;
    var keyfile = data.keyfile;
    var password = data.password;

    var testUser = 'termlogin';
    var testPass = 'termlogin';
    var testKeyFile = '/home/' + testUser + '/key.pem';


    async.waterfall([
        function (cb) {
            //기존 임시 유저 파일 포함 모두 삭제
            cp.exec('/usr/sbin/userdel -r ' + testUser,
                function (error, stdout, stderr) {
                    cb();
                });
        },
        function (cb) {
            //임시 유저 홈디렉토리 생성.
            cp.exec('mkdir /home/' + testUser,
                function (error, stdout, stderr) {
                    cb();
                });
        },
        function (cb) {
            //임시 유저를 생성한다.
            var command = '/usr/sbin/useradd ' + testUser + ' -p ' + testPass + ' -d /home/' + testUser;
            cp.exec(command,
                function (error, stdout, stderr) {
                    cb();
                });
        },
        function (cb) {
            //키파일이 있다면 임시디렉토리로 이동시킨다
            if (keyfile) {
                cp.exec('mv ' + keyfile + ' ' + testKeyFile,
                    function (error, stdout, stderr) {
                        cb();
                    });
            } else cb();
        },
        function (cb) {
            //이동시킨 키파일의 퍼미션을 변경한다
            if (keyfile) {
                cp.exec('chmod 400 ' + testKeyFile,
                    function (error, stdout, stderr) {
                        cp.exec('chown ' + testUser + ':' + testUser + ' ' + testKeyFile,
                            function (error, stdout, stderr) {
                                cb();
                            });
                    });
            } else cb();
        },
        function (cb) {
            //새 유저 디렉토리로 이동한다.
            try {
                process.chdir('/home/' + testUser);
                console.log('New directory: ' + process.cwd());
                process.setgid(testUser);
                process.setuid(testUser);
                cb();
            }
            catch (err) {
                console.log('chdir: ' + err);
                process.send('failed');
            }
            //새 유저의 권한으로 바꾼다.
        },
        function (cb) {
            if (keyfile) {
                validateKey(username, testKeyFile);
            } else {
                validatePassword(username, password);
            }
            cb();
        }
    ], function (err, result) {
        console.log("err : " + err);
        console.log("result : " + result);
        if (err != null)
            process.send('failed');
    });

});

function validateKey(username, keyFile) {
    var lines = [];
    var term = pty.spawn("ssh", [
        "-i",
        keyFile,
        "-o",
        "UserKnownHostsFile=/dev/null",
        "-o",
        "StrictHostKeyChecking=no",
        username + "@localhost",
        "whoami"
    ]);
    term.on("data", function (data) {
        lines.push(data.toString());
    });
    setTimeout(function () {
        if (lines.length > 0) {
            var last_line = lines[lines.length - 1];
            if (last_line.indexOf(username) == 0 &&
                last_line.indexOf('@') == -1) {
                term.destroy();
                process.send('success')
            } else {
                term.destroy();
                process.send('failed')
            }
        } else {
            term.destroy();
            process.send('failed')
        }
    }, 1000);
}

function validatePassword(username, password) {
    var inputEnable = false;
    var lines = [];
    var term = pty.spawn("ssh", [
        "-o",
        "UserKnownHostsFile=/dev/null",
        "-o",
        "StrictHostKeyChecking=no",
        username + "@localhost",
        "whoami"
    ]);
    term.on("data", function (data) {
        console.log(data.toString());
        lines.push(data.toString());
    });
    setTimeout(function () {
        if (lines.length > 0) {
            var last_line = lines[lines.length - 1];
            console.log(last_line.indexOf(username));
            if (last_line.indexOf(username) == 0 &&
                last_line.indexOf('@') == -1) {
                term.destroy();
                process.send('success')
            } else {
                term.destroy();
                process.send('failed')
            }
        } else {
            term.destroy();
            process.send('failed')
        }
    }, 2000);
    setTimeout(function () {
        term.write(password + "\n");
    }, 500);
}

process.on('SIGHUP', function () {
    console.log('Got SIGHUP.  Login_process will exit.');
    process.exit(0);
});
