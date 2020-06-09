sbt assembly
docker build . -t wizard-root
docker build ./ResultTableModule -t wizard-resulttablemodule
docker build ./CardModule -t wizard-cardmodule
docker build -f ./DBDockerfiles/MySQL/Dockerfile.CardModule . -t wizard-mysql-cardmodule
docker build -f ./DBDockerfiles/MySQL/Dockerfile.RootModule . -t wizard-mysql-root
docker build -f ./DBDockerfiles/MySQL/Dockerfile.ResultTableModule . -t wizard-mysql-resulttablemodule