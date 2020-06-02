FROM hseeberger/scala-sbt
EXPOSE 54251
WORKDIR /wizard
ADD . /wizard
ENV DOCKERENV="TRUE"
CMD sbt ResultTableModule/run