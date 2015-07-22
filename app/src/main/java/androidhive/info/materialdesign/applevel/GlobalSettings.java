package androidhive.info.materialdesign.applevel;

/**
 * Created by maheshbabusomineni on 7/14/15.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

public class GlobalSettings {

    public static final String MyPREFERENCES = "MyPrefs" ;

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
        params.put(Constants.x_parse_rest_api_value, Constants.request_content_length_key);

        return params;
    }

    public static void updateAccessTokenSharedValue(String accessToken){

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.access_token_default_key, accessToken);
        editor.commit();

    }
    public static String getBase64(final String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

}
