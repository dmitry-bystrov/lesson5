package ru.geekbrains.android3_5.mvp.model.image.android.cache;

import android.annotation.SuppressLint;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import ru.geekbrains.android3_5.Utils;
import ru.geekbrains.android3_5.mvp.model.entity.realm.RealmStorageFile;
import ru.geekbrains.android3_5.mvp.model.storage.ExternalStorage;
import ru.geekbrains.android3_5.mvp.model.storage.Storage;
import timber.log.Timber;

public class HybridImageCache implements ImageCache {
    @Override
    @SuppressLint("CheckResult")
    public void saveByteArray(String url, byte[] byteArray) {
        String fileName = Utils.SHA1(url);
        Storage storage = new ExternalStorage();
        storage.writeFile(fileName, byteArray)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmStorageFile realmStorageFile = realm.where(RealmStorageFile.class).equalTo("url", url).findFirst();
                    if (realmStorageFile == null) {
                        realm.executeTransaction(innerRealm -> {
                            RealmStorageFile newRealmStorageFile = innerRealm.createObject(RealmStorageFile.class, url);
                            newRealmStorageFile.setFileName(fileName);
                        });
                    }
                    realm.close();
                }, Timber::d);
    }

    @Override
    @SuppressLint("CheckResult")
    public Single<byte[]> getByteArray(String url) {
        return Single.create(emitter -> {
            Storage storage = new ExternalStorage();
            Realm realm = Realm.getDefaultInstance();
            RealmStorageFile realmStorageFile = realm.where(RealmStorageFile.class).equalTo("url", url).findFirst();
            if (realmStorageFile != null) {
                storage.readFile(realmStorageFile.getFileName())
                        .subscribeOn(Schedulers.io())
                        .subscribe(emitter::onSuccess, Timber::d);
            }
            realm.close();
        });
    }
}
