package com.example.starter.service;

import com.example.starter.schema.SchemaDefinitions;
import com.example.starter.validator.SchemaValidator;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;


public class ItemsService implements RequestValidator, DatasourceAccessService {

  private final MongoDBClient mongoDBClient;
  private final SchemaValidator schemaValidator;

  public ItemsService(Vertx vertx) {
    this.mongoDBClient = new MongoDBClient(vertx);
    this.schemaValidator = new SchemaValidator();
  }

  public Future<List<JsonObject>> findAllItems() {
    return mongoDBClient.getClient().find(getCollectionName(), new JsonObject());
  }

  public Future<String> saveItem(JsonObject data) {
    return mongoDBClient.insert(getCollectionName(), data);
  }

  @Override
  public String getCollectionName() {
    return "items";
  }

  @Override
  public void validate(JsonObject object) {
    schemaValidator.validate(SchemaDefinitions.getItemDefinition(), object);
  }

}
