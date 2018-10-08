package ru.geekbrains.android3_5.mvp.di.modules;

import android.widget.ImageView;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_5.mvp.model.image.ImageLoader;
import ru.geekbrains.android3_5.mvp.model.image.android.ImageLoaderGlide;
import ru.geekbrains.android3_5.mvp.model.image.android.cache.ImageCache;

@Module(includes = ImageCacheModule.class)
public class ImageModule {

    @Provides
    public ImageLoader<ImageView> imageLoader(@Named("hybrid") ImageCache imageCache) {
        return new ImageLoaderGlide(imageCache);
    }
}
