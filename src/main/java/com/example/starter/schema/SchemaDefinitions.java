package com.example.starter.schema;


import io.vertx.core.json.JsonObject;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SchemaDefinitions {

  public JsonObject getAuthRequestDefinition() {
    return new JsonObject()
      .put("properties", new JsonObject()
        .put("login", new JsonObject()
          .put("type", "string")
          .put("minLength", 3)
          .put("maxLength", 50)
          .put("password", new JsonObject()
            .put("type", "string")
            .put("minLength", 3)
            .put("maxLength", 50)
          )));
  }

  public JsonObject getItemDefinition() {
    return new JsonObject()
      .put("properties", new JsonObject()
        .put("name", new JsonObject()
          .put("type", "string")
          .put("minLength", 3)
          .put("maxLength", 50)
        ));
  }

}
