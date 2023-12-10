package com.example.starter.service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface Executor<T> {

  Future<T> execute(Callable<T> method) throws Exception;

  void execute(Runnable method) throws Exception;


}
