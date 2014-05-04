package com.overnightApps.myapplication.app.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by andre on 3/5/14.
 */
public class Logger {
    public static void p(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Log.d(context.getClass().getSimpleName(), message);
    }
}
