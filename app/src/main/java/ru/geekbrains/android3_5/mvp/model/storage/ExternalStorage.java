package ru.geekbrains.android3_5.mvp.model.storage;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.geekbrains.android3_5.App;

public class ExternalStorage implements Storage {
    private File directory;

    public ExternalStorage() {
        this.directory = App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    @Override
    public Single<byte[]> readFile(String fileName) {
        return Single.create(emitter -> {
            File file = new File(directory, fileName);
            if (!file.exists()) {
                emitter.onError(new RuntimeException("Error reading file from cache: file not exists"));
            }

            int size = (int) file.length();
            byte[] byteArray = new byte[size];

            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            int read = inputStream.read(byteArray, 0, size);
            inputStream.close();

            if (read == size) {
                emitter.onSuccess(byteArray);
            } else {
                emitter.onError(new RuntimeException("Error reading file from cache: read size error"));
            }
        });
    }

    @Override
    public Completable writeFile(String fileName, byte[] byteArray) {
        return Completable.create(emitter -> {
            File file = new File(directory, fileName);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(byteArray);
            outputStream.flush();
            outputStream.close();
            emitter.onComplete();
        });
    }
}
