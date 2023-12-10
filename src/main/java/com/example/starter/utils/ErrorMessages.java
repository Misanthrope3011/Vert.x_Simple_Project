package com.example.starter.utils;

import lombok.Getter;

@Getter
public enum ErrorMessages {

  CONFLICT("Resource with id already exists"),
  BAD_REQUEST("Request is invalid"),
  SERVER_ERROR("Unexpected error occured"),
  UNKNOWN_FIELD("Unknown field passed to the request"),
  ACCES_TOKEN_EXPIRED("Access token expired, please log in again"),
  UNAUTHORIZED("You have not provided an authentication token, the one provided has expired, was revoked or is not authentic."),
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
