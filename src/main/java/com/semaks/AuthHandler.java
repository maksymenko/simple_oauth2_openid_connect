package com.semaks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AuthHandler implements HttpHandler {

  private final String clientId;

  public AuthHandler(String clientId) {
    this.clientId = clientId;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    getAuthEndpoint();
    StringBuffer redirectUrl = new StringBuffer();
    redirectUrl.append(getAuthEndpoint());
    redirectUrl.append("?client_id=");
    redirectUrl.append(clientId);
    redirectUrl.append("&response_type=code");
    redirectUrl.append("&scope=openid%20email");
    redirectUrl.append("&redirect_uri=http://localhost:9000/auth");
    redirectUrl.append("&state=");
    redirectUrl.append(UUID.randomUUID().toString());

    Headers headers = exchange.getResponseHeaders();

    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
    dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    headers.set("Expires", dateFormatter.format(new Date()));
    headers.set("Last-Modified", dateFormatter.format(new Date()));
    headers.set("Cache-Control", "max-age=0, no-cache, must-revalidate, proxy-revalidate");
    headers.set("Location", redirectUrl.toString());
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
  }

  private String getAuthEndpoint() throws ClientProtocolException, IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet request = new HttpGet("https://accounts.google.com/.well-known/openid-configuration");
    CloseableHttpResponse response = httpClient.execute(request);
    try {
      if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
        HttpEntity httpEntity = response.getEntity();

        String responseStr = new BufferedReader(new InputStreamReader(httpEntity.getContent())).lines()
                .collect(Collectors.joining("\n"));

        JSONObject responseJson = new JSONObject(responseStr);
        String authUrl = responseJson.getString("authorization_endpoint");

        System.out.println(">>> authorization Server Url: " + authUrl);

        EntityUtils.consume(httpEntity);
        return authUrl;
      }
    }finally {
      response.close();
      httpClient.close();
    }
    throw new RuntimeException("Can't retrieve Authorization Server URL");
  }
}
