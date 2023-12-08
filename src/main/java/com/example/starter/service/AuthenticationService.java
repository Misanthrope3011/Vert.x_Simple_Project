package com.example.starter.service;

import com.example.starter.dto.SystemUser;
import com.example.starter.service.auth.JWTAuthService;
import com.example.starter.utils.CryptoUtils;
import com.example.starter.utils.StatusCode;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Slf4j
public class AuthenticationService implements RequestValidator, DatasourceAccessService {

  private final JWTAuthService jwtAuthHandler;
  private final MongoDBClient mongoDBClient;

  public AuthenticationService(Vertx vertx) {
    jwtAuthHandler = new JWTAuthService(vertx);
    mongoDBClient = new MongoDBClient(vertx, new ConnectionInitializer());
  }

  public void register(JsonObject handledUserRequest, RoutingContext ctx) {
    var user = handledUserRequest.mapTo(SystemUser.class);
    registerUser(handledUserRequest, user, ctx);
  }

  private void registerUser(JsonObject handledUserPayload, SystemUser user, RoutingContext context) {
    findUser(user)
      .andThen(result -> handleAlreadyExistingLogin(result, context))
      .andThen((AsyncResult<JsonObject> result) -> {
        handleRegistration(handledUserPayload, user, context);
      });
  }

  private void handleAlreadyExistingLogin(AsyncResult<JsonObject> result, RoutingContext context) {
    if (result.result() != null) {
      context.response().setStatusCode(StatusCode.CONFLICT).end();
    }
  }

  public Future<Boolean> validatePassword(SystemUser user) {
    return findUser(user).map((JsonObject value) -> {
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
    saveUser(handledUserPayload).andThen(handler -> {
      context.response().setStatusCode(StatusCode.CREATED).end();
    }).otherwise(ctx -> {
      throw new RuntimeException(ExceptionUtils.getMessage(ctx.getCause()));
    });
  }

  private Future<String> saveUser(JsonObject user) {
    return mongoDBClient.insert(getCollectionName(), user);
  }

  private Future<JsonObject> findUser(SystemUser user) {
    return mongoDBClient.getClient()
      .findOne(getCollectionName(), new JsonObject().put("login", user.login()), null);
  }

  @Override
  public String getCollectionName() {
    return "users";
  }

  @Override
  public void validate(JsonObject validatedObject) throws Exception {

  }
}
