package com.example.starter.data;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientDeleteResult;

import java.util.List;

public interface MongoRepository {

  Future<List<JsonObject>> findAllItems();
  Future<String> saveItem(JsonObject data);
  String getCollectionName();
  Future<List<JsonObject>> findAllByProperty(JsonObject spec);

  Future<MongoClientDeleteResult> removeDocument(JsonObject spec);

}
