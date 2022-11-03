FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/OperationService-0.0.1-SNAPSHOT.jar ms-operation.jar
ENTRYPOINT ["java","-jar","/ms-operation.jar"]