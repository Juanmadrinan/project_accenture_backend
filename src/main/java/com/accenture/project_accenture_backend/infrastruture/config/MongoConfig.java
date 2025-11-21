package com.accenture.project_accenture_backend.infrastruture.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(
        basePackages = "com.franchise.management.infrastructure.adapter.out.persistence.mongodb.repository"
)
@EnableReactiveMongoAuditing
public class MongoConfig {

}
