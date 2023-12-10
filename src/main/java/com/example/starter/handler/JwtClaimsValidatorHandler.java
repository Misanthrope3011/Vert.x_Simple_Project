package com.example.starter.handler;

import com.example.starter.service.auth.JWTAuthService;
import com.example.starter.utils.ErrorMessages;
import com.example.starter.utils.StatusCode;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class JwtClaimsValidatorHandler implements Handler<RoutingContext> {

  @Override
  public void handle(RoutingContext context) {
    if(isTokenExpired(context)) {
      context.response().setStatusCode(StatusCode.UNAUTHORIZED).end(ErrorMessages.ACCES_TOKEN_EXPIRED.getMessage());
    }
    context.next();
  }

  private boolean isTokenExpired(RoutingContext context) {
    return System.currentTimeMillis() > (Long) context.user().get(JWTAuthService.Claims.EXPIRATION);
  }

}
