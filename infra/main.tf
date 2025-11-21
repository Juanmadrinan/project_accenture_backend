terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-2"

}

# Obtener la VPC por defecto
data "aws_vpc" "default" {
  default = true
}

# Obtener las subnets de la VPC por defecto
data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

# ECR Repository para tu imagen Docker
resource "aws_ecr_repository" "franchise_app" {
  name = "franchise-management"

  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    Project     = "Franchise Management"
    Environment = "dev"
  }
}

# ECS Cluster para ejecutar tu contenedor
resource "aws_ecs_cluster" "main" {
  name = "franchise-cluster-dev"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }

  tags = {
    Project     = "Franchise Management"
    Environment = "dev"
  }
}

# Security Group para ECS
resource "aws_security_group" "ecs_sg" {
  name        = "franchise-ecs-sg-dev"
  description = "Security group for ECS tasks"
  vpc_id      = data.aws_vpc.default.id  # ← CORREGIDO

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Project     = "Franchise Management"
    Environment = "dev"
  }
}

# CloudWatch Log Group
resource "aws_cloudwatch_log_group" "ecs_logs" {
  name              = "/ecs/franchise-app"
  retention_in_days = 7

  tags = {
    Project     = "Franchise Management"
    Environment = "dev"
  }
}

# IAM Role para ECS Task Execution
resource "aws_iam_role" "ecs_execution_role" {
  name = "franchise-ecs-execution-role-dev"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Project     = "Franchise Management"
    Environment = "dev"
  }
}

resource "aws_iam_role_policy_attachment" "ecs_execution_role_policy" {
  role       = aws_iam_role.ecs_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# IAM Role para ECS Task (runtime)
resource "aws_iam_role" "ecs_task_role" {
  name = "franchise-ecs-task-role-dev"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Project     = "Franchise Management"
    Environment = "dev"
  }
}

# Task Definition usando TU imagen Docker
resource "aws_ecs_task_definition" "app" {
  family                   = "franchise-app-dev"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_role.arn

  container_definitions = jsonencode([
    {
      name      = "franchise-app"
      image     = "${aws_ecr_repository.franchise_app.repository_url}:latest"
      essential = true

      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
          protocol      = "tcp"
        }
      ]

      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        },
        {
          name  = "SERVER_PORT"
          value = "8080"
        },
        {
          name  = "MONGODB_URI"
          value = "mongodb+srv://juanmadrinan:6ZTBz9vT1KcPMsKE@cluster0.9p8pcf2.mongodb.net/"
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs_logs.name
          "awslogs-region"        = "us-east-2"
          "awslogs-stream-prefix" = "ecs"
        }
      }

      healthCheck = {
        command     = ["CMD-SHELL", "wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1"]
        interval    = 30
        timeout     = 5
        retries     = 3
        startPeriod = 60
      }
    }
  ])

  tags = {
    Project     = "Franchise Management"
    Environment = "dev"
  }
}

# ECS Service para mantener la tarea corriendo
resource "aws_ecs_service" "app" {
  name            = "franchise-service-dev"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.ecs_sg.id]
    assign_public_ip = true
  }

  tags = {
    Project     = "Franchise Management"
    Environment = "dev"
  }
}

# Outputs útiles
output "ecr_repository_url" {
  description = "URL del repositorio ECR"
  value       = aws_ecr_repository.franchise_app.repository_url
}

output "ecs_cluster_name" {
  description = "Nombre del cluster ECS"
  value       = aws_ecs_cluster.main.name
}

output "ecs_service_name" {
  description = "Nombre del servicio ECS"
  value       = aws_ecs_service.app.name
}

output "next_steps" {
  description = "Siguientes pasos"
  value = <<-EOT
    1. Construye tu imagen: docker build -t franchise-management .
    2. Autentícate en ECR: aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin ${aws_ecr_repository.franchise_app.repository_url}
    3. Tagea la imagen: docker tag franchise-management:latest ${aws_ecr_repository.franchise_app.repository_url}:latest
    4. Sube la imagen: docker push ${aws_ecr_repository.franchise_app.repository_url}:latest
    5. Fuerza nuevo despliegue: aws ecs update-service --cluster ${aws_ecs_cluster.main.name} --service ${aws_ecs_service.app.name} --force-new-deployment --region us-east-2
  EOT
}