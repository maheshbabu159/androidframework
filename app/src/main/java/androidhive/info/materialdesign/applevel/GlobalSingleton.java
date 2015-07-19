package androidhive.info.materialdesign.applevel;

import android.content.Context;

/**
 * Created by maheshbabusomineni on 7/15/15.
 */
public class GlobalSingleton {
    private static GlobalSingleton ourInstance = new GlobalSingleton();
    private static Context context = null;

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

}
