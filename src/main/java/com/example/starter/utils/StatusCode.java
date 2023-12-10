package com.example.starter.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StatusCode {

  public final int OK = 200;
  public final int CREATED = 201;
  public final int BAD_REQUEST = 400;
  public final int UNAUTHORIZED = 401;
  public final int NOT_FOUND = 404;
  public final int CONFLICT = 409;
  public final int INTERNAL_SERVER_ERROR = 500;

}
