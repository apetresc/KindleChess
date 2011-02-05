package com.codethesis.pgnparse;

import java.util.StringTokenizer;

public class PGNParseUtils {
  public static boolean isEmpty(String str) {
    return str.length() == 0;
  }

  public static boolean contains(String str, char c) {
    return str.indexOf(c) >= 0;
  }

  public static String removeFirstOccurence(String str, char c) {
    int charIndex = str.indexOf(c);
    if (charIndex != -1) {
      return str.substring(0, c).concat(str.substring(c + 1));
    } else {
      return str;
    }
  }

  public static String[] splitByWhitespace(String str) {
    StringTokenizer st = new StringTokenizer(str);
    String[] tokens = new String[st.countTokens()];
    int i = 0;
    while (st.hasMoreTokens()) {
      tokens[i++] = st.nextToken();
    }
    return tokens;
  }

  public static boolean matchType1(String move) {
    char a = move.charAt(0);
    char b = move.charAt(1);
    return ((a >= 'a' && a <= 'h') && (b >= '1' && b <= '8'));
  }

  public static boolean matchType2(String move) {
    char p = move.charAt(0);
    char a = move.charAt(1);
    char b = move.charAt(2);
    return (p == PGNParser.PAWN.charAt(0)   || p == PGNParser.KNIGHT.charAt(0) || 
            p == PGNParser.BISHOP.charAt(0) || p == PGNParser.ROOK.charAt(0) ||
            p == PGNParser.QUEEN.charAt(0)  || p == PGNParser.KING.charAt(0)) &&
            ((a >= 'a' && a <= 'h') && (b >= '1' && b <= '8'));
  }

  public static boolean matchType3(String move) {
    char p = move.charAt(0);
    char a1 = move.charAt(1);
    char a2 = move.charAt(2);
    char b = move.charAt(3);
    return (p == PGNParser.PAWN.charAt(0)   || p == PGNParser.KNIGHT.charAt(0) || 
            p == PGNParser.BISHOP.charAt(0) || p == PGNParser.ROOK.charAt(0) ||
            p == PGNParser.QUEEN.charAt(0)  || p == PGNParser.KING.charAt(0)) &&
            ((a1 >= 'a' && a1 <= 'h') && (a2 >= 'a' && a2 <= 'h') && (b >= '1' && b <= '8'));
  }

  public static boolean matchType4(String move) {
    char p = move.charAt(0);
    char a1 = move.charAt(1);
    char b1 = move.charAt(2);
    char a2 = move.charAt(3);
    char b2 = move.charAt(4);
    return (p == PGNParser.PAWN.charAt(0)   || p == PGNParser.KNIGHT.charAt(0) || 
            p == PGNParser.BISHOP.charAt(0) || p == PGNParser.ROOK.charAt(0) ||
            p == PGNParser.QUEEN.charAt(0)  || p == PGNParser.KING.charAt(0)) &&
            ((a1 >= 'a' && a1 <= 'h') && (a2 >= 'a' && a2 <= 'h') &&
             (b1 >= '1' && b1 <= '8') && (b2 >= '1' && b2 <= '8'));
  }

  public static boolean matchType5(String move) {
    char a1 = move.charAt(0);
    char a2 = move.charAt(1);
    char b = move.charAt(2);
    return ((a1 >= 'a' && a1 <= 'h') && (a2 >= 'a' && a2 <= 'h') && (b >= '1' && b <= '8'));
  }

  public static boolean matchType6(String move) {
    char p = move.charAt(0);
    char b1 = move.charAt(1);
    char a = move.charAt(2);
    char b2 = move.charAt(3);
    return (p == PGNParser.PAWN.charAt(0)   || p == PGNParser.KNIGHT.charAt(0) || 
            p == PGNParser.BISHOP.charAt(0) || p == PGNParser.ROOK.charAt(0) ||
            p == PGNParser.QUEEN.charAt(0)  || p == PGNParser.KING.charAt(0)) &&
            ((a >= 'a' && a <= 'h') && (b1 >= '1' && b1 <= '8') && (b2 >= '1' && b2 <= '8'));
  }
}
