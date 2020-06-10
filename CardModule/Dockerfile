#FROM hseeberger/scala-sbt
FROM adoptopenjdk/openjdk14:slim
EXPOSE 1234
WORKDIR /wizard
ADD target/scala-2.13/CardModule-assembly-SAR-6.jar /wizard
ADD ./wait-for-it.sh /wizard
RUN chmod gu+x ./wait-for-it.sh
ENV DOCKERENV="TRUE"
ENV DATABASE_HOST="wizard-cardmodule-db:3306"
CMD java -jar CardModule-assembly-SAR-6.jar