#FROM hseeberger/scala-sbt
FROM adoptopenjdk/openjdk14:slim
EXPOSE 54251
WORKDIR /wizard
ADD target/scala-2.13/ResultTableModule-assembly-SAR-6.jar /wizard
ADD ./wait-for-it.sh /wizard
RUN chmod gu+x ./wait-for-it.sh
ENV DOCKERENV="TRUE"
#ENV DATABASE_HOST="resulttablemoduledb:3306"
CMD java -jar ResultTableModule-assembly-SAR-6.jar