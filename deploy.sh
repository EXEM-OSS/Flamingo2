#!/bin/sh

rm -rf flamingo2-web/src/main/webapp/resources/app
svn update
mvn -Dmaven.test.skip=true clean package
sh ~/web/bin/catalina.sh stop
rm -rf /webapps/ROOT/*
cp /data1/cloudine/build/flamingo2/trunk/flamingo2-web/target/flamingo2-web-2.1.0-SNAPSHOT.war /data1/cloudine/web/webapps/ROOT
cd ~/web/webapps/ROOT
mv flamingo2-web-2.1.0-SNAPSHOT.war ROOT.war
jar xvf ROOT.war
cd resources
sencha app refresh
rm -rf /data1/cloudine/web/webapps/ROOT/resources/resources/manual/*
cp -R /data1/cloudine/build/flamingo2/trunk/flamingo2-documentation/target/manual/* /data1/cloudine/web/webapps/ROOT/resources/resources/manual
sh ~/web/bin/catalina.sh start
tail -f ~/web/logs/app.log