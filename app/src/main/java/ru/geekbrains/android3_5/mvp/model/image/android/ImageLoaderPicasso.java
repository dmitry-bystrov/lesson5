package ru.geekbrains.android3_5.mvp.model.image.android;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ru.geekbrains.android3_5.mvp.model.image.ImageLoader;

/**
 * Created by stanislav on 3/12/2018.
 */

public class ImageLoaderPicasso implements ImageLoader<ImageView>
{
    @Override
    public void loadInto(@Nullable String url, ImageView container)
    {
        Picasso.get().load(url).into(container);
    }
}
