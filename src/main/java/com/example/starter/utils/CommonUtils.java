package com.example.starter.utils;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtils {

  public JsonObject getBodyFromRequest(RoutingContext routingCtx) {
    return routingCtx.body().asJsonObject();
  }

}
