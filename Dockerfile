FROM openjdk:jre-alpine

ADD bin /srv/bin
ADD client/dist /srv/client/dist

EXPOSE 3000

WORKDIR /srv
CMD ["java", "-cp", "bin", "pro4.Server"]
