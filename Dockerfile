FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache git

WORKDIR /app

RUN git clone https://github.com/engarnietemp/EGBank.git . || (echo "Error: impossible to clone the repo" && exit 1)

RUN javac -d bin -sourcepath src $(find src -name "*.java")

CMD ["java", "-cp", "bin", "main.Main"]