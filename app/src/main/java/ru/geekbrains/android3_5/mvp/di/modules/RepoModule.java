package ru.geekbrains.android3_5.mvp.di.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_5.mvp.model.api.ApiService;
import ru.geekbrains.android3_5.mvp.model.api.IUserRepo;
import ru.geekbrains.android3_5.mvp.model.repo.UserRepo;
import ru.geekbrains.android3_5.mvp.model.repo.cache.UserCache;

@Module(includes = {ApiModule.class, CacheModule.class})
public class RepoModule {

    @Provides
    public IUserRepo userRepo(@Named("realm") UserCache cache, ApiService api) {
        return new UserRepo(cache, api);
    }
}
