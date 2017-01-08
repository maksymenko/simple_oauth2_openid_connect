package com.semaks;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.StringUtils;


public class Runner {

  private static final int PORT = 9000;

  public static void main(String[] args) throws IOException {
    String clientId = System.getProperties().getProperty("client_id");
    if (StringUtils.isBlank(clientId)){
      System.out.println("client_id required");
      return;
    }
    System.out.println("ClientID: '" + clientId + "'");
    InetSocketAddress port = new InetSocketAddress(PORT);
    HttpServer httpServer = HttpServer.create(port, 0);
    httpServer.createContext("/", new DefaultHandler());
    httpServer.createContext("/auth", new AuthHandler(clientId));
    httpServer.start();
    System.out.println("Server started. http://localhost:" + PORT);
  }
}