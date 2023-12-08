package com.example.starter;

import com.example.starter.utils.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileInputStream;
import java.io.IOException;

@ExtendWith(VertxExtension.class)
public class TestVerticleInitializer {

  @BeforeEach
  void init(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new VerticleInitializer(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void shouldReturnHttpStatus401_whenUnauthorized(Vertx vertx, VertxTestContext testContext) {
    Future<Buffer> compose = vertx.createHttpClient()
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
  void shouldSaveRequestInDB(Vertx vertx, VertxTestContext testContext) throws IOException {
    var objectMapper = new ObjectMapper();
    JsonObject loadContent = new JsonObject(new String(new FileInputStream("src/test/resources/request.json").readAllBytes()));
    Future<Buffer> compose = vertx.createHttpClient()
      .request(HttpMethod.POST, 3000, "127.0.0.1", "/register")
      .andThen(request -> {
        request.result().send(loadContent.toBuffer());
      })
      .compose(HttpClientRequest::send)
      .compose(httpClientResponse -> {
        testContext.verify(() -> {
          Assertions.assertEquals(httpClientResponse.statusCode(), StatusCode.UNAUTHORIZED);
          testContext.completeNow();
        });
        return null;
      });
  }

  @AfterEach
  void tearDown(VertxTestContext context) {
    context.completeNow();
  }

}
