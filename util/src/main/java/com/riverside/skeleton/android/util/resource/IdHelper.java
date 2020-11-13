package com.riverside.skeleton.android.util.resource;

import android.content.Context;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID帮助类    1.0
 * b_e  2018/08/10
 */
public class IdHelper {
    private final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private WeakReference<Context> context;

    private IdHelper(WeakReference<Context> context) {
        this.context = context;
    }

    public static IdHelper init(Context context) {
        return new IdHelper(new WeakReference<>(context));
    }

    private int getResourceID(String type, String id) {
        return context.get().getResources().getIdentifier(id, type, context.get().getPackageName());
    }

    public int getDrawableID(String id) {
        return getResourceID("drawable", id);
    }

    public int getID(String id) {
        return getResourceID("id", id);
    }

    public int getLayoutID(String id) {
        return getResourceID("layout", id);
    }

    public int getStyleID(String id) {
        return getResourceID("style", id);
    }

    public int getSringID(String id) {
        return getResourceID("string", id);
    }

    public int getColorID(String id) {
        return getResourceID("color", id);
    }

    /**
     * Generate a value suitable for use in {@link #setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * 更新指定View的Id
     *
     * @param parentView
     * @param id
     * @return
     */
    public int updateViewId(View parentView, int id) {
        int newId = generateViewId();
        View view = parentView.findViewById(id);
        view.setId(newId);
        return newId;
    }
}

