package com.semaks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler for login context. Build redirect url to follow user to authentication page.
 */
public class AuthInitiatorHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    // Build authentication redirect URL.
    StringBuffer redirectUrl = new StringBuffer();
    redirectUrl.append(LocalStorage.getAuthEndpoint());
    redirectUrl.append("?client_id=");
    redirectUrl.append(LocalStorage.getClientId());
    redirectUrl.append("&response_type=code");
    redirectUrl.append("&scope=openid%20email");
    redirectUrl.append("&redirect_uri=");
    redirectUrl.append(LocalStorage.getClientRedirectUri());
    redirectUrl.append("&state=");
    redirectUrl.append(LocalStorage.generateState()); //generate anti-forgery state token.

    // Build response header.
    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
    dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    String currentFormattedDate = dateFormatter.format(new Date());
    Headers headers = exchange.getResponseHeaders();
    headers.set("Expires", currentFormattedDate);
    headers.set("Last-Modified", currentFormattedDate);
    headers.set("Cache-Control", "max-age=0, no-cache, must-revalidate, proxy-revalidate");
    headers.set("Location", redirectUrl.toString());

    // Send response to browser.
    exchange.sendResponseHeaders(HttpURLConnection.HTTP_MOVED_TEMP, 0);
  }
}
