package ru.geekbrains.android3_5.mvp.model.repo;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import ru.geekbrains.android3_5.mvp.model.NetworkStatus;
import ru.geekbrains.android3_5.mvp.model.api.ApiHolder;
import ru.geekbrains.android3_5.mvp.model.entity.Repository;
import ru.geekbrains.android3_5.mvp.model.entity.User;
import ru.geekbrains.android3_5.mvp.model.entity.activeandroid.AARepository;
import ru.geekbrains.android3_5.mvp.model.entity.activeandroid.AAUser;
import ru.geekbrains.android3_5.mvp.model.entity.realm.RealmRepository;
import ru.geekbrains.android3_5.mvp.model.entity.realm.RealmUser;

public class RealmUserRepo
{
    public Observable<User> getUser(String username)
    {
        if (NetworkStatus.isOnline())
        {
            return ApiHolder.getInstance().getApi().getUser(username)
                    .subscribeOn(Schedulers.io())
                    .map(user -> {
                        Realm realm = Realm.getDefaultInstance();
                        RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
                        if(realmUser == null)
                        {
                            realm.executeTransaction(innerRealm -> {
                                RealmUser newRealmUser = innerRealm.createObject(RealmUser.class, username);
                                newRealmUser.avatarUrl = user.getAvatarUrl();
                            });
                        }
                        else
                        {
                            realm.executeTransaction(innerRealm -> {
                                realmUser.avatarUrl = user.getAvatarUrl();
                            });
                        }
                        return user;
                    });
        }
        else
        {
            return Observable.create(emitter -> {
                Realm realm = Realm.getDefaultInstance();
                RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", username).findFirst();
                if(realmUser == null)
                {
                    emitter.onError(new RuntimeException("No such user in cache"));
                }
                else
                {
                   emitter.onNext(new User(realmUser.login, realmUser.avatarUrl, realmUser.reposUrl));
                   emitter.onComplete();
                }
                realm.close();
            });
        }
    }

    public Observable<List<Repository>> getUserRepos(User user)
    {
        if (NetworkStatus.isOnline())
        {
            return ApiHolder.getInstance().getApi().getUserRepos(user.getReposUrl())
                    .subscribeOn(Schedulers.io())
                    .map(repos -> {
                        Realm realm = Realm.getDefaultInstance();
                        final RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
                        if(realmUser == null)
                        {
                            realm.executeTransaction(innerRealm -> {
                                RealmUser newRealmUser = realm.createObject(RealmUser.class, user.getLogin());
                                newRealmUser.avatarUrl = user.getAvatarUrl();
                            });
                        }

                        realm.executeTransaction(innerRealm -> {
                            realmUser.repos.deleteAllFromRealm();
                            for(Repository repository: repos)
                            {
                                RealmRepository realmRepository = innerRealm.createObject(RealmRepository.class, repository.getId());
                                realmRepository.name = repository.getName();
                                realmUser.repos.add(realmRepository);
                            }

                        });
                        realm.close();
                        return repos;
                    });
        }
        else
        {
            return Observable.create(emitter -> {
                Realm realm = Realm.getDefaultInstance();
                RealmUser realmUser = realm.where(RealmUser.class).equalTo("login", user.getLogin()).findFirst();
                if(realmUser == null)
                {
                   emitter.onError(new RuntimeException("No such user in cache"));
                }
                else
                {
                    List<Repository> repositories = new ArrayList<>();
                    for(RealmRepository realmRepository : realmUser.repos)
                    {
                        repositories.add(new Repository(realmRepository.id, realmRepository.name));
                    }
                    emitter.onNext(repositories);
                    emitter.onComplete();
                }
                realm.close();
            });
        }
    }
}
