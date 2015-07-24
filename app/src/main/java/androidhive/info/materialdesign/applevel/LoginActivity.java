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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.model.Project;
import androidhive.info.materialdesign.viewcontrollers.StaggeredGridActivity;

import android.util.Base64;

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

        final String abdicationText = usernameEditText.getText().toString()+":"+passwordEditText.getText().toString();

        final ProgressDialog progressDialog = GlobalSettings.showProgressDialog(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.root_url+ "project", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.v("Response:%n %s", response);

                GlobalSettings.hideProgressDialog(progressDialog);

                JSONArray jsonResponse = null;

                try {

                    ArrayList<Project> projectsArrayList = new ArrayList<Project>();

                    jsonResponse = new JSONArray(response);

                    if (jsonResponse.length() > 0) {

                        // looping through json and adding to movies list
                        for (int i = 0; i < jsonResponse.length(); i++) {

                            try {

                                JSONObject projectJsonObject = jsonResponse.getJSONObject(i);

                                Project project = new Project();
                                project.setName(projectJsonObject.getString("name"));

                                projectsArrayList.add(project);

                            } catch (JSONException e) {

                                Log.e("Parse Error:",  e.getMessage());
                            }
                        }


                        //TODO:Navigate to home view
                        Intent openNewActivity = new Intent(getApplicationContext(),
                                StaggeredGridActivity.class);
                        //openNewActivity.putExtra("ProjectsList", projectsArrayList);
                        //GlobalSingleton.setProjectList(projectsArrayList);
                        startActivity(openNewActivity);

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
        })/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "venkata");
                params.put("password", "@Venkata2015");
                params.put("grant_type", "password");
                return params;
            }*/

            {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Basic "+ GlobalSettings.getBase64(abdicationText) );
                params.put("Content-Type", "application/json");

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
