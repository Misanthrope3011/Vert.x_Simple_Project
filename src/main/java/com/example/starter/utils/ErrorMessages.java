package com.example.starter.utils;

import lombok.Getter;

@Getter
public enum ErrorMessages {

  CONFLICT("Resource with id already exists"),
  SERVER_ERROR("Unexpected error occured"),
  UNKNOWN_FIELD("Unknown field passed to the request"),
  REQUIRED("Field is required");
  private String message;

  public String getMessage(Object... msgParams) {
    return String.format(message, msgParams);
  }

  ErrorMessages(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }

}
