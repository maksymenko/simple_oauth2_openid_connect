package com.semaks;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Stores Application runtime parameters.
 */
public class LocalStorage {
  private static Set<String> states = new HashSet<>();
  private static String clientId;
  private static String clientSecret;
  private static String clientRedirectUri;
  private static String authEndpoint;
  private static String tokenEndpoint;

  public static String getClientRedirectUri() {
    return clientRedirectUri;
  }

  public static void setClientRedirectUri(String clientRedirectUri) {
    LocalStorage.clientRedirectUri = clientRedirectUri;
  }

  public static String getAuthEndpoint() {
    return authEndpoint;
  }

  public static void setAuthEndpoint(String authEndpoint) {
    LocalStorage.authEndpoint = authEndpoint;
  }

  public static String getTokenEndpoint() {
    return tokenEndpoint;
  }

  public static void setTokenEndpoint(String tokenEndpoint) {
    LocalStorage.tokenEndpoint = tokenEndpoint;
  }

  public static String getClientId() {
    return clientId;
  }

  public static void setClientId(String clientId) {
    LocalStorage.clientId = clientId;
  }

  public static String getClientSecret() {
    return clientSecret;
  }

  public static void setClientSecret(String clientSecret) {
    LocalStorage.clientSecret = clientSecret;
  }

  public static Object generateState() {
    String state = UUID.randomUUID().toString();
    states.add(state);
    return state;
  }

  public static boolean containsState(String state) {
    return states.contains(state);
  }
}
