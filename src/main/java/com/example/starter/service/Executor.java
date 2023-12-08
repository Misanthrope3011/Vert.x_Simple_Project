package com.example.starter.service;

import java.util.concurrent.Callable;

public interface Executor<T> {

  T execute(Callable<T> method) throws Exception;

  void execute(Runnable method) throws Exception;


}
