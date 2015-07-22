package androidhive.info.materialdesign.viewcontrollers;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.etsy.android.grid.StaggeredGridView;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

import androidhive.info.materialdesign.R;
import androidhive.info.materialdesign.adapter.ProjectsListAdapter;
import androidhive.info.materialdesign.applevel.GlobalSingleton;
import androidhive.info.materialdesign.model.Project;


public class HomeFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ProjectsListAdapter adapter;
    private List<Project> projectList;

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

        listView = (ListView)rootView.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);

        projectList = new ArrayList<Project>();
        projectList = GlobalSingleton.getProjectList();

        adapter = new ProjectsListAdapter(getActivity(), projectList);
        listView.setAdapter(adapter);

        //swipeRefreshLayout.setOnRefreshListener(getActivity());

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);





                                    }
                                }
        );



        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
