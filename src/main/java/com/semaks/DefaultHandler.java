package com.semaks;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DefaultHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String response = "<a href=\"/auth\">SingIn</a>";
    exchange.sendResponseHeaders(200, response.length());
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
}
