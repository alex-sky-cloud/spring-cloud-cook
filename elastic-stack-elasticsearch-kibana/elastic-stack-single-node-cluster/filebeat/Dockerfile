ARG ELK_VERSION

FROM elastic/filebeat:${ELK_VERSION}

# значение данного аргумента будет передано из docker-compose
ARG FILEBEAT_CONFIG

# add custom configuration
COPY --chown=root:filebeat ${FILEBEAT_CONFIG}.yml /usr/share/filebeat/filebeat.yml