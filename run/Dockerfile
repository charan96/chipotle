FROM ubuntu:18.04

WORKDIR /home

COPY ./chipotle_db /home/chipotle_db
COPY ./entryscript.sh /home/entryscript.sh

RUN apt-get update && apt-get install -y openjdk-8-jre systemd mongodb rabbitmq-server
RUN service mongodb start && mongorestore --db chipotle_db --verbose /home/chipotle_db

EXPOSE 8080

ENTRYPOINT ["/home/entryscript.sh"]
