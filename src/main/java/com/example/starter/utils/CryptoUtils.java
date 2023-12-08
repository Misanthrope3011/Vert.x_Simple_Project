package com.example.starter.utils;

import io.vertx.ext.auth.HashingStrategy;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Getter
@UtilityClass
public class CryptoUtils {

  public final HashingStrategy strategy = HashingStrategy.load();
  public final Map<String, String> saltParameters = new HashMap<>();
  public final SecureRandom secureRandom = new SecureRandom();

  public String generateSalt() {
    saltParameters.put("saltLength", "16");
    saltParameters.put("iterations", "10000");

    byte[] saltArray = new byte[16];
    secureRandom.nextBytes(saltArray);

    return Base64.getEncoder().encodeToString(saltArray);
  }

}
