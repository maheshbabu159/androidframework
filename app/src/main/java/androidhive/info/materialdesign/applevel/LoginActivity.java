package androidhive.info.materialdesign.applevel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import androidhive.info.materialdesign.R;

public class LoginActivity extends ActionBarActivity {

    ImageView tv = null;
    RelativeLayout container, signInContainer, loginContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set context
        GlobalSingleton.getInstance(this);


        Button login = (Button) findViewById(R.id.signinButton);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                loginServiceCall();

            }
        });

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                moveViewToScreenCenter(tv);
            }
        }, 1000);*/


    }

    public void loginServiceCall(){

        final EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        final ProgressDialog progressDialog = GlobalSettings.showProgressDialog(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.root_url+ "token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.v("Response:%n %s", response);

                GlobalSettings.hideProgressDialog(progressDialog);

                JSONObject jsonResponse = null;

                try {

                    jsonResponse = new JSONObject(response);

                    GlobalSettings.updateAccessTokenSharedValue(jsonResponse.getString(Constants.access_token_default_key).toString());
                    GlobalSettings.updateUsernameSharedValue(jsonResponse.getString(Constants.user_name_default_key).toString());
                    GlobalSettings.updateExpiryDateSharedValue(jsonResponse.getString(Constants.expiry_date_default_key).toString());
                    GlobalSettings.updateExpiryDurationSharedValue(jsonResponse.getString(Constants.expirs_in_default_key).toString());
                    GlobalSettings.updateIssuedDateSharedValue(jsonResponse.getString(Constants.issued_date_default_key).toString());
                    GlobalSettings.updateTokenTypeSharedValue(jsonResponse.getString(Constants.token_type_default_key).toString());

                    //Get userinfo service call
                    getUserInfoServiceCall();

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                GlobalSettings.hideProgressDialog(progressDialog);

                Toast.makeText(getApplicationContext(),  error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", usernameEditText.getText().toString());
                //params.put("password", passwordEditText.getText().toString());
                params.put("password", "@Venkata2015");

                params.put("grant_type", "password");
                return params;
            }

            /*{

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Basic "+ GlobalSettings.getBase64(abdicationText) );
                params.put("Content-Type", "application/json");

                return params;
            }*/
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, Constants.RequestMethods.LoginRequest.toString());

    }

    public void getUserInfoServiceCall(){

        final ProgressDialog progressDialog = GlobalSettings.showProgressDialog(this);

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString(Constants.user_name_default_key, "");

        String uri = String.format(Constants.serviceUrl + "User?username=%1$s", username);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.v("Response:%n %s", response);

                GlobalSettings.hideProgressDialog(progressDialog);

                JSONObject jsonResponse = null;

                try {

                    jsonResponse = new JSONObject(response);
                    GlobalSettings.updateUserIDSharedValue(jsonResponse.getString(Constants.user_id_default_key).toString());

                    //TODO:Navigate to home view
                    Intent openNewActivity = new Intent(getApplicationContext(),
                            MainActivity.class);
                    //openNewActivity.putExtra("ProjectsList", projectsArrayList);
                    //GlobalSingleton.setProjectList(projectsArrayList);
                    startActivity(openNewActivity);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                GlobalSettings.hideProgressDialog(progressDialog);

            }
        }){

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, Constants.RequestMethods.LoginRequest.toString());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // To animate view slide out from bottom to top
    private void moveViewToScreenCenter(final View view) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int originalPos[] = new int[2];
        view.getLocationOnScreen(originalPos);

        int xDelta = (dm.widthPixels - view.getMeasuredWidth() - originalPos[0]) / 2;
        int yDelta = (dm.heightPixels - view.getMeasuredHeight() - originalPos[1]) / 2;

        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillAfter(true);
        animSet.setDuration(2000);
        // animSet.setInterpolator(new BounceInterpolator());
        TranslateAnimation translate = new TranslateAnimation(0, xDelta, 0,
                yDelta);
        animSet.addAnimation(translate);
        ScaleAnimation scale = new ScaleAnimation(1f, 2f, 1f, 2f,
                ScaleAnimation.RELATIVE_TO_PARENT, .5f,
                ScaleAnimation.RELATIVE_TO_PARENT, .5f);
        animSet.addAnimation(scale);
        view.startAnimation(animSet);
        animSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                /*new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        moveViewToScreenTopLeft(tv);

                    }
                }, 1000);*/
            }
        });
    }

    private void moveViewToScreenTopLeft(final View view) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int originalPos[] = new int[2];
        view.getLocationOnScreen(originalPos);

        int xDelta = (dm.widthPixels - view.getMeasuredWidth() - originalPos[0]) / 2;
        int yDelta = (dm.heightPixels - view.getMeasuredHeight() - originalPos[1]) / 2;

        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillAfter(true);
        animSet.setDuration(2000);

        TranslateAnimation translate = new TranslateAnimation(xDelta,
                -dm.widthPixels / 2 + 50, yDelta, -dm.heightPixels + 200);
        animSet.addAnimation(translate);
        ScaleAnimation scale = new ScaleAnimation(2f, 1f, 2f, 1f,
                ScaleAnimation.RELATIVE_TO_PARENT, .5f,
                ScaleAnimation.RELATIVE_TO_PARENT, .5f);
        animSet.addAnimation(scale);
        view.startAnimation(animSet);
        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                signInContainer.setVisibility(View.VISIBLE);
            }
        });
    }
}
