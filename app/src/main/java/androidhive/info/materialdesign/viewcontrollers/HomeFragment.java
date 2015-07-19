package androidhive.info.materialdesign.viewcontrollers;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;
import com.ogaclejapan.arclayout.ArcLayout;

import androidhive.info.materialdesign.R;


public class HomeFragment extends Fragment {

    private StaggeredGridView mGridView;
    private ArcLayout arcLayout;
    private View menuLayout;
    private View menuButton;

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

        mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view);
        arcLayout = (ArcLayout) rootView.findViewById(R.id.arc_layout);
        menuLayout = rootView.findViewById(R.id.menu_layout);
        menuButton = rootView.findViewById(R.id.menuButton);

        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {

        }



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
