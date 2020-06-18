#!/bin/bash

cd /app
mvn package
java -jar target/chipotle-0.0.1-SNAPSHOT.jar &