package com.example.starter.service.auth;

import com.example.starter.config.ConfigProperties;
import com.example.starter.dto.SystemUser;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.JWTAuthHandler;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.example.starter.service.auth.JWTAuthService.Claims.*;

@Slf4j
@Getter
public class JWTAuthService {

  private final JWTAuth provider;
  private final JWTAuthHandler jwtAuthHandler;
  private final JWTAuthOptions config;
  private ConfigProperties configProperties;

  public JWTAuthService(Vertx vertx) {
    configProperties = new ConfigProperties(vertx);
    configProperties.getProperties().result();
    config = new JWTAuthOptions()
      .setKeyStore(new KeyStoreOptions()
        .setPassword(System.getenv("STORE_SECRET")));
    provider = JWTAuth.create(vertx, config);
    jwtAuthHandler = JWTAuthHandler.create(provider);
  }

  public String generateToken(SystemUser user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(ISSUER, "Sebastian");
    claims.put(SUBJECT, user.login());
    claims.put(EXPIRATION, System.currentTimeMillis() + 3600000);

    return provider.generateToken(new JsonObject(claims), new JWTOptions());
  }

  @UtilityClass
  public static final class Claims {
    public final String ISSUER = "iss";
    public final String SUBJECT = "sub";
    public final String EXPIRATION = "exp";
  }

}
