package com.example.starter.service;

import io.vertx.core.json.JsonObject;

public interface RequestValidator {

  void validate(JsonObject validatedObject) throws Exception;

}
