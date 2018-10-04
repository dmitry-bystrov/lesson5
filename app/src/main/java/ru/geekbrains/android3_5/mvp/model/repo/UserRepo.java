package ru.geekbrains.android3_5.mvp.model.repo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_5.mvp.model.NetworkStatus;
import ru.geekbrains.android3_5.mvp.model.api.ApiHolder;
import ru.geekbrains.android3_5.mvp.model.entity.Repository;
import ru.geekbrains.android3_5.mvp.model.entity.User;

public class UserRepo {
    private UserCache userCache;

    public UserRepo() {
        //this.userCache = new PaperUserCache();
        //this.userCache = new AAUserCache();
        this.userCache = new RealmUserCache();
    }

    public Observable<User> getUser(String username) {
        if (NetworkStatus.isOnline()) {
            return ApiHolder.getInstance().getApi().getUser(username)
                    .subscribeOn(Schedulers.io())
                    .map(user -> {
                        userCache.saveUser(username, user);
                        return user;
                    });
        } else {
            return userCache.getUser(username);
        }
    }

    public Observable<List<Repository>> getUserRepos(User user) {
        if (NetworkStatus.isOnline()) {
            return ApiHolder.getInstance().getApi().getUserRepos(user.getReposUrl())
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
