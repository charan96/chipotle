#!/bin/bash

cd /app
mvn package
java -jar target/chipotle-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &