FROM amazoncorretto:18
RUN mkdir /app

WORKDIR /app

ADD ./api/target/catalog-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "catalog-api-1.0.0-SNAPSHOT.jar"]
ENTRYPOINT ["java", "-jar", "catalog-api-1.0.0-SNAPSHOT.jar"]
CMD java -jar catalog-api-1.0.0-SNAPSHOT.jar
