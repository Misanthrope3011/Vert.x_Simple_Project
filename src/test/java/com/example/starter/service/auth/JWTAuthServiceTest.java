package com.example.starter.service.auth;

import com.example.starter.dto.SystemUser;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
@Slf4j
class JWTAuthServiceTest {

  @Test
  @SuppressWarnings("deprecation")
  void shouldReturnCorrectUser_whenValidaTokenPassed(Vertx vertx, VertxTestContext testContext) {
    JWTAuthService authService = new JWTAuthService(vertx);
    SystemUser user = new SystemUser("username", "password", "salt", "1111");
    String token = authService.generateToken(user);

    authService.getProvider().authenticate(new JsonObject().put("token", token))
      .onComplete(ctx -> {
          testContext.verify(() -> {
            Assertions.assertEquals("username", ctx.result().subject());
            testContext.completeNow();
          });
        });
  }

}


