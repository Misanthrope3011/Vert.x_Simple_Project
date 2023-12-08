package com.example.starter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SystemUser(String login, String password, String salt, String token, @JsonProperty("_id") String uuid) {
}
