sbt assembly
docker build . -t wizard-root
docker build ./ResultTableModule -t wizard-resulttablemodule
docker build ./CardModule -t wizard-cardmodule
docker build -f ./DBDockerfiles/MySQL/Dockerfile.CardModule . -t wizard-cardmodule-mysql
docker build -f ./DBDockerfiles/MySQL/Dockerfile.RootModule . -t wizard-root-mysql
docker build -f ./DBDockerfiles/MySQL/Dockerfile.ResultTableModule . -t wizard-resulttablemodule-mysql
sbt clean