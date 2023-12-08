package com.example.starter.service;

import lombok.Getter;
import org.bson.json.JsonObject;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class ExecutorServiceImpl implements Executor<List<JsonObject>> {

  private final ExecutorService executorService = Executors.newFixedThreadPool(5);

  @Override
  public List<JsonObject> execute(Callable<List<JsonObject>> method) throws Exception {
    return executorService.submit(method).get();
  }

}
