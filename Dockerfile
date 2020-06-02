FROM hseeberger/scala-sbt
WORKDIR /wizard
ADD target/scala-2.13/Wizard-assembly-SAR-6.jar /wizard
CMD java -jar Wizard-assembly-SAR-6.jar