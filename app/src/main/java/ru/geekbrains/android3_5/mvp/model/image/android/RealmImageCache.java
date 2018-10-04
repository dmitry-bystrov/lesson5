package ru.geekbrains.android3_5.mvp.model.image.android;

import io.realm.Realm;
import ru.geekbrains.android3_5.mvp.model.entity.realm.RealmBitmap;

public class RealmImageCache implements ImageCache {
    @Override
    public void saveByteArray(String url, byte[] byteArray) {
        Realm realm = Realm.getDefaultInstance();
        RealmBitmap realmBitmap = realm.where(RealmBitmap.class).equalTo("url", url).findFirst();
        if (realmBitmap == null) {
            realm.executeTransaction(innerRealm -> {
                RealmBitmap newRealmBitmap = innerRealm.createObject(RealmBitmap.class, url);
                newRealmBitmap.setByteArray(byteArray);
            });
        } else {
            realm.executeTransaction(innerRealm -> {
                realmBitmap.setByteArray(byteArray);
            });
        }
        realm.close();
    }

    @Override
    public byte[] getByteArray(String url) {
        Realm realm = Realm.getDefaultInstance();
        RealmBitmap realmBitmap = realm.where(RealmBitmap.class).equalTo("url", url).findFirst();

        byte[] byteArray = null;

        if (realmBitmap != null) {
            byteArray = realmBitmap.getByteArray();
        }

        realm.close();

        return byteArray;
    }
}
