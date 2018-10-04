package ru.geekbrains.android3_5.mvp.model.repo;

import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.geekbrains.android3_5.Utils;
import ru.geekbrains.android3_5.mvp.model.NetworkStatus;
import ru.geekbrains.android3_5.mvp.model.api.ApiHolder;
import ru.geekbrains.android3_5.mvp.model.entity.Repository;
import ru.geekbrains.android3_5.mvp.model.entity.User;

public class PaperUserRepo
{
    public Observable<User> getUser(String username)
    {
        if (NetworkStatus.isOnline())
        {
            return ApiHolder.getInstance().getApi().getUser(username)
                    .subscribeOn(Schedulers.io())
                    .map(user -> {
                        Paper.book("users").write(username, user);
                        return user;
                    });
        }
        else
        {
            if (!Paper.book("users").contains(username))
            {
                return Observable.error(new RuntimeException("No such user in cache"));
            }
            return Observable.fromCallable(() -> Paper.book("users").read(username));
        }
    }

    public Observable<List<Repository>> getUserRepos(User user)
    {
        String sha1 = Utils.SHA1(user.getReposUrl());
        if (NetworkStatus.isOnline())
        {
            return ApiHolder.getInstance().getApi().getUserRepos(user.getReposUrl())
                    .subscribeOn(Schedulers.io())
                    .map(repos -> {
                        Paper.book("repos").write(sha1, repos);
                        return repos;
                    });
        }
        else
        {
            if (!Paper.book("repos").contains(sha1))
            {
                return Observable.error(new RuntimeException("No repos for such user in cache"));
            }
            return Observable.fromCallable(() -> Paper.book("repos").read(sha1));
        }
    }
}
