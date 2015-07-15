package androidhive.info.materialdesign.applevel;

/**
 * Created by maheshbabusomineni on 7/15/15.
 */
public class GlobalSingleton {
    private static GlobalSingleton ourInstance = new GlobalSingleton();

    public static GlobalSingleton getInstance() {
        return ourInstance;
    }

    private GlobalSingleton() {


    }
}
