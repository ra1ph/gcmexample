package com.ra1ph.gcmexample;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.parse.*;

import java.io.IOException;

public class Main extends Activity implements OnRequestResultListener {
    private static final String TAG = "myLogs";
    public static String SENDER_ID = "50225923956";
    private static final String PREFS = "prefs";
    private static final String IS_SENDED = "is_sended";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private PushReceiver receiver;

    Button send;
    private WiFiRequest request;
    SharedPreferences prefs;
    public static final String appId = "tD8QzW1SZ7OhSGQHsDBZGDoNNqMzlK6Mf81pUayw";
    public static final String clientKey = "g5nH8uzAd1lW5yN7oI1ixMGzb3OLsbfopc8cphlc";
    private String os, device, macAddr, uuid;
    private TextView text;
    private String registrationId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Parse.initialize(this, appId, clientKey);

        register(this);

        text = (TextView) findViewById(R.id.text);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (registrationId == null) register(Main.this);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("test");
        query.getInBackground("i9pn40lR2z", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                boolean test = parseObject.getBoolean("test");
                if (test) {
                    if (true) {
                        WifiManager wifiMan = (WifiManager) Main.this.getSystemService(
                                Context.WIFI_SERVICE);
                        WifiInfo wifiInf = wifiMan.getConnectionInfo();
                        macAddr = wifiInf.getMacAddress();

                        uuid = Settings.Secure.getString(Main.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                        os = "Android " + android.os.Build.VERSION.RELEASE;

                        device = Build.MANUFACTURER + " " + Build.MODEL;

                        request = new WiFiRequest(Main.this, Main.this, macAddr
                                , uuid, os, device, registrationId);
                        request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        });
    }


    public void register(final Context context) {
        if (checkPlayServices()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
                        registrationId = gcm.register(SENDER_ID);
                        Log.v("myLog", registrationId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }
            }.execute();
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
    }

    @Override
    public void onSuccess(RequestResult result) {
        if (result.getRequestType() == RequestResult.WIFI) {
            //Toast.makeText(this, result.getResponse(), Toast.LENGTH_SHORT).show();
            ((TextView) findViewById(R.id.reply)).setText(result.getResponse());
            StringBuilder builder = new StringBuilder();
            builder.append("Android ID: ");
            builder.append(uuid);
            builder.append("\n");
            builder.append("Device: ");
            builder.append(device);
            builder.append("\n");
            builder.append("MAC: ");
            builder.append(macAddr);
            builder.append("\n");
            builder.append("OS: ");
            builder.append(os);
            builder.append("\n");
            text.setText(builder.toString());
            prefs.edit().putBoolean(IS_SENDED, true).commit();
        }
    }

    @Override
    public void onFail(RequestResult result) {
        Toast.makeText(this, "Wrong server question", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(Integer progress) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
