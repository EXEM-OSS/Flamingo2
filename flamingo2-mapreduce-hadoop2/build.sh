#!/bin/sh

svn update
mvn -DincludeScope=runtime -DoutputDirectory=target/classes clean dependency:unpack-dependencies package
