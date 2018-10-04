package ru.geekbrains.android3_5.mvp.model.image.android;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.geekbrains.android3_5.mvp.model.NetworkStatus;
import ru.geekbrains.android3_5.mvp.model.image.ImageLoader;
import ru.geekbrains.android3_5.mvp.model.image.android.cache.HybridImageCache;
import ru.geekbrains.android3_5.mvp.model.image.android.cache.ImageCache;

/**
 * Created by stanislav on 3/12/2018.
 */

public class ImageLoaderGlide implements ImageLoader<ImageView> {
    private ImageCache imageCache;

    public ImageLoaderGlide() {
        //this.imageCache = new PaperImageCache();
        //this.imageCache = new RealmImageCache();
        this.imageCache = new HybridImageCache();
    }

    @Override
    @SuppressLint("CheckResult")
    public void loadInto(@Nullable String url, ImageView container) {
        if (NetworkStatus.isOffline()) {
            imageCache.getByteArray(url)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bytes -> GlideApp.with(container.getContext())
                            .load(bytes)
                            .into(container));
        } else {
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageCache.saveByteArray(url, stream.toByteArray());
                    return false;
                }
            }).into(container);
        }
    }
}
