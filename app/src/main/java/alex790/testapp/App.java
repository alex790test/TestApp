package alex790.testapp;

import android.app.Application;

/**
 * Created by alex790 on 07.06.2018.
 */

public class App extends Application {


    private static AppManager appManager;


    @Override
    public void onCreate() {
        super.onCreate();

        appManager = new AppManager(this);
    }


    public static AppManager getAppManager() {
        return appManager;
    }


}
