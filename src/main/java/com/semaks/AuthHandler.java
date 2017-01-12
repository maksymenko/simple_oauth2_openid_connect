package com.semaks;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler for authentication result request.
 */
public class AuthHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String requestMethod = exchange.getRequestMethod();
    if (requestMethod.equalsIgnoreCase("GET")) {
      Map<String, String> queryParams = Utils.queryToMap(exchange.getRequestURI().getQuery());
      // Check anti-forgery state token
      if (LocalStorage.containsState(queryParams.get("state"))) {
        String authCode = queryParams.get("code");

        //request user information from authentication server by passing authentication code.
        JSONObject userInfo = HttpClientHelper.loadUserDetails(authCode);
        System.out.println("User Info\r\n" + userInfo.toString(2));

        //Prepare response payload to view user info.
        StringBuffer response = new StringBuffer();
        response.append("<pre>");
        response.append(userInfo.toString(2));
        response.append("</pre><br><br>");
        response.append("Email: <b>");
        response.append(userInfo.getString("email"));
        response.append("</b><br>");
        response.append("Unique user identifier: ");
        response.append(userInfo.getString("sub"));

        //Send prepares payload to user to show user detail in browser.
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.toString().length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
      } else {
        System.out.println(">>> state '" + queryParams.get("state") + "' not found");
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
      }
    } else {
      System.out.println(">>> NOT GET");
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
    }
  }
}
