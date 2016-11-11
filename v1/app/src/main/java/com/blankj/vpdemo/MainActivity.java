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

        mViews = new ArrayList<>();
        ImageView iv;
        // 插入最后一张
        iv = new ImageView(this);
        iv.setBackgroundResource(ids[ids.length - 1]);
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
        vp.setCurrentItem(1);
        vp.setPageTransformer(true, new CustomPageTransformer());
        vp.addOnPageChangeListener(this);
        vp.setPageMargin(40);

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
        Log.d("cmj", "onPageSelectedPos: " + position);
        if (mViews.size() > 3) {
            if (position <= 0) {
                // 当滑动到第一张的时候，让它跳转到倒数第二张，false为关闭smoothScroll
                vp.setCurrentItem(mViews.size() - 2, false);
            } else if (position >= mViews.size() - 1) {
                // 当滑动到最后一张的时候，让它跳转到第二张，，false为关闭smoothScroll
                vp.setCurrentItem(1, false);
            }
        }
        fl.invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class VpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("cmj", "pos: " + position);
            View view = mViews.get(position);
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
