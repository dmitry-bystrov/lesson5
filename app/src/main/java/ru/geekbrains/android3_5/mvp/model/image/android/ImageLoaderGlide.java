package ru.geekbrains.android3_5.mvp.model.image.android;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;

import io.paperdb.Paper;
import ru.geekbrains.android3_5.Utils;
import ru.geekbrains.android3_5.mvp.model.NetworkStatus;
import ru.geekbrains.android3_5.mvp.model.image.ImageLoader;

/**
 * Created by stanislav on 3/12/2018.
 */

public class ImageLoaderGlide implements ImageLoader<ImageView>
{
    @Override
    public void loadInto(@Nullable String url, ImageView container)
    {
        String sha1 = Utils.SHA1(url);
        if (NetworkStatus.isOffline())
        {
            if (Paper.book("images").contains(sha1))
            {
                byte[] bytes = Paper.book("images").read(sha1);
                GlideApp.with(container.getContext())
                        .load(bytes)
                        .into(container);
            }
        }
        else
        {
            GlideApp.with(container.getContext()).asBitmap().load(url).listener(new RequestListener<Bitmap>()
            {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource)
                {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource)
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
                    Paper.book("images").write(sha1, stream.toByteArray());
                    return false;
                }
            }).into(container);
        }


    }
}
