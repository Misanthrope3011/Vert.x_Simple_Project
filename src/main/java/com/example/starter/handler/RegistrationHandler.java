package com.example.starter.handler;

import com.example.starter.service.RegistrationService;
import com.example.starter.utils.SchemaValidator;
import com.example.starter.utils.StatusCode;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static com.example.starter.utils.CommonUtils.*;

public class RegistrationHandler implements Handler<RoutingContext> {

  private RegistrationService registrationService;
  private SchemaValidator schemaValidator;

  public RegistrationHandler(Vertx vertx) {
    schemaValidator = new SchemaValidator();
    registrationService = new RegistrationService(vertx);
  }

  @Override
  public void handle(RoutingContext event) {
    JsonObject jsonObject = getBodyFromRequest(event);
    schemaValidator.validator(jsonObject);
    registrationService.register(jsonObject, event);
  }
}
