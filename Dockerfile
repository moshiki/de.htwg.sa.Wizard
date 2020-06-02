#FROM hseeberger/scala-sbt
FROM adoptopenjdk/openjdk14:alpine
EXPOSE 1233
WORKDIR /wizard
ADD target/scala-2.13/Wizard-assembly-SAR-6.jar /wizard
CMD java -jar Wizard-assembly-SAR-6.jar
ENV DOCKERENV="TRUE"