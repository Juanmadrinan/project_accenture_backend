# project_accenture_backend

🚀 Instalación Local
1. Clonar el repositorio
bashgit clone [https://github.com/tu-usuario/franchise-management.git](https://github.com/Juanmadrinan/project_accenture_backend.git)

cd project_accenture_backend

3. Variables de entorno:
   MongoAtlas: Teniendo en cuenta que es una prueba tecnica deje expuesto una prueba de MongoAtlas para ti.
   AWS: Configura tus credenciales de AWS para el ingreso con "aws configure".
   Terraform: Funciona con tus datos configurados
   
5. Compilar el proyecto
bash - mvn clean install

7. Ejecutar la aplicación
bash - mvn spring-boot:run


La aplicación estará disponible en http://localhost:8080

🐳 Ejecución con Docker
Construir y ejecutar con Docker Compose
bash# Construir la imagen
docker-compose build

# Iniciar los servicios y ejecutar contenedor de manera rapida con Compose :)
docker-compose up --build -d 

# ☁️ Despliegue en AWS y Ejecución de Terraform.
1. Configurar AWS CLI
bash- aws configure
Ingresa tus credenciales:

AWS Access Key ID
AWS Secret Access Key
Default region: us-east-2
Output format: json

2. Inicializar Terraform
bash - cd terraform
terraform init
3. Revisar el plan de infraestructura
bashterraform plan
4. Aplicar la infraestructura
bashterraform apply
Terraform creará:

Algunas adicionales en la configuración AWS y Terraform
📦 ECR Repository (para almacenar la imagen Docker)
🚀 ECS Cluster (Fargate)
🔒 Security Groups
📊 CloudWatch Logs
🎭 IAM Roles

#### Finalmente deje un Script de Bash que desplego mi Aplicación en AWS, ejecutando el terraform y docker, sin embargo para las pruebas del funcionamiento recomiendo hacer el despliegue local para hacer las pruebas porque hay problemas por demora de respuesta en el servidor, por cuestiones de tiempo teniendo en cuenta que hoy a las 12 de la tarde termina el tiempo de entrega, especifico lo terminado:

# 1. Arquitectura y Estructura ✅

 Clean Architecture implementada (Domain, Application, Infrastructure)
 Hexagonal Architecture (Ports & Adapters)
 Separación clara de capas con dependencias correctas
 Domain-Driven Design (DDD) aplicado

# 2. Capa de Dominio (Domain Layer) ✅
 Value Objects:
 FranchiseId, BranchId, ProductId
 Name (con validaciones 3-100 caracteres)
 Stock (no negativo, operaciones inmutables)

 ### Entidades:
 Franchise
 Branch
 Product

### Excepciones de Dominio:
 DomainException (base)
 InvalidNameException
 InvalidStockException
 FranchiseNotFoundException
 BranchNotFoundException
 ProductNotFoundException

 ###  Ports (In):
 9 casos de uso (3 Franchise, 3 Branch, 5 Product)
 Commands con validaciones integradas

 ###  Ports (Out):
 3 interfaces de persistencia

 # 3. Capa de Aplicación (Application Layer)  ✅

### Services:
 FranchiseService (3 casos de uso)
 BranchService (3 casos de uso)
 ProductService (5 casos de uso)

 # 4. Programación Reactiva (Mono/Flux) ✅
 Validaciones de negocio

 # 5. Capa de Infraestructura (Infrastructure Layer) ✅ 

### MongoDB Entities:
 FranchiseEntity, BranchEntity, ProductEntity


### Repositorios Reactivos:
 FranchiseReactiveRepository
 BranchReactiveRepository
 ProductReactiveRepository


### Mappers:
 FranchiseMapper, BranchMapper, ProductMapper


### Adapters (Out):
FranchisePersistenceAdapter (3 implementaciones)


### Controllers REST:
 FranchiseController (3 endpoints)
 BranchController (3 endpoints)
 ProductController (5 endpoints)


 DTOs:
### 7 Request DTOs
### 5 Response DTOs


### Exception Handling:
 GlobalExceptionHandler


### Configuración:
 BeanConfiguration
 MongoConfig
 WebFluxConfig

# 5. Endpoints Implementados ✅ 
### Obligatorios (6/6):
 POST /api/franchises - Crear franquicia
 POST /api/branches - Crear sucursal
 POST /api/products - Crear producto
 PUT /api/products/{id}/stock - Modificar stock
 GET /api/products/top-stock/franchise/{id} - Top stock por sucursal ⭐
 DELETE /api/products/{id} - Eliminar producto

### Puntos Extra (3/3):

 PUT /api/franchises/{id}/name - Actualizar nombre franquicia
 PUT /api/branches/{id}/name - Actualizar nombre sucursal
 PUT /api/products/{id}/name - Actualizar nombre producto

### Consultas adicionales:

 GET /api/franchises/{id} - Obtener franquicia
 GET /api/branches/{id} - Obtener sucursal

# 6. Tecnologías y Patrones ✅
 Spring Boot 
 Spring WebFlux (Programación Reactiva)
 Spring Data MongoDB Reactive
 Java 21
 Maven
 Lombok
 Jakarta Validation
 MongoDB (persistencia)

# 7. Validaciones ✅
 Validaciones en DTOs (@Valid)
 Validaciones en Commands
 Validaciones en Value Objects
 Manejo global de excepciones

