FROM mysql:5.7
EXPOSE 3306 27017
# === MySQL ENV
#ENV MYSQL_ROOT_PASSWORD=WIZARD
ENV MYSQL_DATABASE=wizard
ENV MYSQL_USER=wizard
ENV MYSQL_PASSWORD=wizard

ENV MONGO_DATA_DIR=/var/lib/mongodb \
    MONGO_EXTRA_ARGS="" \
    MONGO_ROOT_USERNAME=root \
    MONGO_PORT=27017 \
    MONGO_ROOT_PWD=NONE \
    MONGO_USE_SYSLOG=true \
    MONGO_LOG_PATH=/var/log/mongodb \
    MONGO_MAX_CONNECTIONS=NONE \
    MONGO_STORAGEENGINE=wiredTiger \
    MONGO_WIREDTIGER_CACHE_SIZE_GB=NONE \
    MONGO_BINDING=--bind_ip_all \
    MONGO_REPLICA_SET_NAME=Standalone0

ARG MONGODB_VERSION="4.2.7"

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y wget gnupg procps \
    && wget -qO - https://www.mongodb.org/static/pgp/server-4.2.asc | apt-key add - \
    && echo "deb http://repo.mongodb.org/apt/debian buster/mongodb-org/4.2 main" | tee /etc/apt/sources.list.d/mongodb-org-4.2.list \
    && apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get upgrade -y \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y  mongodb-org-server=${MONGODB_VERSION} mongodb-org-shell=${MONGODB_VERSION} mongodb-org-mongos=${MONGODB_VERSION} \
    && DEBIAN_FRONTEND=noninteractive apt-get autoremove -y \
    && rm -rf /etc/mongod.conf \
    && rm -rf /var/lib/apt/lists/*

COPY entrypoint.sh /sbin/entrypoint.sh
RUN chmod 755 /sbin/entrypoint.sh

VOLUME ["${MONGO_DATA_DIR}"]
ENTRYPOINT ["/sbin/entrypoint.sh"]
CMD ["/usr/bin/mongod"]