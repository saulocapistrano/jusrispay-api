# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar pom.xml e baixar dependências (cache layer)
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copiar código fonte e compilar
COPY src ./src
RUN mvn -q -DskipTests package

# Stage 2: Runtime
FROM eclipse-temurin:17-jre

WORKDIR /app

# Criar usuário não-root
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copiar jar do stage de build
COPY --from=build /app/target/*.jar app.jar

# Mudar ownership para usuário não-root
RUN chown appuser:appuser app.jar

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Variáveis de ambiente opcionais
ENV JAVA_OPTS=""

# Entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

