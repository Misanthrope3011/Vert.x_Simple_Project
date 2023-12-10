package com.example.starter.data;

import com.example.starter.service.MongoDBClient;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(VertxExtension.class)
public class MongoRepositoryImplTest {

  @Mock
  private MongoClient mongoClient;

  @Test
  @Disabled
  void testFindAllItems(Vertx vertx, VertxTestContext testContext) {
    String collectionName = "testCollection";
    MongoDBClient mockedMongoDBClient = Mockito.mock(MongoDBClient.class);
    MongoRepositoryImpl mongoRepository = new MongoRepositoryImpl(vertx, collectionName);
    Mockito.when(mockedMongoDBClient.getClient()).thenReturn(mongoClient);

    List<JsonObject> expectedData = List.of(
      new JsonObject().put("key", "value1"),
      new JsonObject().put("key", "value2")
    );

    when(mongoClient.find(collectionName, new JsonObject())).thenReturn(Future.succeededFuture(expectedData));

    mongoRepository.findAllItems().onComplete(testContext.succeeding(result -> {
      assertEquals(expectedData, result);
      testContext.completeNow();
    }));
  }

}
