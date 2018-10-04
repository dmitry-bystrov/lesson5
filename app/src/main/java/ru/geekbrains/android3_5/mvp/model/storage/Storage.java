package ru.geekbrains.android3_5.mvp.model.storage;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface Storage {
    Single<byte[]> readFile(String fileName);

    Completable writeFile(String fileName, byte[] byteArray);
}
