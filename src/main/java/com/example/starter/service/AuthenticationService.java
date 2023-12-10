package com.example.starter.service;

import com.example.starter.data.MongoRepositoryImpl;
import com.example.starter.dto.SystemUser;
import com.example.starter.schema.SchemaDefinitions;
import com.example.starter.schema.SchemaValidator;
import com.example.starter.service.auth.JWTAuthService;
import com.example.starter.utils.CryptoUtils;
import com.example.starter.utils.StatusCode;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.json.schema.OutputUnit;
import io.vertx.json.schema.SchemaRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

@Slf4j
public class AuthenticationService extends MongoRepositoryImpl implements RequestValidator {

  private final JWTAuthService jwtAuthHandler;
  private final MongoDBClient mongoDBClient;
  private final SchemaValidator schemaValidator;

  public AuthenticationService(Vertx vertx) {
    super(vertx, "users");
    schemaValidator = new SchemaValidator();
    jwtAuthHandler = new JWTAuthService(vertx);
    mongoDBClient = new MongoDBClient(vertx, new ConnectionInitializer());
  }

  public void register(JsonObject handledUserRequest, RoutingContext ctx) {
    var user = handledUserRequest.mapTo(SystemUser.class);
    registerUser(handledUserRequest, user, ctx);
  }

  private void registerUser(JsonObject handledUserPayload, SystemUser user, RoutingContext context) {
    findUser(user.login())
      .andThen(result -> handleAlreadyExistingLogin(result, context))
      .andThen((AsyncResult<JsonObject> result) -> {
        handleRegistration(handledUserPayload, user, context);
      });
  }

  public Future<JsonObject> findUser(String login) {
    return mongoDBClient.getClient()
      .findOne(getCollectionName(), new JsonObject().put("login", login), null);
  }

  private void handleAlreadyExistingLogin(AsyncResult<JsonObject> result, RoutingContext context) {
    if (result.result() != null) {
      context.response().setStatusCode(StatusCode.CONFLICT).end();
    }
  }

  public Future<Boolean> validatePassword(SystemUser user) {
    return findUser(user.login()).map((JsonObject value) -> {
      SystemUser validatedUser = value.mapTo(SystemUser.class);
      return validatedUser.password().equals(encryptPassword(user.password(), validatedUser.salt()));
    }).otherwise(false);
  }

  private String encryptPassword(String password, String salt) {
    return CryptoUtils.strategy.hash("sha512", CryptoUtils.saltParameters, salt, password);
  }

  private void handleRegistration(JsonObject handledUserPayload, SystemUser user, RoutingContext context) {
    String salt = CryptoUtils.generateSalt();
    handledUserPayload.put("salt", salt);
    handledUserPayload.put("password", encryptPassword(user.password(), salt));
    saveItem(handledUserPayload).andThen(handler -> {
      context.response().setStatusCode(StatusCode.CREATED).end();
    }).otherwise(ctx -> {
      throw new RuntimeException(ExceptionUtils.getMessage(ctx.getCause()));
    });
  }


  @Override
  public OutputUnit validate(JsonObject validatedObject) {
    return schemaValidator.validate(SchemaDefinitions.getAuthRequestDefinition(), validatedObject);
  }

}
