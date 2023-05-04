 #!/bin/sh

sudo docker build --build-arg JAR_FILE=target/*.jar -t myorg/myapp -t gurkefar/unlockit:v1 .
sudo docker push gurkefar/unlockit:v1