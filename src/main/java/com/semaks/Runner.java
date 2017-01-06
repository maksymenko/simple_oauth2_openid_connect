package com.semaks;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class Runner {

  private static final int PORT = 9000;

  public static void main(String[] args) throws IOException {
    InetSocketAddress port = new InetSocketAddress(PORT);
    HttpServer httpServer = HttpServer.create(port, 0);
    httpServer.createContext("/auth", new AuthHandler());
    httpServer.start();
    System.out.println("Server started. http://localhost:" + PORT);
  }
}