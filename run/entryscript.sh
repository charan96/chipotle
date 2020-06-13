#!/bin/bash

service mongodb start
rabbitmq-server start &
java -jar app/chipotle-0.0.1-SNAPSHOT.jar
