FROM alpine/git as clone
WORKDIR /app
RUN git clone https://github.com/saritha1919/discoveri-happytrip.git

FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app
COPY --from=clone /app/discoveri-happytrip /app
RUN mvn clean package

FROM tomcat:8.0
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/happytrip.war
COPY tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
COPY context.xml /usr/local/tomcat/webapps/manager/META-INF/




