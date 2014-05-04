package com.overnightApps.myapplication.app;

import android.app.Application;
import android.os.StrictMode;

import com.parse.Parse;

/**
 * Created by andre on 3/6/14.
 */
public class CustomApplication extends Application {
    public static final String PARSE_APPLICATION_ID_PROD = "";
    public static final String PARSE_CLIENT_KEY_PROD = "";

    private static final String PARSE_APPLICATION_ID_DEV = "";
    private static final String PARSE_CLIENT_KEY_DEV = "";

    @Override
    public void onCreate() {
        setCorrectParseKeysForApplicationEnvironment();
        super.onCreate();
    }

    private void setCorrectParseKeysForApplicationEnvironment() {
        String parseAppID;
        String parseClientKey;
        if(BuildConfig.DEBUG){
            parseAppID = PARSE_APPLICATION_ID_DEV;
            parseClientKey = PARSE_CLIENT_KEY_DEV;
        }else{
            parseAppID = PARSE_APPLICATION_ID_PROD;
            parseClientKey = PARSE_CLIENT_KEY_PROD;
        }
        Parse.initialize(this, parseAppID, parseClientKey);
    }

    /**
     * execute this method when running app in development so that bad coding practices (e.g. running
     * tasks on the UI thread that should be run on worker thread) will cause an error
     */
    private void enableStrictApplicationRunningMode(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}
