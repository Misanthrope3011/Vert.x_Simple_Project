package com.example.starter;

import com.example.starter.config.ConfigProperties;
import com.example.starter.dto.Item;
import com.example.starter.response.SimpleResponse;
import com.example.starter.handler.LoginRequestHandler;
import com.example.starter.handler.RegistrationHandler;
import com.example.starter.service.ItemsService;
import com.example.starter.service.RegistrationService;
import com.example.starter.service.auth.JWTAuthService;
import com.example.starter.utils.SchemaValidator;
import com.example.starter.utils.StatusCode;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.ext.web.validation.BodyProcessorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;
import static com.example.starter.utils.CommonUtils.*;

@Slf4j
public class VerticleInitializer extends AbstractVerticle {

  //  private static final int ASCENDING_ORDER = 1;
  private RegistrationService registrationService;
  private Router router;
  private SchemaValidator validator;
  private JWTAuthService jwtAuthHandler;
  private ItemsService itemsService;
  private ConfigProperties configProperties;

  private Handler<RoutingContext> validationFailureHandler = (RoutingContext rc) -> {
    if (rc.failure() instanceof BodyProcessorException exception) {
      rc.response()
        .setStatusCode(StatusCode.BAD_REQUEST)
        .end("validation failed." + ExceptionUtils.getMessage(exception));
    } else {
      rc.response().setStatusCode(StatusCode.INTERNAL_SERVER_ERROR).end("Error occured " + ExceptionUtils.getMessage(rc.failure()));
    }
  };

  @Override
  public void start(Promise<Void> startPromise) {
    initObjects();

//    var userNameConstraint = new JsonObject()
//      .put("collection", "users")
//      .put("keys", new JsonObject().put("login", ASCENDING_ORDER))
//      .put("options", new JsonObject().put("unique", true));
//
//    mongoDBClient.getClient().createIndex("users", userNameConstraint);

    SessionStore sessionStore = SessionStore.create(vertx);
    router.route().handler(SessionHandler.create(sessionStore));

    router.get("/items")
      .handler(jwtAuthHandler.getJwtAuthHandler())
      .handler(this::getUserItem)
      .failureHandler(validationFailureHandler);

    router.post("/items")
      .handler(BodyHandler.create())
      .handler(jwtAuthHandler.getJwtAuthHandler())
      .handler(this::saveItem)
      .failureHandler(validationFailureHandler);

    router.post("/login")
      .consumes("application/json")
      .handler(BodyHandler.create())
      .handler(new LoginRequestHandler(vertx));

    router.post("/register")
      .consumes("application/json")
      .handler(BodyHandler.create())
      .handler(new RegistrationHandler(vertx))
      .failureHandler(validationFailureHandler);

    vertx.createHttpServer().requestHandler(router)
      .listen(3000, onHttpServerStartupHandler(startPromise));
  }

  private Handler<AsyncResult<HttpServer>> onHttpServerStartupHandler(Promise<Void> startPromise) {
    return http -> {
      if (http.succeeded()) {
        startPromise.complete();
      } else {
        startPromise.fail(http.cause());
      }
    };
  }

  private void getUserItem(RoutingContext result) {
    itemsService.findAllItems().andThen(items -> {
      List<Item> viewItems = items.result().parallelStream()
        .map(jsonObject -> jsonObject.mapTo(Item.class))
        .toList();
      result.response().setStatusCode(StatusCode.OK).end(SimpleResponse.toGenericResponse(viewItems).toBuffer());
    });
  }

  private void saveItem(RoutingContext routingCtx) {
    JsonObject requestItem = getBodyFromRequest(routingCtx);
    requestItem.put("uuid", routingCtx.get("uuid"));
    itemsService.saveItem(requestItem).onSuccess(handler -> {
      routingCtx.response().setStatusCode(StatusCode.CREATED).end();
    });
  }

  private void initObjects() {
    configProperties = new ConfigProperties();
    itemsService = new ItemsService(vertx);
    router = Router.router(vertx);
    validator = new SchemaValidator();
    registrationService = new RegistrationService(vertx);
    jwtAuthHandler = new JWTAuthService(vertx);
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }

}
