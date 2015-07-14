package com.example.framework;

import android.app.Activity;
import android.app.ProgressDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maheshbabusomineni on 7/14/15.
 */
public class GlobalSettings {

    public static ProgressDialog showProgressDialog(Activity activity) {

        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading...");
        dialog.show();

        return dialog;

    }
    public static void hideProgressDialog(ProgressDialog dialog) {

        dialog.hide();

    }

    public  static Map<String,String> getHeaders(){

        Map<String,String> params = new HashMap<String, String>();
        params.put(Constants.request_content_type_value,Constants.request_content_type_key);
        params.put(Constants.x_parse_application_id_value,Constants.x_parse_application_id_key);
        params.put(Constants.x_parse_rest_api_value,Constants.request_content_length_key);

        return params;
    }


}
