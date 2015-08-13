package androidhive.info.materialdesign.applevel;

/**
 * Created by maheshbabusomineni on 7/14/15.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class GlobalSettings {


    public static ProgressDialog showProgressDialog(Activity activity) {

        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading...");
        dialog.show();

        return dialog;

    }
    public static void hideProgressDialog(ProgressDialog dialog) {

        dialog.hide();
        dialog = null;

    }

    public  static Map<String,String> getHeaders(){

        Map<String,String> params = new HashMap<String, String>();
        params.put(Constants.request_content_type_value,Constants.request_content_type_key);
        params.put(Constants.x_parse_application_id_value, Constants.x_parse_application_id_key);
        params.put(Constants.x_parse_rest_api_value, Constants.request_content_length_key);

        return params;
    }

    public static void updateAccessTokenSharedValue(String value){

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.access_token_default_key, value);
        editor.commit();

    }

    public static void updateUserIDSharedValue(String value){

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.user_id_default_key, value);
        editor.commit();

    }
    public static void updateUsernameSharedValue(String value){

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.user_name_default_key, value);
        editor.commit();

    }
    public static void updateExpiryDateSharedValue(String value){

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.expiry_date_default_key, value);
        editor.commit();

    }

    public static void updateIssuedDateSharedValue(String value){

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.issued_date_default_key, value);
        editor.commit();

    }
    public static void updateTokenTypeSharedValue(String value){

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.token_type_default_key, value);
        editor.commit();

    }

    public static void updateExpiryDurationSharedValue(String value){

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.expirs_in_default_key, value);
        editor.commit();

    }

    public static String getBase64(final String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
