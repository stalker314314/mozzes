/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Static methods for common String operations
 * 
 * @author milos
 */
public class StringUtils {
  public static final String EMPTY_STRING_ARRAY[] = new String[0];

  /**
   * Joins all string representation of objects in iterator with separator between them
   * 
   * @param separator
   *          Separator between joined string representation of objects in iterator
   * @param values
   *          Objects whose string representations will be joined
   * @return String with joined all string representations in values with separator
   */
  public static String joinParams(String separator, Object... values) {
    Object[] objects = JavaUtils.getArrayForVarArgs(values);

    StringBuffer buf = new StringBuffer(256);
    for (Object obj : objects) {
      if (obj != null)
        buf.append(obj);
      if (separator != null)
        buf.append(separator);
    }
    if (separator != null) {
      buf.delete(buf.length() - separator.length(), buf.length());
    }

    return buf.toString();
  }

  /**
   * Joins all string representation of objects in iterator with separator between them
   * 
   * @param iterator
   *          Iterator that holds all objects whose string representations will be joined
   * @param separator
   *          Separator between joined string representation of objects in iterator
   * @return String with joined all string representation in iterator with separator
   */
  public static String join(Iterator<? extends Object> iterator, String separator) {
    if (iterator == null) {
      return null;
    }
    StringBuffer buf = new StringBuffer(256);
    while (iterator.hasNext()) {
      Object obj = iterator.next();
      if (obj != null)
        buf.append(obj);
      if (separator != null && iterator.hasNext())
        buf.append(separator);
    }
    return buf.toString();
  }

  /**
   * Checks if string is not empty
   * 
   * @param str
   *          String to check
   * @return <code>true</code> if string is either empty or <code>null</code>
   */
  public static boolean isNotEmpty(String str) {
    return str != null && str.length() > 0;
  }

  /**
   * @param s
   *          String to be checked
   * @return <code>true</code> if string is empty when trimmed for spaces or <code>null</code>
   */
  public static boolean isEmpty(String s) {
    if (s == null) {
      return true;
    }
    return s.trim().length() == 0;
  }

  /**
   * @param str
   *          String to convert to default
   * @return Original string, or empty string ("") if string is <code>null</code>
   */
  public static String defaultString(String str) {
    return str != null ? str : "";
  }

  /**
   * Check if first string contains second
   */
  public static boolean contains(String searchString, String str) {
    if (searchString == null || str == null)
      return false;

    return searchString.indexOf(str) >= 0;
  }

  /**
   * Replaces up to "max" occurrences of "replace" in "text" with "with"
   * 
   * @param text
   *          Text to search for "replace"
   * @param replace
   *          Text to replace
   * @param with
   *          Text to replace "replace" with
   * @param aMax
   *          Maximum number of occurrences to replace
   * @return String with up to "max" occurrences replaces
   */
  private static String replace(String text, String replace, String with, int aMax) {
    int max = aMax;
    if (text == null || StringUtils.isEmpty(replace) || with == null || max == 0)
      return text;

    StringBuffer buf = new StringBuffer(text.length());
    int start = 0;
    for (int end = 0; (end = text.indexOf(replace, start)) != -1;) {
      buf.append(text.substring(start, end)).append(with);
      start = end + replace.length();
      if (--max == 0)
        break;
    }

    buf.append(text.substring(start));
    return buf.toString();
  }

  /**
   * Replaces all occurrences of "repl" in "text" with "with"
   * 
   * @param text
   *          Text to search for "repl"
   * @param repl
   *          Text to replace
   * @param with
   *          Text to replace "repl" with
   * @return String with all occurrences replaces
   */
  public static String replace(String text, String repl, String with) {
    return replace(text, repl, with, -1);
  }

