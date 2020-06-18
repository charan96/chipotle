#!/bin/bash

pwd > /app-start-pwd1.txt
cd /app
pwd > /app-start-pwd2.txt
java -jar target/chipotle-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &