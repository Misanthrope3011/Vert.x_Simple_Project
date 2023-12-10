package com.example.starter.handler;

import com.example.starter.dto.SystemUser;
import com.example.starter.service.AuthenticationService;
import com.example.starter.service.auth.JWTAuthService;
import com.example.starter.utils.StatusCode;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class LoginRequestHandlerTest {

  private LoginRequestHandler loginRequestHandler;
  private AuthenticationService authenticationService;
  private JWTAuthService jwtAuthService;

  @BeforeEach
  void setUp() {
    authenticationService = Mockito.mock(AuthenticationService.class);
    jwtAuthService = Mockito.mock(JWTAuthService.class);
    loginRequestHandler = new LoginRequestHandler(authenticationService, jwtAuthService);
  }

  @Test
  @Disabled
  void testHandleValidCredentials() {
    RoutingContext routingContext = Mockito.mock(RoutingContext.class);

    // Mocking the Buffer object
    Buffer buffer = Buffer.buffer(
      new JsonObject()
        .put("login", "username")
        .put("password", "password")
        .encode()
        .getBytes()
    );

    when(routingContext.getBody()).thenReturn(buffer);

    SystemUser mockUser = new SystemUser("bla", "bla", "bla", "bla");

    JsonObject userJsonObject = JsonObject.mapFrom(mockUser);

    when(authenticationService.validatePassword(any())).thenReturn(Future.succeededFuture(true));
    when(authenticationService.findUser("username")).thenReturn(Future.succeededFuture(userJsonObject));
    when(jwtAuthService.generateToken(any())).thenReturn("generated_token");

    // Call the handle method
    loginRequestHandler.handle(routingContext);

    // Verify interactions and assertions based on the expected behavior
    verify(routingContext.response()).setStatusCode(StatusCode.OK);
    verify(routingContext.response()).end(any(JsonObject.class).toBuffer());
  }
}
