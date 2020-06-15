sbt assembly
docker build . -t wizard-root
docker build ./ResultTableModule -t wizard-resulttablemodule
docker build ./CardModule -t wizard-cardmodule
docker build -f ./DBDockerfiles/Dockerfile.MySQL . -t wizard-mysql
docker build -f ./DBDockerfiles/Dockerfile.MongoDB . -t wizard-mongodb
#docker build -f ./DBDockerfiles/MySQL/Dockerfile.MySQL . -t wizard-root-mysql
#docker build -f ./DBDockerfiles/MySQL/Dockerfile.ResultTableModule . -t wizard-resulttablemodule-mysql
sbt clean