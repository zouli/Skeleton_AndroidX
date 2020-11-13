package com.riverside.skeleton.android.widgettest;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.riverside.skeleton.android.R;
import com.riverside.skeleton.android.base.activity.BaseActivity;
import com.riverside.skeleton.android.util.notice.SnackbarHelper;
import com.riverside.skeleton.android.widget.activity.FullImageActivity;
import com.riverside.skeleton.android.widget.containers.ImageGridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_imagegridview)
public class ImageGridViewActivity extends BaseActivity {
    private static int REQUEST_CODE_IMAGE = 100;

    @ViewById
    ImageGridView igv_image, igv_image1;

    @AfterViews
    void initView() {
        initIGV();
        initIGV1();
    }

    void initIGV() {
        igv_image.setOnAddImageClickListener(new ImageGridView.AddImageClickListener() {
            @Override
            public void onClick() {
                SImagePicker
                        .from(activity)
                        .maxCount(9)
                        .rowCount(3)
                        .pickMode(SImagePicker.MODE_IMAGE)
                        .showCamera(true)
                        .setSelected(new ArrayList<String>(igv_image.getImageList()))
//                        .fileInterceptor(new SingleFileLimitInterceptor())
                        .forResult(REQUEST_CODE_IMAGE);
            }
        });
        igv_image.setOnImageClickListener(new ImageGridView.ImageClickListener() {
            @Override
            public void onClick(ImageView iv, int position, String url) {
                Intent intent = new Intent(activity, FullImageActivity.class);
                intent.putStringArrayListExtra("image_url", new ArrayList<>(igv_image.getImageList()));
                intent.putExtra("default_index", position);
                startActivity(intent);
            }

            @Override
            public void onDelete(final int position, String url) {
                SnackbarHelper.Builder(activity)
                        .setAction("嗯", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                igv_image.removeImage(position);
                            }
                        })
                        .showSnackbar("是否删除图片", SnackbarHelper.LENGTH_LONG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            final boolean original =
                    data.getBooleanExtra(PhotoPickerActivity.EXTRA_RESULT_ORIGINAL, false);
            igv_image.setImageList(pathList);
        }
    }

    void initIGV1() {
        List<String> list = new ArrayList<>();
        list.add("http://pic1.win4000.com/pic/8/a8/99a938e209.jpg");
        list.add("http://pic1.win4000.com/wallpaper/2017-11-15/5a0bce5db257c.jpg");
        list.add("http://pic1.win4000.com/wallpaper/2017-11-15/5a0bce5a8951e.jpg");
        list.add("http://pic1.win4000.com/wallpaper/2017-11-15/5a0bce6037bd4.jpg");
        list.add("http://pic1.win4000.com/wallpaper/2017-11-15/5a0bce5b56b9f.jpg");
        list.add("http://a.hiphotos.baidu.com/zhidao/pic/item/810a19d8bc3eb13513c00107ad1ea8d3fd1f4402.jpg");
        list.add("http://f.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3ce863f8b519c45d688d53f20d0.jpg");
        list.add("http://03imgmini.eastday.com/mobile/20190411/2019041105_3395409e15bf4cc1aee6fe8d28d529b2_9130_wmk.jpg");
        list.add("http://07imgmini.eastday.com/mobile/20190409/20190409144819_7839f4c2c0ba95a355ae63c59e842219_1.jpeg");
        igv_image1.setOnImageClickListener(new ImageGridView.ImageClickListener() {
            @Override
            public void onClick(ImageView iv, int position, String url) {
                Intent intent = new Intent(activity, FullImageActivity.class);
                intent.putStringArrayListExtra("image_url", new ArrayList<>(igv_image1.getImageList()));
                intent.putExtra("default_index", position);
                startActivity(intent);
            }

            @Override
            public void onDelete(int position, String url) {

            }
        });
        igv_image1.setImageList(list);
    }
}
