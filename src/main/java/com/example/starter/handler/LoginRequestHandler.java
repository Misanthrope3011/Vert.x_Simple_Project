package com.example.starter.handler;

import com.example.starter.dto.SystemUser;
import com.example.starter.service.AuthenticationService;
import com.example.starter.service.auth.JWTAuthService;
import com.example.starter.utils.StatusCode;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static com.example.starter.utils.CommonUtils.*;

public class LoginRequestHandler implements Handler<RoutingContext> {

  private AuthenticationService registrationService;
  private JWTAuthService jwtAuthService;

  public LoginRequestHandler(Vertx vertx) {
    jwtAuthService = new JWTAuthService(vertx);
    registrationService = new AuthenticationService(vertx);
  }

  @Override
  public void handle(RoutingContext event) {
    registrationService.validatePassword(getBodyFromRequest(event).mapTo(SystemUser.class))
      .onComplete(result -> {
        if (result.result()) {
          SystemUser user = event.body().asJsonObject().mapTo(SystemUser.class);
          event.put("uuid", user.uuid());
          event.response().setStatusCode(StatusCode.OK).end(new JsonObject().put("token", jwtAuthService.generateToken(user)).toBuffer());
        } else {
          event.response().setStatusCode(StatusCode.UNAUTHORIZED).end();
        }
      });
  }
}
