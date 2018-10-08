package ru.geekbrains.android3_5.mvp.model.repo.cache;

import java.util.List;

import io.reactivex.Observable;
import ru.geekbrains.android3_5.mvp.model.entity.Repository;
import ru.geekbrains.android3_5.mvp.model.entity.User;

public interface UserCache {
    void saveUser(String username, User user);

    void saveUserRepos(User user, List<Repository> userRepos);

    Observable<User> getUser(String username);

    Observable<List<Repository>> getUserRepos(User user);
}
