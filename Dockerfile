FROM hseeberger/scala-sbt
WORKDIR /wizard
ADD . /wizard
CMD sbt run