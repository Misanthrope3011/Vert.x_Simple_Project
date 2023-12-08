package com.example.starter.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static com.example.starter.service.MongoDBClient.ConnectionProperties.CONNECTION_URL;
import static com.example.starter.service.MongoDBClient.ConnectionProperties.DATABASE_NAME;

@Getter
@Slf4j
public class MongoDBClient {

  private MongoClient client;

  public MongoDBClient(Vertx vertx, ConnectionInitializer config) {
    client = MongoClient.createShared(vertx, initMongoClientProperties(config));
  }

  public MongoDBClient(Vertx vertx) {
    client = MongoClient.createShared(vertx, initMongoClientProperties(new ConnectionInitializer()));
  }

  public Future<String> insert(String collection, JsonObject data) {
    return client.insert(collection, data);
  }

  private JsonObject initMongoClientProperties(ConnectionInitializer initializerData) {
    var connectionProperties = new JsonObject();
    connectionProperties.put(CONNECTION_URL, initializerData.connectionString());
    connectionProperties.put(DATABASE_NAME, initializerData.databaseName());

    return connectionProperties;
  }

  @UtilityClass
  public static class ConnectionProperties {
    public static final String CONNECTION_URL = "connection_string";
    public static final String DATABASE_NAME = "db_name";
  }

}
