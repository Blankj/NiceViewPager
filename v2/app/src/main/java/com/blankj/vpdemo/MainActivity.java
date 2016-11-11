package com.blankj.vpdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener {

    private ViewPager            vp;
    private RelativeLayout       rl;
    private LinearLayout         ll;
    // vp中的点
    private List<ImageView>      mViews;
    // 下面的指示器的点
    private ArrayList<ImageView> mDotViews;
    // 上一个位置
    private int prePos = 0;

    // 数据源长度
    private int len;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (ViewPager) findViewById(R.id.vp);
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
        mDotViews = new ArrayList<>();
        ImageView iv;
        for (int id : ids) {
            iv = new ImageView(this);
            iv.setBackgroundResource(id);
            mViews.add(iv);
        }
        // 时设置页边距
        vp.setPageMargin(dp2px(this, 24));
        VpAdapter adapter = new VpAdapter();
        vp.setAdapter(adapter);
        // 当前页前后缓存页为3即可，即缓存当前项的前三页和后三页效果比较好
        vp.setOffscreenPageLimit(3);
        // 使其显示在中间
        vp.setCurrentItem(len * ((Integer.MAX_VALUE >> 1) / len));
        vp.setPageTransformer(true, new CustomPageTransformer());
        vp.addOnPageChangeListener(this);

        rl = (RelativeLayout) findViewById(R.id.rl);
        ll = (LinearLayout) findViewById(R.id.ll);

        // 父控件把事件分发交给viewPager处理
        findViewById(R.id.rl).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return vp.dispatchTouchEvent(event);
            }
        });

        // 指示器
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(this, 16), dp2px(this, 16));
        params.rightMargin = dp2px(this, 16);
        for (int i = 0; i < len; i++) {
            ImageView imageViewDot = new ImageView(this);
            imageViewDot.setLayoutParams(params);
            //  设置小圆点的背景为暗红图片
            imageViewDot.setBackgroundResource(R.drawable.red_dot_night);
            ll.addView(imageViewDot);
            mDotViews.add(imageViewDot);
        }
        mDotViews.get(prePos).setBackgroundResource(R.drawable.red_dot);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("cmj", "onPageSelectedPos: " + position);
        mDotViews.get(prePos).setBackgroundResource(R.drawable.red_dot_night);
        prePos = position % len;
        mDotViews.get(prePos).setBackgroundResource(R.drawable.red_dot);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class VpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("cmj", "pos: " + position);
            position %= len;
            View view = mViews.get(position);
            // 不加这个的话会由于缓存造成带有ViewGroup的view新增到container中报错
            ViewParent parent = view.getParent();
            if (parent != null) {
                ViewGroup vg = (ViewGroup) parent;
                vg.removeView(view);
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

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
