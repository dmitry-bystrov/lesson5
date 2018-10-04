package ru.geekbrains.android3_5.mvp.model.image.android.cache;

import io.paperdb.Paper;
import io.reactivex.Single;
import ru.geekbrains.android3_5.Utils;

public class PaperImageCache implements ImageCache {
    @Override
    public void saveByteArray(String url, byte[] byteArray) {
        String sha1 = Utils.SHA1(url);
        Paper.book("images").write(sha1, byteArray);
    }

    @Override
    public Single<byte[]> getByteArray(String url) {
        return Single.create(emitter -> {
            String sha1 = Utils.SHA1(url);
            if (Paper.book("images").contains(sha1)) {
                emitter.onSuccess(Paper.book("images").read(sha1));
            } else {
                emitter.onError(new RuntimeException("Error reading byte array from cache"));
            }
        });
    }
}
