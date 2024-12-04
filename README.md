# receipt-processor

This is a Java solution to the take home assessment. It is written using Java 23 and Spring Boot and built with Maven. 

The Dockerfile will take the pre built jar file and start the API.

#Commands used:

mvn clean install (build jar)

docker build -t receipt-processor . (builds docker image)

docker run -p 8080:8080 receipt-processor (runs docker image at 8080 )