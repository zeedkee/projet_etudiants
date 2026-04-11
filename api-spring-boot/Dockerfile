# ============================
# Étape 1 : Construction (build)
# ============================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copier le fichier pom.xml d'abord pour profiter du cache Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source et compiler
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================
# Étape 2 : Image d'exécution
# ============================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copier le JAR compilé depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port 8080
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]