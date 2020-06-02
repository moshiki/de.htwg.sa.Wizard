FROM hseeberger/scala-sbt
EXPOSE 1234
WORKDIR /wizard
ADD target/scala-2.13/CardModule-assembly-SAR-6.jar /wizard
ENV DOCKERENV="TRUE"
CMD java -jar CardModule-assembly-SAR-6.jar