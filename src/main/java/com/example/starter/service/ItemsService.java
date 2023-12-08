package com.example.starter.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;


public class ItemsService {

  private MongoDBClient mongoDBClient;

  public ItemsService(Vertx vertx) {
    this.mongoDBClient = new MongoDBClient(vertx);
  }

  public Future<List<JsonObject>> findAllItems() {
    return mongoDBClient.getClient().find("items", new JsonObject());
  }

  public Future<String> saveItem(JsonObject data) {
    return mongoDBClient.insert("items", data);
  }

}
