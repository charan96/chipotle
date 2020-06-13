#!/bin/bash

service mongodb start
rabbitmq-server start &
java -jar app/chipotle.jar
