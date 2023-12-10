package com.example.starter.service;

import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.OutputUnit;

import java.util.concurrent.Future;

public interface RequestValidator {

  OutputUnit validate(JsonObject validatedObject) throws Exception;

}
