package ru.geekbrains.android3_5;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.geekbrains.android3_5.mvp.di.AppComponent;
import ru.geekbrains.android3_5.mvp.di.DaggerAppComponent;
import ru.geekbrains.android3_5.mvp.di.modules.AppModule;
import timber.log.Timber;

public class App extends Application {
    private static App instance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
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

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
