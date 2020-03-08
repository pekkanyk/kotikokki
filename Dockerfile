FROM openjdk:11
COPY ./build/libs/*.jar ./app.jar
RUN mkdir -p src/main
COPY ./src/main/resources/public ./src/main/resources/public
COPY ./src/main/resources/templates ./src/main/resources/templates
COPY ./database.mv.db ./database.mv.db
ENTRYPOINT ["java","-jar","/app.jar"]
