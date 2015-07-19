package androidhive.info.materialdesign.applevel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import androidhive.info.materialdesign.R;

public class LoginActivity extends ActionBarActivity {

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

                    if (jsonResponse.getString(Constants.access_token_default_key) != null) {

                        GlobalSettings.updateAccessTokenSharedValue(jsonResponse.getString(Constants.access_token_default_key));

                        Intent allPostIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(allPostIntent);
                    }

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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "venkata");
                params.put("password", "@Venkata2015");
                params.put("grant_type", "password");
                return params;
            }
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
}
