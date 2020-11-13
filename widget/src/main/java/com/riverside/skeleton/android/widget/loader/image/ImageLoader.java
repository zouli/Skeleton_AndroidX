package com.riverside.skeleton.android.widget.loader.image;

import android.widget.ImageView;

public interface ImageLoader {
    void loadImage(ImageView imageView, String url, int width, int height);

    void loadImage(ImageView imageView, String url);
}
