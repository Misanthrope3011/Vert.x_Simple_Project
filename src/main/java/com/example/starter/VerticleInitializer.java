package com.example.starter;

import com.example.starter.config.ConfigProperties;
import com.example.starter.dto.Item;
import com.example.starter.handler.BadRequestErrorHandler;
import com.example.starter.handler.JwtClaimsValidatorHandler;
import com.example.starter.handler.LoginRequestHandler;
import com.example.starter.handler.RegistrationHandler;
import com.example.starter.response.SimpleResponse;
import com.example.starter.schema.SchemaValidator;
import com.example.starter.service.AuthenticationService;
import com.example.starter.service.ItemsService;
import com.example.starter.service.auth.JWTAuthService;
import com.example.starter.utils.ErrorMessages;
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
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.json.schema.OutputUnit;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.starter.utils.CommonUtils.*;

@Slf4j
public class VerticleInitializer extends AbstractVerticle {

  //  private static final int ASCENDING_ORDER = 1;
  private AuthenticationService registrationService;
  private Router router;
  private SchemaValidator validator;
  private JWTAuthService jwtAuthHandler;
  private ItemsService itemsService;
  private ConfigProperties configProperties;


  @Override
  public void start(Promise<Void> startPromise) {
    initObjects();

    SessionStore sessionStore = SessionStore.create(vertx);
    router.route().handler(SessionHandler.create(sessionStore));
    router.route().handler(LoggerHandler.create());

    router.get("/items")
      .handler(jwtAuthHandler.getJwtAuthHandler())
      .handler(new JwtClaimsValidatorHandler())
      .handler(this::getUserItem);

    router.post("/items")
      .consumes("application/json")
      .handler(BodyHandler.create())
      .handler(jwtAuthHandler.getJwtAuthHandler())
      .handler(new JwtClaimsValidatorHandler())
      .handler(this::validateRequestHandler)
      .handler(this::saveItem);

    router.post("/logout")
      .handler(handle -> {
        handle.clearUser();
        handle.end();
      });

    router.post("/login")
      .consumes("application/json")
      .handler(BodyHandler.create())
      .handler(new LoginRequestHandler(vertx));

    router.post("/register")
      .consumes("application/json")
      .handler(BodyHandler.create())
      .handler(this::validateRequestHandler)
      .handler(new RegistrationHandler(vertx));

    vertx.createHttpServer().requestHandler(router)
      .listen(3000, onHttpServerStartupHandler(startPromise));

    router.errorHandler(StatusCode.INTERNAL_SERVER_ERROR, new BadRequestErrorHandler())
      .errorHandler(StatusCode.INTERNAL_SERVER_ERROR, routingContext -> {
        routingContext.response().setStatusCode(StatusCode.INTERNAL_SERVER_ERROR).end(SimpleResponse.toGenericResponse(ErrorMessages.SERVER_ERROR.getMessage()).toBuffer());
      })
      .errorHandler(StatusCode.CONFLICT, routingContext -> {
      routingContext.response().setStatusCode(StatusCode.CONFLICT).end(ErrorMessages.CONFLICT.getMessage());
    });
  }

  private void validateItemsRequestHandler(RoutingContext ctx) throws Exception {
    OutputUnit unit = itemsService.validate(ctx.body().asJsonObject());
    if(!unit.getValid()) {
      ctx.response().setStatusCode(StatusCode.BAD_REQUEST).end(SimpleResponse.toGenericResponse(ErrorMessages.BAD_REQUEST.getMessage()).toBuffer());
    } else {
      ctx.next();
    }
  }

  private void validateRequestHandler(RoutingContext ctx) {
    OutputUnit unit = registrationService.validate(ctx.body().asJsonObject());
    if(!unit.getValid()) {
      ctx.response().setStatusCode(StatusCode.BAD_REQUEST).end(SimpleResponse.toGenericResponse(ErrorMessages.BAD_REQUEST.getMessage()).toBuffer());
    } else {
      ctx.next();
    }
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

  private void getUserItem(RoutingContext context) {
    itemsService.findAllByProperty(new JsonObject().put("owner", context.user().attributes().getValue(JWTAuthService.Claims.SUBJECT))).andThen(items -> {
      List<Item> viewItems = items.result().parallelStream()
        .map(jsonObject -> jsonObject.mapTo(Item.class))
        .toList();
      context.response().setStatusCode(StatusCode.OK).end(SimpleResponse.toGenericResponse(viewItems).toBuffer());
    });
  }

  private void saveItem(RoutingContext routingCtx) {
    JsonObject requestItem = getBodyFromRequest(routingCtx);
    requestItem.put("owner", routingCtx.user().attributes().getValue(JWTAuthService.Claims.SUBJECT));
    itemsService.saveItem(requestItem).onSuccess(handler -> {
      routingCtx.response().setStatusCode(StatusCode.CREATED).end();
    });
  }

  private void initObjects() {
    configProperties = new ConfigProperties(vertx);
    itemsService = new ItemsService(vertx);
    router = Router.router(vertx);
    validator = new SchemaValidator();
    registrationService = new AuthenticationService(vertx);
    jwtAuthHandler = new JWTAuthService(vertx);
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }

}
