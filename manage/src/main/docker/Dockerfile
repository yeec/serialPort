FROM java
VOLUME /tmp
ADD manage-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
RUN bash -c 'mkdir -p /log/springbootlog'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]