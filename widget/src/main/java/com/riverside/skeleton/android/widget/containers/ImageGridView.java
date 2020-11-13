package com.riverside.skeleton.android.widget.containers;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.riverside.skeleton.android.widget.R;
import com.riverside.skeleton.android.widget.loader.image.ImageLoader;
import com.riverside.skeleton.android.widget.loader.image.PicassoImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.riverside.skeleton.android.util.converter.UnitConverterUtils.dip2px;

/**
 * 图片显示Grid控件   1.0
 * b_e  2018/02/14
 */
public class ImageGridView extends GridLayout {
    private Context mContext;
    //是否只读
    private boolean readOnly;
    /**
     * 是否动态调整列数
     * 1个图片时为1列
     * 2，4个图片时为2列
     * 其他情况为3列
     */
    private boolean smartColumnCount;
    //添加图片按钮图标
    private int addButtonId;
    //最多可添加几个图片
    private int imageCount;
    //分割线宽度
    private int dividerWidth;

    private GridLayout gl_images;
    private ArrayList<String> imageList;

    private ImageLoader imageLoader;
    private ImageClickListener imageClickListener;
    private AddImageClickListener addImageClickListener;

    public ImageGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getAttrs(attrs);
        initView();
    }

    /**
     * 初始化控件
     */
    void initView() {
        gl_images = this;
        gl_images.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        imageList = new ArrayList<>();
        showImage(imageList);
    }

    void getAttrs(AttributeSet attrs) {
        //取得控件属性
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageGridView, 0, 0);
        readOnly = typedArray.getBoolean(R.styleable.ImageGridView_igv_readOnly, false);
        smartColumnCount = typedArray.getBoolean(R.styleable.ImageGridView_igv_smartColumnCount, false);
        addButtonId = typedArray.getResourceId(R.styleable.ImageGridView_igv_addButtonIcon, 0);
        imageCount = typedArray.getInteger(R.styleable.ImageGridView_igv_imageCount, 9);
        dividerWidth = typedArray.getDimensionPixelSize(R.styleable.ImageGridView_igv_dividerWidth, 0);
        typedArray.recycle();
    }

    /**
     * 控件是否为只读状态
     *
     * @return
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * 设置控件的只读状态
     *
     * @param readOnly
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        showImage(imageList);
    }

    /**
     * 取得动态列数
     *
     * @return
     */
    private int getSmartColumnCount() {
        int imageCol = gl_images.getColumnCount();
        if (readOnly && smartColumnCount) {
            //动态调整显示列数
            switch (imageList.size()) {
                case 1:
                    imageCol = 1;
                    break;
                case 2:
                case 4:
                    imageCol = 2;
                    break;
                case 3:
                default:
                    imageCol = 3;
                    break;
            }
        }
        return imageCol;
    }

    /**
     * 显示图片
     *
     * @param imageList
     */
    private void showImage(final List<String> imageList) {
        gl_images.post(new Runnable() {
            @Override
            public void run() {
                gl_images.setColumnCount(getSmartColumnCount());
                gl_images.removeAllViews();

                for (int i = 0; i < imageList.size(); i++) {
                    String imageItem = imageList.get(i);
                    gl_images.addView(getImageView(imageItem, i));
                }

                //判断是否显示默认添加图片按钮
                if (!readOnly && addButtonId != 0 && gl_images.getChildCount() < imageCount) {
                    gl_images.addView(getAddImageView());
                }
            }
        });
    }

    /**
     * 取得图片控件
     *
     * @param imageItem
     * @return
     */
    View getImageView(String imageItem, int position) {
        //计算各种宽度
        int imageCol = getSmartColumnCount();
        int iWidth = gl_images.getWidth() / imageCol;

        //取得控件
        RelativeLayout rl_image = getRelativeLayout(iWidth, dividerWidth);

        ImageView iv_image = getImageView();
        loadImage(iv_image, position, iWidth, iWidth);
        iv_image.setTag(position);
        //设置全屏图片显示
        iv_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                imageClick((ImageView) v, position);
            }
        });
        rl_image.addView(iv_image);

        //生成删除按钮
        if (!readOnly) {
            ImageView iv_delete = new ImageView(mContext);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(iWidth / 4, iWidth / 4);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            iv_delete.setLayoutParams(layoutParams);
            iv_delete.setPadding(dip2px(mContext, 4), dip2px(mContext, 4), dip2px(mContext, 4), dip2px(mContext, 4));
            iv_delete.setAdjustViewBounds(true);
            iv_delete.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv_delete.setImageResource(R.drawable.imagegrid_delete_image);
            iv_delete.setTag(position);
            iv_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    deleteImageClick(position);
                }
            });
            rl_image.addView(iv_delete);
        }
        return rl_image;
    }

    /**
     * 生成默认添加图片按钮
     *
     * @return
     */
    View getAddImageView() {
        int imageCol = getSmartColumnCount();
        int iWidth = gl_images.getWidth() / imageCol;

        RelativeLayout rl_add_image = getRelativeLayout(iWidth, dividerWidth);

        ImageView iv_add_image = getImageView();
        iv_add_image.setImageResource(addButtonId);
        iv_add_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageClick();
            }
        });
        rl_add_image.addView(iv_add_image);
        return rl_add_image;
    }

    /**
     * 生成图片容器
     *
     * @param width
     * @param dividerWidth
     * @return
     */
    private RelativeLayout getRelativeLayout(int width, int dividerWidth) {
        RelativeLayout rl = new RelativeLayout(mContext);
        rl.setLayoutParams(new ViewGroup.LayoutParams(width, width));
        rl.setPadding(dividerWidth, dividerWidth, dividerWidth, dividerWidth);
        return rl;
    }

    /**
     * 生成图片控件
     *
     * @return
     */
    private ImageView getImageView() {
        ImageView iv_image = new ImageView(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        iv_image.setLayoutParams(layoutParams);
        iv_image.setAdjustViewBounds(true);
        iv_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return iv_image;
    }

    /**
     * 设置显示图片路径
     *
     * @param list
     */
    public void setImageList(List<String> list) {
        imageList = new ArrayList<>(list);
        showImage(imageList);
    }

    /**
     * 取得图片路径列表
     *
     * @return
     */
    public List<String> getImageList() {
        return imageList;
    }

    /**
     * 取得指定图片路径
     *
     * @param index
     * @return
     */
    public String getImagePath(int index) {
        return imageList.get(index);
    }

    /**
     * 删除指定图片
     *
     * @param index
     */
    public void removeImage(int index) {
        imageList.remove(index);
        showImage(imageList);
    }

    /**
     * 加载图片
     *
     * @param iv_image
     * @param position
     * @param width
     * @param height
     */
    private void loadImage(ImageView iv_image, int position, int width, int height) {
        if (imageLoader == null) {
            imageLoader = new PicassoImageLoader();
        }
        imageLoader.loadImage(iv_image, imageList.get(position), width, height);
    }

    /**
     * 设置图片加载器
     *
     * @param loader
     */
    public void setImageLoader(ImageLoader loader) {
        imageLoader = loader;
    }

    public void setOnImageClickListener(ImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    private void imageClick(ImageView iv_image, int position) {
        if (imageClickListener != null) {
            imageClickListener.onClick(iv_image, position, imageList.get(position));
        }
    }

    private void deleteImageClick(int position) {
        if (imageClickListener != null) {
            imageClickListener.onDelete(position, imageList.get(position));
        }
    }

    public interface ImageClickListener {
        void onClick(ImageView iv, int position, String url);

        void onDelete(int position, String url);
    }

    public void setOnAddImageClickListener(AddImageClickListener addImageClickListener) {
        this.addImageClickListener = addImageClickListener;
    }

    private void addImageClick() {
        if (addImageClickListener != null) {
            addImageClickListener.onClick();
        }
    }

    public interface AddImageClickListener {
        void onClick();
    }
}
