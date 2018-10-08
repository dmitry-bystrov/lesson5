package ru.geekbrains.android3_5.mvp.di.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.android3_5.mvp.model.image.android.cache.HybridImageCache;
import ru.geekbrains.android3_5.mvp.model.image.android.cache.ImageCache;
import ru.geekbrains.android3_5.mvp.model.image.android.cache.PaperImageCache;
import ru.geekbrains.android3_5.mvp.model.image.android.cache.RealmImageCache;

@Module
public class ImageCacheModule {
    @Provides
    @Named("paper")
    public ImageCache paperCache() {
        return new PaperImageCache();
    }

    @Provides
    @Named("realm")
    public ImageCache realmCache() {
        return new RealmImageCache();
    }

    @Provides
    @Named("hybrid")
    public ImageCache hybridCache() {
        return new HybridImageCache();
    }
}
