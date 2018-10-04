package ru.geekbrains.android3_5.mvp.model.image.android.cache;

import io.reactivex.Single;

public interface ImageCache {
    void saveByteArray(String url, byte[] byteArray);

    Single<byte[]> getByteArray(String url);
}
