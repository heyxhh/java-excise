package org.excise.dubbo3;

public class LocalContext {
    private static final ThreadLocal<String> localString = new ThreadLocal<>();

    public static void set(String s) {
        localString.set(s);
    }

    public static String get() {
        return localString.get();
    }
}
