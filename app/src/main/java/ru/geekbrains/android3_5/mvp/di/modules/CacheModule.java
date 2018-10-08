package ru.geekbrains.android3_5.mvp.di.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_5.mvp.model.repo.cache.AAUserCache;
import ru.geekbrains.android3_5.mvp.model.repo.cache.PaperUserCache;
import ru.geekbrains.android3_5.mvp.model.repo.cache.RealmUserCache;
import ru.geekbrains.android3_5.mvp.model.repo.cache.UserCache;

@Module
public class CacheModule {

    @Provides
    @Named("active")
    public UserCache aaCache() {
        return new AAUserCache();
    }

    @Provides
    @Named("paper")
    public UserCache paperCache() {
        return new PaperUserCache();
    }

    @Provides
    @Named("realm")
    public UserCache realmCache() {
        return new RealmUserCache();
    }
}
