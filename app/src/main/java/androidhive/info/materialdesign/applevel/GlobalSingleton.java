package androidhive.info.materialdesign.applevel;

import android.content.Context;

import java.util.ArrayList;

import androidhive.info.materialdesign.model.Project;

/**
 * Created by maheshbabusomineni on 7/15/15.
 */
public class GlobalSingleton {
    private static GlobalSingleton ourInstance = new GlobalSingleton();
    private static Context context = null;


    private static ArrayList<Project> projectList = null;

    public static GlobalSingleton getInstance(Context context) {

        GlobalSingleton.context = context;

        return ourInstance;
    }

    private GlobalSingleton() {


    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        GlobalSingleton.context = context;
    }
    public static ArrayList<Project> getProjectList() {
        return projectList;
    }

    public static void setProjectList(ArrayList<Project> projectList) {
        GlobalSingleton.projectList = projectList;
    }

    public static GlobalSingleton getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(GlobalSingleton ourInstance) {
        GlobalSingleton.ourInstance = ourInstance;
    }

}
