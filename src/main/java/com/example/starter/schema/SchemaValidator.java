package com.example.starter.schema;

import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SchemaValidator {

  private final SchemaRepository schemaRepository;

  public SchemaValidator() {
    schemaRepository = SchemaRepository.create(new JsonSchemaOptions().setDraft(Draft.DRAFT7).setBaseUri("http://localhost:3000/"));
  }

  public OutputUnit validate(JsonObject objectSchema, JsonObject validatedObject) {
    JsonSchema schema = JsonSchema.of(objectSchema);
     return schemaRepository.validator(schema).validate(validatedObject);
  }
}
