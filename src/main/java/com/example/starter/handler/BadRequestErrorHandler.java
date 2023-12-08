package com.example.starter.handler;

import com.example.starter.response.SimpleResponse;
import com.example.starter.utils.StatusCode;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.ErrorHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class BadRequestErrorHandler implements ErrorHandler {

  @Override
  public void handle(RoutingContext routingContext) {
    if (routingContext.failure() instanceof IllegalArgumentException ex) {
      routingContext.response().setStatusCode(StatusCode.BAD_REQUEST).end(SimpleResponse.toGenericResponse(ExceptionUtils.getMessage(ex)).toBuffer());
    }
  }

}
