package com.ra1ph.gcmexample;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 05.10.13
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class WiFiRequest extends ApiRequest {
    String MAC, UUID, OS, Device, pushToken;

    public WiFiRequest(Context context, OnRequestResultListener listener, String MAC, String UUID, String OS, String Device, String pushToken) {
        super(context, RequestResult.WIFI, listener);
        this.Device = Device;
        this.MAC = MAC;
        this.UUID = UUID;
        this.OS = OS;
        this.pushToken = pushToken;
    }

    @Override
    protected String getUrlParams() {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getRequestParams() {
        JSONObject o = new JSONObject();
        try{
            o.put("mac",MAC);
            o.put("android_id",UUID);
            o.put("os",OS);
            o.put("device",Device);
            o.put("push_token",pushToken);
        }catch(JSONException e){

        }
        return o.toString();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
