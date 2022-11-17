FROM tomcat:latest
COPY /target /usr/local/tomcat/webapps
EXPOSE 8080