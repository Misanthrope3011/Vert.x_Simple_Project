package com.example.starter.service.auth;

import com.example.starter.dto.SystemUser;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(VertxExtension.class)
class JWTAuthServiceTest {

  @Test
  @SuppressWarnings("deprecation")
  void shouldReturnCorrectClaims_whenValidTokenPassed(Vertx vertx, VertxTestContext testContext) {
    JWTAuthService authService = new JWTAuthService(vertx);
    SystemUser user = new SystemUser("username21", "password", "salt", "1111");
    String token = authService.generateToken(user);

   //TODO: replace with TokenCredentials
    authService.getProvider().authenticate(new JsonObject().put("token", token))
      .onComplete(ctx -> {
          testContext.verify(() -> {
            Assertions.assertEquals("username21", ctx.result().subject());
            testContext.completeNow();
          });
        });
  }

}


