package com.example.starter;

import com.example.starter.service.MongoDBClient;
import com.example.starter.utils.StatusCode;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileInputStream;
import java.io.IOException;

@ExtendWith(VertxExtension.class)
public class TestVerticleInitializer {


  @BeforeEach
  void init(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new VerticleInitializer(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  @Disabled
  void shouldReturnHttpStatus401_whenUnauthorized(Vertx vertx, VertxTestContext testContext) {
    vertx.createHttpClient()
      .request(HttpMethod.GET, 3000, "127.0.0.1", "/items")
      .compose(HttpClientRequest::send)
      .compose(httpClientResponse -> {
        testContext.verify(() -> {
          Assertions.assertEquals(httpClientResponse.statusCode(), StatusCode.UNAUTHORIZED);
          testContext.completeNow();
        });
        return null;
      });
  }

  @Test
  @Disabled
  void shouldSaveRequestInDB_whenValidRequestProvided(Vertx vertx, VertxTestContext testContext) throws IOException {
    try(FileInputStream requestBodyStream = new FileInputStream("src/test/resources/request.json")) {
      var jsonLoadContent =  new JsonObject(new String(requestBodyStream.readAllBytes()));
      vertx.createHttpClient()
        .request(HttpMethod.POST, 3000, "127.0.0.1", "/register")
        .compose(request -> {
          request.putHeader("Content-Type", "application/json");
          return request.send(jsonLoadContent.toBuffer());
        })
        .onComplete(httpClientResponse -> {
          testContext.verify(() -> {
            Assertions.assertEquals( StatusCode.CREATED, httpClientResponse.result().statusCode());
            testContext.completeNow();
          });
        });
    }
  }

  @AfterEach
  void tearDown(VertxTestContext context) {

  }

}
