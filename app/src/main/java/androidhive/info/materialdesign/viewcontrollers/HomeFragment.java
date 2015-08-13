package androidhive.info.materialdesign.viewcontrollers;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.ogaclejapan.arclayout.ArcLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.adapter.StaggeredGridAdapter;
import androidhive.info.materialdesign.applevel.AppController;
import androidhive.info.materialdesign.applevel.Constants;
import androidhive.info.materialdesign.applevel.GlobalSettings;
import androidhive.info.materialdesign.applevel.GlobalSingleton;
import androidhive.info.materialdesign.applevel.MainActivity;
import androidhive.info.materialdesign.components.AnimatorUtils;
import androidhive.info.materialdesign.model.dataobjects.PostsView;
import androidhive.info.materialdesign.model.dataobjects.SampleData;
import androidhive.info.materialdesign.model.operations.PostsViewModel;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private FrameLayout menuLayout;
    private View shareButton;
    private ArcLayout arcLayout;
    private Toast toast = null;
    private StaggeredGridView mGridView;
    private static final int GALLERY_PHOTO_CODE = 1;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        //Show the staggger view

        mGridView = (StaggeredGridView)rootView.findViewById(R.id.grid_view);

        //Buttons properties
        shareButton = (View)rootView.findViewById(R.id.shareButton);
        menuLayout =(FrameLayout)rootView.findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout)rootView.findViewById(R.id.arc_layout);

        //Set on click listener for all the arc layout buttons
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {

            arcLayout.getChildAt(i).setOnClickListener(this);
        }

        shareButton.setOnClickListener(this);


        //Call posts service call
        getAllPostsServiceCall();

        return rootView;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.shareButton) {

            onFabClick(v);
            return;
        }

        if (v instanceof Button) {
            showToast((Button) v);
        }

    }
    private void showToast(Button btn) {

        if (toast != null) {
            toast.cancel();
        }

        String text = btn.getText().toString();

        if(text.equals("T")) {


            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.post_text_layout, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // get user input and set it to result
                                    // edit text
                                    //result.setText(userInput.getText());

                                    sendPostDetailsServiceCall(userInput.getText().toString());
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }else if(text.equals("I")) {

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.vox_logo);

            String base64 = GlobalSettings.encodeTobase64(largeIcon);

            sendPostImageDetailsServiceCall(base64);


        }else{


        }

    }

    public void onItemClick(AdapterView<?> adapterView, View view,
                            int position, long id) {





    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void onFabClick(View v) {
        if (v.isSelected()) {
            hideMenu();
        } else {
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }
    @SuppressWarnings("NewApi")
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {

            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }
    private Animator createShowItemAnimator(View item) {

        float dx = shareButton.getX() - item.getX();
        float dy = shareButton.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f));

        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        float dx = shareButton.getX() - item.getX();
        float dy = shareButton.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy));

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }

    public void loginServiceCall(){

        final ProgressDialog progressDialog = GlobalSettings.showProgressDialog(getActivity());

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

                    //TODO:Navigate to home view
                    Intent openNewActivity = new Intent(getActivity(),
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "venkata");
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
    public void sendPostImageDetailsServiceCall(final String base64){

        final ProgressDialog progressDialog = GlobalSettings.showProgressDialog(getActivity());

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);
        final String userId = sharedpreferences.getString(Constants.user_id_default_key, "");
        final String username = sharedpreferences.getString(Constants.user_name_default_key, "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.serviceUrl +  Constants.RequestMethods.FileUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                getAllPostsServiceCall();

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
                params.put("ID_USR_POS", userId);
                params.put("TAR_AUD_POS", "6");
                params.put("ID_ORG_POS", "4");
                params.put("ID_BSE_POS", "5");
                params.put("IS_SHT_POS", "0");
                params.put("IS_ANO_POS", "0");

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
    public void sendPostDetailsServiceCall(final String message){

        final ProgressDialog progressDialog = GlobalSettings.showProgressDialog(getActivity());

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);
        final String userId = sharedpreferences.getString(Constants.user_id_default_key, "");
        final String username = sharedpreferences.getString(Constants.user_name_default_key, "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.serviceUrl + "Posts/" + Constants.RequestMethods.AddPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                getAllPostsServiceCall();

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
                params.put("ID_USR_POS", userId);
                params.put("TAR_AUD_POS", "6");
                params.put("ID_ORG_POS", "4");
                params.put("ID_BSE_POS", "5");
                params.put("POS_POS", message);
                params.put("KEYW_POS", "text");
                params.put("LIN_POS", "text");
                params.put("TIT_POS", message);
                params.put("CR_BY",username );
                params.put("IS_SHT_POS", "0");
                params.put("IS_ANO_POS", "0");

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
    public void getAllPostsServiceCall(){

        final ProgressDialog progressDialog = GlobalSettings.showProgressDialog(getActivity());

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);
        String userId = sharedpreferences.getString(Constants.user_id_default_key, "");

        //http://voxapi.voxpopulii.com/Api/Posts/rangedata?userid=27&pageno=1&rowcount=10
        String uri = String.format(Constants.serviceUrl + "Posts/rangedata?userid=%s&pageno=%d&rowcount=%d", userId, 1, 10);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VolleyLog.v("Response:%n %s", response);

                GlobalSettings.hideProgressDialog(progressDialog);

                JSONArray jsonResponse = null;

                try {

                    ArrayList<PostsView> postsViewsArray = new ArrayList<PostsView>();

                    jsonResponse = new JSONArray(response);

                    for (int i = 0; i < jsonResponse.length(); i++){

                        //PostsViewModel.initWithDictionary(jsonResponse.getJSONObject(i));
                        JSONObject jsonObject = jsonResponse.getJSONObject(i);

                        PostsView object = new PostsView();

                        object.abu_cou_pos =String.valueOf(jsonObject.getInt("abu_cou_pos".toUpperCase().toString()));
                        object.tit_pos =jsonObject.getString("tit_pos".toUpperCase().toString());
                        object.pho_usr = jsonObject.getString("pho_usr".toUpperCase().toString());
                        object.username= jsonObject.getString("UserName").toString();
                        object.nm_brn = jsonObject.getString("nm_brn".toUpperCase().toString());
                        object.nm_org = jsonObject.getString("nm_org".toUpperCase().toString());
                        object.cr_dt_utc=jsonObject.getString("cr_dt_utc".toUpperCase().toString());
                        object.pos_pos = jsonObject.getString("pos_pos".toUpperCase().toString());
                        object.lin_pos = jsonObject.getString("lin_pos".toUpperCase().toString());

                        postsViewsArray.add(object);


                    }

                    StaggeredGridAdapter mAdapter = new StaggeredGridAdapter(getActivity(), R.layout.list_item_sample, postsViewsArray);
                    mGridView.setAdapter(mAdapter);


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
}
