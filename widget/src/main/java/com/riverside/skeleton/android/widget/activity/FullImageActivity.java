package com.riverside.skeleton.android.widget.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.widget.R;
import com.riverside.skeleton.android.widget.loader.image.ImageLoader;
import com.riverside.skeleton.android.widget.loader.image.PicassoImageLoader;

import java.util.List;

/**
 * 共通图片全屏显示画面   1.0
 * b_e  2018/02/16
 */
public class FullImageActivity extends BaseActivity {
    private ImageLoader imageLoader;

    //图片地址
    private List<String> url;
    //默认图片index
    private int default_index = 0;
    //图片加载器
    private String image_loader = PicassoImageLoader.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        initView();
        setListener();
    }

    private void initView() {
        setContentView(R.layout.activity_full_image);
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(new ImagePagerAdapter());
        mViewPager.setCurrentItem(default_index, true);
    }

    private void setListener() {
    }

    private void getData() {
        Intent intent = getIntent();
        url = intent.getStringArrayListExtra("image_url");
        default_index = intent.getIntExtra("default_index", 0);
        String imageLoader = intent.getStringExtra("image_loader");
        if (!TextUtils.isEmpty(imageLoader)) {
            image_loader = imageLoader;
        }
    }

    private void loadImage(ImageView imageView, int position) {
        if (imageLoader == null) {
            try {
                imageLoader = (ImageLoader) Class.forName(image_loader).newInstance();
            } catch (Exception e) {
                imageLoader = new PicassoImageLoader();
            }
        }
        imageLoader.loadImage(imageView, url.get(position));
    }

    class ImagePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return url.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            loadImage(photoView, position);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
