package com.example.starter.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ConfigProperties {

  private final ConfigRetriever configRetriever;
  private JsonObject properties;

  public ConfigProperties(Vertx vertx) {
    ConfigStoreOptions configStoreOptions = new ConfigStoreOptions()
      .setType("file")
      .setFormat("json")
      .setConfig(new JsonObject().put("path", "config.json"));

    ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions()
      .addStore(configStoreOptions);
    configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
  }

  public Future<JsonObject> getProperties() {
    return configRetriever.getConfig().onComplete(handler -> {
      this.properties = handler.result();
    });
  }

}
