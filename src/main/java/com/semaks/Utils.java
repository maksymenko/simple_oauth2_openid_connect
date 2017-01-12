package com.semaks;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Miscellaneous utilities for application.
 */
public class Utils {
  public static String getArgument(String argName) {
    String argValue = System.getProperties().getProperty(argName);
    if (StringUtils.isBlank(argValue)) {
      System.out.println(argName + " required");
      throw new IllegalArgumentException("Command line argument " + argName + " missed");
    }
    return argValue;
  }

  public static Map<String, String> queryToMap(String query) {
    Map<String, String> result = new HashMap<String, String>();
    for (String param : query.split("&")) {
      String pair[] = param.split("=");
      if (pair.length > 1) {
        result.put(pair[0], pair[1]);
      } else {
        result.put(pair[0], "");
      }
    }
    return result;
  }
}
