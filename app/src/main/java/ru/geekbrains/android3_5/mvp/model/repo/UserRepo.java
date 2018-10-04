package ru.geekbrains.android3_5.mvp.model.repo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_5.mvp.model.api.ApiHolder;
import ru.geekbrains.android3_5.mvp.model.entity.Repository;
import ru.geekbrains.android3_5.mvp.model.entity.User;

public class UserRepo
{
    public Observable<User> getUser(String username)
    {
        return ApiHolder.getInstance().getApi().getUser(username).subscribeOn(Schedulers.io());
    }

    public Observable<List<Repository>> getUserRepos(String url)
    {
        return ApiHolder.getInstance().getApi().getUserRepos(url).subscribeOn(Schedulers.io());
    }
}
