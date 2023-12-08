package com.example.starter;

import io.vertx.core.Vertx;

public class EntryPoint {

  public static void main(String[] args) {
    Vertx.vertx().deployVerticle(new VerticleInitializer());
  }

}
