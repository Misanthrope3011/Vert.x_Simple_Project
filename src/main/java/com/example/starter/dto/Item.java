package com.example.starter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Item(String name, @JsonProperty("_id") String uuid) {

}
