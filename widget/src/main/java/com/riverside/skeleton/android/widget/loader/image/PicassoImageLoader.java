package com.riverside.skeleton.android.widget.loader.image;

import android.widget.ImageView;

import com.riverside.skeleton.android.util.file.FileUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

public class PicassoImageLoader implements ImageLoader {
    @Override
    public void loadImage(ImageView imageView, String url, int width, int height) {
        Picasso picasso = Picasso.get();
        RequestCreator requestCreator;
        if (FileUtils.isExists(url)) {
            requestCreator = picasso.load(new File(url));
        } else {
            requestCreator = picasso.load(url);
        }
        requestCreator.resize(width, height)
                .centerCrop()
                .into(imageView);

    }

    @Override
    public void loadImage(ImageView imageView, String url) {
        Picasso picasso = Picasso.get();
        RequestCreator requestCreator;
        if (FileUtils.isExists(url)) {
            requestCreator = picasso.load(new File(url));
        } else {
            requestCreator = picasso.load(url);
        }
        requestCreator.into(imageView);
    }
}
