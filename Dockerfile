FROM maven:3.9.6-eclipse-temurin-21 AS build

LABEL maintainer="franchise-management"
LABEL stage="build"

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de Maven primero (para cache de dependencias)
COPY pom.xml .
COPY src ./src

# Descargar dependencias y compilar (skip tests para build más rápido)
RUN mvn clean package -DskipTests

# ============================================
# STAGE 2: Runtime
# ============================================

LABEL maintainer="franchise-management"
LABEL stage="build"

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de Maven primero (para cache de dependencias)
COPY pom.xml .
COPY src ./src

# Descargar dependencias y compilar (skip tests para build más rápido)
RUN mvn clean package -DskipTests

# ============================================
# STAGE 2: Runtime (Ahora usando una imagen JRE 21 más ligera)
# ============================================
# OPTIMIZACIÓN: Usamos el JRE 21 (Runtime Environment) que es más pequeño que el JDK
# JRE es suficiente para EJECUTAR la aplicación.
FROM eclipse-temurin:21-jre-alpine AS runtime

LABEL maintainer="franchise-management"
LABEL stage="runtime"

# Instalar curl para health checks (en la base Alpine, es 'apk add')
RUN apk add --no-cache curl

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Directorio de trabajo
WORKDIR /app

# Copiar JAR desde stage de build
COPY --from=build /app/target/*.jar app.jar

# Variables de entorno con valores por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV MONGODB_URI=mongodb+srv://juanmadrinan:6ZTBz9vT1KcPMsKE@cluster0.9p8pcf2.mongodb.net/
ENV SERVER_PORT=8080

# Exponer puerto
EXPOSE 8080

# Health check (mantener como está, es robusto)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de inicio
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-XX:MaxRAMPercentage=75.0", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", \
            "app.jar"]