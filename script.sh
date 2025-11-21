#!/bin/bash

# 🚀 Script de despliegue completo para AWS ECS

set -e  # Detener si hay algún error

REGION="us-east-2"
CLUSTER="franchise-cluster-dev"
SERVICE="franchise-service-dev"

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🚀 Desplegando Franchise Management en AWS"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
  echo "❌ Error: Ejecuta este script desde la raíz del proyecto"
  exit 1
fi

# 1. Verificar AWS CLI
echo "📋 Paso 1/6: Verificando credenciales AWS..."
aws sts get-caller-identity > /dev/null
echo "✅ Credenciales válidas"
echo ""

# 2. Crear infraestructura con Terraform
echo "🏗️  Paso 2/6: Creando infraestructura..."
cd infra

if [ ! -d ".terraform" ]; then
  echo "Inicializando Terraform..."
  terraform init
fi

terraform apply -auto-approve
cd ..
echo "✅ Infraestructura creada"
echo ""

# 3. Obtener ECR URL
echo "📦 Paso 3/6: Preparando Docker..."
ECR_URL=$(cd infra && terraform output -raw ecr_repository_url)
echo "ECR Repository: $ECR_URL"

# 4. Autenticarse en ECR
echo "🔐 Autenticando en ECR..."
aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR_URL
echo "✅ Autenticación exitosa"
echo ""

# 5. Construir y subir imagen
echo "🐳 Paso 4/6: Construyendo imagen Docker..."
docker build -t franchise-management:latest .
echo "✅ Imagen construida"
echo ""

echo "📤 Paso 5/6: Subiendo imagen a ECR..."
docker tag franchise-management:latest $ECR_URL:latest
docker push $ECR_URL:latest
echo "✅ Imagen subida exitosamente"
echo ""

# 6. Desplegar en ECS
echo "🚀 Paso 6/6: Desplegando en ECS..."
aws ecs update-service \
  --cluster $CLUSTER \
  --service $SERVICE \
  --force-new-deployment \
  --region $REGION \
  --output text > /dev/null

echo "✅ Despliegue iniciado"
echo ""

# 7. Esperar a que la tarea esté corriendo
echo "⏳ Esperando a que la aplicación esté lista..."
for i in {1..30}; do
  RUNNING=$(aws ecs describe-services \
    --cluster $CLUSTER \
    --services $SERVICE \
    --region $REGION \
    --query 'services[0].runningCount' \
    --output text)
  
  if [ "$RUNNING" == "1" ]; then
    break
  fi
  
  echo "   Intento $i/30: Esperando... (Running: $RUNNING)"
  sleep 10
done

if [ "$RUNNING" != "1" ]; then
  echo "⚠️  La aplicación está tardando más de lo esperado"
  echo "Revisa los logs con: aws logs tail /ecs/franchise-app --follow --region $REGION"
  exit 1
fi

echo "✅ Aplicación corriendo"
echo ""

# 8. Obtener IP pública
echo "🌐 Obteniendo IP pública..."
TASK_ARN=$(aws ecs list-tasks --cluster $CLUSTER --service-name $SERVICE --region $REGION --query 'taskArns[0]' --output text)
ENI_ID=$(aws ecs describe-tasks --cluster $CLUSTER --tasks $TASK_ARN --region $REGION --query 'tasks[0].attachments[0].details[?name==`networkInterfaceId`].value' --output text)
PUBLIC_IP=$(aws ec2 describe-network-interfaces --network-interface-ids $ENI_ID --region $REGION --query 'NetworkInterfaces[0].Association.PublicIp' --output text)

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🎉 ¡Despliegue Completado Exitosamente!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📍 IP Pública: $PUBLIC_IP"
echo "🔗 URL Base: http://$PUBLIC_IP:8080"
echo "💚 Health Check: http://$PUBLIC_IP:8080/actuator/health"
echo ""
echo "🧪 Prueba rápida:"
echo "curl http://$PUBLIC_IP:8080/actuator/health"
echo ""
echo "📊 Monitoreo:"
echo "aws logs tail /ecs/franchise-app --follow --region $REGION"
echo ""
echo "🗑️  Para eliminar todo:"
echo "cd terraform && terraform destroy"
echo ""
