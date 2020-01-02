package com.smart.himalaya;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.smart.himalaya.adapters.IndicatorAdapter;
import com.smart.himalaya.adapters.MainContentAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;
    private IndicatorAdapter mIndicatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();

    }

    private void initEvent() {
        mIndicatorAdapter.setOnIndicatorTapClickListener(new IndicatorAdapter.OnIndicatorTabClickListener() {
            @Override
            public void onTabClick(int index) {
                Log.d(TAG, "onTabClick: ==========" + "click index is ====>" + index);
                if (mContentPager != null) {
                    mContentPager.setCurrentItem(index);
                }
            }
        });
    }

    private void initView() {
        mMagicIndicator = (MagicIndicator) findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(getResources().getColor(R.color.main_color));
        //创建indicator的适配器
        mIndicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);//设置调整模式(自我调节，平分位置)
        commonNavigator.setAdapter(mIndicatorAdapter);


        //ViewPager
        mContentPager = (ViewPager) findViewById(R.id.content_pager);

        //创建内容适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);

        mContentPager.setAdapter(mainContentAdapter);
        //把ViewPager和indicator绑定到一起
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator,mContentPager);

    }
}
