package com.example.starter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SystemUser(String login, String password, String salt, @JsonProperty("_id") String uuid) {
}
