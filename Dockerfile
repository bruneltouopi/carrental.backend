FROM maven:3.5.2-alpine
WORKDIR /usr/src/java-app
COPY . /usr/src/java-app/
RUN mvn package

WORKDIR /usr/src/java-app/target
EXPOSE 4201
CMD ["java", "-jar", "CarRental.BackEnd-1.0.jar"]