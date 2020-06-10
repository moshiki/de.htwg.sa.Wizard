#FROM hseeberger/scala-sbt
FROM adoptopenjdk/openjdk14:slim
EXPOSE 1233
WORKDIR /wizard
ADD target/scala-2.13/Wizard-assembly-SAR-6.jar /wizard
ADD ./wait-for-it.sh /wizard
RUN chmod gu+x ./wait-for-it.sh
CMD java -jar Wizard-assembly-SAR-6.jar
ENV DOCKERENV="TRUE"
ENV DATABASE_HOST="wizard-root-db:3306"
ENV RESULTTABLEMODULE_HOST="resulttablemodule:54251"
ENV CARDMODULE_HOST="cardmodule:1234"
ENV DATABASE_HOST="wizard-root-db:3306"