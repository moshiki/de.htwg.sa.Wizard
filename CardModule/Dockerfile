FROM hseeberger/scala-sbt
EXPOSE 1234
WORKDIR /wizard
ADD . /wizard
ENV DOCKERENV="TRUE"
CMD sbt CardModule/run