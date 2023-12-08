package com.example.starter.utils;

import com.example.starter.service.ExecutorServiceImpl;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.*;
import io.vertx.json.schema.common.dsl.ObjectSchemaBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SchemaValidator {

  private final ExecutorServiceImpl executor = new ExecutorServiceImpl();
  private final SchemaRepository schemaRepository;

  public SchemaValidator() {
    schemaRepository = SchemaRepository.create(new JsonSchemaOptions().setDraft(Draft.DRAFT7).setBaseUri("http://localhost:8888/"));
  }

  public void validator(JsonObject validatedObject) {
    JsonSchema schema = JsonSchema.of(validatedObject);
    OutputUnit result = schemaRepository.validator(schema).validate(validatedObject);

    if (result.getValid()) {
      log.info("something has been processed");
    }
  }

}
