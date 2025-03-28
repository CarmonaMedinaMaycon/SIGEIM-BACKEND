# Etapa de construcción
FROM openjdk:21-slim AS build

# Instalar Maven
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://archive.apache.org/dist/maven/maven-3/3.8.5/binaries/apache-maven-3.8.5-bin.tar.gz && \
    tar -xvzf apache-maven-3.8.5-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.8.5/bin/mvn /usr/bin/mvn

WORKDIR /app

# Copiar el archivo pom.xml y descargar dependencias
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copiar el resto del código y compilar el proyecto
COPY . . 
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:21-slim
WORKDIR /app

# Copiar el archivo JAR generado en la etapa de construcción
COPY --from=build /app/target/sigeim-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre la aplicación
EXPOSE 8081

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]

