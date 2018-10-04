package ru.geekbrains.android3_5.mvp.model.image.android.cache;

import io.reactivex.Single;
import io.realm.Realm;
import ru.geekbrains.android3_5.mvp.model.entity.realm.RealmByteArray;

public class RealmImageCache implements ImageCache {
    @Override
    public void saveByteArray(String url, byte[] byteArray) {
        Realm realm = Realm.getDefaultInstance();
        RealmByteArray realmByteArray = realm.where(RealmByteArray.class).equalTo("url", url).findFirst();
        if (realmByteArray == null) {
            realm.executeTransaction(innerRealm -> {
                RealmByteArray newRealmByteArray = innerRealm.createObject(RealmByteArray.class, url);
                newRealmByteArray.setByteArray(byteArray);
            });
        } else {
            realm.executeTransaction(innerRealm -> {
                realmByteArray.setByteArray(byteArray);
            });
        }
        realm.close();
    }

    @Override
    public Single<byte[]> getByteArray(String url) {
        return Single.create(emitter -> {
            Realm realm = Realm.getDefaultInstance();
            RealmByteArray realmByteArray = realm.where(RealmByteArray.class).equalTo("url", url).findFirst();

            if (realmByteArray != null) {
                emitter.onSuccess(realmByteArray.getByteArray());
            }

            realm.close();
        });
    }
}
