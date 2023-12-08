package com.example.starter.response;

import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@Getter
@UtilityClass
public class SimpleResponse {

  public JsonObject toGenericResponse(String message) {
    return new JsonObject().put("message", message);
  }

  public JsonObject toGenericResponse(Object responseMessage) {
    return new JsonObject().put("result", responseMessage);
  }

}
