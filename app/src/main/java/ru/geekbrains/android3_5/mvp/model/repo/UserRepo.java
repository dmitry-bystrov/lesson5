package ru.geekbrains.android3_5.mvp.model.repo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_5.mvp.model.NetworkStatus;
import ru.geekbrains.android3_5.mvp.model.api.ApiService;
import ru.geekbrains.android3_5.mvp.model.api.IUserRepo;
import ru.geekbrains.android3_5.mvp.model.entity.Repository;
import ru.geekbrains.android3_5.mvp.model.entity.User;
import ru.geekbrains.android3_5.mvp.model.repo.cache.UserCache;

public class UserRepo implements IUserRepo {
    private UserCache userCache;
    private ApiService api;

    public UserRepo(UserCache userCache, ApiService api) {
        this.userCache = userCache;
        this.api = api;
    }

    @Override
    public Observable<User> getUser(String username) {
        if (NetworkStatus.isOnline()) {
            return api.getUser(username)
                    .subscribeOn(Schedulers.io())
                    .map(user -> {
                        userCache.saveUser(username, user);
                        return user;
                    });
        } else {
            return userCache.getUser(username);
        }
    }

    @Override
    public Observable<List<Repository>> getUserRepos(User user) {
        if (NetworkStatus.isOnline()) {
            return api.getUserRepos(user.getReposUrl())
                    .subscribeOn(Schedulers.io())
                    .map(userRepos -> {
                        userCache.saveUserRepos(user, userRepos);
                        return userRepos;
                    });
        } else {
            return userCache.getUserRepos(user);
        }
    }
}
