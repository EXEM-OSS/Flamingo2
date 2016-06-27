#!/usr/bin/expect
spawn bash -c "git pull"
expect "Password for 'https://fharenheit@github.com': "
sleep 1
send "princo\n"
interact
