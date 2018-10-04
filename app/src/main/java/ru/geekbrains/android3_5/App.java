package ru.geekbrains.android3_5;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

public class App extends Application
{
    static private App instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
        Paper.init(this);
        ActiveAndroid.initialize(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static App getInstance()
    {
        return instance;
    }
}
