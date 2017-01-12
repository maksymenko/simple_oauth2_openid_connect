package com.semaks;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handles default context and shows login link to initiate authentication process.
 */
public class DefaultHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String response = "<a href=\"/login\">SingIn</a>";
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
  
}
