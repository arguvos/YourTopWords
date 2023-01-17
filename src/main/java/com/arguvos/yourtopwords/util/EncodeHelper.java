package com.arguvos.yourtopwords.util;

public class EncodeHelper {
    public static String encode(boolean[] arr) {
        StringBuilder builder = new StringBuilder();
        for (boolean b : arr) {
            builder.append(b ? 1 : 0);
        }
        return builder.toString();
    }

    public static boolean[] decodeOrCreate(String str, int countElement) {
        if (str == null || str.isBlank()) {
            return new boolean[countElement];
        }
        return decode(str);
    }

    public static boolean[] decode(String str) {
        boolean[] arr = new boolean[str.length()];
        for (int i = 0; i < str.length(); i++) {
            arr[i] = str.charAt(i) == '1';
        }
        return arr;
    }
}
