FROM maven:3.5.0-jdk-8-slim as builder

WORKDIR ./home/maven

COPY ./pom.xml .
COPY ./src ./src

RUN mvn install

FROM jboss/wildfly

EXPOSE 8080

COPY --from=builder ./home/maven/target/mythidb-3.2.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

COPY ./config/contrib/wfmodules/ /opt/jboss/wildfly/modules/
COPY ./config/contrib/wfcfg/standalone.xml /opt/jboss/wildfly/standalone/configuration/standalone.xml
COPY ./config/contrib/wfcfg/application-roles.properties /opt/jboss/wildfly/standalone/configuration
COPY ./config/contrib/wfcfg/application-users.properties /opt/jboss/wildfly/standalone/configuration

VOLUME /opt/jboss/FILES_INDEXING

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]


#sudo docker run --name mythidb -d -p 8080:8080 -e "POSTGRESQL_SERVICE_HOST=postgres" -e "POSTGRESQL_SERVICE_PORT=5432" -e "POSTGRESQL_USER=adminejtetcb" -e "POSTGRESQL_PASSWORD=" -e "POSTGRESQL_DATABASE=mythidb" -e "LUCENE_INDEX_DIR=/opt/jboss/FILES_INDEXING" --link postgres:postgres -v /home/marcelo/work/mythidb-lucene/FILES_INDEXING:/opt/jboss/FILES_INDEXING askalien:mythidb


#sudo docker run --name mythidb -d -p 8080:8080 -e "POSTGRESQL_SERVICE_HOST=postgres.ccfpvx9q21xe.us-west-1.rds.amazonaws.com" -e "POSTGRESQL_SERVICE_PORT=5432" -e "POSTGRESQL_USER=postgres" -e "POSTGRESQL_PASSWORD=" -e "POSTGRESQL_DATABASE=mythidb" -e "LUCENE_INDEX_DIR=/opt/jboss/FILES_INDEXING" mmuniz/askalien:mythidb-v1.1


