package androidhive.info.materialdesign.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import androidhive.info.materialdesign.model.Project;
import androidhive.info.materialdesign.R;


/**
 * Created by Ravi on 13/05/15.
 */
public class ProjectsListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Project> projectsList;
    private String[] bgColors;

    public ProjectsListAdapter(Activity activity, List<Project> projectsList) {

        this.activity = activity;
        this.projectsList = projectsList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.movie_serial_bg);
    }

    @Override
    public int getCount() {
        return projectsList.size();
    }

    @Override
    public Object getItem(int location) {
        return projectsList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.fragment_home_list_row, null);

        TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        //serial.setText(String.valueOf(projectsList.get(position).id));
        title.setText(projectsList.get(position).getName());

        String color = bgColors[position % bgColors.length];
        serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }

}