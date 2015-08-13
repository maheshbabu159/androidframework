package androidhive.info.materialdesign.adapter;

import java.io.InputStream;
import java.util.List;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.xperi.avataimageview.DSAvatarImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidhive.info.materialdesign.applevel.Constants;
import androidhive.info.materialdesign.model.dataobjects.PostsView;
import androidhive.info.materialdesign.model.dataobjects.Project;
import androidhive.info.materialdesign.R;


public class StaggeredGridAdapter extends ArrayAdapter<PostsView> {

    Activity activity;
    int resource;
    List<PostsView> datas;

    public StaggeredGridAdapter(Activity activity, int resource, List<PostsView> objects) {
        super(activity, resource, objects);

        this.activity = activity;
        this.resource = resource;
        this.datas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final PostsView object = datas.get(position);

        View row = convertView;
        final DealHolder holder;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new DealHolder();

            if(!object.lin_pos.equals("text")){

                holder.postDynamicHeightImageView = (DynamicHeightImageView) row.findViewById(R.id.postDynamicHeightImageView);

            }

            holder.usernameTextView = (TextView)row.findViewById(R.id.usernameTextView);
            holder.descriptionDynamicHeightTextView = (DynamicHeightTextView)row.findViewById(R.id.descriptionDynamicHeightTextView);
            holder.profileAvatarImageView = (DynamicHeightImageView)row.findViewById(R.id.profileAvatarImageView);
            row.setTag(holder);
        }
        else {
            holder = (DealHolder) row.getTag();
        }

        if(!object.lin_pos.equals("text")){

            new DownloadImageTask(holder.postDynamicHeightImageView)
                    .execute(Constants.post_images_url+object.pos_pos);

        }

        new DownloadImageTask(holder.profileAvatarImageView)
                .execute(Constants.profile_images_url+object.pho_usr);


        //holder.image.setHeightRatio(1.0);
        holder.usernameTextView.setText(object.getUsername());
        holder.descriptionDynamicHeightTextView.setText(object.pos_pos);

        return row;
    }

    static class DealHolder {
        DynamicHeightImageView postDynamicHeightImageView;
        TextView usernameTextView;
        DynamicHeightTextView descriptionDynamicHeightTextView;
        DynamicHeightImageView profileAvatarImageView;

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
