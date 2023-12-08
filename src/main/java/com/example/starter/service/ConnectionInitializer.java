package com.example.starter.service;

public record ConnectionInitializer(String connectionString, String databaseName) {

  public ConnectionInitializer() {
    this("mongodb://root:example@localhost:27017", "mongodb");
  }

}
