package com.riverside.skeleton.android.application;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.imnjh.imagepicker.ImageLoader;
import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;
import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.application.BaseApplication;
import com.riverside.skeleton.android.util.file.FileHelper;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.EApplication;

import java.util.ArrayList;

@EApplication
public class MyApplication extends BaseApplication {
    MyApplication() {
        moduleApplications.add(BaseApplication.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FileHelper.CreateNomediaFile();

        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void bindImage(ImageView imageView, Uri uri, int width, int height) {
                        Picasso.get()
                                .load(uri)
                                .resize(width, height)
                                .centerCrop()
                                .into(imageView);
                    }

                    @Override
                    public void bindImage(ImageView imageView, Uri uri) {
                        Picasso.get()
                                .load(uri)
                                .into(imageView);
                    }

                    @Override
                    public ImageView createImageView(Context context) {
                        ImageView imageView = new ImageView(context);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return imageView;
                    }

                    @Override
                    public ImageView createFakeImageView(Context context) {
                        return new ImageView(context);
                    }
                })
                .setToolbaseColor(getResources().getColor(R.color.colorPrimary))
                .build());
    }
}