  private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
    if (str == null)
      return null;
    int len = str.length();
    if (len == 0)
      return EMPTY_STRING_ARRAY;
    List<String> list = new ArrayList<String>();
    int sizePlus1 = 1;
    int i = 0;
    int start = 0;
    boolean match = false;
    boolean lastMatch = false;
    if (separatorChars == null)
      while (i < len)
        if (Character.isWhitespace(str.charAt(i))) {
          if (match || preserveAllTokens) {
            lastMatch = true;
            if (sizePlus1++ == max) {
              i = len;
              lastMatch = false;
            }
            list.add(str.substring(start, i));
            match = false;
          }
          start = ++i;
        } else {
          lastMatch = false;
          match = true;
          i++;
        }
    else if (separatorChars.length() == 1) {
      char sep = separatorChars.charAt(0);
      while (i < len)
        if (str.charAt(i) == sep) {
          if (match || preserveAllTokens) {
            lastMatch = true;
            if (sizePlus1++ == max) {
              i = len;
              lastMatch = false;
            }
            list.add(str.substring(start, i));
            match = false;
          }
          start = ++i;
        } else {
          lastMatch = false;
          match = true;
          i++;
        }
    } else {
      while (i < len)
        if (separatorChars.indexOf(str.charAt(i)) >= 0) {
          if (match || preserveAllTokens) {
            lastMatch = true;
            if (sizePlus1++ == max) {
              i = len;
              lastMatch = false;
            }
            list.add(str.substring(start, i));
            match = false;
          }
          start = ++i;
        } else {
          lastMatch = false;
          match = true;
          i++;
        }
    }
    if (match || preserveAllTokens && lastMatch)
      list.add(str.substring(start, i));
    return list.toArray(new String[list.size()]);
  }

  /**
   * Split first string where separator is second string
   * 
   * @param str
   *          String to be split
   * @param separatorChars
   *          String to serve as delimiter between parts
   * @return String array
   */
  public static String[] split(String str, String separatorChars) {
    return splitWorker(str, separatorChars, -1, false);
  }

  private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
    if (str == null)
      return null;
    int len = str.length();
    if (len == 0)
      return EMPTY_STRING_ARRAY;
    List<String> list = new ArrayList<String>();
    int i = 0;
    int start = 0;
    boolean match = false;
    boolean lastMatch = false;
    while (i < len)
      if (str.charAt(i) == separatorChar) {
        if (match || preserveAllTokens) {
          list.add(str.substring(start, i));
          match = false;
          lastMatch = true;
        }
        start = ++i;
      } else {
        lastMatch = false;
        match = true;
        i++;
      }
    if (match || preserveAllTokens && lastMatch)
      list.add(str.substring(start, i));
    return list.toArray(new String[list.size()]);
  }

  /**
   * Split first string where separator is second argument
   * 
   * @param str
   *          String to be split
   * @param separatorChar
   *          Character to serve as delimiter between parts
   * @return String array
   */
  public static String[] split(String str, char separatorChar) {
    return splitWorker(str, separatorChar, false);
  }

  /**
   * Returns the string with first letter capitalized
   */
  public static String capitalize(String str) {
    if (str == null)
      throw new NullPointerException();
    if (str.isEmpty())
      return "";
    String firstLetter = str.substring(0, 1); // Get first letter
    String remainder = str.substring(1); // Get remainder of word.
    String capitalized = firstLetter.toUpperCase() + remainder;
    return capitalized;
  }

  /**
   * Given the char c and length n, created string with only character c in it of length n.
   * <p>
   * Example:
   * 
   * <pre>
   * <code>fillWithChar('a', 5) returns "aaaaa"</code>
   * </pre>
   * 
   * <pre>
   * <code>fillWithChar('1', 10) returns "1111111111"</code>
   * </pre>
   * 
   * </p>
   * 
   * @param c
   *          Character to fill string with
   * @param n
   *          Number of times characters appears in created string, e.g. length of the string. Must be non-negative.
   * @return String of length n with only c characters in it
   */
  public static String fillWithChar(char c, int n) {
    if (n < 0)
      throw new IllegalArgumentException("n must be non-negative");
    StringBuffer buffer = new StringBuffer(n);
    for (int i = 0; i < n; i++) {
      buffer.append(c);
    }
    return buffer.toString();
  }

  /**
   * <p>
   * In any word, from string given as input parameter, transform first letter to uppercase, and all other letters to
   * lowercase. Words in string are separated by ' ' and/or '-'. This method can be usefull for correct capitalization
   * in personal first and/or last name.
   * </p>
   * Examples, in input, output format:<br>
   * 1. marko markovic, Marko Markovic<br>
   * 2. dejan petkovic rambo, Dejan Petkovic Rambo<br>
   * 3. ivana brlic-mazuranic, Ivana Brlic-Mazuranic<br>
   * 4. Tim O'Reilly, Tim O'reilly<br>
   * 5. maRkO mArKOviC, Marko Markovic<br>
   * 6. IVAna brLIc-MAzuRANic, Ivana Brlic-Mazuranic<br>
   * 7. Marko Markovic, Marko Markovic<br>
   * 8. Ivana Brlic-Mazuranic, Ivana Brlic-Mazuranic<br>
   * 
   * @return capitalized string
   */
  public static String capitalizeName(String string) {
    if (string == null)
      throw new NullPointerException();
    final String trimmedString = string.trim();
    if (trimmedString.length() == 0) {
      return trimmedString;
    }
    StringBuilder correctString = new StringBuilder(trimmedString.substring(0, 1).toUpperCase());
    boolean toUpperCase = false;
    for (int i = 1, n = trimmedString.length(); i < n; i++) {
      final String next = trimmedString.substring(i, i + 1);
      if (next.equals(" ") || next.equals("-")) {
        correctString.append(next);
        toUpperCase = true;
      } else {
        correctString.append(toUpperCase ? next.toUpperCase() : next.toLowerCase());
        toUpperCase = false;
      }
    }
    return correctString.toString();
  }
}
