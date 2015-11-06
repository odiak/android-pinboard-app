package net.odiak.pinboardapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AuthManager {
    private final static String PREF_NAME = "authService";
    private final static String PREF_AUTH_TOKEN = "authToken";

    public static String getAuthToken(Context context) {
        SharedPreferences data = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String authToken = data.getString(PREF_AUTH_TOKEN, "");
        if (authToken.isEmpty()) return null;
        return authToken;
    }

    public static void saveAuthToken(Context context, String authToken) {
        SharedPreferences data = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(PREF_AUTH_TOKEN, authToken);
        editor.apply();
    }

    public static boolean login(Context context) {
        if (getAuthToken(context) == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return false;
        }

        return true;
    }
}
