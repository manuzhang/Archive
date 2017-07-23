package com.yilidi.core.util;

public class Utils {
  public static final int NAME_LENGTH_THRESHOLD = 6;
  public static final int NAME_DISPLAY_LENGTH = 5;
  public static final String NAME_SUFFIX = "...";

  public static final int COUNT_VALUE_THRESHOLD = 100;
  public static final String COUNT_DISPLAY_VALUE = "99+";


  public static String trimUserName(String name) {
    if (name.length() > NAME_LENGTH_THRESHOLD) {
      return name.substring(0, NAME_DISPLAY_LENGTH) + NAME_SUFFIX;
    } else {
      return name;
    }
  }

  public static String trimCount(int count) {
    if (count >= COUNT_VALUE_THRESHOLD) {
      return COUNT_DISPLAY_VALUE;
    } else {
      return "" + count;
    }
  }
}
