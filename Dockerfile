FROM java:8
VOLUME /tmp
ADD iyzico-todo.jar iyzico-todo.jar
RUN sh -c 'touch /iyzico-todo.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/iyzico-todo.jar"]