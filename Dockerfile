FROM jboss/wildfly

EXPOSE 8080

COPY /target/mythidb-3.2.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

COPY ./config/contrib/wfmodules/ /opt/jboss/wildfly/modules/
COPY ./config/contrib/wfcfg/standalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml
COPY ./config/contrib/wfcfg/application-roles.properties /opt/jboss/wildfly/standalone/configuration
COPY ./config/contrib/wfcfg/application-users.properties /opt/jboss/wildfly/standalone/configuration

VOLUME /opt/jboss/FILES_INDEXING

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]


#docker run --name askalien-server -d -p 8080:8080 -e "POSTGRESQL_SERVICE_HOST=postgres" -e "POSTGRESQL_SERVICE_PORT=5432" -e "POSTGRESQL_USER=adminejtetcb" -e "POSTGRESQL_PASSWORD=dPwJA9hheWUF" -e "POSTGRESQL_DATABASE=mythidb" -e "LUCENE_INDEX_DIR=/opt/jboss/FILES_INDEXING" --link postgres:postgres -v /home/marcelo/work/mythidb-lucene/FILES_INDEXING:/opt/jboss/FILES_INDEXING mmuniz/askalien:wildfly-spring


#docker run --name askalien-server -d -p 8080:8080 -e "POSTGRESQL_SERVICE_HOST=askalien.c8e9u19htm5z.us-east-1.rds.amazonaws.com" -e "POSTGRESQL_SERVICE_PORT=5432" -e "POSTGRESQL_USER=adminejtetcb" -e "POSTGRESQL_PASSWORD=dPwJA9hheWUF" -e "POSTGRESQL_DATABASE=mythidb" -e "LUCENE_INDEX_DIR=/opt/jboss/FILES_INDEXING" -v /home/marcelo/work/mythidb-lucene/FILES_INDEXING:/opt/jboss/FILES_INDEXING mmuniz/askalien:wildfly-spring



