package com.example.starter.service;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface Executor<T> {

  T execute(Callable<T> method) throws Exception;

}
