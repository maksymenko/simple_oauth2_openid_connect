package com.semaks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.UUID;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AuthHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    StringBuffer redirectUrl = new StringBuffer();
    redirectUrl.append("https://accounts.google.com/o/oauth2/v2/auth");
    redirectUrl.append("?client_id=88778855276-4nefbr7ohacd8kc657nj5al51p9gskco.apps.googleusercontent.com");
    redirectUrl.append("&response_type=code");
    redirectUrl.append("&scope=openid%20email");
    redirectUrl.append("&redirect_uri=http://localhost:2000");
    redirectUrl.append("&state=");
    redirectUrl.append(UUID.randomUUID().toString());

    Headers headers = exchange.getResponseHeaders();
    headers.set("Location", redirectUrl.toString());
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
  }
}
