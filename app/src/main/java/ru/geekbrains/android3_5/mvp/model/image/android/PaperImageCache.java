package ru.geekbrains.android3_5.mvp.model.image.android;

import io.paperdb.Paper;
import ru.geekbrains.android3_5.Utils;

public class PaperImageCache implements ImageCache {
    @Override
    public void saveByteArray(String url, byte[] byteArray) {
        String sha1 = Utils.SHA1(url);
        Paper.book("images").write(sha1, byteArray);
    }

    @Override
    public byte[] getByteArray(String url) {
        String sha1 = Utils.SHA1(url);
        if (Paper.book("images").contains(sha1)) {
            return Paper.book("images").read(sha1);
        } else {
            return null;
        }
    }
}
