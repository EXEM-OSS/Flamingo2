#!/bin/sh

mvn -DoutputDirectory=`pwd`/target/dependencies dependency:copy-dependencies
mvn compile
ant
