package appliedsyntax.io.yoga.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HARI @IPC
 */
public class MySharedPreference {
    public static void setPreference(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPreferences(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }
    public static void setDp(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static int getDp(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }
}