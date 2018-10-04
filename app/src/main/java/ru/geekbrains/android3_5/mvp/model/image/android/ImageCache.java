package ru.geekbrains.android3_5.mvp.model.image.android;

public interface ImageCache {
    void saveByteArray(String url, byte[] byteArray);

    byte[] getByteArray(String url);
}
