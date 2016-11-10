package com.blankj.vpdemo;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener {

    private ViewPager       vp;
    private FrameLayout     fl;
    private List<ImageView> mViews;

    // 数据源长度
    private int len;

    // 倍数因子，数据源的倍数
    private int factor = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (ViewPager) findViewById(R.id.vp);
        mViews = new ArrayList<>();
        int[] ids = new int[]{
                R.drawable.img0,
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
                R.drawable.img6,
                R.drawable.img7,
                R.drawable.img8
        };

        len = ids.length;
        mViews = new ArrayList<>();
        ImageView iv;
        // 插入最后一张
        iv = new ImageView(this);
        iv.setBackgroundResource(ids[len - 1]);
        mViews.add(iv);

        for (int id : ids) {
            iv = new ImageView(this);
            iv.setBackgroundResource(id);
            mViews.add(iv);
        }
        // 插入第一张
        iv = new ImageView(this);
        iv.setBackgroundResource(ids[0]);
        mViews.add(iv);

        VpAdapter adapter = new VpAdapter();
        vp.setAdapter(adapter);
        // 当前页前后缓存页为3即可，即缓存当前项的前三页和后三页效果比较好
        vp.setOffscreenPageLimit(3);
        // 使其显示在中间
        vp.setCurrentItem(len * (factor / 2) + 1);
        vp.setPageTransformer(true, new CustomPageTransformer());
        vp.addOnPageChangeListener(this);

        fl = (FrameLayout) findViewById(R.id.fl);

        // 父控件把事件分发交给viewPager处理
        findViewById(R.id.fl).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return vp.dispatchTouchEvent(event);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        Log.d("cmj", "onPageSelectedPos: " + position);
//        if (mViews.size() > 3) {
//            if (position <= 0) {
//                // 当滑动到vp的第一张的时候，其实显示的是最后一张
//                // 让它跳转到中间的倒数第二张，这样再划回去也就不生硬了
//                position = len * (factor / 2) + len;
//                // false为关闭smoothScroll
//                vp.setCurrentItem(position, false);
//            } else if (position >= len * factor + 1) {
//                // 当滑动到最后一张的时候，其实显示的是第一张
//                // 让它跳转到中间的第二张，这样再划回去也就不生硬了
//                position = len * (factor / 2) + 1;
//                // false为关闭smoothScroll
//                vp.setCurrentItem(position, false);
//            }
//        }
//        fl.invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class VpAdapter extends PagerAdapter {

        @Override
        public void finishUpdate(ViewGroup container) {
            int position = vp.getCurrentItem();
            Log.d("cmj", "finishUpdate: " + position);
            if (mViews.size() > 3) {
                if (position <= 0) {
                    // 当滑动到vp的第一张的时候，无动画地切换到最后一张
                    position = len * factor + len;
                    // false为关闭smoothScroll
                    vp.setCurrentItem(position, false);
                } else if (position >= len * factor + 1) {
                    // 当滑动到vp最后一张的时候，无动画地切换到第一张
                    position = 1;
                    // false为关闭smoothScroll
                    vp.setCurrentItem(position, false);
                }
            }
        }

        @Override
        public int getCount() {
            // 个数为原先的个数的factor倍再加首位两个
            return len * factor + 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("cmj", "pos: " + position);
            View view;
            if (position == 0) {
                view = mViews.get(0);
            } else if (position == len * factor + 1) {
                view = mViews.get(len + 1);
            } else {
                view = mViews.get((position - 1) % len + 1);
            }
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
