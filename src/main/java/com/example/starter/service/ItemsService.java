package com.example.starter.service;

import com.example.starter.data.MongoRepositoryImpl;
import com.example.starter.schema.SchemaDefinitions;
import com.example.starter.schema.SchemaValidator;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.OutputUnit;


public class ItemsService extends MongoRepositoryImpl implements RequestValidator {

  private final MongoDBClient mongoDBClient;
  private final SchemaValidator schemaValidator;

  public ItemsService(Vertx vertx) {
    super(vertx, "items");
    this.mongoDBClient = new MongoDBClient(vertx);
    this.schemaValidator = new SchemaValidator();
  }

  @Override
  public OutputUnit validate(JsonObject object) {
   return schemaValidator.validate(SchemaDefinitions.getItemDefinition(), object);
  }

}
