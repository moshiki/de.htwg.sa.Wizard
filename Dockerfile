#FROM hseeberger/scala-sbt
FROM adoptopenjdk/openjdk14:slim
EXPOSE 1233
WORKDIR /wizard
ADD target/scala-2.13/Wizard-assembly-SAR-6.jar /wizard
ADD ./wait-for-it.sh /wizard
CMD java -jar Wizard-assembly-SAR-6.jar
ENV DOCKERENV="TRUE"