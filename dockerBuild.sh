docker volume create --name gradle-cache
docker run --rm -v gradle-cache:/home/gradle/.gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle:6.2.2 gradle build
docker build -t kotikokki .
#docker-compose up
