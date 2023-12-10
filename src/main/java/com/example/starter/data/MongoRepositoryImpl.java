package com.example.starter.data;

import com.example.starter.service.MongoDBClient;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientDeleteResult;
import lombok.Setter;

import java.util.List;

@Setter

public class MongoRepositoryImpl implements MongoRepository {

  private MongoDBClient mongoDBClient;
  private String collectionName;

  public MongoRepositoryImpl(Vertx vertx, String collectionName) {
    this.mongoDBClient = new MongoDBClient(vertx);
    this.collectionName = collectionName;
  }

  @Override
  public Future<List<JsonObject>> findAllItems() {
    return mongoDBClient.getClient().find(getCollectionName(), new JsonObject());
  }

  @Override
  public Future<List<JsonObject>> findAllByProperty(JsonObject spec) {
    return mongoDBClient.getClient().find(getCollectionName(), spec);
  }

  @Override
  public Future<MongoClientDeleteResult> removeDocument(JsonObject spec) {
    return mongoDBClient.getClient().removeDocument(getCollectionName(), spec);
  }

  @Override
  public Future<String> saveItem(JsonObject data) {
    return mongoDBClient.getClient().insert(getCollectionName(), data);
  }


  @Override
  public String getCollectionName() {
    return collectionName;
  }

}
