package saidul.com.creatingthecontentproviderclass;

import android.content.Context;

/**
 * Created by Prime Tech on 7/20/2016.
 */
public class AppController extends android.app.Application {
public static AppController mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getContext(){
        return mInstance;
    }
}

