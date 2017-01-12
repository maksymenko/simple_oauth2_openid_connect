package com.semaks;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpServer;

public class Runner {

  private static final int PORT = 9000;

  public static void main(String[] args) throws IOException {
    // Initialize Client OAuth2.0 parameters.
    LocalStorage.setClientId(Utils.getArgument("client_id"));
    LocalStorage.setClientSecret(Utils.getArgument("client_secret"));
    LocalStorage.setClientRedirectUri(Utils.getArgument("client_redirect_uri"));

    // Initialize Authentication Server parameters.
    JSONObject authServerConfig = HttpClientHelper.loadAuthServerConfig();
    System.out.println("Authenticaion Server configuration \r\n" + authServerConfig.toString(2));
    LocalStorage.setAuthEndpoint(authServerConfig.getString("authorization_endpoint"));
    LocalStorage.setTokenEndpoint(authServerConfig.getString("token_endpoint"));

    // Initialize Http Server.
    InetSocketAddress port = new InetSocketAddress(PORT);
    HttpServer httpServer = HttpServer.create(port, 0);

    //Initialize handlers for server contexts.
    httpServer.createContext("/", new DefaultHandler());
    httpServer.createContext("/login", new AuthInitiatorHandler());
    httpServer.createContext("/auth", new AuthHandler());
    
    //Start Server.
    httpServer.start();
    System.out.println("Server started. http://localhost:" + PORT);
  }
}