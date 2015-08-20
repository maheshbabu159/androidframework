package androidhive.info.materialdesign.viewcontrollers;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    private final int SELECT_PHOTO = 1;

    Button uploadButton;
    int serverResponseCode = 0;

    /**********  File Path *************/
    String uploadFilePath = "/storage/sdcard1/";
    final String uploadFileName = "1.jpg";

    private FrameLayout menuLayout;
    private View shareButton;
    private ArcLayout arcLayout;
    private StaggeredGridView mGridView;
    private Uri mImageCaptureUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private AlertDialog postImagedialog = null;
    private  StaggeredGridAdapter mAdapter =null;

    private View dialogView = null;

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

            showImagePostAlertDialog();
            //chooseImage();

        }else if(text.equals("I")) {




        }

    }
    public void showImagePostAlertDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.app_name);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.image_post_layout, null);
        alertDialog.setView(dialogView);

        //Image Button implmentionation
        ImageButton game_btn = (ImageButton)dialogView.findViewById(R.id.postImageButton);
        game_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                chooseImage();
            }
        });

        //Image Button implmentionation
        Button submitButton = (Button)dialogView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                new Thread(new Runnable() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                            }
                        });

                        uploadFile(uploadFilePath + "" + uploadFileName);

                    }
                }).start();

            }
        });

        postImagedialog = alertDialog.create();
        postImagedialog.show();
    }


    public void chooseImage(){


        final String [] items = new String [] {"From Camera", "From SD Card"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item,items);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Image");

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File file = new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                    mImageCaptureUri = Uri.fromFile(file);
                    try {
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.cancel();

                } else {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();




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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != Activity.RESULT_OK) return;

        Bitmap selectedImage=null;
        String path = "";
        InputStream imageStream = null;


        if (requestCode == PICK_FROM_FILE) {

            try
            {
                //Let's read the picked image -its URI
                Uri pickedImage = data.getData();

                //Let's read the image path using content resolver
                imageStream = getActivity().getContentResolver().openInputStream(pickedImage);

                //Now let's set the GUI ImageView data with data read from the picked file
                selectedImage = BitmapFactory.decodeStream(imageStream);

            } catch(FileNotFoundException e){
                e.printStackTrace();
            }

        } else {

            path = mImageCaptureUri.getPath();
            selectedImage = BitmapFactory.decodeFile(path);
        }

        //Set image
        ImageButton imageButton = (ImageButton)dialogView.findViewById(R.id.postImageButton);

        imageButton.setImageBitmap(selectedImage);
    }
    public String getRealPathFromURI(Uri contentUri) {

        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
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
    public void postImageDetailsServiceCall(final String fileName){

        //Get post details
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.image_post_layout, null);

        //Image Button implmentionation
        final EditText editText = (EditText)dialogView.findViewById(R.id.titleEditText);




        final ProgressDialog progressDialog = GlobalSettings.showProgressDialog(getActivity());

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);
        final String userId = sharedpreferences.getString(Constants.user_id_default_key, "");
        final String username = sharedpreferences.getString(Constants.user_name_default_key, "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.serviceUrl + "Posts/" + Constants.RequestMethods.AddPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GlobalSettings.hideProgressDialog(progressDialog);

                postImagedialog.dismiss();


                getAllPostsServiceCall();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                GlobalSettings.hideProgressDialog(progressDialog);

                VolleyLog.e("Error: ", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID_USR_POS", userId);
                params.put("TAR_AUD_POS", "6");
                params.put("ID_ORG_POS", "4");
                params.put("ID_BSE_POS", "5");
                params.put("POS_POS", "1.jpg");
                params.put("KEYW_POS", "text");
                params.put("LIN_POS", Constants.PostType.Image.toString());
                params.put("TIT_POS", editText.getText().toString());
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

    public void sendPostDetailsServiceCall(final String message){

        final ProgressDialog progressDialog = GlobalSettings.showProgressDialog(getActivity());

        SharedPreferences sharedpreferences = GlobalSingleton.getContext().getSharedPreferences(Constants.AppName, Context.MODE_PRIVATE);
        final String userId = sharedpreferences.getString(Constants.user_id_default_key, "");
        final String username = sharedpreferences.getString(Constants.user_name_default_key, "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.serviceUrl + "Posts/" + Constants.RequestMethods.AddPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GlobalSettings.hideProgressDialog(progressDialog);

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
                params.put("LIN_POS", Constants.PostType.Text.toString());
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

                    mAdapter = new StaggeredGridAdapter(getActivity(), R.layout.list_item_sample, postsViewsArray);
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

    @SuppressLint("LongLogTag")
    public int uploadFile(String sourceFileUri) {


        final String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {


            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(Constants.serviceUrl+Constants.RequestMethods.FileUpload);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " http://www.androidexample.com/media/uploads/"
                                    + uploadFileName;



                            //Call post details
                            postImageDetailsServiceCall(fileName);


                            Toast.makeText(getActivity(), "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
            }
            return serverResponseCode;

        } // End else block
    }

}
