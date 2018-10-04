package ru.geekbrains.android3_5.mvp.model.repo;

import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.geekbrains.android3_5.Utils;
import ru.geekbrains.android3_5.mvp.model.entity.Repository;
import ru.geekbrains.android3_5.mvp.model.entity.User;

public class PaperUserCache implements UserCache {
    @Override
    public void saveUser(String username, User user) {
        Paper.book("users").write(username, user);
    }

    @Override
    public void saveUserRepos(User user, List<Repository> userRepos) {
        String sha1 = Utils.SHA1(user.getReposUrl());
        Paper.book("repos").write(sha1, userRepos);
    }

    public Observable<User> getUser(String username) {
        if (!Paper.book("users").contains(username)) {
            return Observable.error(new RuntimeException("No such user in cache"));
        }

        return Observable.fromCallable(() -> Paper.book("users").read(username));
    }

    public Observable<List<Repository>> getUserRepos(User user) {
        String sha1 = Utils.SHA1(user.getReposUrl());
        if (!Paper.book("repos").contains(sha1)) {
            return Observable.error(new RuntimeException("No repos for such user in cache"));
        }

        return Observable.fromCallable(() -> Paper.book("repos").read(sha1));
    }
}
