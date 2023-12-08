package com.example.starter.validator;

import com.example.starter.service.ExecutorServiceImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class SchemaValidator {

  private final ExecutorServiceImpl executor = new ExecutorServiceImpl();
  private final SchemaRepository schemaRepository;

  public SchemaValidator() {
    schemaRepository = SchemaRepository.create(new JsonSchemaOptions().setDraft(Draft.DRAFT7).setBaseUri("http://localhost:3000/"));
  }

  public void validate(JsonObject objectSchema, JsonObject validatedObject) {
    executor.execute(() -> {
      JsonSchema schema = JsonSchema.of(objectSchema);
      OutputUnit result = schemaRepository.validator(schema).validate(validatedObject);

      if(!result.getValid()) {
        log.error("Exception occured during execution");
      }
    });
  }

}
