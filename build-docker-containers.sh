sbt assembly
docker build . -t wizard-root
docker build ./ResultTableModule -t wizard-resulttablemodule
docker build ./CardModule -t wizard-cardmodule