FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app
COPY . .

RUN mkdir -p bin
RUN find src -name "*.java" -type f -print | xargs javac -d bin -cp bin

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/bin ./bin

COPY --from=build /app/data ./data
CMD ["java", "-cp", "bin", "main.Main"]
