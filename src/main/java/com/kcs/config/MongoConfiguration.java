package com.kcs.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.kcs")
class MongoConfiguration extends AbstractMongoClientConfiguration {

  private final String connectionString;
  private final String databaseName;

  MongoConfiguration(@Value("${MONGO_URI}") String connectionString,
                     @Value("${MONGO_DATABASE}") String databaseName) {
    this.connectionString = connectionString;
    this.databaseName = databaseName;
  }

  @Bean
  MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDbFactory) {
    return new MongoTransactionManager(mongoDbFactory);
  }

  @Override
  protected String getDatabaseName() {
    return databaseName;
  }

  @Override
  public MongoClient mongoClient() {
    var connectionStringObject = new ConnectionString(connectionString);
    var mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(connectionStringObject)
        .build();
    return MongoClients.create(mongoClientSettings);
  }
}
