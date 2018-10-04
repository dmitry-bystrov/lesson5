package ru.geekbrains.android3_5.mvp.model.repo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import ru.geekbrains.android3_5.mvp.model.entity.Repository;
import ru.geekbrains.android3_5.mvp.model.entity.User;
import ru.geekbrains.android3_5.mvp.model.entity.realm.RealmRepository;
import ru.geekbrains.android3_5.mvp.model.entity.realm.RealmUser;

public class RealmUserCache implements UserCache {
    @Override
    public void saveUser(String username, User user) {
        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(innerRealm -> {
//            innerRealm.deleteAll();
//        });

        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
        if (realmUser == null) {
            realm.executeTransaction(innerRealm -> {
                RealmUser newRealmUser = innerRealm.createObject(RealmUser.class, username);
                newRealmUser.setAvatarUrl(user.getAvatarUrl());
            });
        } else {
            realm.executeTransaction(innerRealm -> {
                realmUser.setAvatarUrl(user.getAvatarUrl());
                realmUser.setReposUrl(user.getReposUrl());
            });
        }
        realm.close();
    }

    @Override
    public void saveUserRepos(User user, List<Repository> userRepos) {
        Realm realm = Realm.getDefaultInstance();
        final RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
        if (realmUser != null) {
            realm.executeTransaction(innerRealm -> {
                realmUser.getRepos().deleteAllFromRealm();
                for (Repository repository : userRepos) {
                    RealmRepository realmRepository = innerRealm.createObject(RealmRepository.class, repository.getId());
                    realmRepository.setName(repository.getName());
                    realmUser.getRepos().add(realmRepository);
                }
            });
        }

        realm.close();
    }

    public Observable<User> getUser(String username) {
        return Observable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
            if (realmUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                emitter.onNext(new User(realmUser.getLogin(), realmUser.getAvatarUrl(), realmUser.getReposUrl()));
                emitter.onComplete();
            }
            realm.close();
        });
    }

    public Observable<List<Repository>> getUserRepos(User user) {
        return Observable.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
            if (realmUser == null) {
                emitter.onError(new RuntimeException("No such user in cache"));
            } else {
                List<Repository> repositories = new ArrayList<>();
                for (RealmRepository realmRepository : realmUser.getRepos()) {
                    repositories.add(new Repository(realmRepository.getId(), realmRepository.getName()));
                }
                emitter.onNext(repositories);
                emitter.onComplete();
            }
            realm.close();
        });
    }
}
