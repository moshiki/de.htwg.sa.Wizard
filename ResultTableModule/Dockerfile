#FROM hseeberger/scala-sbt
FROM adoptopenjdk/openjdk14:alpine-slim
EXPOSE 54251
WORKDIR /wizard
ADD target/scala-2.13/ResultTableModule-assembly-SAR-6.jar /wizard
ENV DOCKERENV="TRUE"
CMD java -jar ResultTableModule-assembly-SAR-6.jar