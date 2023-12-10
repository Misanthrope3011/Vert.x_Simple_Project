package com.example.starter.service;

import com.example.starter.schema.SchemaDefinitions;
import com.example.starter.schema.SchemaValidator;
import com.mongodb.assertions.Assertions;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.OutputUnit;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

class SchemaValidatorTest {

  private final SchemaValidator schemaValidator = new SchemaValidator();

  @Test
  void shouldReturnTrue_whenValidRegisterRequestProvided() throws IOException {
    try(FileInputStream requestBodyStream = new FileInputStream("src/test/resources/request.json")) {
      JsonObject loadContent = new JsonObject(new String(requestBodyStream.readAllBytes()));
      OutputUnit outputUnit = schemaValidator.validate(SchemaDefinitions.getAuthRequestDefinition(), loadContent);
      Assertions.assertTrue(outputUnit.getValid());
    }
  }


  @Test
  void shouldReturnTrue_whenInValidRegisterRequestProvided() throws IOException {
    try(FileInputStream requestBodyStream = new FileInputStream("src/test/resources/request_invalid.json")) {
      JsonObject loadContent = new JsonObject(new String(requestBodyStream.readAllBytes()));
      OutputUnit outputUnit = schemaValidator.validate(SchemaDefinitions.getAuthRequestDefinition(), loadContent);
      Assertions.assertFalse(outputUnit.getValid());
    }
  }

  @Test
  void shouldReturnTrue_whenValidItemRequestProvided() throws IOException {
    try(FileInputStream requestBodyStream = new FileInputStream("src/test/resources/item/itemRequest.json")) {
      JsonObject loadContent = new JsonObject(new String(requestBodyStream.readAllBytes()));
      OutputUnit outputUnit = schemaValidator.validate(SchemaDefinitions.getItemDefinition(), loadContent);
      Assertions.assertTrue(outputUnit.getValid());
    }
  }


  @Test
  void shouldReturnTrue_whenInValidItemRequestProvided() throws IOException {
    try(FileInputStream requestBodyStream = new FileInputStream("src/test/resources/item/itemRequest_invalid.json")) {
      JsonObject loadContent = new JsonObject(new String(requestBodyStream.readAllBytes()));
      OutputUnit outputUnit = schemaValidator.validate(SchemaDefinitions.getItemDefinition(), loadContent);
      Assertions.assertFalse(outputUnit.getValid());
    }
  }

}
