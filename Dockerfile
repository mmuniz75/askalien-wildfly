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
COPY ./config/FILES_INDEXING /opt/jboss/FILES_INDEXING

USER 0

RUN chown -R 1001:0 /opt/jboss/FILES_INDEXING && \
    chmod -R ug+rw /opt/jboss/FILES_INDEXING

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]


# sudo docker run --name mythidb -it -e -p 80:8080 "POSTGRESQL_SERVICE_HOST=postgres" -e "POSTGRESQL_SERVICE_PORT=5432" -e "POSTGRESQL_USER=<user>" -e "POSTGRESQL_PASSWORD=<password>" -e "POSTGRESQL_DATABASE=mythidb" --link postgres:postgres askalien:mythidb

