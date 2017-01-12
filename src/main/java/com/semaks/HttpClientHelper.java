package com.semaks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Provides helper methods to make calls to authentication server.
 */
public class HttpClientHelper {

  /**
   * Loads Authentication Server configuration.
   */
  public static JSONObject loadAuthServerConfig() throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet request = new HttpGet("https://accounts.google.com/.well-known/openid-configuration");
    CloseableHttpResponse response = httpClient.execute(request);
    try {
      if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
        HttpEntity httpEntity = response.getEntity();

        String responseStr = new BufferedReader(new InputStreamReader(httpEntity.getContent())).lines()
            .collect(Collectors.joining("\n"));

        EntityUtils.consume(httpEntity);
        return new JSONObject(responseStr);
      }
    } finally {
      response.close();
      httpClient.close();
    }
    throw new RuntimeException("Can't retrieve Authorization Server URL");
  }

  /**
   * Sends code to authentication server to load user details.
   */
  public static JSONObject loadUserDetails(String authCode) throws ClientProtocolException, IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost request = new HttpPost(LocalStorage.getTokenEndpoint());

    // Prepare request payload to exchange authentication code to user details.
    List<NameValuePair> payload = new ArrayList<>();
    payload.add(new BasicNameValuePair("code", authCode));
    payload.add(new BasicNameValuePair("client_id", LocalStorage.getClientId()));
    payload.add(new BasicNameValuePair("client_secret", LocalStorage.getClientSecret()));
    payload.add(new BasicNameValuePair("redirect_uri", LocalStorage.getClientRedirectUri()));
    payload.add(new BasicNameValuePair("grant_type", "authorization_code"));
    request.setEntity(new UrlEncodedFormEntity(payload));

    //Makes call to authentication server to get user details.
    CloseableHttpResponse response = httpClient.execute(request);

    HttpEntity httpEntity = response.getEntity();

    String responseStr = new BufferedReader(new InputStreamReader(httpEntity.getContent())).lines()
        .collect(Collectors.joining("\n"));

    JSONObject userInfoJson = new JSONObject(responseStr);

    //Response contains 3 parts: header, payload, signature.
    String tokenIdParts[] = userInfoJson.getString("id_token").split("\\.");
    if (tokenIdParts.length != 3) {
      throw new RuntimeException("Unexpected Token ID format.");
    }
    
    response.close();
    httpClient.close();

    //Decode payload to access user details.
    String userInfo = new String(Base64.getDecoder().decode(tokenIdParts[1]));

    return new JSONObject(userInfo);
  }

}
