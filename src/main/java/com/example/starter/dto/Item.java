package com.example.starter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "_id")
public record Item( String name, @JsonProperty("owner") String userUUID) {

}
