package com.blankj.vpdemo;

import android.graphics.ColorMatrixColorFilter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/10/26
 *     desc  : ViewPager切换特效
 * </pre>
 */
public class CustomPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE    = 0.80f;
    private static final float MIN_ALPHA    = 0.5f;
    private static final float MAX_DARKNESS = -129f;
    private static final float MAX_ROTATE   = 18f;

    @Override
    public void transformPage(View view, float position) {
        view.setPivotX(view.getWidth() / 2);
        if (position < -1 || position > 1) {// 超出屏幕的部分，-1屏幕左端超出部分，1为右端
            // 设置亮度
            float[] brightnessArray = new float[]{
                    1, 0, 0, 0, MAX_DARKNESS,
                    0, 1, 0, 0, MAX_DARKNESS,
                    0, 0, 1, 0, MAX_DARKNESS,
                    0, 0, 0, 1, 0
            };
            view.getBackground().setColorFilter(new ColorMatrixColorFilter(brightnessArray));

            // 设置缩放
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);

            // 设置透明度
            view.setAlpha(MIN_ALPHA);
            // 设置旋转
            view.setRotationY((position < -1 ? 1 : -1) * MAX_ROTATE);

        } else if (position <= 1) {// 屏幕内部
            // 设置亮度
            float brightnessFactor = MAX_DARKNESS * (Math.abs(position)) + 1;
            float[] brightnessArray = new float[]{
                    1, 0, 0, 0, brightnessFactor,
                    0, 1, 0, 0, brightnessFactor,
                    0, 0, 1, 0, brightnessFactor,
                    0, 0, 0, 1, 0
            };
            view.getBackground().setColorFilter(new ColorMatrixColorFilter(brightnessArray));

            // 设置缩放及平移
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float halfMargin = view.getWidth() * (1 - scaleFactor) / 4;
            view.setScaleX(1 - (1 - MIN_SCALE) * Math.abs(position));
            view.setScaleY(1 - (1 - MIN_SCALE) * Math.abs(position));
            view.setTranslationX((position < 0 ? 1 : -1) * halfMargin);

            // 设置透明度
            view.setAlpha(1 - (1 - MIN_ALPHA) * Math.abs(position));

            // 设置旋转
            view.setRotationY(-position * MAX_ROTATE);
        }
    }
}