package com.example.eadecommerce.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.android.jwt.JWT;

public class JwtUtils {

    private static final String PREFS_NAME = "myPrefs";
    private static final String TOKEN_KEY = "jwt_token";

    // Method to retrieve JWT token from SharedPreferences
    public static String getTokenFromSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(TOKEN_KEY, null);
    }

    // Method to decode JWT and retrieve user ID
    public static String getUserIdFromToken(String token) {
        if (token != null) {
            try {
                JWT jwt = new JWT(token);
                return jwt.getClaim("id").asString();
            } catch (Exception e) {
                Log.e("JWT Error", "Failed to decode token", e);
            }
        }
        return null;
    }

    // Method to decode JWT and retrieve other claims
    public static String getEmailFromToken(String token) {
        if (token != null) {
            try {
                JWT jwt = new JWT(token);
                return jwt.getClaim("email").asString();
            } catch (Exception e) {
                Log.e("JWT Error", "Failed to decode token", e);
            }
        }
        return null;
    }

    public static String getRoleFromToken(String token) {
        if (token != null) {
            try {
                JWT jwt = new JWT(token);
                return jwt.getClaim("role").asString();
            } catch (Exception e) {
                Log.e("JWT Error", "Failed to decode token", e);
            }
        }
        return null;
    }

    public static String getNameFromToken(String token) {
        if (token != null) {
            try {
                JWT jwt = new JWT(token);
                return jwt.getClaim("Name").asString();
            } catch (Exception e) {
                Log.e("JWT Error", "Failed to decode token", e);
            }
        }
        return null;
    }
}
