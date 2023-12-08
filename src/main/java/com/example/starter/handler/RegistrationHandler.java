package com.example.starter.handler;

import com.example.starter.schema.SchemaDefinitions;
import com.example.starter.service.AuthenticationService;
import com.example.starter.validator.SchemaValidator;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static com.example.starter.utils.CommonUtils.*;

public class RegistrationHandler implements Handler<RoutingContext> {

  private final AuthenticationService registrationService;
  private final SchemaValidator schemaValidator;

  public RegistrationHandler(Vertx vertx) {
    schemaValidator = new SchemaValidator();
    registrationService = new AuthenticationService(vertx);
  }

  @Override
  public void handle(RoutingContext event) {
    JsonObject registerRequest = getBodyFromRequest(event);
      try {
          schemaValidator.validate(SchemaDefinitions.getAuthRequestDefinition(), registerRequest);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
      registrationService.register(registerRequest, event);
  }

}
