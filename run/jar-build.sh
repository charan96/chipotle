#!/bin/bash

cd ..
mvn package
cp target/chipotle-0.0.1-SNAPSHOT.jar run/app/chipotle.jar
cd run/
